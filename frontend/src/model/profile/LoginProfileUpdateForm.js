export default class LoginProfileUpdateForm {
  constructor(data) {
    this.fullName = data?.fullName
  }

  static create(data) {
    return new LoginProfileUpdateForm(data)
  }
}
