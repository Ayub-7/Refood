<template>
  <div>
    <img v-if="this.imagePath != null && isDevelopment()" v-bind:src="require('../../../backend/src/main/resources/media/images/businesses/' + getImgUrl(this.imagePath))" alt="Product image"/>
    <img v-if="this.imagePath != null && !isDevelopment()" alt="Product Image" v-bind:src="getImgUrl(this.imagePath)"/>
    <img v-if="!this.imagePath && isDevelopment()" src="ProductShoot.jpg" alt="Product image"/>
    <img v-if="!isDevelopment() && !this.imagePath" :src="getImgUrl(true)" alt="Product image"/>
  </div>
</template>

<script>
export default {
  name: "ReImage",

  props: {
    imagePath: {
      type: String,
      default: null,
    }
  },

  methods: {
    /**
     * Retrieves the image url link for the given product.
     * @param product the product to retrieve the image for.
     * @return a string link to the product image, or the default image if it doesn't have a product.
     **/
    getImgUrl(product) {
      if (product === true && process.env.NODE_ENV !== 'staging') {
        return '/prod/ProductShoot.jpg';
      } else if (product === true) {
        return '/test/ProductShoot.jpg';
      } else if (this.imagePath != null && process.env.NODE_ENV !== 'development' && process.env.NODE_ENV !== 'staging') {
        return '/prod/prod_images/' + this.imagePath.replace("/\\/g", "/");
      } else if (this.imagePath != null && process.env.NODE_ENV !== 'development') {
        return '/test/prod_images/' + this.imagePath.replace("/\\/g", "/");
      } else if (this.imagePath != null) {
        return this.imagePath.replace(/\\/g, "/").replace("./src/main/resources/media/images/businesses/", "");
      } else {
        return '../../public/ProductShoot.jpg';
      }
    },

    /**
     * Checks if the current web environment is in development mode.
     */
    isDevelopment() {
      return (process.env.NODE_ENV === 'development');
    },
  },
}
</script>

<style scoped>

</style>
