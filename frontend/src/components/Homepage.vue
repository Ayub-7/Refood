<template>
  <!-- -->
  <div id="container" v-if="getLoggedInUserId() != null">
      <!-- Header of page, contains link to user profile and welcome message with user's first name -->
      <div id="business-name-container">
        <div v-if="getBusinessName() != null" id="business-name">
          Welcome to your home page, {{getBusinessName()}}!
        </div>
        <div v-else id="business-name">
          Welcome to your home page, {{getUserName()}}! </h1>
        </div>
      </div>

      <div id="business-container">
        <!-- Activity feed THINGS WILL LATER BE PLACE IN THESE FIELDS, JUST SAMPLE AT THE MOMENT -->
        <div id="description" class="sub-container">
          <div class="userinfo-container" v-if="getBusinessId()">
          <ul id="businfo-content">
            <li class="business-nav-item" @click='goToProfile()' style='cursor: pointer; text-decoration: none;'>Business Profile</li>
            <li class="business-nav-item"  @click='goToProductCatalogue()' style='cursor: pointer'>Product Catalogue</li>
          </ul>
          </div>
          <div class="userinfo-container" v-else>
            <ul id="userinfo-content">
              <li class="business-nav-item" @click='goToProfile()' style='cursor: pointer'>Profile</li>
            </ul>
          </div>
        </div>

        <div id="info-container" class="sub-container">
          <p>Favourites</p>
        </div>

      </div>
        <main>
          <nav id="business-navbar">
            <div id="business-name" style="text-align: center;">
              News Feed
            </div>
          </nav>
        </main>
          <!-- Things like user businesses, business inventory etc. -->
  </div>
</template>

<script>
import api from "../Api";
import {mutations, store} from "../store"
const Homepage = {
    name: "Homepage",
    data: function () {
        return {
            userId: null,
            businesses: [],
            actingAsBusinessId: null,
            business: null,

        }
    },

    methods: {
      /**
       * Gets user info from backend and sets userFirstname property (for welcome message)
       * @param userId ID of user who is currently viewing page.
       */
      getUserDetails: function(userId) {
        api.getUserFromID(userId)
          .then((response) => {
            if(store.loggedInUserId != null) {
              this.user = response.data;
              this.businesses = JSON.parse(JSON.stringify(this.user.businessesAdministered));
              this.userLoggedIn = true;
              mutations.setUserDateOfBirth(response.data.dateOfBirth);
              mutations.setUserName(response.data.firstName + " " + response.data.lastName);
              mutations.setUserPrimaryBusinesses(this.businesses);
            } else {
              this.$router.push({path: "/login"}); //If user not logged in send to login page
            }

          }).catch((err) => {
            if (err.response.status === 401) {
              this.$vs.notify({title:'Unauthorized Action', text:'You must login first.', color:'danger'});
              this.$router.push({name: 'LoginPage'});
            }
            throw new Error(`Error trying to get user info from id: ${err}`)
          })
      },

      getBusiness: function(id) {
        api.getBusinessFromId(id)
            .then((res) => {
              this.business = res.data;
            })
            .catch((error) => {
              throw new Error(`ERROR trying to obtain business info from Id: ${error}`);
            })
      },

      getBusinessId: function() {
        let busId = store.actingAsBusinessId;
        if(busId){
          this.getBusiness(busId);
        }
        return busId;
      },

      getBusinessName: function() {
        return store.actingAsBusinessName;
      },

      getUserName: function() {
        return store.userName;
      },
      /**
       * Gets the logged in users id
       */
      getLoggedInUserId: function() {
        this.userId = store.loggedInUserId;
        return this.userId;
      },

      /**
       * Pushes users profile onto router
       */
      goToProfile: function() {
        if(this.getBusinessId() != null){
          this.$router.push({path: `/businesses/${this.getBusinessId() }`});
        } else {
          this.$router.push({path: `/users/${this.getLoggedInUserId()}`});
        }
      },
      goToProductCatalogue: function() {
        this.$router.push({path: `/businesses/${this.getBusinessId()}/products`});
      }
    },

    mounted: function () {
      let userId = store.loggedInUserId;
      this.getUserDetails(userId);
    },
  
}
export default Homepage;
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
  padding: 15px 0 15px 0;
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
  list-style: none;
  padding: 10px 0px;
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
</style>