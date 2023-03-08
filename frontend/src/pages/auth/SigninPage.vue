<template>
  <v-container class="py-6">
    <v-responsive max-width="700" class="mx-auto text-center">
      <div class="text-overline secondary--text mb-3">Account</div>
      <h2 class="text-h3 text-lg-h2 mb-6">Ваш аккаунт</h2>
    </v-responsive>

    <v-card class="pa-4 mx-auto" max-width="600">
      <v-card-text>
        <v-form ref="form" v-model="formValidity" @submit.prevent="submit">
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
            autocomplete="current-password"
            :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
            :type="showPassword ? 'text' : 'password'"
            label="Пароль"
            outlined
            hide-details
            :rules="rules.passwordRules"
            required
            @click:append="showPassword = !showPassword"
          ></v-text-field>
          <v-checkbox v-model="form.rememberMe" dense label="Доверять этому браузеру"></v-checkbox>
          <v-btn
            type="submit"
            block
            class="secondary"
            x-large
            :disabled="!formValidity"
          >Войти
          </v-btn>
        </v-form>

      </v-card-text>
    </v-card>
  </v-container>
</template>

<script>
import ToastService from '@/service/ToastService'
import LoginForm from '@/model/auth/LoginForm'
import { ruleEmail, ruleRequired } from '@/utils/FormRules'

export default {
  name: 'SigninPage',
  data: () => ({
    showPassword: false,
    formValidity: false,
    form: {
      username: '',
      password: '',
      rememberMe: false
    },
    rules: {
      usernameRules: [ruleRequired()],
      passwordRules: [ruleRequired()]
    }
  }),
  created() {
    this.$store.dispatch('auth/logout')
  },
  methods: {
    submit() {
      const loginForm = LoginForm.create(this.form)

      this.$store.dispatch('auth/login', { loginForm })
        .then(() => this.$store.dispatch('profile/fetchProfile'))
        .then(() => {
          this.$router.push({ name: 'home' })
        })
        .catch(() => {
          ToastService.info('Ошибка авторизации.')
        })
    }
  },
  head: {
    title: function () {
      return {
        inner: 'Sign In'
      }
    }
  }
}
</script>

<style scoped>

</style>
