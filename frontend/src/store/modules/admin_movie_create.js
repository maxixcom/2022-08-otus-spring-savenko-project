import MovieService from '@/service/MovieService'
import CategoryService from '@/service/CategoryService'
import Page from '@/model/Page'

export const state = {
  busy: false,
  categories: []
}

// mutations
export const mutations = {
  SET_BUSY(state, { busy }) {
    state.busy = busy
  },
  SET_CATEGORIES(state, { categories }) {
    state.categories = categories ?? []
  }
}

// getters
export const getters = {
}

// actions
export const actions = {
  fetchData({ commit, dispatch }) {
    commit('SET_BUSY', { busy: true })

    return Promise.all([dispatch('fetchCategories')])
      .finally(() => commit('SET_BUSY', { busy: false }))
  },

  fetchCategories({ commit }) {
    return CategoryService.find({}, Page.create({ size: 100 }))
      .then((result) => {
        commit('SET_CATEGORIES', { categories: result.items })
      })
  },

  createMovie({ state, commit }, { form }) {
    commit('SET_BUSY', { busy: true })

    return MovieService.create( { form } )
      .finally(() => commit('SET_BUSY', { busy: false }))
  }
}
