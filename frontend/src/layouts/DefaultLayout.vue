<template>
  <v-layout>
    <v-main>
      <Toolbar></Toolbar>
      <router-view :key="$route.fullPath"></router-view>
    </v-main>
  </v-layout>
</template>
<script>
import Toolbar from '@/components/toolbar/Toolbar.vue'

export default {
  components: { Toolbar }
}
</script>
