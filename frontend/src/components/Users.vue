<template>
  <div id="container" v-if="this.user != null">

    <div id="options-bar">
        <div class="sub-header" style="text-align: center"> Options </div>
        <div class="options-card"> Add to Business </div>
    </div>

    <div id="name-container">
      <div id="full-name"> {{ this.user.firstName }} {{ this.user.middleName }} {{ this.user.lastName }} </div>
      <div id="nickname"> {{ this.user.nickname }} </div>
    </div>

    <!-- Left Profile Side -->
    <div id="profile">
      <div id="bio" class="sub-container">
        <div class="sub-header">Bio</div>
        {{ this.user.bio }}
      </div>

      <div class="sub-container" id="info-container">
        <div id="created">
          <div class="sub-header">Member Since</div>
          {{ this.user.created.split(' ')[0] }}
          <div style="font-size: 12px">{{ calculateDuration(user.registerDate) }}</div>

        </div>
        <div id="email">
          <div class="sub-header">Email</div>
          {{ this.user.email }}
        </div>
        <div id="birthDate">
          <div class="sub-header">Date of Birth</div>
          {{ this.user.dateOfBirth }}
        </div>
        <div id="address">
          <div class="sub-header">Home Address</div>
          {{ this.user.homeAddress }}
        </div>
        <div id="phonenumber">
          <div class="sub-header">Phone Number</div>
          {{ this.user.phoneNumber }}
        </div>
      </div>

    </div>

    <!-- Right Content Side -->
    <main>
      <ul id="business-list">
        <business-card v-for="business in businesses" :key="business.id" v-bind:business="business"/>
      </ul>
    </main>

  </div>
</template>




<script>
const moment = require('moment');
import Vue from "vue";
import api from "@/Api";

Vue.component('business-card', {
  template: ` <li class="card" @click="goToBusinessPage(business)">
                  <div class="card-name">{{ business.name }}</div>
                  <div class="card-type">{{ business.businessType }}</div>
                  <div class="card-description">{{ business.description }}</div>
               </li>`,

  props: ['business'],

  methods: {
    /**
     * Sends user to business page after clicking on business link
     * @param business business who's page will be loaded
     */
    goToBusinessPage: function(business) {
      this.$router.push({path: `/businesses/${business.id}`})
    },
  }

});

const Users = {
  name: "Profile",
  data: function () {
    return {
      user: null,
      businesses: []
    };
  },

  methods: {
    calculateDuration: function(registerDate) {
      const TimeElapsed = Date.now();
      const today = new Date(TimeElapsed);
      let fromTime = moment(registerDate).diff(today);
      let duration = moment.duration(fromTime);
      console.log(duration)

      let timeString = "(";
      if ((duration._data.years / -1) > 1) timeString += duration._data.years / -1 + " years ";
      if ((duration._data.months / -1 ) > 1) timeString += duration._data.months / -1 + " months";
      else timeString += "under 1 month";
      timeString += ")";

      return timeString;
    },

    /**
     * Performs a get request to get user info to display on page
     * @param userId ID of user that is currently being viewed
     */

    getUserInfo: function(userId) {

      api.getUserFromID(userId) //Get user data
          .then((response) => {
            this.user = response.data;
            this.businesses = JSON.parse(JSON.stringify(this.user.businessesAdministered));
            console.log(this.businesses);
          }).catch((err) => {
        throw new Error(`Error trying to get user info from id: ${err}`);
      });



    }

  },

  mounted: function () {
  //On page load call getUserInfo function to get user information
    let userId = this.$route.params.id
    this.user = this.getUserInfo(userId);
  },
}

export default Users;
</script>

<style scoped>

#container {
  display: grid;
  grid-template-columns: 1fr 1fr 3fr 1fr;
  grid-template-rows: 1fr auto;
  grid-column-gap: 1em;
  margin: auto;
  padding: 0 2em;
}

/* Options Bar */
#options-bar {
  grid-column: 1;
  grid-row: 2;

  padding: 2em;
  border-radius: 1.5em;
  box-shadow: 0 11px 35px 2px rgba(0, 0, 0, 0.14);
  background-color: #F5F5F5;
  margin: 1em 0 1em 0;

}


