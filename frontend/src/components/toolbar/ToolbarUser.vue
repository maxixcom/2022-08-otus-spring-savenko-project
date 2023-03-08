<template>
  <v-menu offset-y left transition="slide-y-transition">
    <template v-slot:activator="{ on }">
      <v-btn icon class="elevation-2" v-on="on">
        <v-badge
          color="success"
          dot
          bordered
          offset-x="10"
          offset-y="10"
        >
          <v-avatar size="40">
            <v-img :src="profileId | avatarUrl('thumb', hasImage, true)"></v-img>
            <!--            <v-img src="/images/avatars/avatar1.svg"></v-img>-->
          </v-avatar>
        </v-badge>
      </v-btn>
    </template>

    <!-- user menu list -->
    <v-list dense nav>
      <v-list-item
        v-for="(item, index) in menu"
        :key="index"
        v-can:access="item.acl"
        :exact="item.exact"
        :disabled="item.disabled"
        :to="item.link"
        link
      >
        <v-list-item-icon>
          <v-icon small :class="{ 'grey--text': item.disabled }">{{ item.icon }}</v-icon>
        </v-list-item-icon>
        <v-list-item-content>
          <v-list-item-title>{{ item.text }}</v-list-item-title>
        </v-list-item-content>
      </v-list-item>

      <v-divider class="my-1"></v-divider>

      <v-list-item link @click="logout">
        <v-list-item-icon>
          <v-icon small>mdi-logout-variant</v-icon>
        </v-list-item-icon>
        <v-list-item-content>
          <v-list-item-title>Выйти</v-list-item-title>
        </v-list-item-content>
      </v-list-item>
    </v-list>
  </v-menu>
</template>

<script>
/*
|---------------------------------------------------------------------
| Toolbar User Component
|---------------------------------------------------------------------
|
| Quickmenu for user menu shortcuts on the toolbar
|
*/
import { mapGetters } from 'vuex'

export default {
  name: 'ToolbarUser',
  data() {
    return {
      menu: [
        { icon: 'mdi-video', text: 'Администрирование', link: { name: 'admin' }, acl: { type: 'acl', id: 'ADMIN_AREA' } },
        { icon: 'mdi-account-box-outline', text: 'Профиль', link: { name: 'profile' }, acl: { type: 'acl', id: 'USER_AREA' } }
      ]
    }
  },
  computed: {
    ...mapGetters('profile', ['hasImage', 'profileId'])
  },
  methods: {
    logout() {
      this.$store.dispatch('auth/logout')
      this.$router.push({ name: 'auth.signin' })
    }
  }
}
</script>
