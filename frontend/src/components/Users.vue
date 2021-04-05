<template>
  <div id="body-profile" class="main">
    <h3 class="title text-center">Profiles</h3>
    <div
         v-bind:user="user"
         :key="user.id" v-if ="user != null">
      <form class="profile">
        <div class="profile-text-inner">

          <div class="form-row">
            <div class="form-group col-md-6">
              <label>ID</label>
              <p type="text" style="font-weight: bold; font-size: 25px;" class="form-control" placeholder="Full Name" name="id"> {{ user.id }}</p>
            </div>
            <div class="form-group col-md-6">
              <label>Member Since</label>
              <p type="text" class="form-control" placeholder="Full Name" name="memberTime"> {{ calculateDuration(user.created)}}</p>
            </div>
          </div>

            <div class="form-row">
              <div class="form-group col-md-6">
                <label>Full Name</label>
                <p type="text" class="form-control" placeholder="Full Name" name="fullname"> {{ user.firstName }} {{ user.middleName }} {{ user.lastName}}</p>
              </div>
              <div class="form-group col-md-6">
                <label>Nickname</label>
                <p type="text" class="form-control" placeholder="Nickname" name="nickname"> {{ user.nickname }} </p>
              </div>

            </div>

          <div class="form-row">
          <div class="form-group col-md-6">
            <label>Bio</label>
            <p type="text" class="form-control" placeholder="Bio" name="bio"> {{ user.bio }} </p>
          </div>
          <div class="form-group col-md-6">
            <label>Email</label>
            <p type="text" class="form-control" placeholder="Email" name="email"> {{ user.email }} </p>
          </div>
          </div>

            <div class="form-row">
              <div class="form-group col-md-6">
                <label>Date of Birth</label>
                <p type="text" class="form-control" placeholder="Date of Birth" name="dob"> {{ user.dateOfBirth }} </p>
              </div>

              <div class="form-group col-md-6">
                <label>Home Address</label>
                <p type="text" class="form-control" placeholder="Home Address" name="homeaddress"> {{ user.homeAddress }} </p>
              </div>
            </div>

          <div class="form-row">
          <div class="form-group col-md-6">
            <label>Date registered</label>
            <p type="text" class="form-control" placeholder="Date of Registration" name="registerdate"> {{ user.created.toString()}}</p>
          </div>
            <div class="form-group col-md-6">
              <label>Phone Number</label>
              <p type="text" class="form-control" placeholder="Phone Number" name="phonenumber"> {{ user.phoneNumber }}</p>
            </div>
            <div class="form-group col-md-6" v-if="businesses.length > 0" id='businessNames'>
            <label>Businesses</label>
              <ul>
                <li id ="business" v-for="(business, index) in businesses" :key="index" @click="goToBusinessPage(business)" style="cursor: pointer; color: #8C55AA;">
                  {{business.name}}
                </li>
              </ul>
            </div>
          </div>
          </div>


        </form>
      </div>
  </div>
</template>




<script>
var moment = require('moment');


import api from "../Api";

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
      let timeString = duration._data.years / -1 + " years and " + duration._data.months / -1 + " months";

      return timeString;
    },

    /**
     * Sends user to business page after clicking on business link
     * @param business business who's page will be loaded
     */    
    goToBusinessPage: function(business) {
      this.$router.push({path: `/businesses/${business.id}`})
    },

    /**
     * Performs a get request to get user info to display on page
     * @param userId ID of user that is currently being viewed
     */  

    getUserInfo: function(userId) {

      api.getUserFromID(userId) //Get user data
      .then((response) => {
        let user = response.data;
        let userBusinesses = []

        for(let business of user.businessesAdministered) { //Get business info to display as link
          api.getBusinessFromId(business)
          .then((response) => {
            userBusinesses.push(response.data); //gets pushed to list of all user businesses
          }).catch((err) => {
            throw new Error(`Error trying to retrieve business info from id: ${err}`);
          })

        }
        //Set properties
        this.businesses = userBusinesses;
        this.user = user;
        }).catch((err) => {
          throw new Error(`Error trying to get user info from id: ${err}`);
      })
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
.title {
  padding-top: 40px;
  color: #8C55AA;
  font-family: 'Ubuntu', sans-serif;
  font-weight: bold;
  font-size: 23px;
}

.main {
  background-color: #FFFFFF;
  width: 100%;
  margin: 7em auto;
  border-radius: 1.5em;
  box-shadow: 0px 11px 35px 2px rgba(0, 0, 0, 0.14);
}

.profile {
  background-color: #FFFFFF;
  width: 90%;
  margin: 1em auto;
  border-radius: 1.5em;
  box-shadow: 0px 11px 35px 2px rgba(0, 0, 0, 0.14);
}
.profile-text-inner {
  margin: 7em auto;
  width: 90%;
  height: 80%;
}

</style>