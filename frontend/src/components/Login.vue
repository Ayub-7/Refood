<template>
  <!-- <h1> Basic Login Form</h1> -->
  <div id="body">
  <div class="main">
    <p class="sign" align="center">Sign in</p>
  <form id="login-form">
    <div class="container">

      <div v-if="errors.length">
        <b>Please correct the following error(s):</b>
      <ul>
        <li v-for="error in errors" v-bind:key="error">{{ error }}</li>
      </ul>
      </div>


      <input id="email" type="text" v-model="email" placeholder="Enter Email" name="Email" required>
      <input id="password" v-model="password" type="password" placeholder="Enter password" name="password" required>
      <button type="button" class="loginButton" @click="checkForm(); loginSubmit()" href="/login">Sign in</button>
      <button type="button" class="forgotPassword">Forgot Password?</button>
    </div>
  </form>
  </div>
  </div>
</template>



<script>
import api from "../Api";
import Vue from "vue"
import VueSimpleAlert from "vue-simple-alert";
//let passwordHash = require('password-hash');

Vue.use(VueSimpleAlert);
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
     * @param e
     * @returns {boolean} True if it matches what is stored in the backend; otherwise, false.
     */
    checkForm: function(e) {
      if (this.email && this.password) {
        return true;
      }
      this.errors = [];

      if (!this.email) {
        this.errors.push("Email is required!");
      } else if (!this.validEmail(this.email)) {
        this.errors.push("Please enter a valid email!");
      }

      if (!this.password) {
        this.errors.push('Password required.');
      }
      else if(this.password.length < 8){
        this.errors.push('Password must be 8 characters long.');
      }
      e.preventDefault();
    },

    /**
     * Sends the login request to the backend by calling the login function from the API.
     */
    loginSubmit: function() {
    /**
     *     "email": "wtilsley0@rakuten.co.jp",
     *     "password": "zWkb3AeLn3lc"
     */
      api.login(this.email, this.password)
      .then((response) => {
        console.log(response);
        window.location.replace("http://localhost:9500/Users?id=" + response.data.userId);
      })


      // var user_id = 0;
      // var isRegistered = false;
      // var isVerified = false;
      // var token = null;
      // for (var user of users) {
      //   if (this.email == user.email){
      //     if (passwordHash.verify(this.password, user.hashedPassword)) {
      //       isVerified = true;
      //       user_id = user.id;
      //     }
      //     isRegistered = true;
      //   }
      // }
      // if(isVerified == true){
      //   token = Buffer.from(`${this.username}:${this.password}`, 'utf8').toString('base64')
      // }
      // api.login(this.Email, this.password, token)
      // .then((response) => {
      //   if (isVerified == true) {
      //     this.$log.debug("Login successful!", response.data)
      //     window.location.replace("http://localhost:9500/Users?id=" + user_id);
      //   } else if (isRegistered == true) {
      //     this.$alert("Incorrect username or password!");
      //     this.$log.debug("Login unsuccessful!", response.data);
      //   } else {
      //       this.$alert("You aren't registered You must register.");
      //     }
      //   }).catch((error) => {
      //   this.$log.debug("Login unsuccessful!", error)
      // });
    }
  },

}
export default Login;
</script>

<style scoped>
#body {
  background-color: #F3EBF6;
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
  color: #8C55AA;
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
#username:focus, #password:focus {
  border: 2px solid rgba(0, 0, 0, 0.18) !important;
}

.loginButton {
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
.forgotPassword {
  text-shadow: 0px 0px 3px rgba(117, 117, 117, 0.12);
  color: #E1BEE7;
  padding-top: 15px;
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