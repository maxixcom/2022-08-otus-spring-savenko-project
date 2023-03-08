import LoginProfile from '@/model/profile/LoginProfile'
import axios from 'axios'
import { apiUrl } from '@/utils/ServiceUrls'

export default {
  load() {
    return axios.get(apiUrl('/api/v1/profile'))
      .then((r) => LoginProfile.create(r.data))
  },

  update({ form }) {
    const data = {
      ...form
    }

    return axios.put(apiUrl('/api/v1/profile'), data)
      .then((r) => LoginProfile.create(r.data))
  },

  changePassword({ form }) {
    const data = {
      ...form
    }

    return axios.put(apiUrl('/api/v1/profile/password'), data)
  }
}
