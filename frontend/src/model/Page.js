export default class Page {
  constructor(data) {
    this.page = data?.page ?? 0
    this.size = data?.size ?? 10
    this.sort = data?.sort ?? []
  }

  static create(data) {
    return new Page(data)
  }

  static default() {
    return new Page()
  }
}
