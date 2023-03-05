// import AclResource from "../models/AclResource";

function page(page) {
  return () => import(/* webpackChunkName: '' */ '../pages/' + page).then((m) => m.default || m)
}

function metaCan(permission) {
  return {
    can: function (to, from, can) {
      return can('access', { type: 'acl', id: permission })
    },
    fail: '/error/403'
  }
}

export default [
  { path: '/home', name: 'home', redirect: { name: 'index' } },
  { path: '/', name: 'index', component: page('IndexPage.vue') },
  { path: '/movie/:id', name: 'movie', component: page('MoviePage.vue'), props: true },

  { path: '/profile', name: 'profile', component: page('ProfilePage.vue'), meta: metaCan('USER_AREA')  },

  { path: '/admin', name: 'admin', redirect: { name: 'admin.movie.list' } },
  {
    path: '/admin/movie/list',
    name: 'admin.movie.list',
    component: page('admin/AdminMoviePage.vue'),
    meta: metaCan('ADMIN_AREA')
  },
  {
    path: '/admin/movie/create',
    name: 'admin.movie.create',
    component: page('admin/AdminMovieCreatePage.vue'),
    meta: metaCan('ADMIN_AREA')
  },
  {
    path: '/admin/movie/edit/:id',
    name: 'admin.movie.edit',
    component: page('admin/AdminMovieEditPage.vue'),
    props: true,
    meta: metaCan('ADMIN_AREA')
  },
  {
    path: '/admin/movie/view/:id',
    name: 'admin.movie.view',
    component: page('admin/AdminMovieViewPage.vue'),
    props: true,
    meta: metaCan('ADMIN_AREA')
  },
  {
    path: '/admin/category/list',
    name: 'admin.category.list',
    component: page('admin/AdminCategoryPage.vue'),
    meta: metaCan('ADMIN_AREA')
  },
  {
    path: '/admin/category/create',
    name: 'admin.category.create',
    component: page('admin/AdminCategoryCreatePage.vue'),
    meta: metaCan('ADMIN_AREA')
  },
  {
    path: '/admin/category/edit/:id',
    name: 'admin.category.edit',
    component: page('admin/AdminCategoryEditPage.vue'),
    props: true,
    meta: metaCan('ADMIN_AREA')
  },
  {
    path: '/admin/user/list',
    name: 'admin.user.list',
    component: page('admin/AdminUserPage.vue'),
    meta: metaCan('ADMIN_AREA')
  },
  {
    path: '/admin/user/create',
    name: 'admin.user.create',
    component: page('admin/AdminUserCreatePage.vue'),
    meta: metaCan('ADMIN_AREA')
  },
  {
    path: '/admin/user/edit/:id',
    name: 'admin.user.edit',
    component: page('admin/AdminUserEditPage.vue'),
    props: true,
    meta: metaCan('ADMIN_AREA')
  },

  // Auth
  { path: '/login', name: 'auth.signin', component: page('auth/SigninPage.vue') },
  { path: '/register', name: 'auth.signup', component: page('auth/SignupPage.vue') },
  { path: '/register/complete', name: 'auth.signup-complete', component: page('auth/SignupCompletePage.vue') },

  // Errors
  { path: '/error/404', name: 'error.404', component: page('error/NotFoundPage.vue') },
  { path: '/error/403', name: 'error.403', component: page('error/ForbiddenPage.vue') },
  { path: '/error/500', name: 'error.500', component: page('error/ErrorPage.vue') },

  { path: '*', redirect: { name: 'error.404' } }
]
