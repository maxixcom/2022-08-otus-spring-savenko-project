// state
import AuthService from '@/service/AuthService'
import config from '@/configs'
import store from '@/store'

function parseJwt(token) {
  // eslint-disable-next-line prefer-destructuring
  const base64Url = token.split('.')[1]
  const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/')
  const jsonPayload = decodeURIComponent(atob(base64).split('').map((c) => {
    return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)
  }).join(''))

  return JSON.parse(jsonPayload)
}

export const state = {
  token: null,
  principal: null,
  permissions: [],
  profile: null
}

// mutations
export const mutations = {
  SET_TOKEN(state, { token, rememberMe }) {
    const { sub, authorities, exp, iat } = parseJwt(token)

    console.log(parseJwt(token))

    state.token = token
    state.principal = sub
    state.permissions = authorities

    if (rememberMe) {
      localStorage.setItem('token', token)
    }
  },

  CLEAR_TOKEN(state) {
    state.token = null
    state.principal = null
    state.permissions = []

    localStorage.removeItem('token')
  }
}

// getters
export const getters = {
  token: (state) => state.token,
  hasToken: (state) => !!state.token,
  user: (state) => {
    return { permissions: state.permissions }
  }
}

// actions
export const actions = {
  async restoreLogin({ commit, dispatch }, { token }) {
    commit('SET_TOKEN', { token, rememberMe: false })
    await dispatch('profile/fetchProfile',{},{ root: true })
  },

  login({ commit }, { loginForm }) {
    const { rememberMe } = loginForm

    return AuthService.login(loginForm)
      .then((token) => {
        commit('SET_TOKEN', { token, rememberMe })
      })
  },

  logout({ commit }) {
    commit('CLEAR_TOKEN')
    commit('profile/CLEAR_PROFILE', null, { root: true })
    commit('app/RESET_APP', null, { root: true })
  }
}
