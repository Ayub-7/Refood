<template>
  <vs-card class="main">
      <div class="profile-text-inner">
        <div id="header-container">
          <div id="page-title">Product Catalogue</div>
          <div id="header-menu">
            <vs-button class="header-button" :to="{path: `/addtocatalogue`}">Add Product</vs-button>
            <vs-button @click="$router.push(`/businesses/${$route.params.id}/inventory`)" class="header-button" style="margin-right: 0;">Inventory</vs-button>
          </div>
        </div>

        <vs-divider></vs-divider>

        <div id="catalogue-options">
          <div class="switch-container">
            <div class="title" style="margin-top: 5px; margin-right: 10px">
              <p v-if="displaytype">Grid</p>
              <p v-if="!displaytype">List</p>
            </div>

            <label class="switch">
              <input v-model="displaytype" type="checkbox" checked>
              <span class="slider round"></span>
            </label>
          </div>

          <div id="sort-container">
            <div v-show="displaytype" style="display: flex;">
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

          <!-- If search query returns more than 10 products then this should be active -->
          <div id="grid-pagination">
            <div class="displaying">Displaying {{searchRange[0]}}-{{searchRange[1]}} of {{filteredproducts.length}}</div>
            <div v-if="filteredproducts.length > 10" style="display: flex;">
              <vs-button type="border" class='prevNextSearchButton' @click="decreaseSearchRange()">Previous</vs-button>
              <vs-button type="border" class='prevNextSearchButton' @click="increaseSearchRange()">Next</vs-button>
            </div>
          </div>
        </div>


        <div v-if="displaytype">
          <div class="grid-container" style="margin: auto">
            <vs-card class="grid-item"
                    v-for="product in filteredproducts.slice(productSearchIndexMin, productSearchIndexMax)"
                    v-bind:href="product.id"
                    :key="product.id">

              <div slot="media">
                <img v-if="product.primaryImagePath != null && isDevelopment()" class="grid-image" v-bind:src="require('../../../backend/src/main/resources/media/images/businesses/' + getImgUrl(product))" alt="Product image"/>
                <img v-if="product.primaryImagePath != null && !isDevelopment()" class="grid-image" alt="Product Image" v-bind:src="getImgUrl(product)" alt="Product image"/>
                <img v-if="!product.primaryImagePath && isDevelopment()" class="grid-image" src="ProductShoot.jpg" alt="Product image"/>
                <img v-if="!isDevelopment() && !product.primaryImagePath" class="grid-image" :src="getImgUrl(true)" alt="Product image"/>
              </div>

              <div style="font-size: 13pt; height:100%; line-height: 1.5; display:flex; flex-direction: column;">
                <div style="display: flex; flex-direction: column; justify-content: space-between">
                  <div style="font-size: 16px; font-weight: bold;  text-align: justify; word-break: break-all;">{{ product.name }} </div>
                  <p>{{ product.id }}</p>
                </div>
                <vs-divider></vs-divider>
                <div style="font-size: 16px; font-weight: bold">{{ product.manufacturer }} </div>
                <p style="font-size: 14px; margin-bottom: 8px;">Created: {{ product.created }} </p>
                <div style="height: 75px; font-size: 14px; overflow-y: auto; ">{{ product.description }} </div>
              </div>

              <div slot="footer" class="grid-item-footer">
                <div style="font-size: 25pt; font-weight: bold; margin: auto 0" >{{currencySymbol + " " +  product.recommendedRetailPrice }} </div>
                <vs-dropdown vs-trigger-click class="actionButton">
                  <vs-button style="width: fit-content;" type="flat">Actions</vs-button>
                  <vs-dropdown-menu>
                    <vs-dropdown-item @click="goToModify(product.id);">
                      Modify product
                    </vs-dropdown-item>
                    <vs-dropdown-item @click="openImageUpload(product)">
                      Add Image
                    </vs-dropdown-item>
                    <vs-dropdown-group vs-label="Change Primary Image" vs-collapse>
                      <vs-dropdown-item v-for="pImage in product.images" :key="pImage" @click="setPrimaryImage(product, pImage);">
                        {{pImage.name}}
                      </vs-dropdown-item>
                    </vs-dropdown-group>

                    <vs-dropdown-group vs-label="Delete An Image" vs-collapse>
                      <vs-dropdown-item v-for="pImage in product.images" :key="pImage" @click="deleteImage(product, pImage);">
                        {{pImage.name}}
                      </vs-dropdown-item>
                    </vs-dropdown-group>
                  </vs-dropdown-menu>
                </vs-dropdown>
              </div>
            </vs-card>
          </div>
        </div>


        <div v-if="!displaytype">
            <!-- Separate search within results search bar. Rather than calling the database, this filters the table
            entries within the page by matching the search field to the product's firstname, middlename or lastname -->
            <!-- When each heading is clicked, the sortByName() function is called, passing the json field name and a reference to the toggle array -->

            <vs-table :data="filteredproducts.slice(productSearchIndexMin, productSearchIndexMax)" style="border-spacing: 0px 20px; margin: 1em" stripe>
                <template slot="thead" style="background:blue">
                  <vs-th sort-key="id" style="border-radius: 4px 0 0 0;">
                      <div>ID</div>
                  </vs-th >
                  <vs-th sort-key="name" style="min-width: 100px">
                     <div>Product Name</div>
                  </vs-th>
                  <vs-th sort-key="description">
                    <div>Description</div>
                  </vs-th>
                  <vs-th sort-key="manufacturer">
                    <div>Manufacturer</div>
                  </vs-th>
                  <vs-th sort-key="recommendedRetailPrice">
                    <div>Recommended Retail Price</div>
                  </vs-th>
                  <vs-th sort-key="created">
                    <div>Date Created</div>
                  </vs-th>
                  <vs-th style="border-radius: 0 4px 0 0;"><!-- Actions Column --></vs-th>
                </template>

                <template slot-scope="{data}">
                  <vs-tr :key="product.id" v-for="product in data">
                    <vs-td style="width: 20px; padding-right: 10px">
                      <a style="color: rgb(0,0,238);">{{ product.id }}</a>
                      <div>
                        <img v-if="product.primaryImagePath != null && isDevelopment()" class="table-image" v-bind:src="require('../../../backend/src/main/resources/media/images/businesses/' + getImgUrl(product))" alt="Product image"/>
                        <img v-if="product.primaryImagePath != null && !isDevelopment()" class="table-image"  v-bind:src="getImgUrl(product)" alt="Product image"/>
                        <img v-if="!product.primaryImagePath && isDevelopment()" class="table-image" src="ProductShoot.jpg" alt="Product image"/>
                        <img v-if="!isDevelopment() && !product.primaryImagePath" class="table-image" :src="getImgUrl(true)" alt="Product image"/>
                      </div>
                    </vs-td>
                    <vs-td>{{ product.name }} </vs-td>
                    <vs-td>{{ product.description }} </vs-td>
                    <vs-td>{{ product.manufacturer }} </vs-td>
                    <vs-td style="text-align: center">{{currencySymbol + " " + product.recommendedRetailPrice }} </vs-td>
                    <td>{{ product.created }} </td>
                    <td>
                      <!-- Effectively repeated above, should refactor at some point. -->
                      <vs-dropdown vs-trigger-click>
                        <vs-button>Actions</vs-button>
                        <vs-dropdown-menu>
                          <vs-dropdown-item id="modify-dropdown" @click="goToModify(product.id);">
                            Modify product
                          </vs-dropdown-item>
                          <vs-dropdown-item @click="openImageUpload(product)">
                            Add Image
                          </vs-dropdown-item>
                          <vs-dropdown-group vs-label="Change Primary Image" vs-collapse>
                              <vs-dropdown-item v-for="pImage in product.images" :key="pImage" @click="setPrimaryImage(product, pImage);">
                                {{pImage.name}}
                              </vs-dropdown-item>
                          </vs-dropdown-group>
                          <vs-dropdown-group vs-label="Delete An Image" vs-collapse>
                              <vs-dropdown-item v-for="pImage in product.images" :key="pImage" @click="deleteImage(product, pImage);">
                                {{pImage.name}}
                              </vs-dropdown-item>
                          </vs-dropdown-group>

                        </vs-dropdown-menu>
                      </vs-dropdown>
                    </td>
                  </vs-tr>

                  <!-- If search query returns more than 10 products then this should be active -->
                  <tfoot v-if="filteredproducts.length > 1">
                  <tr>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td class="displaying">Displaying {{searchRange[0]}}-{{searchRange[1]}} of {{filteredproducts.length}}</td>
                    <td><vs-button class='prevNextSearchButton' type='border' @click="decreaseSearchRange()">Previous</vs-button></td>
                    <td><vs-button class='prevNextSearchButton' type='border' @click="increaseSearchRange()">Next</vs-button></td>
                  </tr>
                  </tfoot>
                </template>
            </vs-table>
        </div>
      </div>

    <footer>
      "Product shoot" by Aameerule is licensed under CC BY 2.0
    </footer>
    <input type="file" id="fileUpload" ref="fileUpload" style="display: none;" multiple @change="uploadImage($event)"/>
  </vs-card>
