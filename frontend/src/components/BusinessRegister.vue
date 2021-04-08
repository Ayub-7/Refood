<template>
  <div class="card" id="body">
    <h3 class="card-header text-center">Create a ReFood Business Account</h3>
    <div class="card-body">
      <div v-if="errors.length > 0">
          <b>Please correct the following error(s):</b>
          <ul>
            <li v-for="error in errors" v-bind:key="error">{{ error }}</li>
          </ul>
        </div>
        <form @submit="checkForm" method="post">
        <div class="form-row">
          <div class="form-group col-md-6">
            <input type="text" class="form-control" placeholder="Enter Name of Business" name="businessName"  v-model="businessName">
          </div>

          <div class="form-group col-md-6">
            <textarea type="text" class="form-control" @input="getAddressFromPhoton()" autocomplete='nope' placeholder="Enter Business Address" name="businessAddress" v-model="businessAddress" required></textarea>
            <div v-if="suggestionsActive">
              <ul class="addressSuggestion">Suggestions:
                <li v-for="(address, index) in potentialAddresses" v-bind:key="index" @click = "setAddress(address)" class="address">
                  {{address}}
              </li>
              </ul>
            </div>
          </div>

          <!-- <div class="form-group col-md-6">
            <input type="text" class="form-control" placeholder="Enter Business Address" name="Address" v-model="businessAddress">
          </div> -->

          <div class="form-group col-md-6">
            <textarea type="text" class="form-control" placeholder="Enter Business Description" name="Description" v-model="description"></textarea>
          </div>

          <!-- <div class="form-group col-md-6">
            <input type="tel" class="form-control" placeholder="Enter Business Phone number" name="phonenumber" v-model="phonenumber">
          </div> -->

          <form class="dropdown">
            <label> Select Business Type :   </label>
            <select class="select" name="type" v-model="businessType">
              <option value = "Accommodation and Food Services" >Accommodation and Food Services</option>
              <option value = "Retail Trade">Retail Trade</option>
              <option value = " Charitable organisation">Charitable organisation</option>
              <option value = "Non-profit organisation">Non-profit organisation</option>
            </select>
          </form>


          <div class="form-group col-md-6">
            <button type="button" class="register-button" @click="checkForm(); createBusinessInfo()">Register</button>
          </div>
        </div>
        </form>
      </div>
  </div>
</template>

<script>
import api from "../Api";
import axios from "axios"
//import Vue from 'vue';
//import Dropdown from 'bp-vuejs-dropdown';

// global
//Vue.use(Dropdown);

const BusinessRegister = {
  name: "BusinessRegister",
  data: function () {
    return {
      errors: [],
      businessName: null,
      businessAddress: null,
      description: null,
      businessType: null,
      potentialAddresses: [],
      suggestionsActive: false
    };
  },
  methods:{

    /**
     * The function checks the inputs of the registration form to ensure they are in the right format.
     * The function also updates the errors list that will be displayed on the page if at least one of the input boxes
     * is in the wrong format.
     */
    checkForm: function() {
      this.errors = [];

      if (!this.businessName) {
        this.errors.push("Please enter your business name!");
      }

      if (!this.businessAddress) {
        this.errors.push("Please enter your business address!");
      }
      if (!this.businessType) {
        this.errors.push("Please enter a business type!");
      }
    },

    /**
     * Creates a POST request when user submits form, using the createUser function from Api.js
     */
    createBusinessInfo: function() {

      //Use createUser function of API to POST user data to backend
      //AT THE MOMENT BACKEND IS JUST A JSON-SERVER, THE SERVER IS RUN USING testUser.json AS A JSON-SERVER ON PORT 9499
      //https://www.npmjs.com/package/json-server
      if(this.errors.length == 0){
        api.createBusiness(this.businessName, this.description, this.businessAddress, this.businessType)
      .then((response) => {
        this.$log.debug("New business created:", response.data);
        this.$store.commit('setBusinessId', response.data.businessId); //Store user info into program state, used for later calls
        this.$store.commit('setBusinessName', response.data.businessName);
         this.$router.push({name: 'BusinessPage', params: {id: this.$store.state.businessId}})
      }).catch((error) => {
        if(error.response){
          console.log(error.response.status);
          console.log(error.response.message);
          this.errors.push("Business name already in use");
        }
        this.$log.debug("Error Status:", error)
      });
    }},

    /**
     * Function that filters response from photon API and pushes the address to potentialAddresses, The photon API response contains
     * undefined values which have to be filtered.
     * @param response Response from photon API containing address information
     */
    filterAddressInfo : function(response) {
      //Filters response from photon API and pushes information to potentialAddresses
      let addressesShown = 5;
      let addressList = response.data.features
          for (let address of addressList) {
            let addressDetails = address.properties;
            //Filter null values and make sure not too many addresses are pushed
            if(addressDetails.housenumber != null && addressDetails.street != null && addressDetails.city != null && this.potentialAddresses.length <= addressesShown) {
              this.potentialAddresses.push(`${addressDetails.housenumber} ${addressDetails.street}, ${addressDetails.city}`);
            }
          }

    },

    /**
     * Performs a GET request to the photon API using values from address input field,
     * also handles the showing and hiding of the suggestion bar
     */
    getAddressFromPhoton : function() {
      let minNumberOfCharacters = 3

      if(this.businessAddress.length >= minNumberOfCharacters) {
        this.suggestionsActive = true;
        //Make call to photon API using value from address field, take only values that are houses
        axios.get(`https://photon.komoot.io/api/?q=${this.businessAddress}&osm_tag=:house`)
        .then(response => {     
          //Pass response into filter function which also pushes info to potential addresses
          this.potentialAddresses = []
          this.filterAddressInfo(response);
        })
      } else {
        this.suggestionsActive = false; //Hide address suggestions
      }
    },

    /**
     * Sets address field when address in suggestions is clicked
     */
    setAddress: function(address) {
      this.businessAddress = address
    }
    
  },

}
export default BusinessRegister;

