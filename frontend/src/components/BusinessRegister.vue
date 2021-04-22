<template>
  <div class="card" v-if="this.user != null">
    <h3 class="card-header">Create a ReFood Business Account</h3>
      <form autocomplete="off">
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
            <!-- If wanting to test/check suggested item tiles, remove blur. -->
            <vs-input @blur="suggestCities = false;" v-model="city" @input="getCitiesFromPhoton()" class="form-control" label-placeholder="City"></vs-input>
            <ul v-if="this.suggestCities" class="suggested-box">
              <li v-for="suggested in this.suggestedCities" @mousedown="setCity(suggested)" :key="suggested" :value="suggested" class="suggested-item">{{suggested}}</li>
            </ul>
          </div>
          <div id="region">
            <vs-input v-model="region" class="form-control" label-placeholder="Region"></vs-input>
          </div>
          <div id="country">
            <vs-input @blur="suggestCountries = false;" :danger="this.errors.includes('country')" @input="getCountriesFromPhoton()" v-model="country" class="form-control" label-placeholder="Country (Required)"></vs-input>
            <ul v-if="this.suggestCountries" class="suggested-box">
              <li v-for="suggested in this.suggestedCountries" @mousedown="setCountry(suggested)" :key="suggested" :value="suggested" class="suggested-item">{{suggested}}</li>
            </ul>
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
import {store} from "../store";

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

      suggestCities: false,
      suggestedCities: [],

      suggestCountries: false,
      suggestedCountries: [],
      minNumberOfCharacters: 3,
      user: null
    };
  },
  methods:{
    /**
     * The function checks the inputs of the registration form to ensure they are in the right format.
     * Pushes name of error into an array, and display notification have errors exist.
     */
    checkForm: function() {
      this.errors = [];

      if (this.businessName.length === 0) {
        this.errors.push('businessName');
      }

      if (this.country.length === 0) {
        this.errors.push('country');
      }

      if (!this.checkAge()){
        this.errors.push('dob');
      }

      if (!this.businessType) {
        this.errors.push('businessType');
      }

      if (this.errors.length >= 1) {
        if(this.errors.includes("dob") && this.errors.length == 1){
          this.$vs.notify({title:'Failed to create business', text:'You are too young to create a ReFood account.', color:'danger'});
        } else {
          this.$vs.notify({title:'Failed to create business', text:'Required fields are missing.', color:'danger'});
        }
      }
    },

    /**
     * Creates a POST request when user submits form, using the createUser function from Api.js
     */
    createBusinessInfo: function() {
      // Use createUser function of API to POST user data to backend
      if(this.errors.length === 0) {
        let businessAddress = {
          streetNumber: this.streetNumber,
          streetName: this.streetAddress,
          city: this.city,
          region: this.region,
          country: this.country,
          postcode: this.postcode,
        };

      api.createBusiness(this.businessName, this.description, businessAddress, this.businessType)
        .then((response) => {
          this.$log.debug("New business created:", response.data);
          this.$router.push({path: `/users/${store.loggedInUserId}`});
        }).catch((error) => {
          if(error.response) {
            console.log(error.response.status);
            console.log(error.response.message);
            this.errors.push("Access token is missing/invalid");
          }
          this.$log.debug("Error Status:", error)
        });
    }},

    /**
     * Returns the years since the user was born. No rounding is done in the function.
     * @param enteredDate The user's birthdate
     * @returns {boolean} Whether the user is old enough, 16, to register a business.
     */
    checkAge: function() {
      let enteredDate = store.userDateOfBirth;
      let years = new Date(new Date() - new Date(enteredDate)).getFullYear() - 1970;
      return (years >= 16);
    },


    /**
     * Retrieve a list of suggested cities using the photon open api.
     */
    getCitiesFromPhoton: function() {
      if (this.city.length >= this.minNumberOfCharacters) {

        this.suggestCities = true;
        axios.get(`https://photon.komoot.io/api/?q=${this.city}&osm_tag=place:city&lang=en`)
            .then( res => {
              this.suggestedCities = res.data.features.map(location => location.properties.name);
              this.suggestedCities = this.suggestedCities.filter(city => city != null);
            })
            .catch( error => {
              console.log("Error with getting cities from photon." + error);
            });
      }
      else {
        this.suggestCities = false;
      }
    },

    /**
     * Set the city as the new city.
     * @param selectedCity string to set as the new city.
     */
    setCity: function(selectedCity) {
      this.city = selectedCity;
      this.suggestCities = false;
    },

    /**
     * Retrieve a list of suggested countries using the photon open api.
     */
    getCountriesFromPhoton: function() {
      if (this.country.length >= this.minNumberOfCharacters) {

        this.suggestCountries = true;
        axios.get(`https://photon.komoot.io/api/?q=${this.country}&osm_tag=place:country&lang=en`)
          .then( res => {
            this.suggestedCountries = res.data.features.map(location => location.properties.country);
          })
          .catch( error => {
            console.log("Error with getting countries from photon." + error);
          });
      }
      else {
        this.suggestCountries = false;
      }
    },

    /**
     * Set the country as the new country.
     * @param selectedCountry the country string to set as.
     */
    setCountry: function(selectedCountry) {
        this.country = selectedCountry;
        this.suggestCountries = false;
    },

    getUserInfo: function (userId) {
      api.getUserFromID(userId)
          .then((response) => {
            if(store.loggedInUserId == null) {
              this.user = response.data;
            } else {
              this.$router.push({path: "/login"});
            }
          }).catch((err) => {
            throw new Error(`Error trying to get user info from id: ${err}`);
      });
    },

    mounted: function () {
      let userId = this.$route.params.id
      this.user = this.getUserInfo(userId);
    }
  },

}
export default BusinessRegister;

</script>

<style scoped>

.suggested-box {
  position: absolute;
  display: inline-block;
  list-style: none;
  width: 225px;
}

.suggested-item {
  cursor: pointer;
  position: relative;
  margin: 0 0 0 1em;

  border: 2px solid rgba(0, 0, 0, 0.02);
  padding: 0.5em 1em;
  background: white;
  z-index: 99;
}

.suggested-item:hover {
  background: lightgray;
}

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