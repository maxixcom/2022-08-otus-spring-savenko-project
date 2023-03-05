import icons from './icons'
import theme from './theme'

export default {
  // product display information
  product: {
    name: 'Movies Online',
    version: 'v0.0.1-SNAPSHOT'
  },

  // icon libraries
  icons,

  // theme configs
  theme,

  // API
  apiUrl: process.env.VUE_APP_API_URL,
  cdnUrl: process.env.VUE_APP_CDN_URL
}
