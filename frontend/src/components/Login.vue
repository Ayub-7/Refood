<template>
  <!-- <h1> Basic Login Form</h1> -->
  <div id="main">

    <p id="sign">Sign In</p>
    <form>
        <vs-input class="form-control"
                  id="email" type="text"
                  v-model="email"
                  label-placeholder="Enter Email"
                  :danger="this.errors.email != null"
                  :danger-text="this.errors.email"
                  required></vs-input>
        <vs-input class="form-control"
                  id="password" type="password"
                  v-model="password"
                  label-placeholder="Enter password"
                  :danger="this.errors.password != null"
                  :danger-text="this.errors.password"
                  required></vs-input>

        <button class="loginButton form-input" type="button"  @click="checkForm(); loginSubmit()">Sign in</button>
    </form>

  </div>
</template>

<script>
import api from "../Api";
import {mutations} from "../store"

const Login = {
  name: "Login",
  data: function () {
    return {
      errors: [],
      email: "",
      password: "",
    };
  },
  methods: {
    validEmail: function (email) {
      var re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
      return re.test(email);
    },
    /**
     * Checks if the username and password match on what is stored in the backend.
     */
    checkForm: function() {
      this.errors = {
        hasErrors: false,
        email: null,
        password: null,
      };

      if (this.email.length === 0) {
        this.errors.email = "Email required.";
        this.errors.hasErrors = true;
      }
      else if (!this.validEmail(this.email)) {
        this.errors.email = "Email invalid.";
        this.errors.hasErrors = true;
      }
      if (this.password.length === 0) {
        this.errors.password = "Password required.";
        this.errors.hasErrors = true;
      }

      console.log(this.errors);
    },

    /**
     * Sends the login request to the backend by calling the login function from the API.
     */
    loginSubmit: function() {
      if(!this.errors.hasErrors){
        api.login(this.email, this.password)
          .then((response) => {
            //LOAD USER HOME PAGE, USING ROUTER
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

#main {
  font-family: 'Ubuntu', sans-serif;

  display: grid;
  grid-template-columns: 1fr;
  grid-template-rows: auto auto;
  grid-row-gap: 1em;

  max-width: 400px;
  background-color: white;
  margin: 1em auto;
  padding: 0.5em 0 0.5em 0;
  border-radius: 20px;
  border: 2px solid rgba(0, 0, 0, 0.02);
  box-shadow: 0 .5rem 1rem rgba(0, 0, 0, .15);
}

/* First Header Row */
#sign {
  grid-row: 1;
  grid-column: 1;

  margin: 0;
  padding: 0.5em 0;

  color: #385898;
  font-weight: bold;
  font-size: 24px;
  text-align: center;
}

form {
  grid-row: 2;
  grid-column: 1;

  display: grid;
  grid-template-columns: 1fr;
  grid-template-rows: repeat(3, auto);
  grid-row-gap: 1em;

  margin: auto;

}

.form-control {
  font-family: 'Ubuntu', sans-serif;
}

#email {
  grid-row: 1;
  grid-column: 1;

}

#password {
  grid-row: 2;
  grid-column: 1;
}


.loginButton {
  grid-row: 3;
  grid-column: 1;

  margin: 1em auto 2em auto;
  cursor: pointer;
  border-radius: 5em;
  color: #fff;
  background: #3B5998;
  border: 0;
  padding: 10px 40px;
  font-size: 13px;
  box-shadow: 0 0 20px 1px rgba(0, 0, 0, 0.04);
}

@media (max-width: 600px) {
  .main {
    border-radius: 0px;
  }
}

</style>