<template>
  <v-list nav dense>
    <div v-for="(item, index) in items" :key="index">
      <div v-if="item.text" v-can:access="item.acl" class="pa-1 mt-2 overline">{{ item.text }}</div>
      <nav-menu v-can:access="item.acl" :menu="item.items"/>
    </div>
  </v-list>
</template>

<script>
import NavMenu from './NavMenu'

export default {
  components: {
    NavMenu
  },
  props: {
    menu: {
      type: Array,
      default: () => []
    }
  },
  data: function () {
    for (const item of this.menu) {
      if (!item.acl) {
        item.acl = { type: 'acl', id: 'ADMIN_AREA' }
        console.log('add item acl', item.acl)
      }
    }

    return {
      items: this.menu
    }
  }
}
</script>
