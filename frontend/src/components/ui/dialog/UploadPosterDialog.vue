<template>
  <v-dialog v-model="value" persistent max-width="720px">
    <v-card>
      <v-form ref="form" v-model="form.formValidity" @submit.prevent="submit">
        <v-card-title class="title error--text">Сменить обложку</v-card-title>
        <v-card-text class="my-0">
          <!--          <v-img v-if="!Boolean(imageFile)" :src="userId | imageUrl('small', hasImage, true) "></v-img>-->
          <v-img v-if="!Boolean(imageFile)"></v-img>
          <vue-cropper
            v-if="Boolean(imageFile)"
            ref="cropper"
            :src="imageFileSrc"
            alt="Source Photo"
            :guides="false"
            :view-mode="3"
            drag-mode="crop"
            :auto-crop-area="1"
            :background="false"
            :rotatable="true"
            :movable="true"
            :highlight="false"
            :aspect-ratio="1.77"
            :min-container-width="630"
            :min-container-height="350"
            :min-crop-box-width="100"
            :min-crop-box-height="100"
            :img-style="{ width: '100%', height: 'auto' }"
            :container-style="{ width: '100%', height: 'auto' }"
          ></vue-cropper>
          <v-file-input
            v-model="image"
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
          <!--          <v-btn text color="error" :disabled="!hasImage" @click="remove">-->
          <!--            Удалить-->
          <!--          </v-btn>-->
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
import VueCropper from 'vue-cropperjs/VueCropper'

import 'cropperjs/dist/cropper.css'
import { ruleFileImage, ruleFileMaxSize, ruleRequired } from '@/utils/FormRules'
import ToastService from '@/service/ToastService'
import MovieService from '@/service/MovieService'

export default {
  name: 'UploadPosterDialog',
  components: {
    VueCropper
  },
  props: {
    value: {
      type: Boolean,
      default: false
    },
    movieId: {
      type: [String, Number],
      required: true
    },
    hasImage: {
      type: Boolean,
      default: false
    },
    onSuccessUpload: {
      type: Function,
      default: () => {
      }
    }
  },
  data: function () {
    return {
      busy: false,
      show: false,
      imageFile: null,
      imageFileSrc: null,
      form: {
        formValidity: false,
        rules: [
          ruleRequired(),
          ruleFileMaxSize(10000000, '10 Мб'),
          ruleFileImage()
        ]
      }
    }
  },
  computed: {
    image: {
      get() {
        return this.imageFile
      },
      set(value) {
        this.imageFile = value
        // console.log(this.imageFile)
        if (value) {
          if (typeof FileReader === 'function') {
            const reader = new FileReader()

            reader.onload = (event) => {
              this.imageFileSrc = event.target.result
              // console.log(this.imageFileSrc)
              this.$refs.cropper.replace(event.target.result)
            }
            reader.readAsDataURL(this.imageFile)

          } else {
            console.log('FileReader API not supported')
          }
        } else {
          this.imageFileSrc = null
        }
      }
    }
  },
  created() {
    this.show = this.value
  },
  methods: {
    clean() {
      this.imageFile = null
      this.imageFileSrc = null
      this.$refs.form.reset()
    },
    close() {
      this.show = false
      this.$emit('input', false)
      this.clean()
    },
    submit() {
      this.busy = true

      const promise = new Promise((resolve, reject) => {
        const croppedCanvas = this.$refs.cropper.getCroppedCanvas()

        if (!croppedCanvas) {
          reject(new Error('no cropped canvas'))
        }

        croppedCanvas.toBlob((croppedImage) => {
          resolve(croppedImage)
        })
      })

      promise
        .then((croppedImage) => {
          console.log(croppedImage)
          const form = { id: this.movieId, file: croppedImage }

          return MovieService.uploadPoster(form)
        })
        .then(() => {
          ToastService.success('Картинка закачана')
          this.onSuccessUpload()
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
      this.close()
    }
  }
}
</script>

<style scoped>

</style>
