import Page from '@/model/Page'
import CategoryService from '@/service/CategoryService'
import CategoryFilter from '@/model/category/CategoryFilter'

export const state = {
  items: [],
  filter: CategoryFilter.default(),
  itemsTotalCount: 0,
  page: {
    ...Page.default(),
    size: 10,
    sort: [{ field: 'title', order: 'asc' }]
  },
  pagesTotal: 0,
  busy: false
}

// mutations
export const mutations = {
  SET_FILTER(state, { filter }) {
    state.filter = CategoryFilter.create(filter) ?? CategoryFilter.default()
  },
  SET_ITEMS(state, { items }) {
    state.items = items
  },
  SET_ITEMS_TOTAL_COUNT(state, { count }) {
    state.itemsTotalCount = count ?? 0
  },
  SET_PAGE(state, { page }) {
    state.page = Page.create(page)
  },
  SET_PAGES_TOTAL(state, { count }) {
    state.pagesTotal = count ?? 0
  },
  SET_BUSY(state, { busy }) {
    state.busy = busy
  }
}

// getters
export const getters = {}

// actions
export const actions = {
  fetchData({ commit, dispatch }) {
    commit('SET_BUSY', { busy: true })
    Promise.all([dispatch('fetchCategories')])
      .finally(() => commit('SET_BUSY', { busy: false }))
  },
  fetchCategories({ state, commit }) {
    return CategoryService.find(state.filter, state.page)
      .then((result) => {
        commit('SET_ITEMS', { items: result.items })
        commit('SET_ITEMS_TOTAL_COUNT', { count: result.meta.totalElements })
        commit('SET_PAGES_TOTAL', { count: result.meta.totalPages })
      })
  },
  deleteCategory({ commit, dispatch }, { id }) {
    commit('SET_BUSY', { busy: true })

    return CategoryService.delete({ id })
      .then(() => dispatch('fetchCategories'))
      .finally(() => commit('SET_BUSY', { busy: false }))
  }
}
