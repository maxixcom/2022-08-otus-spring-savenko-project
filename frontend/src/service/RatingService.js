import axios from 'axios'
import { apiUrl } from '@/utils/ServiceUrls'

export default {
  rateMovie({ id, value }) {
    const data = {
      value
    }

    return axios.post(apiUrl('/api/v1/movie/' + id + '/rate'), data)
      .then((result) => result.data)
  }
}
