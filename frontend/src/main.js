import Vue from 'vue'
import App from './App.vue'

// VUEX - https://vuex.vuejs.org/
import store from './store'

// VUE-ROUTER - https://router.vuejs.org/
import router from './router'

// PLUGINS
import vuetify from './plugins/vuetify'
import './plugins/vue-head'
// import './plugins/vue-gtag'
import './plugins/axios'
// import './plugins/toasted'
import './plugins/toastification'

import './plugins/vue-browser-acl'
// FILTERS
import './filters/capitalize'
import './filters/lowercase'
import './filters/uppercase'
import './filters/placeholder'
import './filters/trim'
import './filters/formatDate'
import './filters/formatDateTime'
import './filters/avatarUrl'
import './filters/posterUrl'

// STYLES
// Main Theme SCSS
import './assets/scss/theme.scss'
import ToastService from '@/service/ToastService'
import theme from '@/configs/theme'

// Set this to false to prevent the production tip on Vue startup.
Vue.config.productionTip = false

/*
|---------------------------------------------------------------------
| Main Vue Instance
|---------------------------------------------------------------------
|
| Render the vue application on the <div id="app"></div> in index.html
|
| https://vuejs.org/v2/guide/instance.html
|
*/
export default new Vue({
  vuetify,
  router,
  store,
  created() {
    const token = localStorage.getItem('token')

    if (token) {
      console.log('Restoring authentication ....')
      store.dispatch('auth/restoreLogin', { token })
      // store.commit('auth/SET_TOKEN', { token, rememberMe: false })
    }

    const themeDarkMode = localStorage.getItem('themeDarkMode')
    let isDark = false

    if (themeDarkMode) {
      if (themeDarkMode === 'system') {
        isDark = window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches
      }
    }
    vuetify.framework.theme.dark = isDark
  },
  // render: (h) => h(App)
  ...App
}).$mount('#app')

/**
 * Automatic switch dark mode
 * TODO: Not implemented yet
 */
window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', (e) => {
  const colorScheme = e.matches ? 'dark' : 'light'

  // console.log(store.getters['profile/hasProfile'])
  // console.log(store.getters['profile/settingsValue']('AdminDarkMode'))

  // ToastService.notify('Настройки ночного режима изменены. Перезагрузите страницу чтобы применить изменения')
  // console.log(store.getters('profile/settings', {}, { root: true }))
  // store.commit('app/SET_DARK_MODE', { type: newColorScheme === 'dark' }, { root: true })
})
