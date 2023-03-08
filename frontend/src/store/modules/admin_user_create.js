import UserService from '@/service/UserService'

export const state = {
  busy: false
}

// mutations
export const mutations = {
  SET_BUSY(state, { busy }) {
    state.busy = busy
  }
}

// actions
export const actions = {
  createUser({ state, commit }, { form }) {
    commit('SET_BUSY', { busy: true })

    return UserService.create({ form })
      .finally(() => commit('SET_BUSY', { busy: false }))
  }
}
