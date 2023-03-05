<template>
  <div>
    <h1 class="text-h4">Управление пользователями</h1>
    <v-divider class="my-2"></v-divider>
    <v-form v-model="form.valid" :disabled="busy" @submit.prevent="submitForm">
      <v-card>
        <v-card-title>Редактирование аккаунта #{{ id }}</v-card-title>
        <v-card-text>
          <v-chip-group
            v-model="form.data.role"
            column
            mandatory
          >
            <v-chip
              v-for="role in roles"
              :key="role.id"
              :value="role.id"
              filter
              outlined
            >
              {{ role.title }}
            </v-chip>
          </v-chip-group>
          <v-text-field
            v-model="form.data.username"
            label="Логин"
            counter="150"
            :rules="form.rule.username"
          />
          <v-text-field
            v-model="form.data.fullName"
            label="Имя"
            counter="150"
            :rules="form.rule.fullName"
          />
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn :to="{name: 'admin.user.list'}">Отмена</v-btn>
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
import UserUpdateForm from '@/model/user/UserUpdateForm'

export default {
  ...dashboardPageCommonAttributes,
  name: 'AdminUserEditPage',
  props: {
    id: {
      type: [String, Number],
      required: true
    }
  },
  data() {
    return {
      roles: [
        { id: 'USER', title: 'User' },
        { id: 'ADMIN', title: 'Admin' }
      ],
      form: {
        valid: true,
        data: {
          username: null,
          role: 'USER',
          fullName: null
        },
        rule: {
          username: [ruleRequired()],
          role: [ruleRequired()],
          fullName: [ruleRequired()]
        }
      }
    }
  },
  computed: {
    ...mapState('admin_user_edit', ['item', 'busy'])
  },
  created() {
    this.fetchData({ id: this.id })
      .then(() => {
        this.form.data.role = this.item.role
        this.form.data.username = this.item.username
        this.form.data.fullName = this.item.fullName
      })
  },
  methods: {
    ...mapActions('admin_user_edit', ['fetchData', 'updateUser']),
    submitForm() {
      const form = UserUpdateForm.create({ ...this.form.data })

      this.updateUser({ form })
        .then(() => this.$router.push({ name: 'admin.user.list' }))
    }
  }
}
</script>

<style scoped>

</style>
