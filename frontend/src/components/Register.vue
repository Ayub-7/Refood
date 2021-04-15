<template>
  <div class="card">
    <h3 class="card-header">Create a ReFood Account</h3>
    <form @submit="checkForm" novalidate="true">
      <div id="firstname">
        <vs-input :danger="(errors.includes(firstname))"
                  danger-text="Firstname must be between 2 and 20 letters."
                  :success="(firstname.length >= 2 && firstname.length < 20)"
                  class="form-control"
                  type="text"
                  label-placeholder="First name *"
                  v-model="firstname"/>
      </div>
      <div id="middlename">
      <vs-input type="text"
                class="form-control"
                label-placeholder="Middle name"
                :danger="middlename.length>20"
                danger-text="Middlename must be less that 20 characters"
                :success="middlename.length > 0 && middlename.length < 20"
                v-model="middlename"/>
      </div>
      <div id="lastname">
      <vs-input :danger="(errors.includes(lastname))"
                danger-text="Lastname must be between 2 and 20 letters."
                :success="(lastname.length >= 2 && lastname.length < 20)"
                type="text"
                class="form-control"
                label-placeholder="Last name *"
                v-model="lastname"/>
      </div>
      <div id="nickname">
      <vs-input type="text"
                class="form-control"
                label-placeholder="Nick Name"
                :danger="nickname.length>20"
                danger-text="Nickname must be less that 20 characters"
                :success="nickname.length > 0 && nickname.length < 20"
                name="nickname"
                v-model="nickname"/>
      </div>
      <div id="email">
        <vs-input type="email"
                  class="form-control"
                  label-placeholder="Email *"
                  :danger="errors.includes(email)"
                  danger-text="Invalid email."
                  :success="validEmail(email)"
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
      <vs-input type="date"
                id="date-of-birth"
                class="form-control"
                name="dateofbirth"
                v-model="dateofbirth"
                :danger="errors.includes(dateofbirth)"
                danger-text="Enter date of birth"
                :success="(dateofbirth.length!==0)"
                label="Date of birth *"/>
      <vs-textarea type="text" id="bio" class="form-control" placeholder="Bio" name="bio" v-model="bio"></vs-textarea>
      <vs-input type="password"
                id="password"
                class="form-control"
                label-placeholder="Password *"
                :danger="errors.includes(password)"
                danger-text="Your password must have eight characters, at least one uppercase letter, one lowercase letter, one number and one special character."
                :success="validPassword(password)"
                name="password"
                v-model="password"/>
      <vs-input type="password"
                id="confirm-password"
                class="form-control"
                label-placeholder="Confirm Password *"
                :danger="errors.includes(confirm_password)"
                danger-text="Confirmed password invalid."
                :success="(confirm_password===password && confirm_password.length !== 0)"
                name="confirm_password"
                v-model="confirm_password"/>
      <div id="address-field">
        <label for="address-field" class="label-control">Address *</label>
        <div id="street-number">
          <vs-input v-model="streetNumber" class="address-form-control" label-placeholder="Street Number" size="small"></vs-input>
        </div>
        <div id="street-name">
          <vs-input v-model="streetName" class="address-form-control" label-placeholder="Street Name" size="small"></vs-input>
        </div>
        <div id="postcode">
          <vs-input v-model="postcode" class="address-form-control" label-placeholder="Postcode" size="small"></vs-input>
        </div>
        <div id="city">
          <!-- If wanting to test/check suggested item tiles, remove blur. -->
          <vs-input @blur="suggestCities = false;" v-model="city" @input="getCitiesFromPhoton()" class="address-form-control" label-placeholder="City" size="small"></vs-input>
          <ul v-if="this.suggestCities" class="suggested-box">
            <li v-for="suggested in this.suggestedCities" @mousedown="setCity(suggested)" :key="suggested" :value="suggested" class="suggested-item">{{suggested}}</li>
          </ul>
        </div>
        <div id="region">
          <vs-input v-model="region" class="address-form-control" label-placeholder="Region" size="small"></vs-input>
        </div>
        <div id="country">
          <vs-input @blur="suggestCountries = false;" :danger="this.errors.includes('country')" danger-text="Country required." :success="country.length > 0" @input="getCountriesFromPhoton()" v-model="country" class="address-form-control" label-placeholder="Country *" size="small"></vs-input>
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
  import axios from "axios"
  var passwordHash = require('password-hash');
  // const data = require('../testUser.json');
  // const users = data.users;
  // Need to somehow access the users database to check params.

  const Register = {
    name: "Register",
    data: function () {
      return {
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

        if (!this.validPassword(this.password)) {
          this.errors.push(this.password);
        }

        if (this.confirm_password.length == 0 || this.password !== this.confirm_password) {
          this.errors.push(this.confirm_password);
        }

        if (this.dateofbirth.length === 0) {
          this.errors.push(this.dateofbirth);
        }

        if (this.country.length === 0) {
          this.errors.push('country');
        }

        if (this.phonenumber !== null && this.phonenumber !== "" && !this.validPhoneNum(this.phonenumber)) {
          this.errors.push(this.phonenumber);
        }

        if (this.errors.length >= 1) {
          // let message = "";
          for (let error in this.errors) {
            console.log(this.errors[error]);
          }
          this.$vs.notify({title:'Failed to register', text:'Required fields are missing.', color:'danger'});
        }
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
      // emailUsed: function (email) {
      //   for (var user of users) {
      //     if (email == user.email){
      //       return true;
      //     }
      //   }
      //   return false;
      // },

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

        //Use createUser function of API to POST user data to backend
        //AT THE MOMENT BACKEND IS JUST A JSON-SERVER, THE SERVER IS RUN USING testUser.json AS A JSON-SERVER ON PORT 9499
        //https://www.npmjs.com/package/json-server
        if(this.errors.length == 0){
          var hashedPassword = passwordHash.generate(this.password);
          console.log(hashedPassword);
          api.createUser(this.firstname, this.middlename, this.lastname, this.nickname, this.bio, this.email, this.dateofbirth, this.phonenumber, this.homeaddress, hashedPassword)
                  .then((response) => {
                    this.$log.debug("New item created:", response.data);
                    // window.location.replace("http://localhost:9500/Users?id=" + response.data.id);
                    this.$store.commit('setUserId', response.data.userId); //Store user info into program state, used for later calls
                    this.$store.commit('setUserRole', response.data.role);
                    //LOAD USER PAGE, USING ROUTER
                    this.$router.push({name: 'UserPage', params: {id: this.$store.state.userId}})
                    //this.$router.push({path: `/users/${response.data.id}`});
                  }).catch((error) => {
            if(error.response){
              console.log(error.response.status);
              console.log(error.response.message);
              this.errors.push("Email already in use");
            }
            this.$log.debug("Error Status:", error)
          });
        }},


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
      }
    }
  export default Register;
