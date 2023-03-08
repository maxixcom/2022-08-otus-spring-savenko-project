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

    if (filter.roles.length > 0) {
      params['roles'] = filter.roles?.join(',')
    }

    if (filter.search) {
      params['search'] = filter.search
    }

    return axios.get(apiUrl('/api/v1/user'), { params })
      .then((r) => {
        return r.data
      })
  },

  findById({ id }) {
    return axios.get(apiUrl('/api/v1/user/' + id))
      .then((r) => {
        return r.data
      })
  },

  create({ form }) {
    const data = {
      ...form
    }

    return axios.post(apiUrl('/api/v1/user'), data)
  },

  update({ id, form }) {
    const data = {
      ...form
    }

    return axios.put(apiUrl('/api/v1/user/' + id), data)
  },

  delete({ id }) {
    return axios.delete(apiUrl('/api/v1/user/' + id))
  }
}
