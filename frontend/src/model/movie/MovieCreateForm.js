export default class MovieCreateForm {
  constructor(data) {
    this.title = data?.title
    this.description = data?.description
    this.embedCode = data?.embedCode
    this.categoryId = data?.categoryId
  }
  static create(data) {
    return new MovieCreateForm(data)
  }
}
