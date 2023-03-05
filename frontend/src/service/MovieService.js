import axios from 'axios'
import { apiUrl } from '@/utils/ServiceUrls'

export default {
  find(filter, page) {

    const params = {
      page: page.page,
      size: page.size,
      sort: page.sort.map((s) => [s.field, s.order].join(',')).join(',')
    }

    if (filter.categoryIds.length > 0) {
      params['categoryIds'] = filter.categoryIds?.join(',')
    }

    if (filter.search) {
      params['search'] = filter.search
    }

    return axios.get(apiUrl('/api/v1/movie'), { params })
      .then((r) => {
        return r.data
      })
  },

  findById({ id }) {
    return axios.get(apiUrl('/api/v1/movie/' + id))
      .then((r) => {
        return r.data
      })
  },

  create({ form }) {
    const data = {
      ...form
    }

    return axios.post(apiUrl('/api/v1/movie'), data)
  },

  update({ id, form }) {
    const data = {
      ...form
    }

    return axios.put(apiUrl('/api/v1/movie/' + id), data)
  },

  delete({ id }) {
    return axios.delete(apiUrl('/api/v1/movie/' + id))
  },

  uploadPoster({ id, file }) {
    const form = new FormData()

    form.append('file', file)

    return axios.post(
      apiUrl('/api/v1/movie/' + id + '/poster'),
      form,
      {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }
    )
  },

  deletePoster({ id }) {
    return axios.delete(apiUrl('/api/v1/movie/' + id + '/poster'))
  }

}
