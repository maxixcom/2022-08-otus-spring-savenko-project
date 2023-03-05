<template>
  <v-sheet>
    <v-container class="py-2 py-md-2">
      <h2 class="text-h5">Каталог</h2>
      <div class="d-flex flex-wrap justify-space-between">
        <v-chip-group
          v-model="filterCategories"
          column
          multiple
          @change="updateCatalog"
        >
          <v-chip
            v-for="category in categories"
            :key="category.id"
            :value="category.id"
            filter
            outlined
          >
            {{ category.title }}
          </v-chip>
        </v-chip-group>
        <v-text-field
          v-model="filterSearch"
          placeholder="Search"
          prepend-inner-icon="mdi-magnify"
          hide-details
          filled
          dense
          style="max-width: 400px"
          rounded
          @input="updateCatalog"
        ></v-text-field>
      </div>

      <v-divider class="my-2 mb-3"></v-divider>
      <div v-if="busy">
        <v-row>
          <v-col
            v-for="n in 6"
            :key="n"
            cols="12"
            xl="2"
            lg="4"
            md="6"
            sm="6"
          >
            <v-skeleton-loader
              width="100%"
              max-height="350"
              type="card"
            ></v-skeleton-loader>
          </v-col>
        </v-row>
      </div>
      <div v-else>
        <v-row v-for="(columns, i) in chunkedItems" :key="i">
          <v-col
            v-for="item in columns"
            :key="item.id"
            cols="12"
            xl="4"
            lg="4"
            md="6"
            sm="6"
          >
            <v-card>
              <v-card-text>
                <router-link :to="{ name: 'movie', params: { id: item.id }}" class="text-decoration-none">
                  <v-img v-if="item.hasPoster" width="100%" height="250" :src="item.id | posterUrl"></v-img>
                  <v-sheet
                    v-else
                    color="grey"
                    width="100%"
                    height="250"
                    class="d-flex justify-center"
                  >
                    <v-icon large>mdi-movie-play</v-icon>
                  </v-sheet>
                </router-link>
                <div v-if="item.category!=null" class="mt-1">
                  <v-chip color="secondary">{{ item.category.title }}</v-chip>
                </div>
              </v-card-text>
              <v-card-title>{{ item.title }}</v-card-title>
              <v-card-actions class="d-flex justify-space-between">
                <v-rating
                  :value="item.rating"
                  color="orange"
                  background-color="grey"
                  readonly
                  dense
                ></v-rating>
                <v-btn
                  small
                  text
                  outlined
                  :to="{ name: 'movie', params: { id: item.id } }"
                  class="mr-1"
                >Смотреть
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-col>
        </v-row>
        <div v-if="chunkedItems.length==0" class="text--secondary d-flex justify-center pa-5">
          Ничего не найдено
        </div>
      </div>

      <v-pagination
        v-if="pagesTotal>1"
        class="my-5"
        :value="currentPage"
        :length="pagesTotal"
        @input="setPage"
      ></v-pagination>
    </v-container>
  </v-sheet>
</template>

<script>
import { mapState } from 'vuex'
import _ from 'lodash'
import lodash from 'lodash'

export default {
  name: 'IndexPage',
  data: function () {
    return {
      filterCategories: [],
      filterSearch: null
    }
  },
  computed: {
    ...mapState('index', ['busy', 'categories', 'movies', 'filter', 'pagesTotal', 'page']),
    ...mapState('index', {
      chunkedItems: (state) => lodash.chunk(state.movies, 6),
      currentPage: (state) => parseInt(state.page, 10)
    })
  },
  created() {
    this.$store.dispatch('index/fetchData')
    this.filterCategories = this.filter.categoryIds
    this.filterSearch = this.filter.search
  },
  methods: {
    updateCatalog: _.debounce(function () {
      const filter = {
        ...this.filter,
        categoryIds: this.filterCategories,
        search: this.filterSearch
      }

      this.$store.dispatch('index/setFilter', { filter })
    }, 500),
    setPage(n) {
      const page = {
        ...this.page,
        page: n - 1
      }

      this.$store.dispatch('index/setPage', { page })
    }
  }
}
</script>

<style scoped>

</style>
