<template>
  <v-text-field
    v-bind="$attrs"
    :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
    :type="showPassword ? 'text' : 'password'"
    v-on="$listeners"
    @click:append="showPassword = !showPassword"
  ></v-text-field>
</template>

<script>
export default {
  model: {
    // These are the default values
    prop: 'value',
    event: 'input'
  },
  data() {
    return {
      showPassword: false
    }
  }
}
</script>

<style scoped>

</style>
