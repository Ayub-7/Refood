<template>
  <div class="main" id="body">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <form class="main1">
      <div class="profile-text-inner">
        <h1 class="title text-center" style="font-size: 40px">{{this.business}} Products</h1>

        <div style="margin: 50px;">
          <h2 class="title" style="margin-bottom: 20px">Sort By: </h2>
          <select v-model="selected">
            <option disabled value="">Please select one</option>
            <option @click="sortByName($event, 'id', 0);">ID</option>
            <option @click="sortByName($event, 'name', 1);">Product Name</option>
            <option @click="sortByName($event, 'description', 2);" >Description</option>
            <option @click="sortByName($event, 'recommendedRetailPrice', 3);" >Recommended Retail Price</option>
            <option @click="sortByName($event, 'created', 4);" >Date Created</option>
          </select>
        </div>
        </div>


      <div>
        <!-- Separate search within results search bar. Rather than calling the database, this filters the table
        entries within the page by matching the search field to the product's firstname, middlename or lastname -->
        <!-- When each heading is clicked, the sortByName() function is called, passing the json field name and a reference to the toggle array -->

        <table class="profile-text-inner" style="border-spacing: 0px 20px">
        <tr>

          <th>
            <button type="button" class="row-md-2 headingButton" @click="sortByName($event, 'id', 0);">
              ID <i class="fa fa-angle-double-down" style="font-size:20px"/>
            </button>
          </th>
          <th>
            <button type="button" class="row-md-2 headingButton" @click="sortByName($event, 'name', 1);">
              Product Name <i class="fa fa-angle-double-down" style="font-size:20px"/>
            </button>
          </th>
          <th>
            <button type="button" class="row-md-2 headingButton" @click="sortByName($event, 'description', 2);">
              Description<i class="fa fa-angle-double-down" style="font-size:20px"/>
            </button>
          </th>
          <th>
            <button type="button" class="row-md-2 headingButton" @click="sortByName($event, 'recommendedRetailPrice', 3)">
              Recommended Retail Price<i class="fa fa-angle-double-down" style="font-size:20px"/>
            </button>
          </th>
          <th>
            <button type="button" class="row-md-2 headingButton" @click="sortByName($event, 'created', 4)">
              Date Created<i class="fa fa-angle-double-down" style="font-size:20px"/>
            </button>
          </th>
        </tr>

        <tr v-for="product in filteredproducts.slice(productSearchIndexMin, productSearchIndexMax)"
            v-bind:href="product.id"
            :key="product.id">
          <td><a v-bind:href="'/products?id='+ product.id">{{ product.id }}</a></td>
          <td>{{ product.name }} </td>
          <td>{{ product.description }} </td>
          <td>{{ product.recommendedRetailPrice }} </td>
          <td>{{ product.created }} </td>
        </tr>

        <!-- If search query returns more than 10 products then this should be active -->
        <tfoot v-if="filteredproducts.length > 10">
          <tr>
            <td class="displaying">Displaying {{searchRange[0]}}-{{searchRange[1]}} of {{filteredproducts.length}}</td>
            <td><input class='row-md-2 prevNextSearchButton' type='button' @click="decreaseSearchRange()" value='Prev'/></td>
            <td><input class='row-md-2 prevNextSearchButton' type='button' @click="increaseSearchRange()" value='Next'/></td>
          </tr>
        </tfoot>
      </table>
      </div>

    </form>

  </div>
</template>

<script>
import api from "../Api";
import {store} from "@/store";
//import {store} from "../store"

