export default class Meta {
  constructor(data) {
    this.isDeleted = data.isDeleted
    this.createdAt = data.createdAt
    this.updatedAt = data.updatedAt
  }

  static create(data) {
    return new Meta(data)
  }
}
