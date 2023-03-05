import axios from 'axios'
import { apiUrl } from '@/utils/ServiceUrls'

export default {
  find(filter, page) {

    const params = {
      ...filter,
      page: page.page,
      size: page.size,
      sort: page.sort.map((s) => [s.field, s.order].join(',')).join(',')
    }

    return axios.get(apiUrl('/api/v1/category'), { params })
      .then((r) => {
        return r.data
      })
  },

  findById({ id }) {
    return axios.get(apiUrl('/api/v1/category/' + id))
      .then((r) => {
        return r.data
      })
  },

  create({ form }) {
    const data = {
      title: form.title
    }

    return axios.post(apiUrl('/api/v1/category'), data)
  },

  update({ id, form }) {
    const data = {
      title: form.title
    }

    return axios.put(apiUrl('/api/v1/category/' + id), data)
  },

  delete({ id }) {
    return axios.delete(apiUrl('/api/v1/category/' + id))
  }
}
