<template>
  <!-- -->
  <div id="body" v-if="getLoggedInUserId() != null">
      <!-- Header of page, contains link to user profile and welcome message with user's first name -->
      <div id="welcomeHeader">
        <div v-if="getBusinessId()">
          <h1 id="BusPageTitle"> Welcome to your home page, {{business.name}}! </h1>
        </div>
        <div v-else>
          <h1 id="pageTitle"> Welcome to your home page, {{this.userFirstName}}! </h1>
        </div>
      </div>

      <div id="page-container">
        <!-- Activity feed THINGS WILL LATER BE PLACE IN THESE FIELDS, JUST SAMPLE AT THE MOMENT -->
        <div class="content-container">
          <h2>Content Header</h2>
          <p class="content">Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean in facilisis ligula. Maecenas suscipit at magna vel maximus. Nunc in imperdiet erat. Aenean semper leo tellus, vestibulum interdum tortor aliquet a. Interdum et malesuada fames ac ante ipsum primis in faucibus. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam feugiat dolor consequat erat bibendum, sit amet condimentum lectus lacinia.</p>
        </div>
        <div class="userinfo-container" v-if="getBusinessId()">
          <!-- Things like user businesses, business inventory etc. -->
          <h2>Business links</h2>
          <ul id="businfo-content">
            <li class="profileLink" @click='goToProfilePage()' style='cursor: pointer'>Go to profile</li>
          </ul>
        </div>
        <div class="userinfo-container" v-else>
          <h2>User links</h2>
          <ul id="userinfo-content">
            <li class="profileLink" @click='goToProfilePage()' style='cursor: pointer'>Go to profile</li>
          </ul>
        </div>

      </div>
  </div>
</template>

<script>
import api from "../Api";
import {mutations, store} from "../store"
const Homepage = {
    name: "Homepage",
    data: function () {
        return {
            userFirstName: null,
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
              this.userFirstName = `${response.data.firstName}`
              mutations.setUserName(response.data.firstName + " " + response.data.lastName);
              mutations.setUserPrimaryBusinesses(this.businesses);
            } else {
              this.$router.push({path: "/login"}); //If user not logged in send to login page
            }

          }).catch((err) => {
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
      goToProfilePage: function() {
        if(this.getBusinessId() != null){
          this.$router.push({path: `/businesses/${this.getBusinessId() }`});
        } else {
          this.$router.push({path: `/users/${this.getLoggedInUserId()}`});
        }
      }
    },

    mounted: function () {
      //Retrieve userId and load user details
      let userId = store.loggedInUserId;
      //let businessId = store.actingAsBusinessId;
      //console.log(store.actingAsBusinessId);
      console.log(store.loggedInUserId);
      this.getUserDetails(userId);
    },
  
}
export default Homepage;
</script>

<style scoped>
#body {
  /* background-color: #F3EBF6; */
  font-family: 'Ubuntu', sans-serif;
  padding: 3em;
}

#welcomeHeader {
  display: grid;
  grid-template-columns: 85% 1fr;
  text-align: center;
  background-color: #F3EBF6;
  border-radius: 20px;
}

#pageTitle {
  border-radius: 10px;
  background-color: #F3EBF6;
  font-size: 50px

}

.profileLink {
  border-radius: 30px;
  background-color: #f3e3f9;
  margin-right: 10px;
  margin-top: 5px;
  font-size: 30px;
  padding: 10px 5px;
  list-style-type: none;
  box-shadow: 0px 3px 8px#cfcfcf;
}

ul#businfo-content {
  padding-left: 10px;
}

#page-container {
  display: grid;
  grid-template-columns: 60% 1fr;
  margin-top: 20px;

}

.content-container {
  grid-column: 1;
  text-align: center;
  background: #F3EBF6;
  border-radius: 30px;
  margin-bottom: 30px;
}

.content {
  background: #f3e3f9;
  padding: 1em;
  margin: 1em;
  font-size: 20px;
  border-radius: 30px;
}

.userinfo-container {
  text-align: center;
  background: #F3EBF6;
  margin-left: 10px;
  border-radius: 30px;
  grid-row: 1;
  grid-column: 2;
}

#userinfo-content {
  font-size: 20px
}

@media screen and (max-width: 700px) {
  #welcomeHeader {
    grid-template-columns: 1fr;
  }
  
  #page-container {
    grid-template-columns: 1fr;
  }

  #container {
    display: grid;
    grid-template-columns: 1fr;
    grid-template-rows: auto auto;

    margin: auto;
    padding: 0 2em;
  }

  #pageTitle {
    font-size: 30px;
    grid-column: 1;
    grid-row: 1;
  }

  #profileLink {
    margin-right: 0px;
    grid-column: 1;
    grid-row: 2;
  }

  #userinfo-container {
    grid-column: 1;
    grid-row: 3;
    margin-bottom: 20px;
  }

  .content-container {
    grid-column: 1;
    grid-row: 4
  }

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