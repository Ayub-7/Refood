<template>
  <vs-card class="main">
        <div id="header-container">
          <div id="page-title">
            <vs-icon icon="local_offer"/>
            Sales Listings</div>
          </div>
        <vs-divider style="padding: 4px;"></vs-divider>

        <div id="catalogue-options">
          <vs-input class="search-input" type="search" placeholder="Enter a listing..." name="searchbarUser" v-model="searchbarListings" style="width: 400px; font-size: 24px" size="medium"/>
          <vs-button class="header-button" >Search</vs-button>

        </div>

        <vs-divider style="padding: 4px;"></vs-divider>

        <div>
          <div id="filter-box" style="display: flex;">
            <div id="search-parameters" display="flex">
              <div class="parameter" id="business" style="display: block">
                <div class="vert-row">
                <h3 class="filter-label">
                  Business Type:
                </h3>
                <vs-select v-model="businessType">
                  <option disabled value="">Please select one</option>
                  <option value="name">listings Name</option>
                  <option value="country">Country</option>
                  <option value="recommendedRetailPrice">Recommended Retail Price</option>
                  <option value="expiryDate">Expiry Date</option>
                </vs-select>
                </div>
                <div class="vert-row">
                <h3 class="filter-label">
                  Business Name:
                </h3>
                <vs-input class="filter-input" type="search" placeholder="Business name.." name="searchbarUser"  style="width: 400px; font-size: 24px" size="medium"/>
              </div>
              </div>
              <div class="parameter" id="listings">
                <div class="vert-row">
                <h3 class="filter-label">
                  Product Name:
                </h3>
                <vs-input class="filter-input" type="search" placeholder="Product name.." name="searchbarUser"  style="width: 400px; font-size: 24px" size="medium"/>
              </div>
              <div class="vert-row">
                <h3 class="filter-label">
                  Price range:
                </h3>
                <vs-input class="price-input" type="search" placeholder="Min" name="searchbarUser"  style="width: 400px; font-size: 24px" size="medium"/>
                <vs-input class="price-input" type="search" placeholder="Max" name="searchbarUser"  style="width: 400px; font-size: 24px" size="medium"/>
              </div>
              </div>

              <div class="parameter" id="address-closing-date">
                <div class="vert-row">
                <h3 class="filter-label">
                  Location:
                </h3>
                <vs-input class="filter-input" type="search" placeholder="Country.." name="searchbarUser"  style="width: 400px; font-size: 24px" size="medium"/>
              </div>
                <div class="vert-row">
                <h3 class="filter-label">
                  Closing Date:
                </h3>
                <vs-input class="filter-input" type="date" name="searchbarUser"  style="width: 400px; font-size: 24px"/>
              </div>
            </div>
            </div>
            <div class="vl"></div>
            <div id="sort-container">
              <div>
                <h3 class="filter-label" style="margin: auto; padding-right: 4px;">Sort By </h3>
                <div>
                <vs-select v-model="selected">
                  <option disabled value="">Please select one</option>
                  <option value="name">listings Name</option>
                  <option value="country">Country</option>
                  <option value="recommendedRetailPrice">Recommended Retail Price</option>
                  <option value="expiryDate">Expiry Date</option>
                </vs-select>
                <vs-button class="sort-btn"  style="width: 100px">Sort</vs-button>
                </div>
                </div>
            </div>
          </div>
          <vs-divider style="padding: 4px;"></vs-divider>
          <div class="grid-container" style="margin: auto">
            <vs-card class="listing-card" v-for="listing in listings" :key="listing.id" :fixed-height="true">
              <div slot="media">
                <ReImage :imagePath="listing.inventoryItem.product.primaryImagePath"></ReImage>
              </div>
              <div style="margin: 2px 4px; font-size: 14px; font-weight: bold">{{ listing.inventoryItem.product.name }}</div>
              <div style="font-size: 14px; padding-left: 4px; margin: auto 0;">
                <div>{{ currencySymbol }}{{ listing.price }}</div>
                <div>{{ listing.quantity }}x</div>
              </div>

              <div style="font-size: 12px"> Closes: {{ listing.closes }}</div>
              <vs-divider style="margin-top: 0"></vs-divider>

              <div>{{ listing.moreInfo }}</div>
              <div slot="footer" class="grid-card-footer">
                Listed: {{ listing.created }}
              </div>
              <div v-if="likedListingsIds.includes(listing.id)">
                <vs-button disabled color="primary" type="border" icon="thumb_up"></vs-button>
                <vs-button color="danger" type="border" icon="thumb_down"></vs-button>
              </div>
              <div v-else>
                <vs-button color="primary" type="border" icon="thumb_up" @click="sendLike(listing.id, listing.inventoryItem.product.name)"></vs-button>
                <vs-button disabled color="danger" type="border" icon="thumb_down"></vs-button>
              </div>
            </vs-card>
            <div class="title-centre">
            </div>
          </div>
        </div>
  </vs-card>
