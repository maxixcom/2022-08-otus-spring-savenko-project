import axios from 'axios'
import { apiUrl } from '@/utils/ServiceUrls'

export default {
  login(loginForm) {
    return axios.post(
      apiUrl('/api/v1/auth/login'),
      {
        username: loginForm.username,
        password: loginForm.password
      }
    ).then((result) => {
      console.log('result', result.data)

      const { token } = result.data

      if (token) {
        return token
      }

      throw new Error('Authentication is failed')
    })
  },

  register(registerForm) {
    return axios.post(
      apiUrl('/api/v1/auth/register'),
      {
        username: registerForm.username,
        password: registerForm.password,
        fullName: registerForm.firstName + ' ' + registerForm.lastName
      }
    ).then((result) => {
      console.log('result', result.data)

      const { token } = result.data

      if (token) {
        return token
      }

      throw new Error('Registration is failed')
    })
  }
}
