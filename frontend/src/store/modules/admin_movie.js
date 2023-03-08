import Page from '@/model/Page'
import MovieFilter from '@/model/movie/MovieFilter'
import MovieService from '@/service/MovieService'
import CategoryService from '@/service/CategoryService'

export const state = {
  items: [],
  filter: MovieFilter.default(),
  itemsTotalCount: 0,
  page: {
    ...Page.default(),
    size: 10,
    sort: [{ field: 'title', order: 'asc' }]
  },
  pagesTotal: 0,
  busy: false,

  categories: []
}

// mutations
export const mutations = {
  SET_FILTER(state, { filter }) {
    state.filter = MovieFilter.create(filter) ?? MovieFilter.default()
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
  },

  SET_CATEGORIES(state, { categories }) {
    state.categories = categories ?? []
  }
}

// getters
export const getters = {}

// actions
export const actions = {
  fetchData({ commit, dispatch }) {
    commit('SET_BUSY', { busy: true })

    return Promise.all([dispatch('fetchMovies'), dispatch('fetchCategories')])
      .finally(() => commit('SET_BUSY', { busy: false }))
  },

  fetchMovies({ state, commit }) {
    return MovieService.find(state.filter, state.page)
      .then((result) => {
        commit('SET_ITEMS', { items: result.items })
        commit('SET_ITEMS_TOTAL_COUNT', { count: result.meta.totalElements })
        commit('SET_PAGES_TOTAL', { count: result.meta.totalPages })
      })
  },

  fetchCategories({ commit }) {
    return CategoryService.find({}, Page.create({ size: 100 }))
      .then((result) => {
        commit('SET_CATEGORIES', { categories: result.items })
      })
  },

  deleteMovie({ commit, dispatch }, { id }) {
    commit('SET_BUSY', { busy: true })

    return MovieService.delete({ id })
      .then(() => dispatch('fetchMovies'))
      .finally(() => commit('SET_BUSY', { busy: false }))
  }
}
