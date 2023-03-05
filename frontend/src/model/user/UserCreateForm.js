export default class UserCreateForm {
  constructor(data) {
    this.username = data?.username
    this.fullName = data?.fullName
    this.role = data?.role ?? 'USER'
    this.password = data?.password
  }

  static create(data) {
    return new UserCreateForm(data)
  }
}
