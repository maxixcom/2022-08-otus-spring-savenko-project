<template>
  <v-dialog v-model="value" persistent max-width="400px">
    <v-card>
      <v-form ref="form" v-model="form.formValidity" @submit.prevent="submit">
        <v-card-title class="title error--text">Сменить аватар</v-card-title>
        <v-card-text class="my-0">
          <v-img v-if="!Boolean(avatarFile)" :src="profileId | avatarUrl('small', hasImage, true) "></v-img>
          <vue-cropper
            v-if="Boolean(avatarFile)"
            ref="cropper"
            :src="avatarFileSrc"
            alt="Source Photo"
            :guides="false"
            :view-mode="3"
            drag-mode="crop"
            :auto-crop-area="1"
            :background="false"
            :rotatable="true"
            :movable="true"
            :highlight="false"
            :aspect-ratio="1"
            :min-container-width="200"
            :min-container-height="200"
            :min-crop-box-width="50"
            :min-crop-box-height="50"
            :img-style="{ width: '100%', height: 'auto' }"
            :container-style="{ width: '100%', height: 'auto' }"
          ></vue-cropper>
          <v-file-input
            v-model="avatar"
            chips
            show-size
            truncate-length="15"
            label="Выберите файл"
            :rules="form.rules"
          ></v-file-input>
        </v-card-text>
        <v-card-actions>
          <v-btn text color="primary" type="submit" :disabled="!form.formValidity">
            Отправить
          </v-btn>
          <v-btn text color="error" :disabled="!hasImage" @click="remove">
            Удалить
          </v-btn>
          <v-btn text @click="cancel">
            Отмена
          </v-btn>
        </v-card-actions>
      </v-form>
    </v-card>
    <v-overlay :value="busy">
      <v-progress-circular
        indeterminate
        size="64"
      ></v-progress-circular>
    </v-overlay>
  </v-dialog>
</template>

<script>
import ToastService from '@/service/ToastService'
import VueCropper from 'vue-cropperjs/VueCropper'

import 'cropperjs/dist/cropper.css'
import { mapActions, mapGetters } from 'vuex'
import { ruleFileImage, ruleFileMaxSize, ruleRequired } from '@/utils/FormRules'
import config from '@/configs'

export default {
  name: 'MyDialogUploadProfileAvatar',
  components: {
    VueCropper
  },
  props: {
    value: {
      type: Boolean,
      default: false
    },
    onSuccessUpload: {
      type: Function,
      default: () => {
      }
    },
    onSuccessRemove: {
      type: Function,
      default: () => {
      }
    }
  },
  data: function () {
    return {
      busy: false,
      show: false,
      avatarFile: null,
      avatarFileSrc: null,
      form: {
        formValidity: false,
        rules: [
          ruleRequired(),
          ruleFileMaxSize(15000000, '15 Мб'),
          ruleFileImage()
        ]
      }
    }
  },
  computed: {
    ...mapGetters('profile', ['hasImage', 'profileId']),
    avatar: {
      get() {
        return this.avatarFile
      },
      set(value) {
        this.avatarFile = value
        if (value) {
          if (typeof FileReader === 'function') {
            const reader = new FileReader()

            reader.onload = (event) => {
              this.avatarFileSrc = event.target.result
              this.$refs.cropper.replace(event.target.result)
            }
            reader.readAsDataURL(this.avatarFile)

          } else {
            console.log('FileReader API not supported')
          }
        } else {
          this.avatarFileSrc = null
        }
      }
    }
  },
  created() {
    this.show = this.value
  },
  methods: {
    ...mapActions('profile', ['uploadAvatar', 'loginProfileAvatarDelete']),
    clean() {
      this.avatarFile = null
      this.avatarFileSrc = null
      this.$refs.form.reset()
    },
    close() {
      this.show = false
      this.clean()
    },
    submit() {
      this.busy = true
      // console.debug(this.avatar)
      const croppedCanvas = this.$refs.cropper.getCroppedCanvas()

      croppedCanvas.toBlob((croppedAvatar) => {
        this.uploadAvatar({ file: croppedAvatar })
          .then(() => {
            ToastService.success('Картинка закачана')
          })
          .then(() => {
            this.onSuccessUpload()
            this.$emit('input', false)
            this.close()
          })
          .catch((error) => {
            console.warn(error)
            ToastService.error('Что-то пошло не так')
          })
          .finally(() => {
            this.busy = false
          })
      })
    },
    remove() {
      this.busy = true
      this.loginProfileAvatarDelete()
        .then(() => {
          ToastService.success('Картинка удалена')
        })
        .then(() => {
          this.onSuccessRemove()
          this.$emit('input', false)
          this.close()
        })
        .catch((error) => {
          console.warn(error)
          ToastService.error('Что-то пошло не так')
        })
        .finally(() => {
          this.busy = false
        })
    },
    cancel() {
      this.$emit('input', false)
      this.close()
    }
  }
}
</script>

<style scoped>

</style>
