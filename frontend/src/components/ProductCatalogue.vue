<template>
  <div class="main" id="body">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <form class="main1">
      <div class="profile-text-inner">
        <div style="display: flex; ">
          <h1 class="title text-center" style="font-size: 40px; margin-bottom: 50px; ">{{this.business}} Products</h1>

          <div style="margin-right: 0; margin-left: auto; display: flex">
            <div class="title" style="margin-top: 5px; margin-right: 10px">
              <p v-if="displaytype">Grid</p>
              <p v-if="!displaytype">List</p>
            </div>

            <label class="switch" >
              <input v-model="displaytype" type="checkbox" checked>
              <span class="slider round"></span>
            </label>

          </div>
        </div>
        <div style="display: flex; ">
          <div style="display: flex;">
            <h2 class="title" style="margin-top: 5px; margin-right: 5px">Sort By: </h2>
            <select v-model="selected">
              <option disabled value="">Please select one</option>
              <option @click="sortByName($event, 'id', 0);">ID</option>
              <option @click="sortByName($event, 'name', 1);">Product Name</option>
              <option @click="sortByName($event, 'description', 2);" >Description</option>
              <option @click="sortByName($event, 'recommendedRetailPrice', 3);" >Recommended Retail Price</option>
              <option @click="sortByName($event, 'created', 4);" >Date Created</option>
            </select>
          </div>

          <!-- If search query returns more than 10 products then this should be active -->
          <tfoot style="margin-right: 0; margin-left: auto">
          <tr>
            <td class="displaying">Displaying {{searchRange[0]}}-{{searchRange[1]}} of {{filteredproducts.length}}</td>
            <div  v-if="filteredproducts.length > 10">
              <td><input class='row-md-2 prevNextSearchButton' type='button' @click="decreaseSearchRange()" value='Prev'/></td>
              <td><input class='row-md-2 prevNextSearchButton' type='button' @click="increaseSearchRange()" value='Next'/></td>
            </div>
          </tr>
          </tfoot>
        </div>

        <div v-if="displaytype">
          <div class="grid-container">
            
            <div style="position:relative" class="grid-item sub-container" v-for="product in filteredproducts.slice(productSearchIndexMin, productSearchIndexMax)"
                v-bind:href="product.id"
                :key="product.id">
              <div>
                <img v-if="!product.images[0]" style="width: 100%; height: 100%;   border-radius: 1em;" src="../../public/ProductShoot.jpg"/>
                <img v-if="product.images[0]" style="width: 100%; height: 100%;   border-radius: 1em;" v-bind:src="require('../../../backend/' + product.images[0].filename.replace('./',''))"/>
               </div>
              <div style="font-family: 'Ubuntu', sans-serif; font-size: 13pt; margin: 10px;  line-height: 1.5; display:flex; flex-direction: column;">
              
                <div style="display: flex;">
                  <p><a v-bind:href="'/products?id='+ product.id">{{ product.id }}</a></p>
                  <p style="margin-right: 0; margin-left: auto">{{ product.created }} </p>
                </div>
                <div class="action_btn">
                  <ImageUpload v-bind:productId=product.id v-bind:businessId=businessId style="margin-bottom: 10px; margin-top: 15px;"/>
                  <button type="button" id="modify" style="margin-bottom: 7px; margin-top: -9px;" @click="goToModify(); setProductToAlter(product.id)">Modify product</button>
                </div>
                <p style="font-size: 20pt; font-weight: bold;  text-align: justify; margin-bottom: 20px;">{{ product.name }} </p>
                <p style="font-size: 15pt; margin-bottom: 35px">{{ product.description }} </p>
                <p style="color: #9c27b0; font-size: 25pt; font-weight: bold; position: absolute; bottom: 15px;" >{{currencySymbol + " " +  product.recommendedRetailPrice }} </p>
              </div>
            </div>
          </div>
        </div>


        <div v-if="!displaytype">
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

                <td style="width: 20px; padding-right: 10px">
                  <a v-bind:href="'/products?id='+ product.id">{{ product.id }}</a>
                  <div>
                    <img style="width: 100%; height: 100%;   border-radius: 1em;" src="../../public/ProductShoot.jpg"/>
                  </div>
                </td>
                <td>{{ product.name }} </td>
                <td>{{ product.description }} </td>
                <td style="text-align: center">{{currencySymbol + " " + product.recommendedRetailPrice }} </td>
                <td>{{ product.created }} </td>
                <td>
                  <ImageUpload v-bind:productId=product.id v-bind:businessId=businessId />
                  <button type="button" id="modify" style="margin-bottom: 10px; margin-top: 10px;" @click="goToModify(); setProductToAlter(product.id)">Modify product</button>
                </td>
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
        </div>
      </div>

    </form>

    <footer>
      "Product shoot" by Aameerule is licensed under CC BY 2.0
    </footer>
  </div>
