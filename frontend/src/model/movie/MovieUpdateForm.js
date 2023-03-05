export default class MovieUpdateForm {
  constructor(data) {
    this.title = data?.title
    this.description = data?.description
    this.embedCode = data?.embedCode
    this.categoryId = data?.categoryId
  }
  static create(data) {
    return new MovieUpdateForm(data)
  }
}
