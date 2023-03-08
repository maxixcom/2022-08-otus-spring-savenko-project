export const Asc = 'asc'
export const Desc = 'desc'

export default class SortOrder {
  constructor(data) {
    this.field = data?.field
    this.order = data?.order
  }

  static default() {
    return new SortOrder({ field: 'id', order: Asc })
  }
}
