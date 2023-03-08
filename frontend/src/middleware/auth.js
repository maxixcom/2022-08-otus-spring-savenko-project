import store from '../store'

/**
 * Check if user has token and have profile loaded.
 * Without token user is redirected to login page.
 * If profile isn't loaded we try to load it.
 *
 * @param to
 * @param from
 * @param next
 * @returns {Promise<void>}
 */
export default async (to, from, next) => {
  if (!store.getters['auth/hasToken']) {
    next({ name: 'auth.signin' })

    return
  }

  if (!store.getters['profile/hasProfile']) {

    store.dispatch('profile/fetchProfile')
      .then((result) => next())

    return
  }

  next()
}
