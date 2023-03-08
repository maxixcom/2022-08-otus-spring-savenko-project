export default class CategoryFilter {
  constructor(data) {
    this.search = data?.search
  }

  static default() {
    return new CategoryFilter()
  }

  static create(data) {
    return new CategoryFilter(data)
  }
}
