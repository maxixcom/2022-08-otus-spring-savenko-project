<template>
  <div>
    <v-navigation-drawer v-model="drawer" app temporary>
      <v-list dense nav>
        <v-subheader class="text-uppercase font-weight-bold">Меню</v-subheader>
        <v-list-item v-for="(item, index) in menu" :key="index" :to="item.link">
          <v-list-item-content>
            <v-list-item-title>{{ item.title }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
      <template v-slot:append>
        <div class="pa-2">
          <v-btn block class="mb-2" :to="{name:'auth.signin'}">
            Войти
          </v-btn>
          <v-btn block class="primary" :to="{name:'auth.signup'}">
            Регистрация
          </v-btn>
        </div>
      </template>
    </v-navigation-drawer>

    <v-btn
      class="d-md-none drawer-button"
      rounded
      @click="drawer = !drawer"
    >
      <v-icon right>mdi-menu</v-icon>
    </v-btn>

    <v-app-bar app height="80">
      <v-container class="py-0 px-0 px-sm-2 fill-height" :fluid="isFluid">
        <router-link :to="{ name: 'home' }" class="d-flex align-center text-decoration-none mr-2">
          <v-icon large color="red">mdi-video-vintage</v-icon>
          <span class="font-weight-black text-uppercase">
          </span>
        </router-link>

        <div class="d-none d-md-block">
          <v-btn
            v-for="(item, index) in menu"
            :key="index"
            text
            :to="item.link"
            class="mx-1"
          >
            {{ item.title }}
          </v-btn>
        </div>

        <v-spacer></v-spacer>

        <!-- search input desktop -->
        <!--        <v-text-field-->
        <!--          ref="search"-->
        <!--          class="hidden-xs-only mx-2"-->
        <!--          placeholder="Поиск"-->
        <!--          prepend-inner-icon="mdi-magnify"-->
        <!--          hide-details-->
        <!--          filled-->
        <!--          dense-->
        <!--          style="max-width: 600px"-->
        <!--          rounded-->
        <!--        ></v-text-field>-->

        <v-btn v-if="!hasProfile" text :to="{name:'auth.signup'}">Регистрация</v-btn>
        <v-btn v-if="!hasProfile" text color="primary" :to="{name:'auth.signin'}">Войти</v-btn>

        <div v-if="hasProfile" class="mr-2 mt-1">{{ profileFullName }}</div>

        <ToolbarUser v-if="hasProfile"></ToolbarUser>

      </v-container>
    </v-app-bar>
  </div>
</template>

<script>
import { menu2 as menu } from './menu.js'
import ToolbarUser from '@/components/toolbar/ToolbarUser.vue'
import { mapGetters } from 'vuex'

export default {
  components: { ToolbarUser },
  props: {
    isFluid: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      drawer: null,
      menu
    }
  },
  computed: {
    ...mapGetters('profile', ['hasProfile','profileFullName'])
  }
}
</script>

<style scoped>
.drawer-button {
  position: fixed;
  top: 60px;
  left: -22px;
  z-index: 6;
}
</style>
