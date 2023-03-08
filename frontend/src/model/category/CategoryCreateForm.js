export default class CategoryCreateForm {
  constructor(data) {
    this.title = data?.title
  }
  static create(data) {
    return new CategoryCreateForm(data)
  }
}
