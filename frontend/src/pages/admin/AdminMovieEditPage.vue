<template>
  <div>
    <h1 class="text-h4">Управление видеороликами</h1>
    <v-divider class="my-2"></v-divider>
    <v-form v-model="form.valid" :disabled="busy" @submit.prevent="submitForm">
      <v-card>
        <v-card-title>Редактирование видеоролика #{{ id }}</v-card-title>
        <v-card-text>
          <v-text-field
            v-model="form.data.title"
            label="Название"
            counter="150"
            :rules="form.rule.title"
          />
          <v-select
            v-model="form.data.categoryId"
            label="Категория"
            :items="categories"
            item-text="title"
            item-value="id"
          ></v-select>
          <v-textarea
            v-model="form.data.description"
            label="Описание"
            counter="5000"
            :rules="form.rule.description"
          />
          <v-textarea
            v-model="form.data.embedCode"
            label="Код видео(html)"
            counter="2400"
            :rules="form.rule.embedCode"
          />
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn :to="{name: 'admin.movie.list'}">Отмена</v-btn>
          <v-btn color="primary" :disabled="!form.valid" type="submit">Отправить</v-btn>
        </v-card-actions>
      </v-card>
    </v-form>
  </div>
</template>

<script>
import { dashboardPageCommonAttributes } from '@/mixins/DashboardPageCommonAttributes'
import { ruleRequired } from '@/utils/FormRules'
import { mapActions, mapState } from 'vuex'
import MovieUpdateForm from '@/model/movie/MovieUpdateForm'

export default {
  ...dashboardPageCommonAttributes,
  name: 'AdminMovieEditPage',
  props: {
    id: {
      type: [String, Number],
      required: true
    }
  },
  data() {
    return {
      form: {
        valid: true,
        data: {
          title: null,
          description: null,
          embedCode: null,
          categoryId: 0
        },
        rule: {
          title: [ruleRequired()],
          description: [ruleRequired()],
          embedCode: [ruleRequired()]
        }
      }
    }
  },
  computed: {
    ...mapState('admin_movie_edit', ['item', 'busy']),
    ...mapState('admin_movie_edit', {
      categories: (state) => [{ id: 0, title: '- Нет категории -' }
        , ...state.categories
      ]
    })
  },
  created() {
    this.fetchData({ id: this.id })
      .then(() => {
        this.form.data.title = this.item.title
        this.form.data.description = this.item.description
        this.form.data.embedCode = this.item.embedCode
        this.form.data.categoryId = this.item.category?.id ?? 0
      })
  },
  methods: {
    ...mapActions('admin_movie_edit', ['fetchData', 'updateMovie']),
    submitForm() {
      const form = MovieUpdateForm.create({ ...this.form.data })

      this.updateMovie({ form })
        .then(() => this.$router.push({ name: 'admin.movie.list' }))
    }
  }
}
</script>

<style scoped>

</style>
