import '@mdi/font/css/materialdesignicons.css'
import Vue from 'vue'

// For full framework
// import Vuetify from 'vuetify/lib/framework'
// For a-la-carte components - https://vuetifyjs.com/en/customization/a-la-carte/
import Vuetify from 'vuetify/lib'

import * as directives from 'vuetify/lib/directives'
import config from '../configs'

/**
 * Vuetify Plugin
 * Main components library
 *
 * https://vuetifyjs.com/
 *
 */
Vue.use(Vuetify, {
  directives
})

export default new Vuetify({
  theme: {
    dark: config.theme.globalTheme === 'dark',
    options: {
      customProperties: true
    },
    themes: {
      dark: config.theme.dark,
      light: config.theme.light
    },
    icons: {
      iconfont: 'mdi' // default - only for display purposes
    }
  }
})
