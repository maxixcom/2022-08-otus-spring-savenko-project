import MovieService from '@/service/MovieService'

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
  itemId: (state) => state.id,
  embedCode: (state) => state.item?.embedCode,
  hasPoster:  (state) => state.item?.hasPoster ?? false
}

// actions
export const actions = {
  fetchData({ commit, dispatch }, { id }) {
    commit('SET_BUSY', { busy: true })
    commit('SET_ITEM_ID', { id })

    return Promise.all([dispatch('fetchMovie')])
      .finally(() => commit('SET_BUSY', { busy: false }))
  },
  fetchMovie({ state, commit }) {
    return MovieService.findById({ id: state.id })
      .then((result) => {
        commit('SET_ITEM', { item: result })
      })
  },

  deletePoster({ state, commit, dispatch }) {
    commit('SET_BUSY', { busy: true })

    return MovieService.deletePoster({ id: state.id })
      .then((result) => {
        return dispatch('fetchMovie')
      })
      .finally(() => commit('SET_BUSY', { busy: false }))
  }
}
