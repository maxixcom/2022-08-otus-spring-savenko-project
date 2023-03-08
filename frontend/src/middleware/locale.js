// import store from '../store'
// import { loadMessages } from '../plugins/i18n'
/**
 * (!) Not used yet.
 *
 * It's for multilingual purposes.
 *
 * @param to
 * @param from
 * @param next
 * @returns {Promise<void>}
 */
export default async (to, from, next) => {
  // await loadMessages(store.getters['lang/locale'])

  next()
}
