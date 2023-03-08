<template>
  <div>
    <h1 class="text-h4">Управление пользователями</h1>
    <v-divider class="my-2"></v-divider>
    <v-card>
      <v-card-title>
        Пользователи
        <v-spacer></v-spacer>
        <v-text-field
          v-model="table.filter.search"
          append-icon="mdi-magnify"
          label="Поиск"
          single-line
          hide-details
          clearable
          @input="onInputFilter"
        ></v-text-field>
        <v-btn icon class="ml-2 mt-1" @click="showAdvancedSearch=!showAdvancedSearch">
          <v-icon>{{ showAdvancedSearch ? 'mdi-chevron-up' : 'mdi-chevron-down' }}</v-icon>
        </v-btn>
      </v-card-title>
      <transition name="slide">
        <v-card-text v-show="showAdvancedSearch">
          <v-chip-group
            v-model="table.filter.roles"
            column
            multiple
            @change="onInputFilter"
          >
            <v-chip
              v-for="role in roles"
              :key="role.id"
              :value="role.id"
              filter
              outlined
            >
              {{ role.title }}
            </v-chip>
          </v-chip-group>
        </v-card-text>
      </transition>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-tooltip bottom>
          <template v-slot:activator="{ on, attrs }">
            <v-btn
              fab
              color="success"
              :disabled="busy"
              v-bind="attrs"
              :to="{ name: 'admin.user.create' }"
              v-on="on"
            >
              <v-icon>mdi-account-plus-outline</v-icon>
            </v-btn>
          </template>
          <span>Добавить пользователя</span>
        </v-tooltip>
      </v-card-actions>
      <v-data-table
        :loading="busy"
        :headers="table.headers"
        :items="items"
        :server-items-length="itemsTotalCount"
        :footer-props="table.footerProps"
        :options.sync="table.options"
      >
        <template v-slot:item.actions="{ item }">
          <v-btn
            fab
            text
            small
            :to="{ name: 'admin.user.edit', params: { id: item.id} }"
            class="mr-1"
          >
            <v-icon small>mdi-pencil</v-icon>
          </v-btn>
          <v-btn
            fab
            text
            small
            @click="deleteAction(item)"
          >
            <v-icon small>mdi-delete</v-icon>
          </v-btn>
        </template>
      </v-data-table>
    </v-card>

    <v-dialog v-model="deleteDialog.show" persistent max-width="600px">
      <v-card>
        <v-card-title class="title error--text">Удаление данных</v-card-title>
        <v-card-text class="my-2">
          <div class="text-body-2">
            Вы действительно хотите произвести удаление данных?
          </div>
        </v-card-text>
        <v-card-actions>
          <v-btn text color="error" @click="deleteItemConfirm">
            Продолжить
          </v-btn>
          <v-btn text @click="deleteDialog.show = false">
            Отмена
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import { dashboardPageCommonAttributes } from '@/mixins/DashboardPageCommonAttributes'
import { mapActions, mapMutations, mapState } from 'vuex'
import { Asc, Desc } from '@/model/SortOrder'
import Page from '@/model/Page'
import _ from 'lodash'
import UserFilter from '@/model/user/UserFilter'

export default {
  ...dashboardPageCommonAttributes,
  name: 'AdminUserPage',
  data: function () {
    return {
      showAdvancedSearch: false,
      roles: [
        { id: 'USER', title: 'User' },
        { id: 'ADMIN', title: 'Admin' }
      ],
      table: {
        options: {},
        headers: [
          { text: 'Id', align: 'left', value: 'id' },
          { text: 'Логин', value: 'username' },
          { text: 'Роль', value: 'role' },
          { text: 'Имя', value: 'fullName' },
          { text: 'Действия', value: 'actions', sortable: false, align: 'right' }
        ],
        filter: {
          search: '',
          roles: []
        },
        footerProps: {
          itemsPerPageOptions: [5, 10, 15, 30, 50],
          itemsPerPageText: 'Строк на странице:',
          // showCurrentPage:true,
          showFirstLastPage: true
        }
      },
      deleteDialog: {
        show: false,
        value: []
      }
    }
  },
  computed: {
    ...mapState('admin_user', ['items', 'busy', 'itemsTotalCount', 'filter'])
  },
  watch: {
    'table.options': {
      handler() {
        this.loadTable()
      },
      deep: true
    }
  },
  created() {
    this.table.filter.roles = this.filter.roles
  },
  methods: {
    ...mapMutations('admin_user', {
      setPage: 'SET_PAGE',
      setFilter: 'SET_FILTER'
    }),
    ...mapActions('admin_user', ['fetchData', 'deleteUser']),
    loadTable() {
      const { sortBy, sortDesc, page, itemsPerPage } = this.table.options

      const sort = []

      sortBy.forEach((field, index) => {
        sort.push({
          field,
          order: (sortDesc[index]) ? Desc : Asc
        })
      })

      const p = Page.create({
        page: page - 1,
        size: itemsPerPage,
        sort
      })

      this.setPage({ page: p })

      this.fetchData()
    },
    deleteAction(item) {
      this.deleteDialog.value = [item]
      this.deleteDialog.show = true
    },
    deleteItemConfirm() {
      const [{ id }] = this.deleteDialog.value

      this.deleteUser({ id })
        .then(() => {
          this.deleteDialog.value = []
          this.deleteDialog.show = false
        })
    },
    onInputFilter: _.debounce(function () {
      const filter = UserFilter.create({
        ...this.table.filter
      })

      this.table.options.page = 1

      this.setFilter({ filter })

      this.loadTable()
    }, 500)
  }
}
</script>

<style scoped>

</style>
