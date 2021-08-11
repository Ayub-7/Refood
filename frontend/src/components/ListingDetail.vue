<template>
  <vs-card id="listing-detail-container">
    <vs-row>
      <!-- Image area -->
      <vs-col vs-w="6" id="image-area">
        <div id="listing-image-container">
          <ReImage id="listing-image" :imagePath="currentImage"></ReImage>
        </div>
                  <vs-button @click="previousImage(currentImage)">
            <vs-icon icon="arrow_back"></vs-icon>
          </vs-button>

          <vs-button @click="nextImage(currentImage)">
            <vs-icon icon="arrow_forward"></vs-icon>
          </vs-button>
      </vs-col>
      <!-- Listing details (closing date, business, etc) -->
      <vs-col vs-w="6" id="listing-info-area">
        <div id="listing-info-container">
          <div id="business-name">Business: {{listing.inventoryItem.product.business.name}}</div>
          <div> Price: {{currency.symbol}}{{listing.price}} {{currency.code}}</div>
          <div>Quantity: {{listing.quantity}}</div>
          <div>Closes: {{listing.created}}</div>
          <div>Created: {{listing.closes}}</div>
          <div id="listing-moreInfo">{{listing.moreInfo}}</div>
          <div>Likes: {{listing.likes}}</div>
          <div>
            <vs-button class="listing-detail-btn">Like listing</vs-button>
          </div>
          <div>
            <vs-button class="listing-detail-btn">Buy</vs-button>
          </div>
        </div>
      </vs-col>
    </vs-row>

    <vs-row>
      <vs-col vs-w="12">
        <div id="product-info-area">
          <div id="listing-name">{{listing.inventoryItem.product.name}}</div> 
          <div>Manufacturer: {{listing.inventoryItem.product.manufacturer}} </div>
          <div v-if="listing.inventoryItem.manufactured">Manufactured: {{listing.inventoryItem.manufactured}}</div>
          <div v-if="listing.inventoryItem.sellBy">Sell By: {{listing.inventoryItem.sellBy}}</div>
          <div v-if="listing.inventoryItem.bestBefore">Best Before: {{listing.inventoryItem.bestBefore}}</div>
          <div v-if="listing.inventoryItem.expires">Expires: {{listing.inventoryItem.expires}}</div>
          <div>{{listing.inventoryItem.product.description}}</div>
        </div>
      </vs-col>


    </vs-row>
    
  </vs-card>
</template>

<script>

import api from "../Api";
import ReImage from "../components/ReImage.vue";
import axios from "axios";
import { store } from "../store";
export default {
  components: {
    ReImage
  },

  data() {
    return {
      listingId: null,
      businessId: null,
      listing: null,
      noBusiness: false,
      currency: {symbol: '$', code: 'NZD'},
      currentImage: null,
      listingImages: []
    }
  },

  mounted() {
    this.businessId = this.$route.params.businessId
    this.listingId = this.$route.params.listingId

    this.getBusinessListings(this.businessId, this.listingId);
    this.setCurrency(store.userCountry);
  },


  methods: {

    /**
     * Gets business listings by calling get endpoint
     * @param businessId ID of the business that has the listings
     * @param listingId ID of the specific listing from that business we want
     */
    getBusinessListings(businessId, listingId) {
      api.getBusinessListings(businessId)
        .then((response) => {
          console.log(response.data)
          this.listing = this.filterListingFromListingsResponse(response.data, listingId)
          this.listingImages = this.getListingImages(this.listing.inventoryItem.product.images)
          this.currentImage = this.getPrimaryImage(this.listingImages, this.listing)
        }).catch((error) => {
          if(error.response.status == 406) {
            this.noBusiness = false;
          }
        })
    },

    /**
     * Sets display currency based on the user's home country.
     * @param country country for which currency is going to be retrieved
     */
    setCurrency(country) {
      axios.get(`https://restcountries.eu/rest/v2/name/${country}`)
        .then(response => {
          console.log(response)
          this.currency = {
            symbol: response.data[0].currencies[0].symbol,
            code: response.data[0].currencies[0].code
          }
        }).catch(err => {
          this.$log.debug(err);
        })
      },

    /**
     * Filters response from API to only have listing with matching ID
     * @param listingsResponse response from backend with business listings
     * @param listingId id of the listing that we need to filter
     * @return Array containing listings that match ID, should only contain one.
     */
    filterListingFromListingsResponse(listingsResponse, listingId) {
      return listingsResponse.filter(listing => listing.id == listingId)[0]
    },


    getListingImages(images) {
      let productImages = [];
      for (let image of images) {
        console.log(image.fileName.split('/'));
        let imagePathSplit = image.fileName.split('/')
        let imagePath = imagePathSplit.splice(imagePathSplit.length - 2, 2).join('/')
        productImages.push(imagePath)
      }

      return productImages;
    },


    getPrimaryImage(images, listing) {
      return images.filter(imagePath => imagePath == listing.inventoryItem.product.primaryImagePath)[0]
    },

    nextImage(currentImage) {
      console.log(this.listingImages)
      let indexOfImage = this.listingImages.indexOf(currentImage);
      console.log(indexOfImage)
      this.currentImage = this.listingImages[(indexOfImage + 1) % this.listingImages.length]

    },

    previousImage(currentImage) {
      console.log(this.listingImages)
      let indexOfImage = this.listingImages.indexOf(currentImage);
      console.log(indexOfImage)
      this.currentImage = this.listingImages[(indexOfImage - 1) % this.listingImages.length]

    }


  }

}
</script>

<style>

#listing-detail-container {
  height: 100%;
  width: 60%;
  margin: 1em auto;
}

#listing-info-area {
  text-align: center;
  font-size: 15px;
}


#listing-image-container {
  margin: auto;
  width: 80%;
  overflow: hidden;
}

#listing-image > img {
  max-height: 300px;
  min-height: 300px;
  width: 100%;
}

#product-info-area {
  margin-top: 3em;
  text-align: center;
  font-size: 15px;
  padding-bottom: 2em;
}

#listing-name {
  font-size: 25px;
  margin-bottom: 10px;
}

#business-name {
  font-size: 25px;
  margin-bottom: 1em;
}

.listing-detail-btn {
  margin-top: 5px;
  width: 40%;
}

</style>