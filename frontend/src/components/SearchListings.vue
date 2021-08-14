<template>
  <vs-card class="main">
        <div id="header-container">
          <div id="page-title">
            <vs-icon icon="local_offer"/>
            Sales Listings</div>
          </div>
        <vs-divider style="padding: 4px;"></vs-divider>

        <div id="catalogue-options">
          <vs-input class="search-input" type="search" placeholder="Enter a listing..." name="searchbarUser" v-model="productQuery" style="width: 400px; font-size: 24px" size="medium"/>
          <vs-button class="header-button" @click="filterListings">Search</vs-button>

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
                <vs-select v-model="businessType" class="form-control">
                  <vs-select-item :key="business" :value="business" :text="business" v-for="business in businessTypes"></vs-select-item>
                </vs-select>
                </div>
                <div class="vert-row">
                <h3 class="filter-label">
                  Business Name:
                </h3>
                <vs-input class="filter-input" v-model="businessQuery" type="search" placeholder="Business name.." style="font-size: 24px" size="medium"/>
              </div>
              </div>
              <div class="parameter" id="listings">
                <div class="vert-row">
                  <h3 class="filter-label">
                    Location:
                  </h3>
                  <vs-input class="filter-input" v-model="addressQuery" type="search" placeholder="Address.." style="font-size: 24px" size="medium"/>
                </div>
              <div class="vert-row">
                <h3 class="filter-label">
                  Price range:
                </h3>
                <vs-input class="price-input" v-model="minPrice" type="search" placeholder="Min" style="font-size: 24px" size="medium"/>
                <vs-input class="price-input" v-model="maxPrice" type="search" placeholder="Max"  style="font-size: 24px" size="medium"/>
              </div>
              </div>

              <div class="parameter" id="address-closing-date">
                <div class="vert-row">
                <h3 class="filter-label">
                  Min Closing Date:
                </h3>
                <vs-input class="filter-input" v-model="minClosingDate" type="date" style="font-size: 24px"/>
              </div>
                <div class="vert-row">
                  <h3 class="filter-label">Max Closing Date:
                  </h3>
                  <vs-input class="filter-input" v-model="maxClosingDate" type="date"  style="font-size: 24px"/>
                </div>
            </div>
            </div>
            <div class="vl"></div>
            <div id="sort-container">
              <div>
                <h3 class="filter-label" style="margin: auto; padding-right: 4px;">Sort By </h3>
                <div>
                <vs-select v-model="sortBy" autocomplete class="form-control" style="margin-bottom: 10px;">
                  <vs-select-item :key="item.value" :value="item.value" :text="item.text" v-for="item in sortOptions">Please select one</vs-select-item>
                </vs-select>
                  <vs-select v-model="sortDirection">
                    <vs-select-item key="asc" value="asc" text="Ascending"></vs-select-item>
                    <vs-select-item key="desc" value="desc" text="Descending"></vs-select-item>
                </vs-select>
                <vs-button class="sort-btn" @click="filterListings" style="width: 100px">Sort</vs-button>
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
              <div style="margin: 2px 4px; font-size: 14px; font-weight: bold">{{ listing.inventoryItem.product.business.name }}</div>
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
            </vs-card>
            <div class="title-centre">
            </div>
          </div>
        </div>
    <div class="title-container">
      <div class="title-centre">
        <vs-pagination v-model="pageNum" :total="totalPages" @change="filterListings"/>
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
      listings: [],
      searchbarListings: "",
      businessTypes: ["Accommodation and Food Services", "Charitable organisation", "Non-profit organisation", "Retail Trade"],
      errors: [],
      toggle: [1,1,1,1,1],
      filteredListings: [],
      currencySymbol: "",
      selected: "",
      sortOptions:[
        {text: 'Price', value: "price"},
        {text: 'Closing Date', value: "closes"},
        {text: 'Created Date', value: "created"},
        {text: 'City', value: "city"},
        {text: 'Country', value: "country"},
        {text: 'Business Type', value: "businessType"},
        {text: 'Product Name', value: "name"},
        {text: 'Quantity', value: "quantity"},
        {text: 'Manufacturer', value: "manufacturer"},
        {text: 'Seller', value: "seller"},
      ],
      businessQuery: null,
      productQuery: null,
      addressQuery: null,
      businessType: null,
      sortBy: null,
      minPrice: 10.0,
      maxPrice: 20.0,
      minClosingDate: null,
      maxClosingDate: null,

      numListings: 10,
      pageNum: 1,
      totalPages: 0,
      sortDirection: "desc"
    }
  },

  mounted() {
    api.checkSession()
      .then((response) => {
        this.userId = response.data.id;
        this.filterListings();
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

    filterListings: function(){
      api.filterListingsQuery(this.businessQuery, this.productQuery, this.addressQuery, this.sortBy, this.businessType, this.minPrice, this.maxPrice,
      this.minClosingDate,  this.maxClosingDate, this.numListings, this.pageNum-1, this.sortDirection)
      .then((response) => {
        console.log(response.data)
        this.listings = response.data.content
        console.log(this.listings)
        this.totalPages = response.data.totalPages;
      })
      .catch((error) => {
        console.log(error)
      });
    },
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
  width: auto;
}

div#filter-box {
  display: flex;
  border-radius: 10px;
}

.con-select, .filter-input {
  width: auto;
  margin-right: 15px;
  clear: both;
}

#sort-container .con-select {
  margin-right: 0px;
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