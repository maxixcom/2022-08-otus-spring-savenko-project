export default class MovieFilter {
  constructor(data) {
    this.search = data?.search
    this.categoryIds = data?.categoryIds ?? []
  }

  static default() {
    return new MovieFilter()
  }

  static create(data) {
    return new MovieFilter(data)
  }
}
