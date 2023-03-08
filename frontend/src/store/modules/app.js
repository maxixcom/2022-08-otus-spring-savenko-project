import vuetify from '@/plugins/vuetify'
import config from '@/configs'

export const state = {
  themeDarkMode: config.theme.globalTheme
}

export const mutations = {
  SET_THEME_DARK_MODE(state, mode) {
    state.themeDarkMode = mode
    localStorage.setItem('themeDarkMode', state.themeDarkMode)
    if (state.themeDarkMode === 'system') {
      vuetify.framework.theme.dark = window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches
    } else {
      vuetify.framework.theme.dark = state.themeDarkMode === 'dark'
    }
  },
  RESET_APP(state) {
    const themeDarkMode = 'light'

    state.themeDarkMode = themeDarkMode
    localStorage.setItem('themeDarkMode', themeDarkMode)
    vuetify.framework.theme.dark = themeDarkMode === 'dark'
  }
}

export const getters = {}

export const actions = {}
