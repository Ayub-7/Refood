<template>

  <div>
    <div v-if="isBusiness" style="display: flex">
      <img v-if="this.imagePath != null && isDevelopment()" v-bind:src="require('../../../backend/src/main/resources/media/images/business_images/' + getImgUrl(this.imagePath))" alt="Business image"/>
      <img v-else-if="this.imagePath != null && !isDevelopment()" alt="Business Image" v-bind:src="getImgUrl(this.imagePath)"/>
      <img v-else-if="!this.imagePath && isDevelopment()" src="placeholder.png" alt="Business image"/>
      <img v-else-if="!isDevelopment() && !this.imagePath" :src="getImgUrl(true)" alt="Business image"/>
    </div>
    <div v-else>
      <img v-if="this.imagePath != null && isDevelopment()" v-bind:src="require('../../../backend/src/main/resources/media/images/businesses/' + getImgUrl(this.imagePath))" alt="Product image"/>
      <img v-else-if="this.imagePath != null && !isDevelopment()" alt="Product Image" v-bind:src="getImgUrl(this.imagePath)"/>
      <img v-else-if="!this.imagePath && isDevelopment()" src="placeholder.png" alt="Product image"/>
      <img v-else-if="!isDevelopment() && !this.imagePath" :src="getImgUrl(true)" alt="Product image"/>
    </div>
  </div>
</template>

<script>
export default {
  name: "ReImage",

  props: {
    imagePath: {
      type: String,
      default: null,
    },
    isBusiness: {
      type: Boolean,
      default: false,
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
        return '/prod/placeholder.png';
      } else if (product === true) {
        return '/test/placeholder.png';
      } else if (this.imagePath != null && !this.isDevelopment() && process.env.NODE_ENV !== 'staging') {
        return '/prod/prod_images/' + this.imagePath.replace("/\\/g", "/");
      } else if (this.imagePath != null && !this.isDevelopment()) {
        return '/test/prod_images/' + this.imagePath.replace("/\\/g", "/");
      } else if (this.imagePath != null) {
        return this.imagePath.replace(/\\/g, "/").replace("./src/main/resources/media/images/businesses/", "");
      } else {
        return '../../public/placeholder.png';
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
