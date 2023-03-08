<template>
  <v-card class="text-center w-full error-page pa-3">
    <v-img :src="require('@/assets/images/403-illustration.svg')" max-height="300" contain />
    <div class="display-2 mt-10">This is restricted area</div>
    <div class="mt-3 mb-6">Sorry you are not authorized to be here.</div>
    <v-btn to="/" block large color="primary">Send me Back</v-btn>
  </v-card>
</template>
<script>
export default {
  layout: 'ErrorLayout'
}
</script>
<style>
.error-page {
  max-width: 500px;
}
</style>
