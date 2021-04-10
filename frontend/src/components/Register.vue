<template>
    <div class="card" id="body">
      <h3 class="card-header text-center">Create a ReFood Account</h3>
      <div class="card-body">
        <div v-if="errors.length > 0">
          <b>Please correct the following error(s):</b>
          <ul>
            <li v-for="error in errors" v-bind:key="error">{{ error }}</li>
          </ul>
        </div>
        <form @submit="checkForm" action="/something" method="post" novalidate="true">
          <div class="form-row">
            <div class="form-group col-md-6">
              <input type="text" class="form-control" placeholder="Enter First name" name="firstname" v-model="firstname" required>
            </div>

            <div class="form-group col-md-6">
              <input type="text" class="form-control" placeholder="Enter Middle name" name="middlename" v-model="middlename">
            </div>

            <div class="form-group col-md-6">
              <input type="text" class="form-control" placeholder="Enter Last name" name="lastname" v-model="lastname" required>
            </div>

            <div class="form-group col-md-6">
              <input type="text" class="form-control" placeholder="Enter Nick Name" name="nickname" v-model="nickname">
            </div>

            <div class="form-group col-md-6">
              <textarea type="text" class="form-control" placeholder="Enter Bio" name="bio" v-model="bio"></textarea>
            </div>

            <div class="form-group col-md-6">
              <input type="email" class="form-control" placeholder="Enter Email" name="email" v-model="email" required>
            </div>

            <div class="form-group col-md-6">
              <input type="password" class="form-control" placeholder="Enter your password" name="password" v-model="password" required>
            </div>

            <div class="form-group col-md-6">
              <input type="password" class="form-control" placeholder="Confirm your password" name="confirm_password" v-model="confirm_password" required>
            </div>

            <div class="form-group col-md-6">
              <input type="date" class="form-control" placeholder="Enter DoB" name="dateofbirth" v-model="dateofbirth" required>
            </div>

            <div class="form-group col-md-6">
              <input type="tel" class="form-control" placeholder="Enter Phone number" name="phonenumber" v-model="phonenumber">
            </div>

            <div class="form-group col-md-6">
              <textarea type="text" class="form-control" @input="getAddressFromPhoton()" autocomplete='nope' placeholder="Enter Home Address" name="homeaddress" v-model="homeaddress" required></textarea>
              <div v-if="suggestionsActive">
                <ul class="addressSuggestion">Suggestions:
                  <li v-for="(address, index) in potentialAddresses" v-bind:key="index" @click = "setAddress(address)" class="address">
                    {{address}}
                </li>
                </ul>
              </div>
            </div>

            <div class="form-group col-md-6">
              <button type="button" class="register-button" @click="checkForm(); createUserInfo()">Register</button>
            </div>

            <div class="form-group col-md-6">
              <label>Already registered? </label>
              <router-link to="/Login">
              <button type="button" class="loginButton" href="/login">Login</button>
              </router-link>
            </div>
          </div>
        </form>
      </div>
    </div>
</template>

<script>

import api from "../Api";
import axios from "axios"

const Register = {
  name: "Register",
  data: function () {
    return {
      errors: [],
      firstname: null,
      middlename: null,
      lastname: null,
      nickname: null,
      bio: null,
      email: null,
      password: null,
      confirm_password: null,
      dateofbirth: null,
      phonenumber: null,
      homeaddress: null,
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

      if (!this.firstname) {
        this.errors.push("First name required!");
      } else if (this.firstname.length > 20) {
        this.errors.push("First name is too long!");
      } else if (this.firstname.length < 2) {
        this.errors.push("First name is too short!");
      }

      if (!this.lastname) {
        this.errors.push("Last name required!");
      } else if (this.lastname.length > 20) {
        this.errors.push("Last name is too long!");
      } else if (this.lastname.length < 2) {
        this.errors.push("Last name is too short!");
      }

      if (!this.email) {
        this.errors.push("Email is required!");
      } else if (!this.validEmail(this.email)) {
        this.errors.push("Please enter a valid email!");
      }

      if (!this.password) {
        this.errors.push("Please enter your password!");
      } else if (!this.validPassword(this.password)) {
        this.errors.push("Your password must have eight characters, at least one uppercase letter, one lowercase letter, one number and one special character");
      }

      if (!this.confirm_password && this.password) {
        this.errors.push("Please confirm your password!");
      } else if (!(this.password === this.confirm_password)) {
        this.errors.push("Your confirmed password does not match!");
      }

      if (!this.dateofbirth) {
        this.errors.push("Please enter your date of birth");
      }

      if (!this.homeaddress) {
        this.errors.push("Please enter your home address!");
      }

      if (this.middlename !== null && this.middlename.length > 20) {
        this.errors.push("Middle name is too long!");
      }

      if (this.nickname !== null && this.nickname.length > 20) {
        this.errors.push("Nickname is too long!");
      }

      if (!this.validPhoneNum(this.phonenumber)) {
        this.errors.push("Invalid phone number!");
      } else if (this.phonenumber.length > 13) {
        this.errors.push("The phone number you inputted is too long!");
      } else if (this.phonenumber.length < 3) {
        this.errors.push("The phone number you inputted is too short!");
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
        api.createUser(this.firstname, this.middlename, this.lastname, this.nickname, this.bio, this.email, this.dateofbirth, this.phonenumber, this.homeaddress, this.password)
      .then((response) => {
        this.$log.debug("New item created:", response.data);
        // window.location.replace("http://localhost:9500/Users?id=" + response.data.id);
        this.$store.commit('setUserId', response.data.userId); //Store user info into program state, used for later calls
        this.$store.commit('setUserRole', response.data.role);
        //LOAD USER PAGE, USING ROUTER
        this.$router.push({name: 'UserPage', params: {id: this.$store.state.userId}})
      }).catch((error) => {
        if(error.response){
          console.log(this.errors);
          console.log(error.response.status);
          console.log(error.response.message);
          this.errors.push("Email already in use");
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

      if(this.homeaddress.length >= minNumberOfCharacters) {
        this.suggestionsActive = true;
        //Make call to photon API using value from address field, take only values that are houses
        axios.get(`https://photon.komoot.io/api/?q=${this.homeaddress}&osm_tag=:house`)
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
      this.homeaddress = address
    }
  },

}
export default Register;
</script>
<style scoped>
/**
Registration form's background styling
 */
.card-body {
  background-color: white;
}

/**
Address suggestion list's styling
 */
.addressSuggestion {
  text-align: center; 
  padding-right: 40px; 
  border-radius: 30px;
  color: white;

  background: linear-gradient(to right, #9C27B0, #E040FB);
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
  background: linear-gradient(to right, #9C27B0, #E040FB);
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

/*
Login button's styling
 */
.loginButton {
  cursor: pointer;
  border-radius: 2em;
  color: #fff;
  background: linear-gradient(to right, #9C27B0, #E040FB);
  border: 0;
  padding-left: 40px;
  padding-right: 40px;
  padding-bottom: 10px;
  padding-top: 10px;
  font-family: 'Ubuntu', sans-serif;
  margin-left: 10%;
  font-size: 13px;
  box-shadow: 0 0 20px 1px rgba(0, 0, 0, 0.04);
}

/*

 */
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
  height: 750px;
  margin: 1em auto;
  border-radius: 2.5em;
  outline: none;
  border: none;
}

#body {
  background-color: #F3EBF6;
  font-family: 'Ubuntu', sans-serif;
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
  padding: 10px 20px;
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