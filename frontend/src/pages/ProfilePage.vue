<template>
  <v-sheet>
    <v-container class="py-2 py-md-2">
      <h2 class="text-h5">Профиль пользователя</h2>

      <v-divider class="my-2 mb-3"></v-divider>

      <v-form v-model="form.valid" :disabled="busy" class="mb-2" @submit.prevent="submitProfileForm">
        <v-card>
          <v-card-title>Основная информация</v-card-title>
          <v-card-text>
            <v-text-field
              disabled
              :value="profileUsername"
              label="Логин"
            />
            <v-text-field
              v-model="form.data.fullName"
              label="Полное имя"
              counter="150"
              :rules="form.rule.fullName"
            />
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="primary" :disabled="!form.valid" type="submit">Применить</v-btn>
          </v-card-actions>
        </v-card>
      </v-form>

      <v-form ref="formPassword" v-model="formPassword.valid" :disabled="busy" @submit.prevent="submitPasswordForm">
        <v-card>
          <v-card-title class="text red--text">Сменить пароль</v-card-title>
          <v-card-text>
            <v-text-field
              v-model="formPassword.data.passwordOld"
              :append-icon="formPassword.show.passwordOld ? 'mdi-eye' : 'mdi-eye-off'"
              label="Пароль"
              counter="150"
              :rules="formPassword.rule.passwordOld"
              hint="Укажите ваш текущий пароль"
              persistent-hint
              :type="formPassword.show.passwordOld ? 'text' : 'password'"
              @click:append="formPassword.show.passwordOld = !formPassword.show.passwordOld"
            />
            <v-text-field
              v-model="formPassword.data.passwordNew"
              :append-icon="formPassword.show.passwordNew ? 'mdi-eye' : 'mdi-eye-off'"
              label="Новый пароль"
              counter="150"
              :rules="formPassword.rule.passwordNew"
              hint="Укажите ваш новый пароль"
              persistent-hint
              :type="formPassword.show.passwordNew ? 'text' : 'password'"
              @click:append="formPassword.show.passwordNew = !formPassword.show.passwordNew"
            />
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="error" :disabled="!formPassword.valid" type="submit">Сменить пароль</v-btn>
          </v-card-actions>
        </v-card>
      </v-form>
    </v-container>
  </v-sheet>
</template>

<script>
import { ruleRequired } from '@/utils/FormRules'
import { mapActions, mapGetters, mapState } from 'vuex'
import LoginProfileUpdateForm from '@/model/profile/LoginProfileUpdateForm'
import ToastService from '@/service/ToastService'
import LoginProfileChangePasswordForm from '@/model/profile/LoginProfileChangePasswordForm'

export default {
  name: 'ProfilePage',
  data() {
    return {
      form: {
        valid: true,
        data: {
          fullName: null
        },
        rule: {
          fullName: [ruleRequired()]
        }
      },
      formPassword: {
        valid: true,
        data: {
          passwordOld: '',
          passwordNew: ''
        },
        rule: {
          passwordOld: [ruleRequired()],
          passwordNew: [ruleRequired()]
        },
        show: {
          passwordOld: false,
          passwordNew: false
        }
      }
    }
  },
  computed: {
    ...mapGetters('profile', ['profileUsername']),
    ...mapState('profile', ['busy', 'profile'])
  },
  created() {
    this.fetchData()
      .then(() => {
        this.form.data.fullName = this.profile.fullName
      })
  },
  methods: {
    ...mapActions('profile', ['fetchData', 'updateProfile', 'changePassword']),
    submitProfileForm() {
      const form = LoginProfileUpdateForm.create({ ...this.form.data })

      this.updateProfile({ form })
        .then(() => ToastService.success('Данные обновлены'))
    },
    submitPasswordForm() {
      const form = LoginProfileChangePasswordForm.create({ ...this.formPassword.data })

      this.changePassword({ form })
        .then(() => {
          this.formPassword.data.passwordNew = ''
          this.formPassword.data.passwordOld = ''
          this.$refs.formPassword.resetValidation()
          ToastService.success('Пароль изменен')
        })
        .catch((error) => ToastService.error('Проверьте данные. Пароль не изменен!'))

    }
  }
}
</script>

<style scoped>

</style>
