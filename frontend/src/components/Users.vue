<template>
  <div id="body-profile" class="main">
    <h3 class="title text-center">Profiles</h3>
    <div
         v-bind:user="user"
         :key="user.id">
      <form class="profile">
        <div class="profile-text-inner">

          <div class="form-row">
            <div class="form-group col-md-6">
              <label>ID</label>
              <p type="text" style="font-weight: bold; font-size: 25px;" class="form-control" placeholder="Full Name" name="id"> {{ user.id }}</p>
            </div>
            <div class="form-group col-md-6">
              <label>Member Since</label>
              <p type="text" class="form-control" placeholder="Full Name" name="memberTime"> {{ calculateDuration(user.registerDate)}}</p>
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
            <div class="form-group col-md-6">
            <label>Businesses</label>
              <ul>
                <li v-for="business in businesses" :key="business" @click="goToBusinessPage(business)" style="cursor: pointer; color: #8C55AA;">
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


import api from "@/Api";

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
      let timeString = duration._data.years / -1 + " years and " + duration._data.months / -1 + " months";

      return timeString;
    },

    
    goToBusinessPage: function(business) {
      this.$router.push({path: `/businesses/${business.id}`})
    }
  },


  mounted: function () {
    let userId = this.$route.params.id
    // if(this.$store.state.viewingUserId != null) {
    //   userId = this.$store.state.viewingUserId
    // }
    api.getUserFromID(userId)
    .then((response) => {
      console.log(response.data);
      this.user = response.data;
    })

    //THIS CODE NEEDS TO BE UNCOMMENTED WHEN BUSINESS API CALLS IS SETUP
    // for(let business of this.user.businessesAdministered) {
    //   api.getBusinessFromId(business)
    //   .then((response) => {
    //     this.businesses.push(response);
    //   })
    // }

    //Sample business to display
    this.businesses = [
        {
        "id": 1,
        "administrators": [
            {
                "id": 9,
                "firstName": "Joete",
                "middleName": "YEP",
                "lastName": "Stopps",
                "nickname": "Multi-layered",
                "bio": "responsive capacity",
                "email": "jstopps7@flickr.com",
                "dateOfBirth": "1984-10-14",
                "phoneNumber": "+36 694 564 9090",
                "homeAddress": "34 Mendota Avenue",
                "created": "2021-04-04 02:42:54",
                "role": "USER",
                "businessesAdministered": [
                    1
                ]
            }
        ],
        "name": "Business1",
        "description": "Test Business 1",
        "address": "123 Test Street",
        "businessType": "Accommodation and Food Services",
        "created": "2021-04-04 02:42:55"
      }
    ]
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