</template>

<script>
import api from "../Api";
import axios from "axios";
import ReImage from "./ReImage";

const SearchListings = {
  name: "SearchListings",
  components: {ReImage},
  data: function() {
    return {
      listings: [
        {
          "id": 9030,
          "inventoryItem": {
            "id": 5509,
            "product": {
              "id": "SEO-487ATE",
              "name": "Garlic - Peeled",
              "description": "Ut at dolor quis odio consequat varius.",
              "manufacturer": "Reenolds Ranch",
              "recommendedRetailPrice": 47.96,
              "created": "2021-01-13 00:03:00",
              "images": [],
              "primaryImagePath": null
            },
            "quantity": 49,
            "pricePerItem": 50.76,
            "totalPrice": 2487.24,
            "manufactured": "2020-09-13",
            "sellBy": "2020-11-22",
            "bestBefore": "2020-07-27",
            "expires": "2021-06-05"
          },
          "quantity": 12,
          "price": 609.1,
          "moreInfo": null,
          "created": "2021-04-26 21:54:09",
          "closes": "2021-06-05 15:06:38",
          "productName": "Garlic - Peeled"
        },
        {
          "id": 4,
          "inventoryItem": {
            "id": 2,
            "product": {
              "id": "W04GP5EC0B1798680",
              "name": "Compound - Mocha",
              "description": "vel ipsum praesent blandit lacinia erat vestibulum sed magna at nunc",
              "manufacturer": "Nestle",
              "recommendedRetailPrice": 88.93,
              "created": "2021-01-11 07:54:46",
              "images": [],
              "primaryImagePath": null
            },
            "quantity": 7,
            "pricePerItem": 3,
            "totalPrice": 80,
            "manufactured": "2020-01-26",
            "sellBy": null,
            "bestBefore": "2021-08-27",
            "expires": "2021-08-27"
          },
          "quantity": 1,
          "price": 15.5,
          "moreInfo": "Contact us for more information.",
          "created": "2021-02-01 23:00:00",
          "closes": "2021-09-08 00:00:00",
          "productName": "Compound - Mocha"
        },
        {
          "id": 5,
          "inventoryItem": {
            "id": 2,
            "product": {
              "id": "W04GP5EC0B1798680",
              "name": "Compound - Mocha",
              "description": "vel ipsum praesent blandit lacinia erat vestibulum sed magna at nunc",
              "manufacturer": "Nestle",
              "recommendedRetailPrice": 88.93,
              "created": "2021-01-11 07:54:46",
              "images": [],
              "primaryImagePath": null
            },
            "quantity": 7,
            "pricePerItem": 3,
            "totalPrice": 80,
            "manufactured": "2020-01-26",
            "sellBy": null,
            "bestBefore": "2021-08-27",
            "expires": "2021-08-27"
          },
          "quantity": 2,
          "price": 5.99,
          "moreInfo": "Do not contact us for more information.",
          "created": "2021-02-02 23:00:00",
          "closes": "2021-10-18 23:00:00",
          "productName": "Compound - Mocha"
        },
        {
          "id": 1219,
          "inventoryItem": {
            "id": 2,
            "product": {
              "id": "W04GP5EC0B1798680",
              "name": "Compound - Mocha",
              "description": "vel ipsum praesent blandit lacinia erat vestibulum sed magna at nunc",
              "manufacturer": "Nestle",
              "recommendedRetailPrice": 88.93,
              "created": "2021-01-11 07:54:46",
              "images": [],
              "primaryImagePath": null
            },
            "quantity": 7,
            "pricePerItem": 3,
            "totalPrice": 80,
            "manufactured": "2020-01-26",
            "sellBy": null,
            "bestBefore": "2021-08-27",
            "expires": "2021-08-27"
          },
          "quantity": 1,
          "price": 3,
          "moreInfo": "Nullam porttitor lacus at turpis.",
          "created": "2021-05-24 08:03:32",
          "closes": "2021-08-26 13:01:09",
          "productName": "Compound - Mocha"
        },
        {
          "id": 4086,
          "inventoryItem": {
            "id": 2,
            "product": {
              "id": "W04GP5EC0B1798680",
              "name": "Compound - Mocha",
              "description": "vel ipsum praesent blandit lacinia erat vestibulum sed magna at nunc",
              "manufacturer": "Nestle",
              "recommendedRetailPrice": 88.93,
              "created": "2021-01-11 07:54:46",
              "images": [],
              "primaryImagePath": null
            },
            "quantity": 7,
            "pricePerItem": 3,
            "totalPrice": 80,
            "manufactured": "2020-01-26",
            "sellBy": null,
            "bestBefore": "2021-08-27",
            "expires": "2021-08-27"
          },
          "quantity": 2,
          "price": 6,
          "moreInfo": null,
          "created": "2021-04-01 19:26:03",
          "closes": "2021-08-27 03:06:29",
          "productName": "Compound - Mocha"
        },
        {
          "id": 9922,
          "inventoryItem": {
            "id": 2,
            "product": {
              "id": "W04GP5EC0B1798680",
              "name": "Compound - Mocha",
              "description": "vel ipsum praesent blandit lacinia erat vestibulum sed magna at nunc",
              "manufacturer": "Nestle",
              "recommendedRetailPrice": 88.93,
              "created": "2021-01-11 07:54:46",
              "images": [],
              "primaryImagePath": null
            },
            "quantity": 7,
            "pricePerItem": 3,
            "totalPrice": 80,
            "manufactured": "2020-01-26",
            "sellBy": null,
            "bestBefore": "2021-08-27",
            "expires": "2021-08-27"
          },
          "quantity": 3,
          "price": 9,
          "moreInfo": null,
          "created": "2021-03-23 12:20:45",
          "closes": "2021-08-27 11:01:03",
          "productName": "Compound - Mocha"
        },
        {
          "id": 1,
          "inventoryItem": {
            "id": 1,
            "product": {
              "id": "WAUVT64B54N722288",
              "name": "Pastry - Cheese Baked Scones",
              "description": "amet erat nulla tempus vivamus",
              "manufacturer": "Watties",
              "recommendedRetailPrice": 19.88,
              "created": "2021-03-05 01:36:54",
              "images": [],
              "primaryImagePath": null
            },
            "quantity": 10,
            "pricePerItem": 5,
            "totalPrice": 50,
            "manufactured": "2021-01-26",
            "sellBy": "2021-05-25",
            "bestBefore": "2021-05-27",
            "expires": "2021-05-27"
          },
          "quantity": 3,
          "price": 10,
          "moreInfo": "Not negotiable.",
          "created": "2021-01-26 23:00:00",
          "closes": "2021-09-22 00:00:00",
          "productName": "Pastry - Cheese Baked Scones"
        },
        {
          "id": 2,
          "inventoryItem": {
            "id": 1,
            "product": {
              "id": "WAUVT64B54N722288",
              "name": "Pastry - Cheese Baked Scones",
              "description": "amet erat nulla tempus vivamus",
              "manufacturer": "Watties",
              "recommendedRetailPrice": 19.88,
              "created": "2021-03-05 01:36:54",
              "images": [],
              "primaryImagePath": null
            },
            "quantity": 10,
            "pricePerItem": 5,
            "totalPrice": 50,
            "manufactured": "2021-01-26",
            "sellBy": "2021-05-25",
            "bestBefore": "2021-05-27",
            "expires": "2021-05-27"
          },
          "quantity": 5,
          "price": 10,
          "moreInfo": "Could be negotiable.",
          "created": "2021-01-27 23:00:00",
          "closes": "2021-10-26 23:00:00",
          "productName": "Pastry - Cheese Baked Scones"
        },
        {
          "id": 3,
          "inventoryItem": {
            "id": 1,
            "product": {
              "id": "WAUVT64B54N722288",
              "name": "Pastry - Cheese Baked Scones",
              "description": "amet erat nulla tempus vivamus",
              "manufacturer": "Watties",
              "recommendedRetailPrice": 19.88,
              "created": "2021-03-05 01:36:54",
              "images": [],
              "primaryImagePath": null
            },
            "quantity": 10,
            "pricePerItem": 5,
            "totalPrice": 50,
            "manufactured": "2021-01-26",
            "sellBy": "2021-05-25",
            "bestBefore": "2021-05-27",
            "expires": "2021-05-27"
          },
          "quantity": 5,
          "price": 10,
          "moreInfo": "Price is negotiable.",
          "created": "2021-01-31 23:00:00",
          "closes": "2021-11-11 23:00:00",
          "productName": "Pastry - Cheese Baked Scones"
        },
        {
          "id": 4939,
          "inventoryItem": {
            "id": 4,
            "product": {
              "id": "WAUVT64B54N722288",
              "name": "Pastry - Cheese Baked Scones",
              "description": "amet erat nulla tempus vivamus",
              "manufacturer": "Watties",
              "recommendedRetailPrice": 19.88,
              "created": "2021-03-05 01:36:54",
              "images": [],
              "primaryImagePath": null
            },
            "quantity": 1,
            "pricePerItem": 6,
            "totalPrice": 50,
            "manufactured": "2021-01-26",
            "sellBy": null,
            "bestBefore": "2021-09-26",
            "expires": "2021-11-26"
          },
          "quantity": 1,
          "price": 6,
          "moreInfo": "Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus.",
          "created": "2021-03-20 20:19:35",
          "closes": "2021-11-27 09:13:51",
          "productName": "Pastry - Cheese Baked Scones"
        },
        {
          "id": 6733,
          "inventoryItem": {
            "id": 5,
            "product": {
              "id": "WAUVT64B54N722288",
              "name": "Pastry - Cheese Baked Scones",
              "description": "amet erat nulla tempus vivamus",
              "manufacturer": "Watties",
              "recommendedRetailPrice": 19.88,
              "created": "2021-03-05 01:36:54",
              "images": [],
              "primaryImagePath": null
            },
            "quantity": 1,
            "pricePerItem": 6,
            "totalPrice": 50,
            "manufactured": "2021-01-26",
            "sellBy": null,
            "bestBefore": "2021-09-26",
            "expires": "2021-11-26"
          },
          "quantity": 1,
          "price": 6,
          "moreInfo": "Sed vel enim sit amet nunc viverra dapibus.",
          "created": "2021-04-30 22:03:38",
          "closes": "2021-11-26 11:41:25",
          "productName": "Pastry - Cheese Baked Scones"
        },
        {
          "id": 8380,
          "inventoryItem": {
            "id": 5,
            "product": {
              "id": "WAUVT64B54N722288",
              "name": "Pastry - Cheese Baked Scones",
              "description": "amet erat nulla tempus vivamus",
              "manufacturer": "Watties",
              "recommendedRetailPrice": 19.88,
              "created": "2021-03-05 01:36:54",
              "images": [],
              "primaryImagePath": null
            },
            "quantity": 1,
            "pricePerItem": 6,
            "totalPrice": 50,
            "manufactured": "2021-01-26",
            "sellBy": null,
            "bestBefore": "2021-09-26",
            "expires": "2021-11-26"
          },
          "quantity": 1,
          "price": 6,
          "moreInfo": "Duis mattis egestas metus.",
          "created": "2021-05-23 14:35:36",
          "closes": "2021-11-26 11:00:50",
          "productName": "Pastry - Cheese Baked Scones"
        },
        {
          "id": 3717,
          "inventoryItem": {
            "id": 8725,
            "product": {
              "id": "WAUVT64B54N722288",
              "name": "Pastry - Cheese Baked Scones",
              "description": "amet erat nulla tempus vivamus",
              "manufacturer": "Watties",
              "recommendedRetailPrice": 19.88,
              "created": "2021-03-05 01:36:54",
              "images": [],
              "primaryImagePath": null
            },
            "quantity": 51,
            "pricePerItem": 18.87,
            "totalPrice": 962.37,
            "manufactured": "2020-07-07",
            "sellBy": "2021-01-12",
            "bestBefore": "2021-02-19",
            "expires": "2022-02-01"
          },
          "quantity": 4,
          "price": 75.5,
          "moreInfo": "Praesent blandit lacinia erat.",
          "created": "2021-04-13 02:02:01",
          "closes": "2022-02-01 18:38:03",
          "productName": "Pastry - Cheese Baked Scones"
        },
        {
          "id": 9871,
          "inventoryItem": {
            "id": 781,
            "product": {
              "id": "ZSP-632VBQ",
              "name": "Tomato - Peeled Italian Canned",
              "description": "In hac habitasse platea dictumst.",
              "manufacturer": "Parturient Montes Foundation",
              "recommendedRetailPrice": 45.11,
              "created": "2021-02-05 21:09:46",
              "images": [],
              "primaryImagePath": null
            },
            "quantity": 35,
            "pricePerItem": 44.43,
            "totalPrice": 1555.05,
            "manufactured": "2020-11-15",
            "sellBy": "2021-02-25",
            "bestBefore": "2021-03-08",
            "expires": "2021-05-25"
          },
          "quantity": 17,
          "price": 755.3,
          "moreInfo": "Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Mauris viverra diam vitae quam.",
          "created": "2021-03-01 23:35:12",
          "closes": "2021-05-25 18:47:59",
          "productName": "Tomato - Peeled Italian Canned"
        }
      ],
      currentPage: 1,
      searchbarListings: "",
      businessType: "",
      errors: [],
      toggle: [1,1,1,1,1],
      filteredListings: [],
      enableTable: false,
      displaytype: true,
      currencySymbol: "",
      selected: "",
      likedListingsIds: [],

    };
  },

  mounted() {
    api.checkSession()
      .then((response) => {
        this.userId = response.data.id;
        api.getUserLikedListings(this.userId)
        .then((response) => {
          for (let i = 0; i < response.data.length; i++) {
            this.likedListingsIds.push(response.data[i]["id"]);
          }
        }).catch((err) => {
          throw new Error(`Error trying to get user's likes: ${err}`)
        })
      }).catch((err) => {
      throw new Error(`Error trying to get user id: ${err}`);
    })
  },

  methods: {
    /**
     * Calls the third-party RESTCountries API to retrieve currency information based on user home country.
     * Sets the currency symbol view to the retrieved data.
     * @param country the country to obtain the currency symbol from.
     **/
    setCurrency: function (country) {
      axios.get(`https://restcountries.eu/rest/v2/name/${country}`)
          .then( response => {
            this.currencySymbol = response.data[0].currencies[0].symbol;
          }).catch( err => {
        console.log("Error with getting cities from REST Countries." + err);
      });
    },

    sendLike: function(listingId, listingName) {
      api.addLikeToListing(listingId)
        .then(() => {
          this.$vs.notify(`${listingName} has been added to your watchlist!`);
        })
        .catch((err) => {
          throw new Error(`Error trying to like listing ${listingId}: ${err}`);
        })
    },

    deleteLike: function(listingId, listingName) {
      api.removeLikeFromLising(listingId)
          .then(() => {
            this.$vs.notify(`${listingName} has been deleted from your watchlist!`);
          })
          .catch((err) => {
            throw new Error(`Error trying to delete listing ${listingId} from your watchlist: ${err}`);
          })
    }
  },
}

