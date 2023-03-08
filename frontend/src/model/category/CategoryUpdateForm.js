export default class CategoryUpdateForm {
  constructor(data) {
    this.title = data?.title
  }
  static create(data) {
    return new CategoryUpdateForm(data)
  }
}
