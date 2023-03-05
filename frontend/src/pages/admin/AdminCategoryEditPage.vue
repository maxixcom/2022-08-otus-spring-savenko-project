<template>
  <div>
    <h1 class="text-h4">Управление категориями</h1>
    <v-divider class="my-2"></v-divider>
    <v-form v-model="form.valid" :disabled="busy" @submit.prevent="submitForm">
      <v-card>
        <v-card-title>Редактирование категории #{{ id }}</v-card-title>
        <v-card-text>
          <v-text-field
            v-model="form.data.title"
            label="Название"
            counter="150"
            :rules="form.rule.title"
          />
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn :to="{name: 'admin.category.list'}">Отмена</v-btn>
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
import CategoryUpdateForm from '@/model/category/CategoryUpdateForm'

export default {
  ...dashboardPageCommonAttributes,
  name: 'AdminCategoryEditPage',
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
          title: ''
        },
        rule: {
          title: [ruleRequired()]
        }
      }
    }
  },
  computed: {
    ...mapState('admin_category_edit', ['item', 'busy'])
  },
  created() {
    this.fetchData({ id: this.id })
      .then(() => {
        this.form.data.title = this.item.title
      })
  },
  methods: {
    ...mapActions('admin_category_edit', ['fetchData', 'updateCategory']),
    submitForm() {
      const form = CategoryUpdateForm.create({ ...this.form.data })

      this.updateCategory({ form })
        .then(() => this.$router.push({ name: 'admin.category.list' }))
    }
  }
}
</script>

<style scoped>

</style>