export default SearchListings;
</script>

<style scoped>
#filter-box {
  background: rgb(31, 116, 255);
  padding: 10px;
}

#filter-box .vs-button-primary.vs-button-filled {
  background: #ffffff !important;
  color: black;
}


#page-title {
  font-size: 30px;
  margin: auto 0;
}

#header-container {
  display: flex;
  justify-content: space-between;
}

#sort-container {
  display: flex;
}

.header-button {
  margin: 0 0.5em;
  min-width: 100px;
}

.main {
  background-color: white;
  width: 90%;
  margin: 1em auto;
}

.prevNextSearchButton {
  margin-left: 1em;
  width: 100px;
}

.displaying {
  text-align: right;
  margin: auto;
}


.profile-text-inner {
  margin: 2em auto;
  width: 95%;
}

/* ===== GRID CARD ===== */

.grid-container {
  display: grid;
  justify-content: space-around;
  grid-template-columns: repeat(auto-fill, 375px);
  grid-column-gap: 2em;

  margin: 50px auto auto auto;
}

.grid-item {
  border-radius: 4px;
  font-size: 30px;
  text-align: left;
  margin: 10px;
  max-width: 350px;
}

.grid-image {
  height: 225px;
  border-radius: 4px 4px 0 0;
  object-fit: cover;
}

