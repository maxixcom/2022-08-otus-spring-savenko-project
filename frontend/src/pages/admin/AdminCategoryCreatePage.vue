<template>
  <div>
    <h1 class="text-h4">Управление категориями</h1>
    <v-divider class="my-2"></v-divider>
    <v-form v-model="form.valid" :disabled="busy" @submit.prevent="submitForm">
      <v-card>
        <v-card-title>Новая категория</v-card-title>
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
import CategoryCreateForm from '@/model/category/CategoryCreateForm'
import { mapActions, mapState } from 'vuex'

export default {
  ...dashboardPageCommonAttributes,
  name: 'AdminCategoryCreatePage',
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
    ...mapState('admin_category_create',['busy'])
  },
  methods: {
    ...mapActions('admin_category_create',['createCategory']),
    submitForm() {
      const form = CategoryCreateForm.create(this.form.data)

      this.createCategory({ form })
        .then(() => this.$router.push({ name: 'admin.category.list' }))
    }
  }
}
</script>

<style scoped>

</style>
