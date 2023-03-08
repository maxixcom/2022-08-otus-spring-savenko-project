import Vue from 'vue'

export default {
  notify(message) {
    const options = {
      icon: 'mdi mdi-alien'
    }

    Vue.$toast(message, options)
  },

  info(message) {
    Vue.$toast.info(message)
  },

  success(message) {
    Vue.$toast.success(message)
  },

  error(message) {
    Vue.$toast.error(message)
  },

  warning(message) {
    Vue.$toast.warning(message)
  },

  notImplementedYet() {
    this.warning('Эта функция пока не реализована')
  }
}