</template>

<script>
import api from "../Api";
import {store, mutations} from "@/store";
//import {store} from "../store"
import ImageUpload from "./ImageUpload";
import axios from "axios";
const Search = {
  name: "Search",

  components: {
    ImageUpload
  },
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
      business: null,
      businessId: null,
      displaytype: true,
      currencySymbol: "",
      currencyCode: "",
      selected: "",
    };
  },

  /**
   *
   * api.getBusinessProducts() queries the test back-end (json-server)
   * at /businesses/${businessId}/products which returns a JSON object list of test products which can
   * be filtered by the webpage.
 */
  mounted() {
    let userId = store.loggedInUserId;
    this.getUserInfo(userId);

    this.business = this.getBusinessName();
    this.businessId = this.getBusinessID();
    api.getBusinessProducts(this.businessId)
        .then((response) => {
          console.log(response.data);
          this.$log.debug("Data loaded: ", response.data);
          this.products = response.data;
          this.filteredproducts = response.data;

          console.log("this.products[0].images[0].filename");
          console.log(this.products[0].images[0].filename);

        })
        .catch((error) => {
          this.$log.debug(error);
          this.error = "Failed to load products";
        })
        .finally(() => (this.loading = false));
  },


  methods: {
    getUserInfo: function(userId) {
      if(store.loggedInUserId != null) {
        api.getUserFromID(userId) //Get user data
            .then((response) => {
              this.user = response.data;
              this.setCurrency(this.user.homeAddress.country);
            }).catch((err) => {
          throw new Error(`Error trying to get user info from id: ${err}`);
        });
      } else {
        this.$router.push({path: "/login"}); //If user not logged in send to login page
      }
    },

    setCurrency: function (country) {
      axios.get(`https://restcountries.eu/rest/v2/name/${country}`)
          .then( response => {
            this.currencySymbol = response.data[0].currencies[0].symbol;
            this.currencyCode = response.data[0].currencies[0].code;
          }).catch( err => {
        console.log("Error with getting cities from REST Countries." + err);
      });
    },

    getBusinessID: function () {
      return store.actingAsBusinessId
    },

    getBusinessName: function () {
      return store.actingAsBusinessName
    },

    //sets the product to alter id
    setProductToAlter(productId) {
      mutations.setProductToAlter(productId);
    },
    //modifies selected catalog item
    goToModify: function () {
      this.$router.push({path: `/businesses/${store.actingAsBusinessId}/products/modify`})
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
#modify {
  background: #3B5998;
  color: white;
  padding: 0.8em;
  border-radius: 20px;
  cursor: pointer;
}

#modify:hover {
  background: #30487c
}

.prevNextSearchButton {
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
  font-size: 13px;
  box-shadow: 0 0 20px 1px rgba(0, 0, 0, 0.04);
  margin-left: 0;
  margin-right: auto;
}

.profile-text-inner {
  margin: 7em auto;
  padding-top: 5em;
  width: 90%;
  height: 80%;
}

.grid-container {
  display: grid;
  grid-template-columns: auto auto auto auto;
  padding: 10px;
  margin-top: 50px;
}
.grid-item {
  border: 1px solid rgba(0, 0, 0, 0.1);
  font-size: 30px;
  text-align: left;
  margin: 10px;
}
.sub-container {
  padding: 0.75em;
  border-radius: 1.5em;
  box-shadow: 0 11px 35px 2px rgba(0, 0, 0, 0.14);
  background-color: #F5F5F5;
}


.switch {
  position: relative;
  display: inline-block;
  width: 60px;
  height: 34px;
}

.switch input {
  opacity: 0;
  width: 0;
  height: 0;
}

.slider {
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #ccc;
  -webkit-transition: .4s;
  transition: .4s;
}

.slider:before {
  position: absolute;
  content: "";
  height: 26px;
  width: 26px;
  left: 4px;
  bottom: 4px;
  background-color: white;
  -webkit-transition: .4s;
  transition: .4s;
}

input:checked + .slider {
  background-color: #2196F3;
}

input:focus + .slider {
  box-shadow: 0 0 1px #2196F3;
}

input:checked + .slider:before {
  -webkit-transform: translateX(26px);
  -ms-transform: translateX(26px);
  transform: translateX(26px);
}

/* Rounded sliders */
.slider.round {
  border-radius: 34px;
}

.slider.round:before {
  border-radius: 50%;
}

</style>