import Page from '@/model/Page'
import UserService from '@/service/UserService'
import UserFilter from '@/model/user/UserFilter'

export const state = {
  items: [],
  filter: UserFilter.default(),
  itemsTotalCount: 0,
  page: {
    ...Page.default(),
    size: 10,
    sort: [{ field: 'username', order: 'asc' }]
  },
  pagesTotal: 0,
  busy: false,

  categories: []
}

// mutations
export const mutations = {
  SET_FILTER(state, { filter }) {
    state.filter = UserFilter.create(filter) ?? UserFilter.default()
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

    return Promise.all([dispatch('fetchUsers')])
      .finally(() => commit('SET_BUSY', { busy: false }))
  },

  fetchUsers({ state, commit }) {
    return UserService.find(state.filter, state.page)
      .then((result) => {
        commit('SET_ITEMS', { items: result.items })
        commit('SET_ITEMS_TOTAL_COUNT', { count: result.meta.totalElements })
        commit('SET_PAGES_TOTAL', { count: result.meta.totalPages })
      })
  },

  deleteUser({ commit, dispatch }, { id }) {
    commit('SET_BUSY', { busy: true })

    return UserService.delete({ id })
      .then(() => dispatch('fetchUsers'))
      .finally(() => commit('SET_BUSY', { busy: false }))
  }
}
