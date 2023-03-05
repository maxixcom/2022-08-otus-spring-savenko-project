export default class UserFilter {
  constructor(data) {
    this.search = data?.search
    this.roles = data?.roles ?? []
  }

  static default() {
    return new UserFilter()
  }

  static create(data) {
    return new UserFilter(data)
  }
}
