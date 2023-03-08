export default class LoginForm {
  constructor(data) {
    this.username = data.username
    this.password = data.password
    this.rememberMe = data.rememberMe
  }

  static create(data) {
    return new LoginForm(data)
  }
}
