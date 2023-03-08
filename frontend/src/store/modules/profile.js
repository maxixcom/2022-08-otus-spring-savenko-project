// state
import LoginProfile from '@/model/profile/LoginProfile'
import LoginProfileService from '@/service/LoginProfileService'

export const state = {
  profile: null,
  busy: false
}

// mutations
export const mutations = {
  SET_PROFILE(state, { profile }) {
    state.profile = LoginProfile.create(profile)
  },
  CLEAR_PROFILE(state) {
    state.profile = null
  },
  SET_BUSY(state, { busy }) {
    state.busy = busy
  }
}

// getters
export const getters = {
  profileId: (state) => state?.profile?.id,
  profileUsername: (state) => state?.profile?.username,
  profileFullName: (state) => state?.profile?.fullName,
  profile: (state) => state.profile,
  hasProfile: (state) => !!state.profile,
  hasImage: (state) => !!state.profile?.hasImage ?? false
}

// actions
export const actions = {
  fetchData({ commit, dispatch }) {
    commit('SET_BUSY', { busy: true })

    return Promise.all([dispatch('fetchProfile')])
      .finally(() => commit('SET_BUSY', { busy: false }))
  },

  fetchProfile({ commit, getters }) {
    return LoginProfileService.load()
      .then((profile) => {
        commit('SET_PROFILE', { profile })
      })
  },

  updateProfile({ commit }, { form }) {
    return LoginProfileService.update({ form })
      .then((profile) => {
        commit('SET_PROFILE', { profile })
      })
  },

  changePassword({ commit }, { form }) {
    commit('SET_BUSY', { busy: true })

    return LoginProfileService.changePassword({ form })
      .finally(() => commit('SET_BUSY', { busy: false }))
  }
}
