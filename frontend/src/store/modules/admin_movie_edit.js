import MovieService from '@/service/MovieService'
import CategoryService from '@/service/CategoryService'
import Page from '@/model/Page'

export const state = {
  id: null,
  item: null,
  busy: false,
  categories: []
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
  },
  SET_CATEGORIES(state, { categories }) {
    state.categories = categories ?? []
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

    return Promise.all([dispatch('fetchMovie'),dispatch('fetchCategories')])
      .finally(() => commit('SET_BUSY', { busy: false }))
  },
  fetchMovie({ state, commit }) {
    return MovieService.findById({ id: state.id })
      .then((result) => {
        commit('SET_ITEM', { item: result })
      })
  },
  fetchCategories({ commit }) {
    return CategoryService.find({}, Page.create({ size: 100 }))
      .then((result) => {
        commit('SET_CATEGORIES', { categories: result.items })
      })
  },
  updateMovie({ state, commit }, { form }) {
    commit('SET_BUSY', { busy: true })

    return MovieService.update({ id: state.id, form })
      .then((result) => {
        commit('SET_ITEM', { item: result })
      })
      .finally(() => commit('SET_BUSY', { busy: false }))
  }
}