</script>

<style scoped>
/* Dropdown Button */


.dropdown {
  cursor: pointer;
  border-radius: 5em;
  color: #fff;
  background: linear-gradient(to right, #9C27B0, #E040FB);
  border: 0;
  padding-left: 40px;
  padding-right: 40px;
  padding-bottom: 10px;
  padding-top: 10px;
  font-family: 'Ubuntu', sans-serif;
  margin-left: 50%;
  font-size: 13px;
  box-shadow: 0 0 20px 1px rgba(0, 0, 0, 0.04);
}

.register-button {
  cursor: pointer;
  border-radius: 5em;
  color: #fff;
  background: linear-gradient(to right, #9C27B0, #E040FB);
  border: 0;
  padding-left: 40px;
  padding-right: 40px;
  padding-bottom: 10px;
  padding-top: 10px;
  font-family: 'Ubuntu', sans-serif;
  margin-left: 35%;
  margin-bottom: 100%;
  font-size: 13px;
  box-shadow: 0 0 20px 1px rgba(0, 0, 0, 0.04);
}
/* The container <div> - needed to position the dropdown content */


/* Dropdown Content (Hidden by Default) */
.dropdown-content {
  display: none;
  position: absolute;
  background-color: #f1f1f1;
  margin-left: 80px;
  margin-right: 80px;
  min-width: 160px;
  box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
  z-index: 1;
}

/* Links inside the dropdown */
.dropdown-content a {
  color: black;
  padding: 3px 6px;
  text-decoration: none;
  display: block;
}

/* Change color of dropdown links on hover */
.dropdown-content a:hover {background-color: #ddd;}

/* Show the dropdown menu on hover */
.dropdown:hover .dropdown-content {display: block;}

/* Change the background color of the dropdown button when the dropdown content is shown */
.dropdown:hover .dropbtn {background-color: #3e8e41;}

.form-group col-md-6{
  color: rgb(38, 50, 56);
  font-weight: 700;
  font-size: 14px;
  letter-spacing: 1px;
  background: rgba(136, 126, 126, 0.04);
  padding: 10px 20px;
  border: none;
  border-radius: 20px;
  outline: none;
  box-sizing: border-box;
  border: 2px solid rgba(0, 0, 0, 0.02);

  text-align: center;
  margin-bottom: 27px;
  font-family: 'Ubuntu', sans-serif;
}

.card {
  background-color: #FFFFFF;
  width: 700px;
  height: 400px;
  margin: 1em auto;
  border-radius: 2.5em;
  outline: none;
  border: none;
}

.card-header {
  font-family: 'Ubuntu', sans-serif;
  font-weight: bold;
  font-size: 23px;
  color: #8C55AA;
}

.form-control {
  width: 76%;
  font-weight: 700;
  font-size: 14px;
  letter-spacing: 1px;
  background: rgba(136, 126, 126, 0.04);
  padding: 10px 5px;
  border: none;
  border-radius: 20px;
  outline: none;
  box-sizing: border-box;
  border: 2px solid rgba(0, 0, 0, 0.02);
  margin-bottom: 50px;
  margin-left: 46px;
  text-align: center;
  margin-bottom: 27px;
  font-family: 'Ubuntu', sans-serif;
}

</style>