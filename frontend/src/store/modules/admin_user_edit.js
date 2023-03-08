import UserService from '@/service/UserService'

export const state = {
  id: null,
  item: null,
  busy: false
}

// mutations
export const mutations = {
  SET_ITEM_ID(state, { id }) {
    state.id = id
  },
  SET_ITEM(state, { item }) {
    state.item = item
  },
  SET_BUSY(state, { busy }) {
    state.busy = busy
  }
}

// getters
export const getters = {
  itemId: (state) => state.id
}

// actions
export const actions = {
  fetchData({ commit, dispatch }, { id }) {
    commit('SET_BUSY', { busy: true })
    commit('SET_ITEM_ID', { id })

    return Promise.all([dispatch('fetchUser')])
      .finally(() => commit('SET_BUSY', { busy: false }))
  },

  fetchUser({ state, commit }) {
    return UserService.findById({ id: state.id })
      .then((result) => {
        commit('SET_ITEM', { item: result })
      })
  },

  updateUser({ state, commit }, { form }) {
    commit('SET_BUSY', { busy: true })

    return UserService.update({ id: state.id, form })
      .then((result) => {
        commit('SET_ITEM', { item: result })
      })
      .finally(() => commit('SET_BUSY', { busy: false }))
  }
}