.grid-item-footer {
  display: flex;
  justify-content: space-between;
  padding: 0;
}

.grid-item >>> footer {
  padding-bottom: 1em;
  margin-bottom: 4px;
}

/* ===== ===== ===== */

#catalogue-options {
  display: flex;
  margin-left: 10px;
  margin-bottom: 1em;
}

#catalogue-options .search-input {
  width: 80% !important;
}

#catalogue-options .header-button {
  width: 10%;
}



#grid-pagination {
  margin: auto 0 auto auto;
  display: flex;
}


th {
  background: #1F74FF;
  color: white;
}

.table-image >>> img {
  width: 100%;
  height: 100px;
  border-radius: 4px 4px 0 0;
  object-fit: cover;
}

.actionButton {
  text-align: left;
  cursor: pointer;
}

#sort-container {
  width: 12%;
  float: right;
}

div#filter-box {
  display: flex;
  border-radius: 10px;
}

#search-parameter {
  width: 85%;
  display: flex;
}

.vert-row .price-input {
  float: left;
  margin-right: 10px;
}

.parameter {
  width: 33%;
  display: inline-block;
  float: left;
  margin-right: 0px
}

.vl {
  border-left: 2px solid white;
  border-radius: 2px;
  height: auto;
  margin-right: 10px;
}

div#search-parameters {
  width: 80%;
}

.sort-btn {
  margin-top: 10px;
  width: 100% !important;
}

.filter-label {
  color: white;
}

.filter-input {
  width: 200px !important;
}

.price-input {
  width: 70px !important;
}

.con-vs-card.fixedHeight {
  height: auto;
}

@media screen and (max-width: 900px) {
  #catalogue-options {
    flex-direction: column;
  }

  #grid-pagination {
    margin: 1em auto 0 0;
  }

  .header-button {
    margin: 8px;
  }

}

@media screen and (max-width: 625px) {
  .main {
    width: 95%;
  }

  #header-container {
    flex-direction: column;
    margin: auto;
  }

  #page-title {
    margin: auto;
  }

  #header-menu {
    margin: 2em auto 0 auto;
    justify-content: space-evenly;
  }

  #sort-container {
    flex-direction: column;
  }

}

</style>