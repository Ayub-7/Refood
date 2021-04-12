<template>
    <div class="card">
      <h3 class="card-header">Create a ReFood Account</h3>
        <form @submit="checkForm" novalidate="true">
          <vs-input :danger="(firstname.length < 2)" danger-text="The password does not meet the standards" :success="(firstname.length >= 2)" class="form-control" id="firstname" type="text" placeholder="First name" v-model="firstname" required/>
          <vs-input type="text" class="form-control" placeholder="Middle name" v-model="middlename"/>
          <vs-input type="text" id="lastname" class="form-control" placeholder="Last name" name="lastname" v-model="lastname" required/>
          <vs-input type="text" id="nickname" class="form-control" placeholder="Nick Name" name="nickname" v-model="nickname"/>
          <vs-input type="email" id="email" class="form-control" placeholder="Email" name="email" v-model="email" required/>

          <vs-input type="password" id="password" class="form-control" placeholder="Password" name="password" v-model="password" required/>
          <vs-input type="password" id="confirm-password" class="form-control" placeholder="Confirm Password" name="confirm_password" v-model="confirm_password" required/>
          <vs-input type="date" id="date-of-birth" class="form-control" name="dateofbirth" v-model="dateofbirth" required/>
          <vs-input type="tel" id="phonenumber" class="form-control" placeholder="Phone number" name="phonenumber" v-model="phonenumber"/>
          <vs-textarea type="text" id="bio" class="form-control" placeholder="Bio" name="bio" v-model="bio"></vs-textarea>
          <vs-textarea type="text" class="form-control" @input="getAddressFromPhoton()" autocomplete='nope' placeholder="Home Address" name="homeaddress" v-model="homeaddress" required></vs-textarea>

          <div v-if="suggestionsActive">
            <ul class="addressSuggestion">Suggestions:
              <li v-for="(address, index) in potentialAddresses" v-bind:key="index" @click = "setAddress(address)" class="address">
                {{address}}
            </li>
            </ul>
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
      suggestionsActive: false,
    };
  },
  methods:{
    /*test: function() {
      if (this.firstname.length < 5) {
        this.danger = false;
      }
      return true;
    },
    */

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
      } else if (this.password !== this.confirm_password) {
        this.errors.push("Your confirmed password does not match!");
      }

      if (!this.dateofbirth) {
        this.errors.push("Please enter your date of birth");
      }

      if (!this.homeaddress) {
        this.errors.push("Please enter your home address!");
      }

      if (this.middlename.length > 20) {
        this.errors.push("Middle name is too long!");
      }

      if (this.nickname.length > 20) {
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
      console.log(this.errors);
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

form {
  grid-row: 2;
  grid-column: 1;

  margin: auto;
  text-align: center;

  display: grid;
  grid-template-columns: 50% 50%;
  grid-template-rows: repeat(6, auto);
}

.form-control {
  font-weight: 700;
  font-size: 14px;
  letter-spacing: 1px;
  border-radius: 20px;
  outline: none;
  box-sizing: border-box;
  text-align: center;
  font-family: 'Ubuntu', sans-serif;

  padding: 10px 20px;
  margin: 0.5em;

}

#firstname {
  grid-column: 1;
  grid-row: 1;
}

#lastname {
  grid-column: 1;
  grid-row: 2;
}

#email {
  align-self: center;
  grid-column: 1 / 3;
  grid-row: 3;
}

#bio {
  grid-column: 1 / 3;
  grid-row: 6;
}

#login-container {
  grid-column: 2;
}

#login-container label {
  margin: auto;
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

vs-input:invalid,
textarea:invalid {
  border-color: black ;
}
input:optional,
textarea:optional {
  border-color: gray;
}
input:focus:invalid,
textarea:focus:invalid {
  background: lightpink;
  background-size: 25px;
}
input:required:focus:valid,
textarea:required:focus:valid {
  border-color: green;
  background: mediumseagreen;
}
input:required:valid,
textarea:required:valid {
  border-color: green;
}
</style>