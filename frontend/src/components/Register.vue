<template>
  <div class="card">
    <h3 class="card-header">Create a ReFood Account</h3>
    <form>
      <div id="info-field">
        <div id="firstname">
          <vs-input :danger="(errors.includes(firstname))"
                    danger-text="Firstname must be between 2 and 20 letters."
                    :success="(firstname.length >= 2 && firstname.length < 20)"
                    class="form-control"
                    type="text"
                    label-placeholder="First name (Required)"
                    v-model="firstname"/>
        </div>
        <div id="middlename">
          <vs-input type="text"
                    class="form-control"
                    label-placeholder="Middle name"
                    :danger="middlename.length>20"
                    danger-text="Middlename must be less than 20 characters"
                    :success="middlename.length > 0 && middlename.length < 20"
                    v-model="middlename"/>
        </div>
        <div id="lastname">
          <vs-input :danger="(errors.includes(lastname))"
                    danger-text="Lastname must be between 2 and 20 letters."
                    :success="(lastname.length >= 2 && lastname.length < 20)"
                    type="text"
                    class="form-control"
                    label-placeholder="Last name (Required)"
                    v-model="lastname"/>
        </div>
        <div id="nickname">
          <vs-input type="text"
                    class="form-control"
                    label-placeholder="Nick Name"
                    :danger="nickname.length>20"
                    danger-text="Nickname must be less than 20 characters"
                    :success="nickname.length > 0 && nickname.length < 20"
                    name="nickname"
                    v-model="nickname"/>
        </div>
        <div id="email">
          <vs-input type="email"
                    class="form-control"
                    label-placeholder="Email (Required)"
                    :danger="errors.includes(email) && emailInUse"
                    danger-text="Invalid email. (This email may already be in use)"
                    :success="validEmail(email) && !emailInUse"
                    v-model="email"/>
        </div>
        <div id="phonenumber">
          <vs-input type="tel"
                    class="form-control"
                    label-placeholder="Phone number"
                    :danger="phonenumber.length>0 && errors.includes(phonenumber)"
                    danger-text="Invalid phone number."
                    :success="validPhoneNum(phonenumber)"
                    name="phonenumber"
                    v-model="phonenumber"/>
        </div>
        <div id="password">
          <vs-input type="password"
                    class="form-control"
                    label-placeholder="Password *"
                    :danger="errors.includes(password)"
                    danger-text="Your password must have eight characters, at least one uppercase letter, one lowercase letter, one number and one special character."
                    :success="validPassword(password)"
                    name="password (Required)"
                    v-model="password"/>
        </div>
        <div id="confirm-password">
          <vs-input type="password"
                    class="form-control"
                    label-placeholder="Confirm Password *"
                    :danger="errors.includes(confirm_password)"
                    danger-text="Confirmed password invalid."
                    :success="(confirm_password===password && confirm_password.length !== 0)"
                    name="confirm_password (Required)"
                    v-model="confirm_password"/>
        </div>
        <div id="date-of-birth">
          <vs-input type="date"
                    class="form-control"
                    name="dateofbirth"
                    v-model="dateofbirth"
                    :danger="errors.includes(dateofbirth)"
                    danger-text="Enter date of birth"
                    :success="(dateofbirth.length!==0)"
                    label="Date of birth (Required)"/>
        </div>

        <div id="bio">
          <vs-textarea width="200px" type="text" class="form-control text-areas" label="Bio" name="bio" v-model="bio"></vs-textarea>
        </div>
      </div>
      <label for="address-field" class="label-control">Address</label>
      <div id="address-field">
        <div id="street-number">
          <vs-input v-model="streetNumber" class="form-control" label-placeholder="Street Number"></vs-input>
        </div>
        <div id="street-name">
          <vs-input v-model="streetName" class="form-control" label-placeholder="Street Name"></vs-input>
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
          <vs-input autocomplete="off" @blur="suggestCountries = false;" :danger="this.errors.includes('country')" danger-text="Country required." :success="country.length > 0" @input="getCountriesFromPhoton()" v-model="country" class="form-control" label-placeholder="Country (Required)"></vs-input>
          <ul v-if="this.suggestCountries" class="suggested-box">
            <li v-for="suggested in this.suggestedCountries" @mousedown="setCountry(suggested)" :key="suggested" :value="suggested" class="suggested-item">{{suggested}}</li>
          </ul>
        </div>
      </div>

      <button type="button" class="register-button" @click="checkForm(); createUserInfo()">Register</button>

      <div id="login-container">
        <label>Already registered? </label>
        <router-link to="/login">
          <button type="button" class="login-button">Login</button>
        </router-link>
      </div>
    </form>
  </div>
</template>
<script>
import api from "../Api";
import axios from "axios";
import {mutations} from '../store.js';


