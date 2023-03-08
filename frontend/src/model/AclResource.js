export default class AclResource {
  constructor(id) {
    this.type = 'AclResource'
    this.id = id
  }

  static create(id) {
    return new AclResource(id)
  }
}