</template>

<script>
import api from "../Api";
import {store} from "../store";
import axios from "axios";

const Search = {
  name: "Search",

  data: function() {
    return {
      products: [],
      business: null,
      businessId: null,

      errors: [],
      toggle: [1,1,1,1,1],
      filteredproducts: [],
      enableTable: false,
      productSearchIndexMin: 0,
      productSearchIndexMax: 12,
      displaytype: true,
      currencySymbol: "",
      selected: "",

      selectedProduct: null, // Used to select product to upload image to.
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
        this.businessId = this.$route.params.id;
        this.userId = response.data.id;
        this.getUserInfo(this.userId);
        this.business = this.getBusinessName();

        api.getBusinessProducts(this.businessId)
            .then((innerResponse) => {
              this.$log.debug("Data loaded: ", innerResponse.data);
              this.products = innerResponse.data;
              this.filteredproducts = innerResponse.data;
            })
            .catch((error) => {
              this.$log.debug(error);
              this.error = "Failed to load products";
            })
            .finally(() => (this.loading = false));
      }).catch((error) => {
        this.$log.error(error);
    });
  },

  methods: {
    isDevelopment() {
      return (process.env.NODE_ENV === 'development')
    },

    setPrimaryImage(product, image) {
      this.imageId = image.id;
      this.productId = product.id;
      api.setPrimaryImage(this.businessId, this.productId, this.imageId)
          .then(() => {
            location.reload();
          }).catch((err) => {
            throw new Error(`Error trying to get user info from id: ${err}`);
      });
      this.$vs.notify({title:"Product image updated.", text:"New primary image successfully set.", color:"success"});
    },

    deleteImage(product, image) {
      this.imageId = image.id;
      this.productId = product.id;
      api.deletePrimaryImage(this.businessId, this.productId, this.imageId)
          .then(() => {
            location.reload();
            this.$vs.notify({title:"Product image deleted.", text:"Image successfully deleted.", color:"success"});
          }).catch((err) => {
            console.log(err);
      });
    },

    /**
     * Retrieves the image url link for the given product.
     * @param product the product to retrieve the image for.
     * @return a string link to the product image, or the default image if it doesn't have a product.
     **/
    getImgUrl(product) {
      if (product === true && process.env.NODE_ENV !== 'staging') {
        return '/prod/ProductShoot.jpg';
      } else if (product === true) {
        return '/test/ProductShoot.jpg';
      } else if (product.primaryImagePath != null && process.env.NODE_ENV !== 'development' && process.env.NODE_ENV !== 'staging') {
        return '/prod/prod_images/' + product.primaryImagePath.toString().replace("\\", "/")
      } else if (product.primaryImagePath != null && process.env.NODE_ENV !== 'development') {
        return '/test/prod_images/' + product.primaryImagePath.toString().replace("\\", "/")
      } else if (product.primaryImagePath != null) {
        return product.primaryImagePath.toString().replace("\\", "/")
      } else {
        return '../../public/ProductShoot.jpg'
      }
    },

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

    getBusinessID: function () {
      return store.actingAsBusinessId
    },

    getBusinessName: function () {
      return store.actingAsBusinessName
    },


    //modifies selected catalog item
    goToModify (productId) {
      console.log(productId)
      this.$router.push({path: `/businesses/${store.actingAsBusinessId}/products/${productId}/modify`})
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
    sortByName: function (event, JSONField) {

      const indexarray = ["id", "name", "description", "recommendedRetailPrice", "created"];

      //toggles the classlist (arrow up or down) in the child DOM element: <i/>
      if(event) {
        if(event.target.firstElementChild) {
          event.target.firstElementChild.classList.toggle('fa-angle-double-down');
          event.target.firstElementChild.classList.toggle('fa-angle-double-up');
        }
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

        let index = indexarray.indexOf(JSONField);

        if (index >= 0) {
          if (this.toggle[index]) {
            this.filteredproducts.reverse();
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
      if(this.productSearchIndexMax < this.filteredproducts.length) {
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

    /**
     * Trigger the file upload box to appear.
     * Used for when the actions dropdown add image action is clicked.
     */
    openImageUpload: function(product) {
      this.selectedProduct = product;
      this.$refs.fileUpload.click();
    },

    /**
     * Upload product image when image is uploaded on web page
     * @param e Event object which contains file uploaded
     */
    uploadImage: function(e) {
      //Setup FormData object to send in request
      this.$vs.loading(); //Loading spinning circle while image is uploading (can remove if not wanted)
      for (let image of e.target.files) {
        const fd = new FormData();
        fd.append('filename', image, image.name);
        api.postProductImage(this.businessId, this.selectedProduct.id, fd)
          .then(() => { //On success
            this.$vs.notify({title:`Image for ${this.selectedProduct.id} was uploaded`, color:'success'});
          })
          .catch((error) => { //On fail
            if (error.response.status === 400) {
              this.$vs.notify({title:`Image failed to upload`, color:'danger'});
            } else if (error.response.status === 500) {
              this.$vs.notify({title:`Image cannot be uploaded, there is problem with the server`, color:'danger'});
            }
            this.$log.debug("HERHEHRE");
          })
          .finally(() => {
            this.$vs.loading.close();
            location.reload();
        })
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

#page-title {
  font-size: 30px;
  margin: auto 0;
}

#header-container {
  display: flex;
  justify-content: space-between;
}

#header-menu {
  display: flex;
}

#sort-container {
  display: flex;
}

.switch-container {
  display: flex;
  margin-right: 2em;
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

th {
  background: #1F74FF;
  color: white;
}

.table-image {
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

  .header-button {
    min-width: 0;
    margin: 0 4px;
  }

  #sort-container {
    flex-direction: column;
  }

  .switch-container {
    margin: 1em auto;
  }
}

</style>