<template>
  <vs-card id="listing-detail-container">

    <vs-row>
      <!-- Image area -->
      <vs-col vs-w="8" id="image-area">
        <div id="image-container">
          <ReImage id="image"></ReImage>
        </div>
      </vs-col>
      <!-- Listing details (closing date, business, etc) -->
      <vs-col vs-w="4" id="listing-info-area">
        <div id="listing-info-container">
          <div>Business: {{listing.inventoryItem.product.business.name}}</div>
          <div>Price: {{listing.price}}</div>
          <div>Quantity: {{listing.quantity}}</div>
          <div>Closes: {{listing.created}}</div>
          <div>Created: {{listing.closes}}</div>
          <div>Likes: {{listing.likes}}</div>
          <div>
            <vs-button>Like listing</vs-button>
          </div>
        </div>
      </vs-col>
    </vs-row>

    <vs-row>
      <vs-col vs-w="12">
        <div id="listing-name">{{listing.inventoryItem.product.name}}</div> 
        <div id="listing-moreInfo">{{listing.moreInfo}}</div>
      </vs-col>

      
    </vs-row>
    
  </vs-card>
</template>

<script>

import api from "../Api";
import ReImage from "../components/ReImage.vue";
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
    }
  },

  mounted() {
    this.businessId = this.$route.params.businessId
    this.listingId = this.$route.params.listingId

    this.getBusinessListings(this.businessId, this.listingId);
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
        this.listing = this.filterListingFromListingsResponse(response.data, listingId)
        console.log(this.listing)
      }).catch((error) => {
        if(error.response.status == 406) {
          this.noBusiness = false;
        }
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


    getListingImages() {

    }

  }

}
</script>

<style>

#listing-detail-container {
  height: 100%;
  width: 70%;
  margin: 1em auto;
}

#image-container {
  width: 60%
}

#image > img {
  max-width: 100%;
  max-height: auto;
}


</style>