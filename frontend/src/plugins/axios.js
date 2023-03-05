import axios from 'axios'
import store from '@/store'
import router from '@/router'
import ToastService from '@/service/ToastService'

// Request interceptor
axios.interceptors.request.use((request) => {
  const token = store?.getters['auth/token']

  if (token) {
    request.headers.common['Authorization'] = `Bearer ${token}`
  }
  // console.log('Request', request)

  return request
})

// Response interceptor
axios.interceptors.response.use((response) => {
  return response
}, (error) => {
  const { status } = error.response

  if (status === 401 && store?.getters['auth/hasToken']) {
    console.debug('401 REDIRECT TO LOGIN')
    router.push({ name: 'auth.signin' }).then()
  }

  if (status === 403 && store?.getters['auth/hasToken']) {
    console.debug('403 REDIRECT TO LOGIN')
    router.push({ name: 'auth.signin' }).then()
  }

  if (status >= 500) {
    ToastService.error('Ошибка на стороне сервера')
  }

  const { data } = error.response

  if (data.error) {
    return Promise.reject(data)
  }

  return Promise.reject(error)

  // console.log('Response Error', error.response)
})
