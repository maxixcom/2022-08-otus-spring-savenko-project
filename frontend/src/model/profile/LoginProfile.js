import Meta from '@/model/Meta'

export default class LoginProfile {
  constructor(data) {
    this.id = data.id
    this.username = data.username
    this.fullName = data.fullName
    this.roles = data.roles
    this.permissions = data.permissions
    this.settings = data.settings ?? []
    this.hasImage = data.hasImage ?? false
  }

  static create(data) {
    return new LoginProfile(data)
  }
}
