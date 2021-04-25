<template>

  <div v-if="this.user != null">
    <div id="container" v-if="this.business != null">
      <!-- Left Side Business Information Panel -->
      <div id="business-name-container">
        <div id="business-name"  >{{ business.name }}</div>
        <div id="business-type">{{ business.businessType }}</div>
      </div>

      <div id="business-container">
        <div id="description" class="sub-container">
          <div class="sub-header">Description</div>
          {{ business.description }}
        </div>

        <div id="info-container" class="sub-container">

          <div id="created-date">
            <div class="sub-header">Created</div>
            {{ business.created.split(' ')[0] }}
          </div>

          <div id="address">
            <div class="sub-header">Address</div>
            <div id="street-address">{{ business.address.streetNumber }} {{ business.address.streetName }}</div>
            <div id="city">{{ business.address.city }}</div>
            <div id="region">{{ business.address.region }}</div>
            <div id="country">{{ business.address.country }}</div>
            <div id="postcode">{{ business.address.postcode }}</div>
          </div>

        </div>

      </div>

      <main>
        <!-- Sub Navigation Bar -->
        <nav id="business-navbar">
          <router-link class="business-nav-item" :to="{name: `Business`, params:{id: business.id}}">Products</router-link>
          <router-link class="business-nav-item" :to="{name: `BusinessAdministrators`}">Administrators</router-link>
        </nav>

        <div id="content">
          <router-view></router-view>
        </div>

      </main>
    </div>
    <!-- 406 Error: Business with given Id does not exist. -->
    <div id="error" v-else>
      <div id="error-header"> Error 406 </div>
      <div id="error-description" style="font-size: 16px"> This business could not be found :( </div>
    </div>
  </div>

</template>


<script>
import api from "../Api";
import {store} from "../store";
const Business = {
  name: "Business",

  // App's initial state.
  data: function () {
    return {
      business: null,
      adminList: null,
      user: null
    };
  },

  methods: {
    getBusiness: function () {
      api.getBusinessFromId(this.$route.params.id)
          .then((res) => {
            this.business = res.data;
            this.adminList = JSON.parse(JSON.stringify(this.business.administrators)); // It just works?
            console.log(this.business);
          })
          .catch((error) => {
            throw new Error(`ERROR trying to obtain business info from Id: ${error}`);
          })
    },

    getUserInfo: function (userId) {
      api.getUserFromID(userId)
          .then((response) => {
            if (store.loggedInUserId == null) {
              this.user = response.data;
            } else {
              this.$router.push({path: "/login"});
            }
          }).catch((err) => {
        throw new Error(`Error trying to get user info from id: ${err}`);
      });
    },

    mounted() {
      let userId = this.$route.params.id
      this.user = this.getUserInfo(userId);
      // Retrieve business info.
      this.getBusiness();
    }

  }
}

export default Business;
</script>


<style scoped>

#container {
  display: grid;
  grid-template-columns: 1fr 1fr 3fr 1fr;
  grid-template-rows: auto auto;
  grid-column-gap: 1em;
}

/* Top Business Name Container */
#business-name-container {
  grid-column: 2 / 4;
  grid-row: 1;

  text-align: center;
  background-color: transparent;
  padding: 0.5em 0 0.5em 0;
  border-radius: 20px;
  border: 2px solid rgba(0, 0, 0, 0.02);
  margin: 8px 0 0 0;
  box-shadow: 0 .5rem 1rem rgba(0,0,0,.15);
}

#business-name {
  font-size: 32px;
  padding: 0.5em 0 0.5em 0;
}

#business-type {
  font-size: 16px;
  padding: 0 0 0.5em 0;
}

/* Business Info Panel on left side */
#business-container {
  grid-column: 2;
  grid-row: 2;

  display: grid;
  grid-template-columns: 1fr;
  grid-template-rows: repeat(3, auto) repeat(1, 1fr);
  grid-row-gap: 1em;
}

.sub-container {
  padding: 2em;
  border-radius: 1.5em;
  box-shadow: 0 11px 35px 2px rgba(0, 0, 0, 0.14);
  background-color: #F5F5F5;
}

.sub-header {
  font-size: 12px;
  color: gray;
}

#description {
  grid-row: 2;
}

#info-container {
  grid-column: 1;
  grid-row: 3;

  display: grid;
  grid-template-columns: 1fr;
  grid-template-rows: repeat(1, auto) repeat(1, 1fr);
  grid-row-gap: 2em;

}

#created-date {
  grid-column: 1;
  grid-row: 1;
}

#address {
  grid-row: 2;
  height: fit-content;
}

/* Right Hand Content Side. */
main {
  grid-column: 3;
  grid-row: 2;

  margin: 1em 0 1em 0;
  border-radius: 1.5em;
  box-shadow: 0 11px 35px 2px rgba(0, 0, 0, 0.14);
  background-color: #F5F5F5;
}

#business-navbar {
  grid-column: 2;
  grid-row: 1;

  font-size: 18px;

  padding-top: 1em;
  padding-bottom: 1em;

  box-shadow: 0 0 35px 0 rgba(0, 0, 0, 0.14);
  border-radius: 1em;
  border: 2px solid rgba(0, 0, 0, 0.02);

}

.business-nav-item {
  text-align: center;
  color: black;
  font-weight: 700;
  font-size: 14px;
  letter-spacing: 1px;
  text-decoration: none;

  padding: 10px 20px;
  margin: 10px;

  background: #dbe0dd linear-gradient(to right, #abd9c1 10%, #fceeb5 50%, #ee786e 100%);
  background-size: 500%;
  border: none;
  border-radius: 5rem;
  box-shadow: 0 .5rem 1rem rgba(0,0,0,.15);
}

.business-nav-item:hover {
  box-shadow: 0 0.25em 1em rgba(0,1,1,.25);
}

/* For when the screen gets too narrow - mainly for mobile view */
@media screen and (max-width: 700px) {
  #container {
    display: grid;
    grid-template-columns: 1fr;
    grid-template-rows: auto auto auto;

    margin: auto;
    padding: 0 2em;
  }

  #business-name-container {
    grid-column: 1;
    grid-row: 1;
  }

  #business-container {
    grid-column: 1;
    grid-row: 2;
  }

  main {
    grid-column: 1;
    grid-row: 3;
  }

  #business-navbar {
    align-content: center;
  }

}


/* Error 406 Styling */
#error {
  text-align: center;
  padding: 0 1em 1em 1em;

  margin: 1em 0 1em 0;
  border-radius: 1.5em;
  box-shadow: 0 11px 35px 2px rgba(0, 0, 0, 0.14);
  background-color: #F3EBF6;
}
#error-header {
  font-size: 48px;
  padding: 0.75em 1em 1em 1em;
}


</style>