<template>
  <!-- <h1> Basic Login Form</h1> -->
  <div id="body">
  <div class="main">
    <p class="sign" align="center">Sign in</p>
  <form id="login-form" >
      <div v-if="errors.length">
        <b>Please correct the following error(s):</b>
      <ul>
        <li v-for="error in errors" v-bind:key="error">{{ error }}</li>
      </ul>
      </div>


      <input id="email" type="text" v-model="email" placeholder="Enter Email" name="Email" required>
      <input id="password" v-model="password" type="password" placeholder="Enter password" name="password" required>

      <button type="button" class="loginButton" @click="checkForm(); loginSubmit()" to="/users">Sign in</button>
      <div type="button" class="forgotPassword">Forgot Password?</div>
  </form>
  </div>
  </div>
</template>



<script>
import api from "../Api";
import {mutations} from "../store"
//import Vue from "vue"
//import VueSimpleAlert from "vue-simple-alert";
//let passwordHash = require('password-hash');


//const data = require('../testUser.json');
//const users = data.users;
const Login = {
  name: "Login",
  data: function () {
  return {
    errors: [],
    email: null,
    password: null,
  };
  },
  methods: {
    validEmail: function (email) {
      var re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
      return re.test(email);
    },
    /**
     * Checks if the username and password match on what is stored in the backend.
     * @returns {boolean} True if it matches what is stored in the backend; otherwise, false.
     */
    checkForm: function() {
      this.errors = [];

      if (!this.email) {
        this.errors.push("Email is required!");
      } else if (!this.validEmail(this.email)) {
        this.errors.push("Please enter a valid email!");
      }

      if (!this.password) {
        this.errors.push('Password required.');
      }
      // else if(this.password.length < 8){
      //   this.errors.push('Password must be 8 characters long.');
      // }

      if (this.email && this.password) {
        return true;
      }
    },

    /**
     * Sends the login request to the backend by calling the login function from the API.
     */
    loginSubmit: function() {
      if(this.errors.length == 0){
        api.login(this.email, this.password)
        .then((response) => {
          //LOAD USER PAGE, USING ROUTER
          mutations.setUserLoggedIn(response.data.userId, response.data.role);
          mutations.setUserPrimaryBusinesses(response.data.businessesAdministered);
          this.$router.push({path: `/home`});


        }).catch(err => {
          if(err.response) { //Catch bad request
            console.log(err.response.message)
            this.email = this.password = null;
            this.errors.push('Incorrect email or password')
          }
        })
      }
    }
  },

}
export default Login;
</script>




<style scoped>
#body {
  background-color: white;
  font-family: 'Ubuntu', sans-serif;

}

.main {
  background-color: #FFFFFF;
  width: 400px;
  height: 400px;
  margin: 7em auto;
  border-radius: 1.5em;
  box-shadow: 0px 11px 35px 2px rgba(0, 0, 0, 0.14);
}

.sign {
  padding-top: 40px;
  color: #385898;
  font-family: 'Ubuntu', sans-serif;
  font-weight: bold;
  font-size: 23px;
}

#email {
  width: 76%;
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
  margin-bottom: 50px;
  margin-left: 46px;
  text-align: center;
  margin-bottom: 27px;
  font-family: 'Ubuntu', sans-serif;
}
form#login-form {
  padding-top: 40px;
}
#password {
  width: 76%;
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
  margin-bottom: 50px;
  margin-left: 46px;
  text-align: center;
  margin-bottom: 27px;
  font-family: 'Ubuntu', sans-serif;
}
#email:focus, #password:focus {
  border: 2px solid rgba(0, 0, 0, 0.18) !important;
}

.loginButton {
  cursor: pointer;
  border-radius: 5em;
  color: #fff;
  background: #3B5998;
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
.forgotPassword {
  text-shadow: 0px 0px 3px rgba(117, 117, 117, 0.12);
  color: #3B5998;
  padding-top: 15px;
  text-align: center;
}
a {
  text-shadow: 0px 0px 3px rgba(117, 117, 117, 0.12);
  color: #E1BEE7;
  text-decoration: none
}
@media (max-width: 600px) {
  .main {
    border-radius: 0px;
  }
}
</style>