<template>
  <div class="card" v-if="this.user != null">
    <h3 class="card-header">Add a Product to your Catalogue</h3>
    <form>
      <div id="info-field">
        <div id="product-name">
          <vs-input
              class="form-control"
              type="text"
              label-placeholder="Product name (required)"
              v-model="productname"/>
        </div>
        <div id="product-id">
          <vs-input
              class="form-control"
              type="text"
              label-placeholder="Product ID (required)"
              v-model="productid"/>
        </div>
        <div id="manufacturer">
          <vs-input
              class="form-control"
              type="text"
              label-placeholder="Manufacturer"
              v-model="manufacturer"/>
        </div>
        <div id="description">
          <vs-textarea
              class="form-control"
              type="text"
              label="Description"
              v-model="description"/>
        </div>
        <div id="rrp">
          <p id="rrp-title">Recommended Retail Price:</p>
          <currency-input
              class="form-control"
              id="currency-box"
              label="Price"
              v-model="rrp"
              :options="{
                locale: 'en-NZ',
                currency: `NZD`,
                valueRange: { min: 0 },
                precision: 2,
                distractionFree: {
                  hideCurrencySymbol: true,
                  hideGroupingSymbol: true
                },
                autoSign: true,
                useGrouping: true
              }"
          />
        </div>
      </div>
      <button
        type="button"
        class="add-button"
        @click="checkForm()">Add Item to Catalogue</button>
      <div>{{this.currencySymbol}}, {{this.currencyCode}}</div>
    </form>
  </div>
</template>

<script>
import CurrencyInput from "@/components/CurrencyInput";
import api from "@/Api";
import axios from "axios";
import {store} from "../store";

const AddToCatalogue = {
  name: "AddToCatalogue",
  components: {CurrencyInput},
  data: function () {
    return {
      user: null,
      errors: [],
      productid: "",
      productname: "",
      description: "",
      manufacturer: "",
      currencySymbol: "",
      currencyCode: "",
      rrp: null
    };
  },
  methods: {
/*    /!**
     * The function checks the inputs of the registration form to ensure they are in the right format.
     * The function also updates the errors list that will be displayed on the page if at least one of the input boxes
     * is in the wrong format.
     *!/
    checkForm: function() {
      this.errors = [];
      if (this.productname.length === 0) {
        this.errors.push(this.productname);
      }

      if (this.productid.length === 0) {
        this.errors.push(this.productid);
      }

      if (this.errors.length >= 1) {
        this.$vs.notify({title:'Failed to create catalogue item', text:'Required fields are missing.', color:'danger'});
      }
    },
    /!**
     * Creates a POST request when user submits form, using the createUser function from Api.js
     *!/
    createItem: function() {

      //Use createUser function of API to POST user data to backend
      //AT THE MOMENT BACKEND IS JUST A JSON-SERVER, THE SERVER IS RUN USING testUser.json AS A JSON-SERVER ON PORT 9499
      //https://www.npmjs.com/package/json-server
      if(this.errors.length == 0){

        //const homeAddress = {



        api.createProduct()
            .then((response) => {
              this.$log.debug("New item created:", response.data);
              // window.location.replace("http://localhost:9500/Users?id=" + response.data.id);
              mutations.setUserLoggedIn(response.data.userId, response.data.role); //Store user info into program state, used for later calls
              //LOAD USER PAGE, USING ROUTER
              this.$router.push({name: 'UserPage', params: {id: response.data.userId}})
              //this.$router.push({path: `/users/${response.data.id}`});
            }).catch((error) => {
          if(error.response){
            console.log(error.response.status);
            console.log(error.response.message);
            this.errors.push("Email already in use");
          }
          this.$log.debug("Error Status:", error)
        });
      }
    }*/

    getUserInfo: function(userId) {
      if(store.loggedInUserId != null) {
        api.getUserFromID(userId) //Get user data
            .then((response) => {
              console.log(response.data.id);
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
      console.log(country);
      axios.get(`https://restcountries.eu/rest/v2/name/${country}`)
          .then( response => {
            console.log(response.data[0].currencies[0].symbol);
            this.currencySymbol = response.data[0].currencies[0].symbol;
            this.currencyCode = response.data[0].currencies[0].code;
          }).catch( err => {
        console.log("Error with getting cities from REST Countries." + err);
      });
    },



  },

  mounted: function () {
    let userId = store.loggedInUserId;
    this.getUserInfo(userId);
  },
}

export default AddToCatalogue;

</script>

<style scoped>

  /*
  Add button's styling
   */
  .add-button {
    cursor: pointer;
    border-radius: 5em;
    color: #fff;
    background: #3B5998;
    border: 0;
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
    color: #3B5998;

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
    grid-template-rows: repeat(2, auto);
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
    grid-column: 1;
    grid-row: 2;
  }

  #description {
    grid-column: 2;
    grid-row: 2;
    width: 180px;
    position: relative;
    left: 10px;
  }

  #rrp {
    grid-column: 1;
    grid-row: 2;
    position: relative;
    top: 58px;
    left: 10px;
  }

  #rrp-title {
    position: relative;
    top: 3px;
    left: 8px;
  }

  #currency-box {
    border-radius: 5px;
    border: solid thin #CCCCCC;
    width: 180px;
  }
</style>