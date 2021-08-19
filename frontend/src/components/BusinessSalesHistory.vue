<template>
  <vs-card id="container">
    <div id="header-container">
      <vs-icon icon="history" size="32px"></vs-icon>
      <div id="title">Sales History</div>
    </div>
    <vs-divider/>

    <vs-table
        :data="notifications"
        noDataText="Your listings will be displayed here once you have sold at least one product."
        stripe>
      <template slot="thead">
        <vs-th style="border-top-left-radius: 4px;"><!-- IMAGE COLUMN --></vs-th>
        <vs-th>Product</vs-th>
        <vs-th>Sold</vs-th>
        <vs-th>Listed</vs-th>
        <vs-th>Quantity</vs-th>
        <vs-th>Price</vs-th>
        <vs-th style="border-top-right-radius: 4px;">Likes</vs-th>
      </template>
      <template slot-scope="{data}">
        <vs-tr v-for="(listing, index) in data" :key="index">
          <vs-td class="listing-image-column"><ReImage :image-path="listing.boughtListing.product.primaryImagePath" class="listing-image"/></vs-td>
<!--          <vs-td>{{listing.listing.inventoryItem.product.name}}</vs-td>-->
          <vs-td>{{listing.created}}</vs-td>
<!--          <vs-td>{{listing.listing.created}}</vs-td>-->
<!--          <vs-td>{{listing.listing.quantity}}</vs-td>-->
<!--          <vs-td>{{currency}}{{listing.listing.price}}</vs-td>-->
<!--          <vs-td>{{listing.listing.likes}}</vs-td>-->
        </vs-tr>

      </template>
    </vs-table>
  </vs-card>
</template>

<script>
import ReImage from "./ReImage";
//import {store} from "../store"
import api from "../Api";
import axios from "axios";

export default {
  name: "BusinessSalesHistory",
  components: {ReImage},

  data: function() {
    return {
      currency: "$",
      businessId: '',
      notifications: [],
      salesHistory: [],
      testData: [
        {
          "id": 1,
          "inventoryItem": {
            "id": 101,
            "product": {
              "id": "WATT-420-BEANS",
              "name": "Watties Baked Beans - 420g can",
            },
          },
          "quantity": 3,
          "price": 17.99,
          "moreInfo": "Seller may be willing to consider near offers",
          "created": "2021-07-14 11:44:00",
          "closes": "2021-07-21T23:59:00Z",
          "productName": "Watties Baked Beans - 420g can",
          "sold": "2021-08-08 12:00:00",
          "likes": 5
        },
        {
          "id": 2,
          "inventoryItem": {
            "id": 102,
            "product": {
              "id": "Doritos",
              "name": "Doritos - The spicy purple one",
            },
          },
          "quantity": 5,
          "price": 5.99,
          "moreInfo": "Seller may be willing to consider near offers",
          "created": "2021-07-15 11:44:00",
          "closes": "2021-07-20T23:59:00Z",
          "productName": "Doritos - The spicy purple one",
          "sold": "2021-08-08 12:00:00",
          "likes": 10
        }
      ]
    }
  },

  mounted: function() {
    this.businessId = this.$route.params.id;
    this.getSalesHistory();
    this.getSession();
  },

  methods: {
    /**
     * Calls get sales history
     */
    filterNotifications: function () {
      for (const item of this.notifications) {
        if (item.status == "Bought") {
          this.salesHistory.push(item);
        }
      }
    },

    /**
     * Calls get sales history
     */
    getSalesHistory: function () {
      api.getBusinessListingNotifications(this.businessId)
        .then((res) => {

          this.notifications = res.data[0];
          console.log(this.notifications.created)
          //this.filterNotifications();
        })
        .catch(err => {
          console.log(err)
        });
    },

    /**
     * Calls get session endpoint to get user country, if successful calls setCurrency ()
     */
    getSession: function() {
      api.checkSession()
        .then((response) => {
          this.setCurrency(response.data.homeAddress.country)
        })
        .catch(err => {
          this.$log.debug(err);
      });
    },

    /**
     * Sets display currency based on the user's home country.
     */
    setCurrency: function (country) {
      axios.get(`https://restcountries.eu/rest/v2/name/${country}`)
        .then(response => {
          this.currency = response.data[0].currencies[0].symbol;
        })
        .catch(err => {
          this.$log.debug(err);
      });
    },
  },
}
</script>

<style scoped>

#container {
  background-color: white;
  width: 75%;
  margin: 1em auto;
}

#header-container {
  display: flex;
  margin: auto;
  padding-bottom: 0.5em;
  padding-top: 1em;
}

#title {
  font-size: 30px;
  margin: auto auto auto 4px;
}

.listing-image-column {
  width: 100px!important;
}

.listing-image >>> img {
  margin: auto;
  width: 90%;
  height: 70%;
  object-fit: cover;
  border-radius: 4px;
}

</style>
