import CategoryService from '@/service/CategoryService'

export const state = {
  busy: false
}

// mutations
export const mutations = {
  SET_BUSY(state, { busy }) {
    state.busy = busy
  }
}

// getters
export const getters = {
}

// actions
export const actions = {
  createCategory({ state, commit }, { form }) {
    commit('SET_BUSY', { busy: true })

    return CategoryService.create( { form } )
      .finally(() => commit('SET_BUSY', { busy: false }))
  }
}
