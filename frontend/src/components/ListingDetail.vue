<template>
  <vs-card v-if="listing" id="listing-detail-container">
    <div v-if="fromSearch" class="return-button">
      <vs-button @click="returnToSearch()" title="Go Back">Return To Search</vs-button>
    </div>
    <vs-row>
      <!-- Image area -->
      <vs-col vs-w="6" vs-sm="12" vs-xs="12" vs-justify="center" id="image-area">
        <div id="listing-image-container">
          <transition name="slide-fade" mode="out-in">
            <ReImage id="listing-image" :key="currentImage" :imagePath="currentImage"></ReImage>
          </transition>
          <div v-if="listingImages.length > 1">
            <vs-button @click="previousImage(currentImage)">
              <vs-icon icon="arrow_back"></vs-icon>
            </vs-button>
            <vs-button @click="nextImage(currentImage)">
              <vs-icon icon="arrow_forward"></vs-icon>
            </vs-button>
        </div>
        </div>
      </vs-col>
      <!-- Listing details (closing date, business, etc) -->
      <vs-col vs-w="6" vs-sm="12" id="listing-info-area">
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
      <!-- Product & Inventory details (manufacturer, description, inventory dates, etc) -->
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
      fromSearch: sessionStorage.getItem("listingSearchCache"),
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

    if(store.actingAsBusinessId != null) {
      this.$router.push({path: "/home"}) //Only users should be able to access this page (as a logged-in user)
    }
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
          this.listing = this.filterListingFromListingsResponse(response.data, listingId);
          this.listingImages = this.getListingImages(this.listing.inventoryItem.product.images);
          this.currentImage = this.getPrimaryImage(this.listingImages, this.listing)
        }).catch((error) => {
          this.$log.error(error);
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
          this.currency = { //need symbol and code
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


    /**
     * Gets all image paths from listing, also mutates path to fit into ReImage component
     * @param images list of images of the listing
     * @return Array of listing image paths
     */
    getListingImages(images) {
      return images.map(image => image.fileName);
    },


    /**
     * Gets primary image from list of images and primary image path
     * @param listing listing used to get primaryImagePath to filter images
     * @return image path of primary image
     */
    getPrimaryImage(images, listing) {
      return ".\\src\\main\\resources\\media\\images\\businesses\\" + listing.inventoryItem.product.primaryImagePath.replace("/", "\\");
    },

    /**
     * Sets current image to next image in image list
     * @param currentImage the current image that is being displayed
     */
    nextImage(currentImage) {
      let indexOfImage = this.listingImages.indexOf(currentImage);
      this.currentImage = this.listingImages[(indexOfImage + 1) % this.listingImages.length]
    },

    /**
     * Sets current image to previous image in image list
     * @param currentImage the current image that is being displayed
     */
    previousImage(currentImage) {
      let indexOfImage = this.listingImages.indexOf(currentImage);
      let length = this.listingImages.length
      this.currentImage = this.listingImages[(((indexOfImage - 1) % length) + length) % length] //Negative modulo in JavaScript doesn't work since it's just remainder
    },
    /**
     * Returns user to listing search page,
     * their previous search is shown
     */
    returnToSearch: function() {
      this.$router.push({path: '/search-listings'})
    },


  }

}
</script>

<style>

.return-button {
  margin-top: -15px;
  padding-left: 10px;
  position: fixed;
  left: 0px;
}

.slide-fade-enter-active {
  transition: all .1s ease;
}
.slide-fade-leave-active {
  transition: all .3s cubic-bezier(1.0, 0.5, 0.8, 1.0);
}
.slide-fade-enter, .slide-fade-leave-to {
  transform: translateX(10px);
  opacity: 0;
}

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
}

#listing-image > img {
  width:  400px;
  height: 300px;
  object-fit: cover;
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


@media screen and (max-width: 1300px) {
  #listing-image > img {
    width:  300px;
    height: 200px;
    object-fit: cover;
  }

  #product-info-area {
    margin-top: 3em;
    text-align: center;
    font-size: 12px;
    padding-bottom: 2em;
  }

  #listing-info-area {
    text-align: center;
    font-size: 12px;
  }

  #listing-name {
    font-size: 20px;
    margin-bottom: 5px;
  }

  #business-name {

    font-size: 20px;
    margin-top: 1em;
    margin-bottom: 0.5em;
  }

  .listing-detail-btn {
    margin-top: 5px;
    width: 150px;
  }

  #listing-image {
        margin: auto;
  }
  #listing-image-container {
    display: flex;
    flex-direction: column;
    justify-content: center;
  }
}


@media screen and (max-width: 600px) {
  #listing-detail-container {
    width: 90%;
  }
}


@media screen and (max-width: 400px) {
  #listing-detail-container {
    width: 100%;
  }

  #listing-image > img {
    width:  200px;
    height: 150px;
    object-fit: cover;
  }
}


</style>