</script>
<style scoped>

  /**
  Address suggestion list's styling
   */
  .addressSuggestion {
    text-align: center;
    padding-right: 40px;
    border-radius: 30px;
    color: white;

    background: white;
  }

  .address {
    list-style-type: none;
    list-style-position: outside;
    cursor: pointer;
    background: #e44aff;
    border-radius: 10px;
    padding-bottom: 3px;
    background-clip: content-box;

  }

  /*
  Register button's styling
   */
  .register-button {
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

  /*
  Login button's styling
   */
  .login-button {
    cursor: pointer;
    border-radius: 2em;
    color: #fff;
    background: #3B5998;
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
    text-align: center;

    display: grid;
    grid-template-columns: repeat(2, auto);
    grid-template-rows: repeat(6, auto);
  }

  /**
  Styling for form elements.
   */
  .form-control {
    font-weight: 700;
    font-size: 14px;
    letter-spacing: 1px;
    border-radius: 20px;
    outline: none;
    box-sizing: border-box;
    text-align: center;
    font-family: 'Ubuntu', sans-serif;

    padding: 3px 10px;
    margin: 0.5em;

  }

  /**
  Styling for address inputs.
   */
  .address-form-control {
    font-weight: 700;
    font-size: 14px;
    letter-spacing: 1px;
    border-radius: 20px;
    outline: none;
    box-sizing: border-box;
    text-align: center;
    font-family: 'Ubuntu', sans-serif;

    margin: 0.5em;
    padding: 0px 0px;

  }

  /**
  Label styling.
   */
  .label-control {
    font-family: 'Ubuntu', sans-serif;
    font-weight: 700;
    font-size: 14px;
    padding-bottom: 5px;
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

  #bio {
    grid-column: 1 / 3;
    grid-row: 6;
  }

  #address-field {
    grid-column: 1 / 3;
    display: grid;
    padding: 0px;
    grid-template-columns: repeat(2, auto);
    grid-template-rows: repeat(3, auto);
  }


  #login-container {
    grid-column: 2;
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
    grid-column: 4;
  }

  #city {
    grid-row: 2;
    grid-column: 4;
  }

  #region {
    grid-row: 3;
    grid-column: 1;
  }

  #country {
    grid-row: 3;
    grid-column: 4;
  }

  #postcode {
    grid-row: 2;
    grid-column: 1 / 3;
  }



  @media screen and (max-width: 700px) {
    .card {
      max-width: 80%;

    }

    form {
      display: grid;
      grid-template-columns: 1fr;
      grid-template-rows: repeat(11, auto);
      margin: 0 5em;
    }

    .form-control {
      grid-column: 1;
      grid-row: auto;
    }

    #email {
      grid-column: 1;
    }

    #bio {
      grid-column: 1;
    }

    .register-button {
      grid-column: 1;
    }

    #login-container {
      grid-column: 1;
    }

  }
</style>
