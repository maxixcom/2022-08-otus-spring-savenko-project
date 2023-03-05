<template>
  <div>
    <!-- menu level 1 -->
    <nav-menu-item
      v-for="(level1Item, level1Index) in items"
      :key="level1Index"
      v-can:access="level1Item.acl"
      :menu-item="level1Item"
    >
      <template v-if="level1Item.items">

        <!-- menu level 2 -->
        <nav-menu-item
          v-for="(level2Item, level2Index) in level1Item.items"
          :key="level2Index"
          v-can:access="level2Item.acl"
          :menu-item="level2Item"
          subgroup
          small
        >
          <template v-if="level2Item.items">

            <!-- menu level 3 -->
            <nav-menu-item
              v-for="(level3Item, level3Index) in level2Item.items"
              :key="level3Index"
              v-can:access="level3Item.acl"
              :menu-item="level3Item"
              small
            />
          </template>
        </nav-menu-item>
      </template>
    </nav-menu-item>
  </div>
</template>

<script>
import NavMenuItem from './NavMenuItem'

/*
|---------------------------------------------------------------------
| Navigation Menu Component
|---------------------------------------------------------------------
|
| Multi-layer navigation menu
|
| menu: [{ text: 'Menu Levels',
|    items: [
|      { text: 'Menu Levels 2.1' },
|      { text: 'Menu Levels 2.2',
|        items: [
|          { text: 'Menu Levels 3.1' },
|          { text: 'Menu Levels 3.2' }
|        ]
|      }
|    ]
|  }]
|
*/
export default {
  components: {
    NavMenuItem
  },
  props: {
    menu: {
      type: Array,
      default: () => []
    }
  },
  data: function () {
    for (const level1item of this.menu) {
      if (!level1item.acl) {
        level1item.acl = { type: 'acl', id: 'ADMIN_AREA' }
      }
      if (level1item.items) {
        // level 2
        for (const level2item of level1item.items) {
          if (!level2item.acl) {
            level2item.acl = { type: 'acl', id: 'ADMIN_AREA' }
          }
          if (level2item.items) {
            // level 3
            for (const level3item of level2item.items) {
              if (!level3item.acl) {
                level3item.acl = { type: 'acl', id: 'ADMIN_AREA' }
              }
            }
          }
        }
      }
    }

    return {
      items: this.menu
    }
  }
}
</script>
