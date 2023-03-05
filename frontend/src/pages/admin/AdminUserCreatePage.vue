<template>
  <div>
    <h1 class="text-h4">Управление пользователями</h1>
    <v-divider class="my-2"></v-divider>
    <v-form v-model="form.valid" :disabled="busy" @submit.prevent="submitForm">
      <v-card>
        <v-card-title>Новый аккаунт</v-card-title>
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
          <v-text-field
            v-model="form.data.password"
            :append-icon="form.show.password ? 'mdi-eye' : 'mdi-eye-off'"
            label="Пароль"
            counter="150"
            :rules="form.rule.password"
            :type="form.show.password ? 'text' : 'password'"
            @click:append="form.show.password = !form.show.password"
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
import UserCreateForm from '@/model/user/UserCreateForm'

export default {
  ...dashboardPageCommonAttributes,
  name: 'AdminUserCreatePage',
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
          fullName: null,
          password: null
        },
        rule: {
          username: [ruleRequired()],
          role: [ruleRequired()],
          fullName: [ruleRequired()],
          password: [ruleRequired()]
        },
        show: {
          password: false
        }
      }
    }
  },
  computed: {
    ...mapState('admin_user_create', ['busy'])
  },
  methods: {
    ...mapActions('admin_user_create', ['createUser']),
    submitForm() {
      const form = UserCreateForm.create(this.form.data)

      this.createUser({ form })
        .then(() => this.$router.push({ name: 'admin.user.list' }))
    }
  }
}
</script>

<style scoped>

</style>
