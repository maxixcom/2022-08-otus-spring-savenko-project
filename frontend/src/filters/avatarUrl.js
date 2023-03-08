import Vue from 'vue'
import { avatarUrl } from '@/utils/ServiceUrls'

Vue.filter('avatarUrl', (value, size, hasImage, noCache) => {
  if (!value) return ''

  return avatarUrl(value, size, hasImage, noCache)
})
