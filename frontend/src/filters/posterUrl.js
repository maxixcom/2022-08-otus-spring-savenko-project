import Vue from 'vue'
import { cdnUrl } from '@/utils/ServiceUrls'

Vue.filter('posterUrl', (value, noCache = false) => {
  if (!value) return ''

  let url = cdnUrl('/posters/' + value + '.jpg')

  if (noCache) {
    url += '?ts=' + Date.now()
  }

  return url
})
