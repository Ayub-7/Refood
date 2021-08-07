<template>
  <vs-card class="main">
      <div class="profile-text-inner">
        <div id="header-container">
          <div id="page-title">Sales Listings</div>
          </div>
        </div>
        <vs-divider style="padding: 4px;"></vs-divider>

        <div id="catalogue-options">
          <vs-input class="search-input" type="search" placeholder="Enter a listing..." name="searchbarUser" v-model="searchbarListings" style="width: 400px; font-size: 24px" size="large"/>
          <vs-button class="header-button" >Search</vs-button>

          <!-- If search query returns more than 10 products then this should be active -->
<!--          <div id="grid-pagination">-->
<!--            <div class="displaying">Displaying {{ searchRange[0] }}-{{ searchRange[1] }} of-->
<!--              {{ filteredListings.length }}</div>-->
<!--            <div v-if="filteredListings.length > 10" style="display: flex;">-->
<!--              <div v-if="filteredListings.length > productsPerPage" style="display: flex;">-->
<!--                <vs-pagination v-model="currentPage" :total="Math.round(listings.length/productsPerPage +0.4)"/>-->
<!--              </div>-->
<!--            </div>-->
<!--          </div>-->
        </div>

        <vs-divider style="padding: 4px;"></vs-divider>

        <div>
          <div id="filter-box">
            <div id="sort-container">
              <div style="display: flex;">
                <h2 style="margin: auto; padding-right: 4px;">Sort By </h2>
                <select v-model="selected">
                  <option disabled value="">Please select one</option>
                  <option value="id">ID</option>
                  <option value="name">Product Name</option>
                  <option value="description">Description</option>
                  <option value="recommendedRetailPrice">Recommended Retail Price</option>
                  <option value="created">Date Created</option>
                </select>
                <vs-button @click="sortByName(null, selected, 0);" style="margin: 0 2em 0 0.5em; width: 100px">Sort</vs-button>
              </div>
            </div>
          </div>
          <vs-divider style="padding: 4px;"></vs-divider>
          <div class="grid-container" style="margin: auto">
            <vs-card class="grid-item"
                    v-for="listing in listings"
                    v-bind:href="listing.id"
                    :key="listing.id">

<!--              <div slot="media">-->
<!--                <ReImage :imagePath="listing.primaryImagePath" class="grid-image"/>-->
<!--              </div>-->

              <div style="font-size: 13pt; height:100%; line-height: 1.5; display:flex; flex-direction: column;">
                <div style="display: flex; flex-direction: column; justify-content: space-between">
                  <div style="font-size: 16px; font-weight: bold;  text-align: justify; word-break: break-all;">{{ product.name }} </div>
                  <p>{{ product.id }}</p>
                </div>
                <vs-divider></vs-divider>
                <div style="font-size: 16px; font-weight: bold; height: 24px;">{{ product.manufacturer }} </div>
                <p style="font-size: 14px; margin-bottom: 8px;">Created: {{ product.created }} </p>
                <div style="height: 75px; font-size: 14px; overflow-y: auto; ">{{ product.description }} </div>
              </div>

              <div slot="footer" class="grid-item-footer">
                <div style="font-size: 25pt; font-weight: bold; margin: auto 0" >{{currencySymbol + " " +  product.recommendedRetailPrice }} </div>
              </div>
            </vs-card>
<!--            <div class="title-centre">-->
<!--              <vs-pagination v-model="currentPage" :total="Math.round(listings.length/productsPerPage +0.4)"/>-->
<!--            </div>-->
          </div>
        </div>
  </vs-card>
</template>

<script>
import api from "../Api";
import axios from "axios";

const SearchListings = {
  name: "SearchListings",
  data: function() {
    return {
      listings: [],
      currentPage: 1,
      productsPerPage: 12,
      searchbarListings: "",
      errors: [],
      toggle: [1,1,1,1,1],
      filteredListings: [],
      enableTable: false,
      productSearchIndexMin: 0,
      productSearchIndexMax: 12,
      displaytype: true,
      currencySymbol: "",
      selected: "",
    };
  },

  /**
   * api.getBusinessProducts() queries the test back-end (json-server)
   * at /businesses/${businessId}/products which returns a JSON object list of test products which can
   * be filtered by the webpage.
 */
  mounted() {
    api.checkSession()
      .then((response) => {
        this.userId = response.data.id;
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

    /**
     * Filters the displayed products alphabetically by
     * @param event
     * @param JSONField is the name of the field to sort by (as string)
     */
    sortByName: function (event, JSONField) {

      const indexarray = ["id", "name", "description", "recommendedRetailPrice", "created"];

      //toggles the classlist (arrow up or down) in the child DOM element: <i/>
      if(event) {
        if(event.target.firstElementChild) {
          event.target.firstElementChild.classList.toggle('fa-angle-double-down');
          event.target.firstElementChild.classList.toggle('fa-angle-double-up');
        }
      }

      if (this.filteredListings) {
        this.filteredListings.sort(function(a, b) {
          var aField = a[JSONField];
          var bField = b[JSONField];

          //first check if null
          if(aField == null) {
            return 1;
          }
          if(bField == null) {
            return -1;
          }

          //check if a or b contains any numbers or whitespace
          //before capitalzation to avoid errors
          //also check if boolean
          if (!(typeof aField === "boolean" || typeof bField === "boolean")) {
            if ( !(/[^a-zA-Z]/.test(aField) && (/[^a-zA-Z]/.test(bField))) ) {
              aField = aField.toUpperCase();
              bField = bField.toUpperCase();
            }
          }


          //first < second
          if (aField > bField ) {
            return 1;
          }
          //second < first
          if (aField< bField) {
            return -1;
          }
          // a must be equal to b
          return 0;
        });

        let index = indexarray.indexOf(JSONField);

        if (index >= 0) {
          if (this.toggle[index]) {
            this.filteredListings.reverse();
            this.toggle[index]=0;
          } else {
            this.toggle[index]=1;
          }
        }
      }
    },

    /**
     * Helper function to control the number of search range values.
     * It increments Minimum and Maximum search index by 10 as long as the maximum search index does not exceed
     * the number of filtered products.
     */
    increaseSearchRange: function () {
      //Stop value from going over range
      if(this.productSearchIndexMax < this.filteredListings.length) {
        //console.log(this.productSearchIndexMax, this.filteredproducts.length, this.productSearchIndexMin)
        let minMaxDiff = this.productSearchIndexMax - this.productSearchIndexMin;
        this.productSearchIndexMin += minMaxDiff;
        this.productSearchIndexMax += minMaxDiff;
      }
    },

    /**
     * Helper function to control the number of search range values.
     * It decrements Minimum and Maximum search index by 10 as long as the minimum is at least 10.
     */
    decreaseSearchRange: function () {
      //Stop value from reaching negative
      if(this.productSearchIndexMin >= 1) {
        let minMaxDiff = this.productSearchIndexMax - this.productSearchIndexMin;
        this.productSearchIndexMin -= minMaxDiff;
        this.productSearchIndexMax -= minMaxDiff;
      }
    },
  },

  computed: {
    /**
     * Computes ranges to be displayed below table, max of range is switched
     * to length of product query if the length is less than the current range
     * @returns {Array} Array with start index and end index for searchRange
     */
    searchRange: function () {
      let max = this.productSearchIndexMax;

      if (max > this.filteredProducts.length) {
        max = this.filteredProducts.length
      }

      return [this.productSearchIndexMin + 1, max]
    }
  }
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
  margin-bottom: 1em;
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