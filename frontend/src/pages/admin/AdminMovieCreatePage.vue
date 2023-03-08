<template>
  <div>
    <h1 class="text-h4">Управление видеороликами</h1>
    <v-divider class="my-2"></v-divider>
    <v-form v-model="form.valid" :disabled="busy" @submit.prevent="submitForm">
      <v-card>
        <v-card-title>Новый видеоролик</v-card-title>
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
import MovieCreateForm from '@/model/movie/MovieCreateForm'

export default {
  ...dashboardPageCommonAttributes,
  name: 'AdminMovieCreatePage',
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
    ...mapState('admin_movie_create', ['busy']),
    ...mapState('admin_movie_create', {
      categories: (state) => [{ id: 0, title: '- Нет категории -' }
        , ...state.categories
      ]
    })
  },
  created() {
    this.fetchData()
  },
  methods: {
    ...mapActions('admin_movie_create', ['fetchData', 'createMovie']),
    submitForm() {
      const form = MovieCreateForm.create(this.form.data)

      this.createMovie({ form })
        .then(() => this.$router.push({ name: 'admin.movie.list' }))
    }
  }
}
</script>

<style scoped>

</style>
