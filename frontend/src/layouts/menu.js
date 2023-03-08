export default [{
  text: '',
  items: [
    { icon: 'mdi-arrow-left-bold-outline', key: 'menu.site', text: 'На сайт', link: { name: 'home' } }
  ]
}, {
  text: 'Контент',
  key: 'menu.manage',
  acl: { type: 'acl', id: 'ADMIN_AREA' },
  items: [
    { icon: 'mdi-filmstrip-box-multiple', key: 'admin.movie.list', text: 'Видеоролики', link: { name: 'admin.movie.list' } },
    { icon: 'mdi-file-tree-outline', key: 'admin.category.list', text: 'Категории', link: { name: 'admin.category.list' } }
  ]
}, {
  text: 'Администрирование',
  key: 'menu.admin',
  acl: { type: 'acl', id: 'ADMIN_AREA' },
  items: [
    { icon: 'mdi-account-multiple-outline', key: 'admin.user.list', text: 'Пользователи', link: { name: 'admin.user.list' } }
  ]
}]
