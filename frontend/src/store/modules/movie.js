import MovieService from '@/service/MovieService'
import RatingService from '@/service/RatingService'

export const state = {
  movie: {},
  busy: false
}

export const getters = {
  rating: (state) => state.movie?.rating ?? 0,
  ratingCount: (state) => state.movie?.ratingCount ?? 0
}

// mutations
export const mutations = {
  SET_MOVIE(state, { movie }) {
    state.movie = movie
  },
  SET_BUSY(state, { busy }) {
    state.busy = busy
  }
}

// actions
export const actions = {
  fetchMovie({ state, commit }, { id }) {
    commit('SET_BUSY', { busy: true })

    return MovieService.findById({ id })
      .then((result) => {
        commit('SET_MOVIE', { movie: result })
      })
      .finally(() => commit('SET_BUSY', { busy: false }))
  },

  rateMovie({ state, commit }, { value }) {
    commit('SET_BUSY', { busy: true })

    return RatingService.rateMovie({ id: state.movie.id, value })
      .then((rating) => {
        const movie = {
          ... state.movie,
          ... rating
        }

        commit('SET_MOVIE', { movie })
      })
      .finally(() => commit('SET_BUSY', { busy: false }))
  }

}
