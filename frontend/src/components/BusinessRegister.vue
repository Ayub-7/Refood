<template>
  <div class="card">
    <h3 class="card-header">Create a ReFood Business Account</h3>
      <form>
          <vs-input id="business-name"
                    :danger="this.errors.includes('businessName')"
                    type="text"
                    class="form-control"
                    label-placeholder="Business Name (Required)"
                    v-model="businessName"/>

          <vs-select
              width="90%"
              id="business-type"
              :danger="this.errors.includes('businessType')"
              class="form-control"
              label="Select Business Type (Required)"
              v-model="businessType"
              autocomplete >
            <vs-select-item v-for="type in availableBusinessTypes" :key="type" :text="type" :value="type"/>
          </vs-select>

        <div id="address-container">
          <div id="street-number">
            <vs-input v-model="streetNumber" class="form-control" label-placeholder="Street Number"></vs-input>
          </div>
          <div id="street-address">
            <vs-input v-model="streetAddress" class="form-control" label-placeholder="Street Address"></vs-input>
          </div>
          <div id="postcode">
            <vs-input v-model="postcode" class="form-control" label-placeholder="Postcode"></vs-input>
          </div>
          <div id="city">
            <vs-input v-model="city" class="form-control" label-placeholder="City"></vs-input>
          </div>
          <div id="region">
            <vs-input v-model="region" class="form-control" label-placeholder="Region"></vs-input>
          </div>
          <div id="country">
            <vs-input :danger="this.errors.includes('country')" v-model="country" class="form-control" label-placeholder="Country (Required)"></vs-input>
          </div>
        </div>

          <vs-textarea type="text" class="form-control text-areas" label="Business Description" v-model="description"/>

          <button type="button" class="register-button" @click="checkForm(); createBusinessInfo()">Register</button>
      </form>
  </div>
</template>

<script>
import api from "../Api";
import axios from "axios"
//import Vue from 'vue';
import {store} from "../store"

const BusinessRegister = {
  name: "BusinessRegister",
  data: function () {
    return {
      availableBusinessTypes: ["Accommodation and Food Services", "Charitable organisation", "Non-profit organisation", "Retail Trade"],

      errors: [],
      businessName: "",

      streetNumber: "",
      streetAddress: "",
      postcode: "",
      city: "",
      region: "",
      country: "",

      description: "",
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

      if (this.businessName.length === 0) {
        this.errors.push('businessName');
      }

      if (this.country.length === 0) {
        this.errors.push('country');
      }
      if (!this.businessType) {
        this.errors.push('businessType');
      }
      this.$vs.notify({title:'Failed to create business', text:'Required fields are missing.', color:'danger'})

    },

    /**
     * Creates a POST request when user submits form, using the createUser function from Api.js
     */
    createBusinessInfo: function() {

      //Use createUser function of API to POST user data to backend
      //AT THE MOMENT BACKEND IS JUST A JSON-SERVER, THE SERVER IS RUN USING testUser.json AS A JSON-SERVER ON PORT 9499
      //https://www.npmjs.com/package/json-server
      if(this.errors.length === 0){
        api.createBusiness(this.businessName, this.description, this.businessAddress, this.businessType)
      .then((response) => {
        this.$log.debug("New business created:", response.data);
        // this.$store.commit('setBusinessId', response.data.businessId); //Store user info into program state, used for later calls
        // this.$store.commit('setBusinessName', response.data.businessName);
         this.$router.push({path: `/users/${store.loggedInUserId}`});
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

.register-button {
  margin: 1em auto;
  cursor: pointer;
  border-radius: 5em;
  color: #fff;
  background: #3B5998;
  border: 0;
  padding: 10px 40px;
  font-family: 'Ubuntu', sans-serif;
  font-size: 13px;
  box-shadow: 0 0 20px 1px rgba(0, 0, 0, 0.04);
}

.card {
  max-width: 650px;
  background-color: white;
  margin: 1em auto;
  padding: 0.5em 0 0.5em 0;
  border-radius: 20px;
  border: 2px solid rgba(0, 0, 0, 0.02);
  box-shadow: 0 .5rem 1rem rgba(0, 0, 0, .15);

  display: grid;
  grid-template-columns: 1fr;
  grid-template-rows: auto auto;
  grid-row-gap: 1em;

}

.card-header {
  grid-row: 1;

  text-align: center;
  font-weight: bold;
  font-size: 24px;
  color: #3B5998;

  margin: 0;
  padding: 1em 0;

}

form {
  grid-row: 2;

  margin: 0 4em;

  display: grid;
  grid-template-columns: 1fr;
  grid-template-rows: repeat(5, auto);
  grid-row-gap: 4px;

}

.form-control {
  font-family: 'Ubuntu', sans-serif;
  margin: 0.25em auto;
  width: 90%;

}

#business-name {
  padding: 0;
}

#address-container {
  display: grid;
  grid-template-columns: repeat(2, 2fr) repeat(3, 3fr);
  grid-template-rows: repeat(3, auto);

}

vs-input {
  margin: 0;
}

#street-number {
  grid-row: 1;
  grid-column: 1 / 3;
}

#street-address {
  grid-row: 1;
  grid-column: 3 / 6;
}

#city {
  grid-row: 2;
  grid-column: 3 / 6;
}

#region {
  grid-row: 3;
  grid-column: 1 / 3;
}

#country {
  grid-row: 3;
  grid-column: 3 / 6;
}

#postcode {
  grid-row: 2;
  grid-column: 1 / 3;
}

.text-areas {
  margin-top: 1em;
  width: 90%;
  font-family: 'Ubuntu', sans-serif;
  font-size: 14px;
}

@media screen and (max-width: 600px) {
  .card {
    width: 90%;
  }
}

</style>