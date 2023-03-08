<template>
  <v-sheet>
    <v-container class="py-2 py-md-2">
      <h2 class="text-h5">{{ movie.title }}</h2>

      <v-divider class="my-2 mb-3"></v-divider>

      <v-card>
        <v-card-text>
          <v-sheet ref="container" color="black" height="400px" width="100%">
            <div
              id="player-container"
              style="width: 100%; height: 100%"
              class="d-flex justify-center align-center"
              v-html="movie.embedCode"
            ></div>
          </v-sheet>
        </v-card-text>
        <v-card-text class="mb-4">
          <div class="d-flex justify-end mb-2">
            <div v-show="!canRate" class="pt-2 pr-2 text-caption">
              <router-link :to="{ name: 'auth.signin' }">Войдите</router-link>
              или
              <router-link :to="{ name: 'auth.signup' }">зарегистрируйтесь</router-link>
              , чтобы делать оценки
            </div>
            <div class="align-self-md-center mx-2 text-caption">Голосов {{ ratingCount }}</div>
            <div class="align-self-md-center mx-2 text-caption">Оценка {{ rating }}</div>
            <v-rating
              :value="rating"
              hover
              x-large
              color="orange"
              background-color="grey"
              :readonly="!canRate || busy"
              @input="updateRating($event)"
            >
            </v-rating>
          </div>
          <div class="mb-4">
            <pre class="movie-description">{{ movie.description }}</pre>
          </div>
        </v-card-text>
      </v-card>

    </v-container>
  </v-sheet>
</template>

<script>
import { mapActions, mapGetters, mapState } from 'vuex'
import ToastService from '@/service/ToastService'

export default {
  name: 'MoviePage',
  props: {
    id: {
      type: [String, Number],
      required: true
    }
  },
  data() {
    return {
      canRate: false
    }
  },
  computed: {
    ...mapState('movie', ['movie', 'busy']),
    ...mapGetters('movie', ['rating', 'ratingCount'])
  },
  created() {
    this.fetchMovie({ id: this.id })
    this.canRate = this.$can('user')
  },
  methods: {
    ...mapActions('movie', ['fetchMovie', 'rateMovie']),
    updateRating(event) {
      this.rateMovie({ value: event })
        .then(() => {
          ToastService.success('Ваша оценка принята ' + event)
        })
    }
  }
}
</script>

<style scoped>
.movie-description {
  white-space: pre-wrap; /* Since CSS 2.1 */
  white-space: -moz-pre-wrap; /* Mozilla, since 1999 */
  white-space: -pre-wrap; /* Opera 4-6 */
  white-space: -o-pre-wrap; /* Opera 7 */
  word-wrap: break-word; /* Internet Explorer 5.5+ */
}
</style>