const Register = {
  name: "Register",
  data: function () {
    return {
      emailInUse: false,
      errors: [],
      firstname: "",
      middlename: "",
      lastname: "",
      nickname: "",
      bio: null,
      email: "",
      password: "",
      confirm_password: "",
      dateofbirth: "",
      phonenumber: "",

      streetNumber: "",
      streetName: "",
      postcode: "",
      city: "",
      region: "",
      country: "",

      suggestCities: false,
      suggestedCities: [],

      suggestCountries: false,
      suggestedCountries: [],
      minNumberOfCharacters: 3


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
      if (this.firstname.length < 2 || this.firstname.length >= 20) {
        this.errors.push(this.firstname);
      }

      if (this.lastname.length < 2 || this.lastname.length >= 20) {
        this.errors.push(this.lastname);
      }

      if (!this.validEmail(this.email)) {
        this.errors.push(this.email);
      }
      if (this.bio != null) {
        if (this.bio.length > 40) {
          this.errors.push(this.bio);
        }
      }

      if (!this.validPassword(this.password)) {
        this.errors.push(this.password);
      }

      if (this.confirm_password.length === 0 || this.password !== this.confirm_password) {
        this.errors.push(this.confirm_password);
      }
      if (this.dateofbirth.length === 0 || !this.validAge(this.dateofbirth)) {
        this.errors.push(this.dateofbirth);
      }

      if (this.country.length === 0) {
        this.errors.push('country');
      }

      if (this.phonenumber !== null && this.phonenumber !== "" && !this.validPhoneNum(this.phonenumber)) {
        this.errors.push(this.phonenumber);
      }

      if (this.errors.length >= 1) {
        this.$vs.notify({title: 'Failed to register', text: 'Required fields are missing.', color: 'danger'});
        if (this.errors.includes(this.bio)) {
          this.$vs.notify({
            title: 'Failed to register',
            text: 'Bio must be less that 40 characters.',
            color: 'danger'
          });
        }
      }
    },


    /**
     * from https://stackoverflow.com/questions/14231381/to-check-if-age-is-not-less-than-13-years-in-javascript
     * @param birthDateString string of date inputted by user
     * @returns {Boolean} True if user is 13 and above false if below
     */

    validAge: function(birthDateString) {
      let years = new Date(new Date() - new Date(birthDateString)).getFullYear() - 1970;
      return (years >= 13);
    },



    /**
     * The function ensures that the inputted email is in the right email format using a Regular Expression to check it.
     * @param email The email to be checked.
     * @returns {boolean} True if is in the right format; otherwise, false.
     */
    validEmail: function (email) {
      var re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
      return re.test(email);
    },

    /**
     * Checks if the inputted phone number is in the right format using a Regular Expression to check it.
     * @param phonenumber The phone number to be checked
     * @returns {boolean} True if is in the right format; otherwise, false.
     */
    validPhoneNum: function (phonenumber) {
      var re = /(?<![A-Za-z0-9.])[0-9.]+(?![A-Za-z0-9.])/;
      return re.test(phonenumber);
    },

    /**
     * Checks if the new user's password has at least one lowercase letter, one uppercase letter, one number,
     * one special character, and is at least 8 characters long.
     * @param password The password to be checked
     * @returns {boolean} True if is in the right format; otherwise, false.
     */
    validPassword: function (password) {
      var re = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
      return re.test(password);
    },

    /**
     * Creates a POST request when user submits form, using the createUser function from Api.js
     */
    createUserInfo: function() {
      if(this.errors.length === 0){
        this.emailInUse = false;
        const homeAddress = {
          streetNumber: this.streetNumber,
          streetName: this.streetName,
          city:this.city,
          region: this.region,
          country: this.country,
          postcode: this.postcode
        }

        api.createUser(this.firstname, this.middlename, this.lastname, this.nickname, this.bio, this.email, this.dateofbirth, this.phonenumber, homeAddress, this.password)
            .then((response) => {
              this.$log.debug("New item created:", response.data);
              mutations.setUserLoggedIn(response.data.userId, response.data.role); //Store user info into program state, used for later calls
              api.login(this.email, this.password)
                  .then((response) => {
                    mutations.setUserLoggedIn(response.data.userId, response.data.role);
                    //LOAD USER PAGE, USING ROUTER
                    this.$router.push({path: '/home'})
                  }).catch((error) => {
                this.$log.debug("Error logging in from registration: " + error);
              });
            }).catch((error) => {
          if(error.response.status === 409){
            this.emailInUse = true;
            this.errors.push(this.email);
          }
        });
      }},


    getCitiesFromPhoton: function() {
      if (this.city.length >= this.minNumberOfCharacters) {

        this.suggestCities = true;
        axios.get(`https://photon.komoot.io/api/?q=${this.city}&osm_tag=place:city&lang=en`)
            .then( res => {
              this.suggestedCities = res.data.features.map(location => location.properties.name);
              this.suggestedCities = this.suggestedCities.filter(city => city != null);
              var found = {};
              this.suggestedCities = this.suggestedCities.filter(function(country) {
                // eslint-disable-next-line no-prototype-builtins
                return found.hasOwnProperty(country) ? false : (found[country] = true);
              });
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
  }
}
export default Register;
</script>
<style scoped>

/*
Register button's styling
 */
.register-button {
  cursor: pointer;
  border-radius: 5em;
  color: #fff;
  background: #1F74FF;
  border: 0;
  padding: 10px 40px;
  margin: 2em;
  font-size: 13px;
  box-shadow: 0 0 20px 1px rgba(0, 0, 0, 0.04);
}

/*
Login button's styling
 */
.login-button {
  cursor: pointer;
  border-radius: 2em;
  color: #fff;
  background: #1F74FF;
  border: 0;
  padding: 10px 40px;
  font-size: 13px;
  box-shadow: 0 0 20px 1px rgba(0, 0, 0, 0.04);

  margin: 0.5em;
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
  grid-column: 1/3;

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

/**
Label styling.
 */
.label-control {
  grid-column: 1/3;
  font-family: 'Ubuntu', sans-serif;
  font-weight: 700;
  font-size: 14px;
  margin: auto auto 0 auto;
}

.suggested-box {
  position: absolute;
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


vs-input {
  margin: 0;
}

#info-field {
  grid-column: 1/3;
  display: grid;
  margin: auto;
  padding: 0px;
  grid-template-columns: repeat(2, auto);
  grid-template-rows: repeat(5, auto);
}

#firstname {
  grid-column: 1;
  grid-row: 1;
}

#middlename {
  grid-column: 2;
  grid-row: 1;
}

#lastname {
  grid-column: 1;
  grid-row: 2;
}

#nickname {
  grid-column: 2;
  grid-row: 2;
}

#email {
  grid-column: 1;
  grid-row: 3;
}

