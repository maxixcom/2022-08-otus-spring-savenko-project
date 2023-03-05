<template>
  <v-container class="py-6">
    <v-responsive max-width="700" class="mx-auto text-center">
      <div class="text-overline secondary--text mb-3">Account</div>
      <h2 class="text-h3 text-lg-h2 mb-6">Регистрация</h2>
    </v-responsive>

    <v-card class="pa-4 mx-auto" max-width="600">
      <v-card-text>
        <v-form v-model="formValidity" @submit.prevent="submit">
          <v-text-field
            v-model="form.username"
            autocomplete="username"
            label="Имя пользователя"
            outlined
            :rules="rules.usernameRules"
            required
          ></v-text-field>
          <v-text-field
            v-model="form.password"
            autocomplete="new-password"
            label="Пароль"
            outlined
            :rules="rules.passwordRules"
            :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
            :type="showPassword ? 'text' : 'password'"
            required
            @click:append="showPassword = !showPassword"
          ></v-text-field>
          <div class="d-flex flex-column flex-md-row">
            <v-text-field
              v-model="form.firstName"
              autocomplete="name"
              label="Имя"
              outlined
              class="mr-md-2"
              :rules="rules.firstNameRules"
              required
            ></v-text-field>
            <v-text-field
              v-model="form.lastName"
              autocomplete="family-name"
              label="Фамилия"
              outlined
              :rules="rules.lastNameRules"
              required
            ></v-text-field>
          </div>
          <v-btn
            block
            class="secondary"
            x-large
            type="submit"
            :disabled="!formValidity"
          >Зарегистрироваться
          </v-btn>
        </v-form>

      </v-card-text>
    </v-card>

  </v-container>
</template>

<script>
import AuthService from '@/service/AuthService'
import RegisterForm from '@/model/auth/RegisterForm'
import ToastService from '@/service/ToastService'
import { ruleRequired } from '@/utils/FormRules'

export default {
  name: 'SignupPage',
  data: () => ({
    showPassword: false,
    formValidity: false,
    form: {
      firstName: '',
      lastName: '',
      username: '',
      password: ''
    },
    rules: {
      firstNameRules: [ruleRequired()],
      lastNameRules: [ruleRequired()],
      usernameRules: [ruleRequired()],
      passwordRules: [ruleRequired()]
    }
  }),
  created() {
    this.$store.dispatch('auth/logout')
  },
  methods: {
    submit() {
      const registerForm = RegisterForm.create(this.form)

      AuthService.register(registerForm)
        .then(() => {
          this.$router.push({ name: 'auth.signup-complete' })
        })
        .catch((error) => {
          const { status } = error.response

          if (status === 409) {
            ToastService.warning('Пользователь с таким именем уже зарегистрирован')
          } else {
            ToastService.error('Что-то пошло не так. Ошибка регистрации.')
          }
        })
    }
  },
  head: {
    title: function () {
      return {
        inner: 'Sign up'
      }
    }
  }
}
</script>

<style scoped>

</style>
