export default class RegisterForm {
  constructor(data) {
    this.firstName = data.firstName
    this.lastName = data.lastName
    this.username = data.username
    this.password = data.password
  }

  static create(data) {
    return new RegisterForm(data)
  }
}
