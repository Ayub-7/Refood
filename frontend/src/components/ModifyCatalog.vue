<template>
<!-- THIS SHOULD BE USED AS A COMPONENT INSTEAD OF A WHOLE PAGE, THIS NEEDS TO BE TURNED INTO A MODAL ON THE PRODUCT CATALOGUE PAGE -->
  <div class="card">
    <h3 class="card-header">Modify Catalog Product</h3>
    <form>
      <div id="info-field">
        <div id="product-name">
          <vs-input
              :danger="(errors.includes(productName))"
              danger-text="Product name is required"
              class="form-control"
              type="text"
              label-placeholder="Product name (required)"
              v-model="productName"/>
        </div>
        <div id="product-id">
          <vs-input
              :danger="(errors.includes(productId))"
              danger-text="Product id is required"
              class="form-control"
              type="text"
              label-placeholder="Product ID (required)"
              v-model="productId"/>
        </div>
        <div id="rrp">
          <div id="currencySymbol">{{this.currencySymbol}}</div>
          <vs-input
              :danger="(errors.includes('no-rrp') || errors.includes('rrp') || errors.includes('invalid-rrp'))"
              danger-text="RRP is required and must be at least 0 and a Number."
              id="currencyInput"
              label-placeholder="Recommended Retail Price (required)"
              type="text"
              v-model="rrp"/>
          <div id="currencyCode">{{this.currencyCode}}</div>
        </div>
        <div id="manufacturer">
          <vs-input
              :danger="(errors.includes('no-manu'))"
              danger-text="Manufacturer is Required."
              class="form-control"
              type="text"
              value="fdwsd"
              label-placeholder="Manufacturer (required)"
              v-model="manufacturer"/>
        </div>
        <div id="description">
          <vs-textarea
              :danger="(errors.includes('no-desc'))"
              danger-text="Description is Required."
              class="form-control"
              type="text"
              label="Description (required)"
              width="400px"
              v-model="description"/>
        </div>

      </div>
      <button
          type="button"
          class="add-button"
          @click="checkForm(); ModifyItem();">Save Changes</button>
      <button
          type="button"
          class="add-button"
          @click="cancel();">Cancel</button>
    </form>
  </div>
</template>

<script>
import CurrencyInput from "../components/CurrencyInput";
import api from "../Api";
import axios from "axios";
import {store} from "../store";