#phonenumber {
  grid-column: 2;
  grid-row: 3;
}

#password {
  grid-column: 1;
  grid-row: 4;
}

#confirm-password {
  grid-column: 2;
  grid-row: 4;
}

#date-of-birth {
  grid-column: 1;
  grid-row: 5;
}

#bio {
  grid-column: 2;
  grid-row: 5;
}

#address-field {
  grid-column: 1/3;
  display: grid;
  margin: auto;
  padding: 0;
  grid-template-columns: repeat(2, auto);
  grid-template-rows: repeat(3, auto);
}

#login-container {
  grid-column: 2;
  text-align: center;
}

#login-container label {
  margin: auto;
}

#street-number {
  grid-row: 1;
  grid-column: 1;
}

#street-name {
  grid-row: 1;
  grid-column: 2;
}

#city {
  grid-row: 2;
  grid-column: 2;
}

#region {
  grid-row: 3;
  grid-column: 1;
}

#country {
  grid-row: 3;
  grid-column: 2;
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

@media screen and (max-width: 700px) {
  .card {
    max-width: 80%;

  }

  form {
    display: grid;
    grid-template-columns: 1fr;
    grid-template-rows: repeat(11, auto);
    grid-row-gap: 0.5em;
    margin: 0;
  }

  #info-field {
    margin: auto;
    grid-template-columns: 1fr;
    grid-template-rows: repeat(11, auto);
  }

  #firstname {
    grid-column: 1;
    grid-row: 1;
  }

  #middlename {
    grid-column: 1;
    grid-row: 2;
  }

  #lastname {
    grid-column: 1;
    grid-row: 3;
  }

  #nickname {
    grid-column: 1;
    grid-row: 4;
  }

  #email {
    grid-column: 1;
    grid-row: 5;
  }

  #phonenumber {
    grid-column: 1;
    grid-row: 6;
  }

  #password {
    grid-column: 1;
    grid-row: 7;
  }

  #confirm-password {
    grid-column: 1;
    grid-row: 8;
  }

  #date-of-birth {
    grid-column: 1;
    grid-row: 9;
  }

  #bio {
    grid-column: 1;
    grid-row: 10;
  }

  #address-field {
    margin: auto;
    grid-template-columns: 1fr;
    grid-template-rows: repeat(11, auto);
  }

  #street-number {
    grid-column: 1;
    grid-row: 1;
  }

  #street-name {
    grid-column: 1;
    grid-row: 2;
  }

  #city {
    grid-column: 1;
    grid-row: 3;
  }

  #region {
    grid-column: 1;
    grid-row: 4;
  }

  #country {
    grid-column: 1;
    grid-row: 5;
  }

  #postcode {
    grid-column: 1;
    grid-row: 6;
  }

  .register-button {
    grid-column: 1;
    margin: auto;
  }

  #login-container {
    grid-column: 1;
    text-align: center;
  }



}
</style>
