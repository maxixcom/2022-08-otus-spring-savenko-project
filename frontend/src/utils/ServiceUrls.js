import config from '../configs'

const cdnUrl = function(uri) {
  return `${config.cdnUrl}/${uri.replace(/^\/+|\/+$/g,'')}`
}

const apiUrl = function(uri) {
  return `${config.apiUrl}/${uri.replace(/^\/+|\/+$/g,'')}`
}

const avatarUrl = function (userId, size, hasImage, noCache) {
  if (!!hasImage === true) {
    let ts = ''

    if (noCache) {
      ts = '?ts=' + Date.now()
    }

    return `${config.cdnUrl}/avatar/${userId}/${size}.jpg` + ts
  }

  return '/images/avatars/avatar1.svg'
}

export { apiUrl, cdnUrl, avatarUrl }
