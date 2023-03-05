import Vue from 'vue'
import { format, parseISO } from 'date-fns'

Vue.filter('formatDateTime', (value, filterFormat) => {
  if (value) {
    if (value instanceof Date) {
      return format(value, filterFormat || 'yyyy-MM-dd HH:mm:ss')
    } else {
      return format(parseISO(value), filterFormat || 'yyyy-MM-dd HH:mm:ss')
    }
  }

  return ''
})
