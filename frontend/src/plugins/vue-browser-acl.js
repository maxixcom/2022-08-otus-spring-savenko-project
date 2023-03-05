import store from '@/store'
import Vue from 'vue'
import Acl from 'vue-browser-acl'
import AclResource from '@/model/AclResource'
import router from '@/router'

const user = () => {
  return store.getters['auth/user']
}

/**
 * Option 1
 * Access permission through policy
 * <tag v-can:access"="AclResource.create('system.admin')"></tag>
 */

/**
 * Option 2
 * Access permission through global rules: role:user, role:admin
 * <tag v-can:admin"></tag>
 */

/**
 * Option 3
 * Access permission through generic object with type=acl and id set to permission:
 * <tag v-can:access="{type:'acl', id:'system.admin'}"></tag>
 */

/**
 * It's possible to add check to route meta section.
 * See https://github.com/mblarsen/vue-browser-acl#vue-router
 */

class AclResourcePolicy {
  // eslint-disable-next-line
  beforeAll(_, user) {
    if (!user) {
      return false
    }
    console.log('let rules rule')

    // returning void is different from false
    // false is a hard no
    // void lets the other rules decide
    // eslint-disable-next-line
  }

  access(user, resource) {
    return user.permissions.includes(resource.id)
  }
}

Vue.use(
  Acl,
  user,
  (acl) => {
    // Option 1
    acl.policy(AclResourcePolicy, AclResource)
    // Option 2
    acl.rule('user', (user) => {
      console.log('user',user)
      console.log(user && user.permissions.includes('USER_AREA'))

      return user && user.permissions.includes('USER_AREA')
    })
    acl.rule('admin', (user) => {
      return user && user.permissions.includes('ADMIN_AREA')
    })
    // Option 3
    acl.rule('access', 'acl', (user, acl) => {
      return user && user.permissions.includes(acl.id)
    })
    acl.verbObjectMapper = (s) => (typeof s === 'string' ? s : s.type)
  },
  { router }
)