const ModifyCatalog = {
  name: "ModifyCatalog",
  components: {CurrencyInput},
  data: function () {
    return {
      product: null,
      user: null,
      errors: [],
      productName: "",
      productId: "",
      description: "",
      manufacturer: "",
      currencySymbol: "",
      currencyCode: "",
      rrp: "",
      currencyMultiplier: 1,
    };
  },
  methods: {

    /**
     * The function checks the inputs of the registration form to ensure they are in the right format.
     * The function also updates the errors list that will be displayed on the page if at least one of the input boxes
     * is in the wrong format.
     */

    checkForm: function() {
      var invalidChars = /[^a-zA-Z/ -\d]/i;
      //var isValidName = !(invalidChars.test(this.productName));
      //var isValidID = !(invalidChars.test(this.productId));
      //var isValidManu = !(invalidChars.test(this.manufacturer));
      this.errors = [];
      if (this.productName.match(invalidChars)) {
        this.errors.push("invalid-chars");
      }
      if (this.productId.match(invalidChars)) {
        this.errors.push("invalid-chars");
      }
      if (this.manufacturer.match(invalidChars)) {
        this.errors.push("invalid-chars");
      }
      if (this.productName.length === 0) {
        this.errors.push(this.productName);
      }

      if (this.productName.length > 25) {
        this.errors.push("long-name");
      }

      if (this.productId.length === 0) {
        this.errors.push(this.productId);
      }

      if (this.productId.length > 20) {
        this.errors.push("long-id");
      }

      if (this.description.length === 0) {
        this.errors.push('no-desc');
      }

      if (this.description.length > 200) {
        this.errors.push('long-desc');
      }

      if (this.manufacturer.length === 0) {
        this.errors.push('no-manu');
      }

      if (this.rrp.length === 0 || this.rrp === null) {
        this.errors.push('no-rrp');
      } else if (this.rrp < 0) {
        this.errors.push('rrp');
      }

      if (isNaN(this.rrp)) {
        this.errors.push('invalid-rrp');
      }

      if (this.errors.length >= 1) {
        if (this.errors.includes(this.productName) || this.errors.includes(this.productId)
            || this.errors.includes('rrp') || this.errors.includes('no-rrp')
            || this.errors.includes('invalid-rrp') || this.errors.includes('no-manu')) {
          this.$vs.notify({
            title: 'Failed to create catalogue item',
            text: 'Required fields are missing.',
            color: 'danger'
          });
        }
      }
      if (this.errors.includes('no-desc')) {
        this.$vs.notify({
          title: 'Failed to create catalogue item',
          text: 'Description is Required.',
          color: 'danger'
        });
      }

      if (this.errors.includes('long-desc')) {
        this.$vs.notify({
          title: 'Failed to create catalogue item',
          text: 'Description is too long (70 characters MAX).',
          color: 'danger'
        });
      }
      if (this.errors.includes('long-name')) {
        this.$vs.notify({
          title: 'Failed to create catalogue item',
          text: 'Name is too long (15 characters MAX).',
          color: 'danger'
        });
      }
      if (this.errors.includes('long-id')) {
        this.$vs.notify({
          title: 'Failed to create catalogue item',
          text: 'ID is too long (17 characters MAX).',
          color: 'danger'
        });
      }
      if (this.errors.includes('invalid-chars')) {
        this.$vs.notify({
          title: 'Failed to create catalogue item',
          text: 'Invalid Characters',
          color: 'danger'
        });
      }
    },

    /**
     * Creates a put request when user submits form, using the modifyProduct function from Api.js
     */
    ModifyItem: function() {
      //Use creatItem function of API to POST user data to backend
      //https://www.npmjs.com/package/json-server
      if(this.errors.length === 0){
        // var RRPUSD = this.convertRRPtoUSD(this.rrp);
        api.modifyProduct(this.$route.params.id, this.$route.params.productId , this.productId, this.productName, this.description, this.manufacturer, this.rrp)
            .then((response) => {
              this.$log.debug("catalogue item modified:", response.data);
              this.$router.push({path: `/businesses/${store.actingAsBusinessId}/products`});
            }).catch((error) => {
          if(error.response){
            if(error.response.status === 400){
              this.$vs.notify({title:'Failed to modify catalogue item', text:'Product ID is already in use', color:'danger'});
            }
          }
          this.$log.debug("Error Status:", error)
        });
      }
    },

    /**
     * Calls API getBusinessProducts, filters to get product from route, then sets prefilled values to be the products values
     * @param businessId id of business, usually retrieved from route parameters
     * @param productId id of product, usually retrieved from route parameters
     */

    getProduct(businessId, productId) {
      api.getBusinessProducts(businessId)
      .then((response) => {
        this.product = response.data.filter(x => x.id == productId)[0] //Get product that matches id in route param
        if (this.product == null) {
          this.$router.push({path: `/businesses/${store.actingAsBusinessId}/products`})
        }
        this.productId = this.product.id;
        this.productName = this.product.name;
        this.manufacturer = this.product.manufacturer;
        this.description = this.product.description;
        this.rrp = this.product.recommendedRetailPrice
      }).catch((err) => {
        if(err.response.status == 401) {
          this.$router.push({name:'LoginPage'});
        } else {
          this.$log.error("Couldnt preload productInformation");
        }
      })
    },

    cancel: function(){
      this.$router.push({path: `/businesses/${this.$route.params.id}/products`});
    },

    getUserInfo: function (userId) {
      api.getUserFromID(userId) //Get user data
          .then((response) => {
            this.user = response.data;
            this.setCurrency(this.user.homeAddress.country);
          }).catch((err) => {
        if (err.response.status === 401) {
          this.$vs.notify({title: 'Unauthorized Action', text: 'You must login first.', color: 'danger'});
          this.$router.push({name: 'LoginPage'});
        } else {
          throw new Error(`ERROR trying to obtain user info from Id: ${err}`);
        }
      });
    },

    setCurrency: function (country) {
      axios.get(`https://restcountries.eu/rest/v2/name/${country}`)
          .then( response => {
            this.currencySymbol = response.data[0].currencies[0].symbol;
            this.currencyCode = response.data[0].currencies[0].code;

            var query = this.currencyCode + '_USD';
            const url = "https://free.currconv.com/api/v7/convert?q="+query+"&compact=ultra&apiKey=a67b4ad2aba59aca187c"
            axios
                .get(url)
                .then(response => {
                  this.currencyMultiplier = response.data[query];
                }).catch( err => {
              this.$log.error("Error with getting multiplier from REST Currencies." + err);

            });
          }).catch( err => {
        console.log("Error with getting cities from REST Countries." + err);
      });
    },
    checkUserSession: function() {
      api.checkSession()
          .then((response) => {
            this.getUserInfo(response.data.id);
          })
          .catch((error) => {
            this.$log.error("Error checking sessions: " + error);
            this.$vs.notify({title:'Error', text:'ERROR trying to obtain user info from session:', color:'danger'});
          });
    },
    convertRRPtoUSD: function (rrp) {

      return this.currencyMultiplier*rrp;
    }
  },
  mounted() {
    this.checkUserSession();
    this.getProduct(this.$route.params.id, this.$route.params.productId);
    
    // this.presetValues();
  }
}
export default ModifyCatalog;