const Search = {
  name: "Search",
  data: function() {
    return {
      errors: [],
      toggle: [1,1,1,1,1],
      searchbar: "",
      searchbarResults: "",
      products: [],
      filteredproducts: [],
      reducedproducts: [],
      enableTable: false,
      resultTrack: "",
      productSearchIndexMin: 0,
      productSearchIndexMax: 10,
      business: null
    };
  },

  /**
   *
   * api.getBusinessProducts() queries the test back-end (json-server)
   * at /businesses/${businessId}/products which returns a JSON object list of test products which can
   * be filtered by the webpage.
 */
  mounted() {
    console.log("this.getBusinessID()");
    console.log(this.getBusinessID());

    api.getBusinessFromId(this.getBusinessID())
        .then((response) => {
          console.log(response.data);
          this.$log.debug("getBusinessFromId: ", response.data);
          this.business = response.data;
        })
        .catch((error) => {
          this.$log.debug(error);
          this.error = "Failed to get Business";
        })
        .finally(() => (this.loading = false));


    api.getBusinessProducts(this.getBusinessID())
        .then((response) => {
          console.log(response.data);
          this.$log.debug("Data loaded: ", response.data);
          this.products = response.data;
          this.filteredproducts = response.data;
        })
        .catch((error) => {
          this.$log.debug(error);
          this.error = "Failed to load products";
        })
        .finally(() => (this.loading = false));
  },


  methods: {
    //todo: update getBusinessID get to use new store.js upon merge...
    getBusinessID: function () {
      return store.actingAsBusinessId
    },

    /**
     * Searches for the products in the database by calling the API function with an SQL query to find the
     * products based on the input in the search box.
     */
    searchProducts: function () {
      this.productSearchIndexMin = 0;
      this.productSearchIndexMax = 10;
      if (this.searchbar.length > 0) {
        this.enableTable = true;
        this.resultTrack = this.searchbar;
        console.log(this.searchbar);
        api
            .searchQuery(this.searchbar)
            .then((response) => {
              console.log(response.data);
              this.$log.debug("Data loaded: ", response.data);
              this.products = response.data;
              this.filteredproducts = response.data;
            })
            .catch((error) => {
              this.$log.debug(error);
              this.error = "Failed to load products";
            })
            .finally(() => (this.loading = false));
      } else {
        this.errors.push("Please enter input the product you want to search for");
      }
    },

    /**
     * makes the checkproduct an administrator
     * if they are already, revoke privledges...
     */

    toggleAdmin: function (currentproduct) {
      if (currentproduct.role == 'product') {
        //currentproduct.id = true;
        api.makeproductAdmin(currentproduct.id);
        currentproduct.role = 'GAA'
        //console.log("admin true"+currentproduct.id+currentproduct.firstName)
      } else if (currentproduct.role == 'GAA') {
        api.revokeproductAdmin(currentproduct.id);
        currentproduct.role = 'product'
      }
    },

    /**
     * Filters the displayed products alphabetically by
     * @param JSONField is the name of the field to sort by (as string)
     * @param index references the toggle state list in the data object (int)
     */
    sortByName: function (event, JSONField, index) {
      //toggles the classlist (arrow up or down) in the child DOM element: <i/>
      if(event.target.firstElementChild) {
        event.target.firstElementChild.classList.toggle('fa-angle-double-down');
        event.target.firstElementChild.classList.toggle('fa-angle-double-up');
      }

      if (this.filteredproducts) {
        this.filteredproducts.sort(function(a, b) {
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
        if (this.toggle[index]) {
          this.filteredproducts.reverse();
          this.toggle[index]=0;
        } else {
          this.toggle[index]=1;
        }
      }
    },

    /**
     * Filters the SQL query results to be displayed on this webpage.
     */
    filterproducts: function () {
      if (this.searchbar && this.resultTrack == this.searchbar) {
        this.filteredproducts = this.products.filter((item) => {
          return (item.firstName + " " + item.middleName + " " + item.lastName).includes(this.searchbar) || (item.firstName + " " + item.lastName).includes(this.searchbar);
        });

      }
    },



    /**
     * Helper function to control the number of search range values.
     * It increments Minimum and Maximum search index by 10 as long as the maximum search index does not exceed
     * the number of filtered products.
     */
    increaseSearchRange: function () {
      //Stop value from going over range
      if(this.productSearchIndexMax < this.filteredproducts.length) {
        //console.log(this.productSearchIndexMax, this.filteredproducts.length, this.productSearchIndexMin)
        this.productSearchIndexMin += 10;
        this.productSearchIndexMax += 10;
      }
    },

    /**
     * Helper function to control the number of search range values.
     * It decrements Minimum and Maximum search index by 10 as long as the minimum is at least 10.
     */
    decreaseSearchRange: function () {
      //Stop value from reaching negative
      if(this.productSearchIndexMin >= 10) {
        this.productSearchIndexMin -= 10;
        this.productSearchIndexMax -= 10;
      }
    }

  },
  computed: {
    /**
     * Computes ranges to be displayed below table, max of range is switched
     * to length of product query if the length is less than the current range
     * @returns {Array} Array with start index and end index for searchRange
     */
    searchRange: function () {
      let max = this.productSearchIndexMax;

      if (max > this.filteredproducts.length) {
        max = this.filteredproducts.length
      }

      return [this.productSearchIndexMin + 1, max]
    }
  }
}

export default Search;
</script>

<style scoped>

.main1{
  top:-100px;
}

.title {
  padding-top: 40px;
  color: #3B5998;
  font-family: 'Ubuntu', sans-serif;
  font-weight: bold;
  font-size: 23px;
}

.main {
  background-color: white;
  top: 1px;
}

.searchButton {
  cursor: pointer;
  border-radius: 5em;
  color: #fff;
  background: #3B5998;
  border: 0;
  padding-left: 40px;
  padding-right: 40px;
  padding-bottom: 10px;
  padding-top: 10px;
  font-family: 'Ubuntu', sans-serif;
  margin-left: 35%;
  font-size: 13px;
  box-shadow: 0 0 20px 1px rgba(0, 0, 0, 0.04);
}

.prevNextSearchButton {
  cursor: pointer;
  border-radius: 5em;
  color: #fff;
  background: linear-gradient(to right, #9C27B0, #E040FB);
  border: 0;
  padding-left: 20px;
  padding-right: 20px;
  padding-bottom: 5px;
  padding-top: 5px;
  font-family: 'Ubuntu', sans-serif;
  margin-top: 10px;
  font-size: 13px;
  box-shadow: 0 0 20px 1px rgba(0, 0, 0, 0.04);
}



.displaying {
  padding-top: 15px;
}


.headingButton {
  cursor: pointer;
  border-radius: 5em;
  color: #fff;
  background: #3B5998;
  border: 0;
  padding-left: 20px;
  padding-right: 20px;
  padding-bottom: 10px;
  padding-top: 10px;
  font-family: 'Ubuntu', sans-serif;
  margin-left: 35%;
  font-size: 13px;
  box-shadow: 0 0 20px 1px rgba(0, 0, 0, 0.04);
}

.profile-text-inner {
  margin: 7em auto;
  width: 90%;
  height: 80%;
}

</style>