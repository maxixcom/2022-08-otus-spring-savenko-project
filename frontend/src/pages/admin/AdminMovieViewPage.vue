<template>
  <div>
    <h1 class="text-h4">Управление видеороликами</h1>
    <v-divider class="my-2"></v-divider>
    <v-card class="mb-2">
      <v-card-title>Обложка видеоролика</v-card-title>
      <v-card-text>
        <v-img v-if="hasPoster" contain max-height="300px" :src="id | posterUrl(true)"></v-img>
        <v-sheet
          v-else
          color="grey"
          min-height="250px"
          width="100%"
          class="d-flex justify-center"
        >
          <v-icon large>mdi-movie-play</v-icon>
        </v-sheet>

      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn :to="{name: 'admin.movie.list'}">К списку</v-btn>
        <v-btn color="error" @click="deletePoster">Удалить</v-btn>
        <v-btn @click="showUploadPosterDialog=true">Загрузить</v-btn>
      </v-card-actions>
    </v-card>
    <v-card>
      <v-card-title>Предварительный просмотр</v-card-title>
      <v-card-text>
        <div style="height: 450px" v-html="embedCode"></div>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn :to="{name: 'admin.movie.list'}">К списку</v-btn>
        <v-btn color="primary" :to="{name: 'admin.movie.edit', params: { id } }">Редактировать</v-btn>
      </v-card-actions>
    </v-card>
    <upload-poster-dialog
      v-model="showUploadPosterDialog"
      :movie-id="id"
      :on-success-upload="posterUploadedCallback"
    ></upload-poster-dialog>
  </div>
</template>

<script>
import { dashboardPageCommonAttributes } from '@/mixins/DashboardPageCommonAttributes'
import { mapActions, mapGetters, mapState } from 'vuex'
import UploadPosterDialog from '@/components/ui/dialog/UploadPosterDialog.vue'

export default {
  ...dashboardPageCommonAttributes,
  name: 'AdminMovieViewPage',
  components: { UploadPosterDialog },
  props: {
    id: {
      type: [String, Number],
      required: true
    }
  },
  data() {
    return {
      showUploadPosterDialog: false
    }
  },
  computed: {
    ...mapState('admin_movie_view', ['item', 'busy']),
    ...mapGetters('admin_movie_view', ['embedCode', 'hasPoster'])
  },
  created() {
    this.fetchData({ id: this.id })
  },
  methods: {
    ...mapActions('admin_movie_view', ['fetchData', 'deletePoster']),
    posterUploadedCallback() {
      this.fetchData({ id: this.id })
    }
  }
}
</script>

<style scoped>
</style>