</script>

<style scoped>

/*
Add button's styling
 */
.add-button {
  cursor: pointer;
  border-radius: 5em;
  color: #fff;
  background: #1F74FF;
  border: 0;
  z-index: 1000;
  padding: 10px 40px;
  margin: 2em;
  font-size: 13px;
  box-shadow: 0 0 20px 1px rgba(0, 0, 0, 0.04);
}

/**
Card styling.
*/
.card {
  font-family: 'Ubuntu', sans-serif;

  display: grid;
  grid-template-columns: 1fr;
  grid-template-rows: auto auto auto;
  grid-row-gap: 1em;

  max-width: 650px;
  background-color: white;
  margin: 1em auto;
  padding: 0.5em 0 0.5em 0;
  border-radius: 20px;
  border: 2px solid rgba(0, 0, 0, 0.02);
  box-shadow: 0 .5rem 1rem rgba(0, 0, 0, .15);
}

/**
Card header styling.
*/
.card-header {
  grid-row: 1;
  grid-column: 1;

  text-align: center;
  font-weight: bold;
  font-size: 24px;
  color: #1F74FF;

  margin: 0;
  padding: 0.5em 0;
}

/**
Form styling
*/
form {
  grid-row: 2;
  grid-column: 1;

  margin: auto;

  display: grid;
  grid-template-columns: repeat(2, auto);
  grid-template-rows: repeat(3, auto);
}

label, input {
  display: block;
}

/**
Styling for form elements.
*/
.form-control {
  font-family: 'Ubuntu', sans-serif;
  padding: 3px 10px;
  margin: 0.5em;
}

#info-field {
  grid-column: 1/3;
  display: grid;
  margin: auto;
  grid-template-columns: repeat(2, auto);
  grid-template-rows: repeat(5, auto);
}

#product-name {
  grid-column: 1;
  grid-row: 1;
}

#product-id {
  grid-column: 2;
  grid-row: 1;
}

#manufacturer {
  grid-column: 2;
  grid-row: 2;
}

#description {
  grid-column: 1 / 3;
  grid-row: 3;
  width: 100%;
}

#rrp {
  grid-column: 1;
  grid-row: 2;

  margin: 0;
  display: flex;
}

#currencySymbol {
  grid-row: 1;
  grid-column: 1;
  margin: auto;
  font-size: 15px;
  line-height: 20px;
}

#currencyInput {
  grid-row: 1;
  grid-column: 2;
}

#currencyCode {
  grid-row: 1;
  grid-column: 3;

  margin: auto;
  font-size: 15px;
}


</style>