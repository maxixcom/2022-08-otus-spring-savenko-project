export default class LoginProfileChangePasswordForm {
  constructor(data) {
    this.passwordOld = data?.passwordOld
    this.passwordNew = data?.passwordNew
  }

  static create(data) {
    return new LoginProfileChangePasswordForm(data)
  }
}
