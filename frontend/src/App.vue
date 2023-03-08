<template>
  <v-app>
    <Loading ref="loading"></Loading>
    <transition name="page" mode="out-in">
      <component :is="layout" v-if="layout" />
    </transition>
  </v-app>
</template>

<script>
import config from './configs'
import Loading from '@/components/layout/Loading'

// Load layout components dynamically.
const requireContext = require.context('./layouts', false, /.*\.vue$/)

const layouts = requireContext.keys()
  .map((file) =>
    [file.replace(/(^.\/)|(\.vue$)/g, ''), requireContext(file)]
  )
  .reduce((components, [name, component]) => {
    components[name] = component.default || component

    return components
  }, {})

/*
|---------------------------------------------------------------------
| Main Application Component
|---------------------------------------------------------------------
*/
export default {
  components: { Loading },
  data: () => ({
    layout: null,
    defaultLayout: 'DefaultLayout'
  }),
  mounted () {
    this.$loading = this.$refs.loading
  },
  methods: {
    /**
     * Set the application layout.
     *
     * @param {String} layout
     */
    setLayout (layout) {
      if (!layout || !layouts[layout]) {
        layout = this.defaultLayout
      }

      this.layout = layouts[layout]
    }
  },
  head: {
    link: [
      // adds config/icons into the html head tag
      // ...config.icons.map((href) => ({ rel: 'stylesheet', href }))
    ]
  }
}
</script>
