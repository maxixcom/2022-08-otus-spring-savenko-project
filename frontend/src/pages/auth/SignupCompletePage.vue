<template>
  <v-container class="py-6">
    <v-responsive max-width="700" class="mx-auto text-center">
      <div class="text-overline secondary--text mb-3">Account</div>
      <h2 class="text-h3 text-lg-h2 mb-6">Регистрация</h2>
    </v-responsive>

    <v-card class="pa-4 mx-auto" max-width="600">
      <v-card-text>
        Регистрация вашего аккаунта прошла успешно. Теперь вы можете
        <router-link :to="{ name: 'auth.signin' }">войти на сайт</router-link>
        .
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script>
export default {
  name: 'SignupCompletePage',
  head: {
    title: function () {
      return {
        inner: 'Sign up complete'
      }
    }
  }
}
</script>

<style scoped>

</style>
