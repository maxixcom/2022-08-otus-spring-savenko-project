import Page from '@/model/Page'
import MovieFilter from '@/model/movie/MovieFilter'
import MovieService from '@/service/MovieService'
import CategoryService from '@/service/CategoryService'

export const state = {
  movies: {},
  filter: MovieFilter.default(),
  moviesCount: 0,
  page: {
    ...Page.default(),
    size: 12,
    sort: [{ field: 'title', order: 'asc' }]
  },
  pagesTotal: 0,
  categories: [],
  busy: false
}

// mutations
export const mutations = {
  SET_FILTER(state, { filter }) {
    state.filter = MovieFilter.create(filter) ?? MovieFilter.default()
  },
  SET_MOVIES(state, { movies }) {
    state.movies = movies ?? []
  },
  SET_MOVIES_TOTAL(state, { count }) {
    state.moviesCount = count ?? 0
  },
  SET_PAGE(state, { page }) {
    state.page = Page.create(page)
  },
  SET_PAGES_TOTAL(state, { count }) {
    state.pagesTotal = count ?? 0
  },
  SET_CATEGORIES(state, { categories }) {
    state.categories = categories ?? []
  },
  SET_BUSY(state, { busy }) {
    state.busy = busy
  }
}

// actions
export const actions = {
  setFilter({ state, commit, dispatch }, { filter }) {
    commit('SET_FILTER', { filter })
    commit('SET_PAGE', { page: { ...state.page, page: 0 } })

    commit('SET_BUSY', { busy: true })

    return dispatch('fetchMovies')
      .finally(() => commit('SET_BUSY', { busy: false }))
  },
  setPage({ state, commit, dispatch }, { page }) {
    commit('SET_PAGE', { page })

    commit('SET_BUSY', { busy: true })

    return dispatch('fetchMovies')
      .finally(() => commit('SET_BUSY', { busy: false }))
  },
  fetchMovies({ state, commit }) {
    return MovieService.find(state.filter, state.page)
      .then((result) => {
        commit('SET_MOVIES', { movies: result.items })
        commit('SET_MOVIES_TOTAL', { count: result.meta.totalElements })
        commit('SET_PAGES_TOTAL', { count: result.meta.totalPages })
      })
  },

  fetchCategories({ commit }) {
    return CategoryService.find({}, Page.create({ size: 100 }))
      .then((result) => {
        commit('SET_CATEGORIES', { categories: result.items })
      })
  },

  fetchData({ commit, dispatch }) {
    commit('SET_BUSY', { busy: true })
    Promise.all([dispatch('fetchMovies'), dispatch('fetchCategories')])
      .finally(() => commit('SET_BUSY', { busy: false }))
  }
}