.options-card {
  cursor: pointer;

  text-align: center;
  color: black;
  font-weight: 500;
  font-size: 14px;
  letter-spacing: 1px;
  text-decoration: none;

  padding: 10px 20px;
  margin: 1em;
  background: #dbe0dd linear-gradient(to right, #abd9c1 10%, #fceeb5 50%, #ee786e 100%);
  background-size: 500%;
  border: none;
  border-radius: 5rem;
  box-shadow: 0 .5rem 1rem rgba(0,0,0,.15);
}

.options-card:hover {
  box-shadow: 0 0.25em 1em rgba(0,1,1,.25);
}

/* Name Header */
#name-container {
  grid-column: 2 / 4;

  text-align: center;

  background-color: transparent;
  padding: 0.5em 0 0.5em 0;
  border-radius: 20px;
  border: 2px solid rgba(0, 0, 0, 0.02);
  margin: 8px 0 0 0;
  box-shadow: 0 .5rem 1rem rgba(0,0,0,.15);

}

#full-name {
  font-size: 32px;
  padding: 0.5em 0 0.5em 0;
}

#nickname {
  font-size: 16px;
  padding: 0 0 0.5em 0;
}

/* Left Profile Side */
#profile {
  grid-column: 2;
  grid-row: 2;

  display: grid;
  grid-template-columns: 1fr;
  grid-template-rows: repeat(3, auto) repeat(1, 1fr);
  grid-row-gap: 1em;

}

.sub-header {
  font-size: 12px;
  color: gray;
}

.sub-container {
  padding: 2em;
  border-radius: 1.5em;
  box-shadow: 0 11px 35px 2px rgba(0, 0, 0, 0.14);
  background-color: #F5F5F5;
}

#bio {
  grid-column: 1;
  grid-row: 2;

}

#info-container {
  grid-column: 1;
  grid-row: 3;

  font-size: 16px;

  display: grid;
  grid-template-columns: 1fr;
  grid-template-rows: repeat(3, auto) repeat(1, 1fr);
  grid-row-gap: 1em;

}

/* Main Content Side */
main {
  grid-column: 3;
  grid-row: 2;

  border-radius: 1.5em;
  box-shadow: 0px 11px 35px 2px rgba(0, 0, 0, 0.14);
  margin: 1em 0 1em 0;
  padding: 0;
  background-color: #F5F5F5;
}

/* Business Card Component Related */
#business-list {
  padding: 1em;
}

.card {
  background-color: transparent;
  padding: 1em;
  border-radius: 20px;
  border: 2px solid rgba(0, 0, 0, 0.02);
  margin: 1em;
  box-shadow: 0 .5rem 1rem rgba(0,0,0,.15);

  display: grid;
  grid-template-columns: auto auto;
  grid-template-rows: auto auto auto;
}

.card:hover {
  box-shadow: 0 0.5em 1em rgba(0,1,1,.25);
  cursor: pointer;
}

.card >>> .card-name {
  grid-row: 1;
  grid-column: 1;

  padding: 0.25em 0 0.25em 0;
  font-size: 24px;
}

.card >>> .card-type {
  grid-row: 1;
  grid-column: 2;

  text-align: end;
  height: fit-content;
  line-height: normal;
  font-size: 16px;
  padding: 0.5em 0 1em 0;
}

.card >>> .card-description {
  font-size: 12px;
  padding: 0.5em 0 0.5em 0;
}



/* For when the screen gets too narrow - mainly for mobile view */
@media screen and (max-width: 700px) {
  #container {
    display: grid;
    grid-template-columns: 1fr;
    grid-template-rows: auto auto auto auto;
  }

  #name-container {
    grid-column: 1;
    grid-row: 1;
  }

  #options-bar {
    grid-column: 1;
    grid-row: 2;
  }

  #profile {
    grid-column: 1;
    grid-row: 3;
  }

  main {
    grid-column: 1;
    grid-row: 4;
    margin: 0;
  }

  .card {
    max-width: 100%;
  }

}


</style>