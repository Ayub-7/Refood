<template>
  <div id="app" class="main" >

    <vs-navbar
        class="vs-navbar"
        v-model="indexActive"
        type="fund"
        color="#1F74FF"
        text-color="rgba(255,255,255,.6)"
        active-text-color="rgba(255,255,255,1)">
      <div slot="title">
        <vs-navbar-title>
          ReFood
        </vs-navbar-title>
      </div>
      <vs-navbar-item index="0" v-if="getLoggedInUser() == null">
        <router-link to="/">Register</router-link>
      </vs-navbar-item>
      <vs-navbar-item index="1" v-if="getLoggedInUser() == null">
        <router-link to="/login">Login</router-link>
      </vs-navbar-item>

      <vs-navbar-item index="0" v-if="getLoggedInUser() != null">
        <router-link :to="{path: '/home'}">Home</router-link>
      </vs-navbar-item>
      <vs-navbar-item index="1" v-if="getLoggedInUser() != null && getActingAsUserId() == null">
        <router-link to="/businesses">Register a Business</router-link>
      </vs-navbar-item>
      <vs-navbar-item index="2" v-if="getLoggedInUser() != null">
        <router-link to="/search">Search</router-link>
      </vs-navbar-item>
      <vs-navbar-item index="3" v-if="getLoggedInUser() != null && getActingAsUserId() == null">
        <router-link :to="{path: `/users/${getLoggedInUser()}`}">Profile</router-link>
      </vs-navbar-item>
      <vs-navbar-item index="4" v-if="getLoggedInUser() != null && getActingAsUserId() != null">
        <router-link :to="{path: `/businesses/${getActingAsUserId()}`}">Business Profile</router-link>
      </vs-navbar-item>
      <vs-navbar-item index="5" v-if="getLoggedInUser() != null && getActingAsUserId() != null">
        <router-link :to="{path: `/addtocatalogue`}">Add To Catalogue</router-link>
      </vs-navbar-item>
      <vs-navbar-item index="6" v-if="getLoggedInUser() != null && getActingAsUserId() != null">
        <router-link :to="{path: `/businesses/${getActingAsBusinessId()}/products`}">Product Catalogue</router-link>
      </vs-navbar-item>
      <div @click="logoutUser()">
        <vs-navbar-item index="7" v-if="getLoggedInUser() != null">
          <router-link :to="{path: '/login'}">
          <span>Logout</span>
          </router-link>
        </vs-navbar-item>
      </div>

      <div class="userDetail" v-if="getLoggedInUser() != null">
        <ActingAs/>
      </div>

    </vs-navbar>


    <div id="view">
      <router-view></router-view>
    </div>
    <footer class="info">
      <h4>REFOOD 2021</h4>
    </footer>
  </div>
</template>
<script>
import Register from "./components/Register";
import ActingAs from "./components/ActingAs";
import Login from "./components/Login";
import ProductCatalogue from "./components/ProductCatalogue";
import BusinessRegister from "./components/BusinessRegister";
import AddToCatalogue from "@/components/AddToCatalogue";
import CurrencyInput from "@/components/CurrencyInput";
import {store, mutations} from "./store"
import api from "./Api"
import 'vuesax';
import 'vuesax/dist/vuesax.css';
// @click="goToUserPage()"

// Vue app instance
// it is declared as a reusable component in this case.
// For global instance https://vuejs.org/v2/guide/instance.html
// For comparison: https://stackoverflow.com/questions/48727863/vue-export-default-vs-new-vue
const app = {
  name: "app",
  components: {
    // list your components here to register them (located under 'components' folder)
    // https://vuejs.org/v2/guide/components-registration.html
    Login, Register, BusinessRegister, ActingAs, AddToCatalogue, ProductCatalogue, CurrencyInput
  },
  // app initial state
  // https://vuejs.org/v2/guide/instance.html#Data-and-Methods
  data: () => {
    return {
      indexActive: 0
    };
  },
  methods: {
    /**
     * Method used to get the current logged in user to use inside template
     * @returns {int} userId of current logged in user
     */
    getLoggedInUser() {
      return store.loggedInUserId;
    },

    getActingAsUserId(){
      return store.actingAsBusinessId;
    },

    getActingAsBusinessId() {
      return store.actingAsBusinessId;
    },

    getPrimaryBusinesses(){
      return store.userBusinesses;
    },
    /**
     * Calls the logout function which removes loggedInUserId
     */
    logoutUser() {
      api.logout()
      .then(() => {
        mutations.userLogout();
      })
    },
    toggleMobileMenu: function () {
      let x = document.getElementById("navLinks");
      let y = document.getElementById("topBar")
      if (x.style.display === "block") {
        x.style.display = "none";
        y.style.height = "55px";
      } else {
        x.style.display = "block";
        if(store.loggedInUserId){
          y.style.height = "140px";
        } else {
          y.style.height = "105px";
        }
      }
    }
  },


  beforeMount() {
    api.checkSession()
    .then((response) => {
      if(response.data.id != null){
        api.checkBusinessSession()
        .then((busResponse) => {
            if(busResponse.status == 200){
              mutations.setActingAsBusiness(busResponse.data.id, busResponse.data.name);
            } else {
              mutations.setActingAsUser();
            }
        });
      }
      mutations.setUserLoggedIn(response.data.id, response.data.role);
      mutations.setUserBusinesses(response.data.businessesAdministered);
      mutations.setUserName(response.data.firstName + " " + response.data.lastName);
    }).catch((err) => {
      this.$log.debug(err);
    });
  },
};

// make the 'app' available
export default app;
</script>

<style scoped>

[v-cloak] {
  display: none;
}

.vs-navbar {
  padding: 5px;
  color: rgb(255,255,255);
}

.userDetail:hover {
  background: #E0E0E0;
  /*color: #f5f5f5;*/
}

</style>