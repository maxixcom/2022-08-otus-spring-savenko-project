<template>
  <v-card class="text-center w-full error-page pa-3">
    <v-img :src="require('@/assets/images/500-illustration.svg')" max-height="300" contain />
    <div class="display-2 mt-10">OOPS! Something went wrong here</div>
    <div class="mt-3 mb-6">Our experts are working to fix the issue.</div>
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
