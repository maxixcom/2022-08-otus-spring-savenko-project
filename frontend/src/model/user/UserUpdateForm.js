export default class UserUpdateForm {
  constructor(data) {
    this.username = data?.username
    this.fullName = data?.fullName
    this.role = data?.role ?? 'USER'
  }

  static create(data) {
    return new UserUpdateForm(data)
  }
}
