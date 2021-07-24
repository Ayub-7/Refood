<template>
  <div class="main" id="body">
    <div id="search">
      <vs-input class="search-input" type="search" placeholder="Search for user" name="searchbarUser" v-model="searchbarUser" style="width: 400px; font-size: 24px" size="large"/>
      <vs-button id="submitSearchUser" size="large" type="border" @click="searchUsers">Search</vs-button>
      <vs-input class="search-input" type="search" placeholder="Search for business" name="searchbarBusiness" v-model="searchbarBusiness" style="width: 400px; font-size: 24px" size="large"/>
      <vs-button id="submitSearchBusiness" size="large" type="border" @click="searchBusiness">Search</vs-button>
    </div>


    <div v-if="users.length" id="userTable">
      <vs-table :data="this.users" max-items="10" pagination stripe >
        <template slot="thead" id="usersTableHeader">

          <vs-th sort-key="firstName" style="border-radius: 4px 0 0 0;">
            First name
          </vs-th>
          <vs-th sort-key="lastName">
            Last name
          </vs-th>
          <vs-th sort-key="city">
            City
          </vs-th>
          <vs-th sort-key="country" v-if="mobileMode==false">
            Country
          </vs-th>
          <vs-th sort-key="email" v-if="mobileMode==false">
            Email
          </vs-th>

          <!-- Extra header for go to profile button -->
          <vs-th style="border-radius: 0 4px 0 0;">
          </vs-th>

          <vs-th v-if="isDGAA">Is Admin</vs-th>
          <vs-th class="dgaaCheckbox" v-if="isDGAA">Toggle Admin</vs-th>



        </template>

        <template slot-scope="{data}">
          <vs-tr :key="indextr" v-for="(tr, indextr) in data">


            <vs-td :data="data[indextr].firstName">{{data[indextr].firstName}}</vs-td>

            <vs-td :data="data[indextr].lastName">{{data[indextr].lastName}}</vs-td>

            <vs-td :data="data[indextr].city">{{`${data[indextr].city}`}}</vs-td>

            <vs-td :data="data[indextr].country" v-if="mobileMode==false">{{`${data[indextr].country}`}}</vs-td>

            <vs-td :data="data[indextr].email" v-if="mobileMode==false">{{data[indextr].email}}</vs-td>

            <vs-td>
              <vs-button id="goToProfileButton" @click="goToUserProfile(data[indextr].id)">Go to profile</vs-button>
            </vs-td>

            <vs-td :data="data[indextr].role" v-if="isDGAA"> {{data[indextr].role}} </vs-td>

            <vs-td v-if="isDGAA" class="dgaaCheckbox">
              <input type="checkbox" @click="toggleAdmin(data[indextr])">
            </vs-td>


          </vs-tr>
        </template>
      </vs-table>
      <div id="displayingUsers">Showing {{searchIndexMin}} - {{searchIndexMax}} of {{users.length}} results</div>
    </div>
    <div v-if="businesses.length" id="businessTable">
      <vs-table :data="this.businesses" max-items="10" pagination stripe>
        <template slot="thead" id="businessesTableHeader">

          <vs-th sort-key="businessName" style="border-radius: 4px 0 0 0;">
            Business name
          </vs-th>
          <vs-th sort-key="businessType">
            Business type
          </vs-th>
          <vs-th sort-key="city" v-if="mobileMode==false">
            City
          </vs-th>
          <vs-th sort-key="country" v-if="mobileMode==false">
            Country
          </vs-th>

          <!-- Extra header for go to profile button -->
          <vs-th style="border-radius: 0 4px 0 0;">
          </vs-th>

          <vs-th v-if="isDGAA">Is Admin</vs-th>
          <vs-th class="dgaaCheckbox" v-if="isDGAA">Toggle Admin</vs-th>



        </template>

        <template slot-scope="{data}">
          <vs-tr :key="indextr" v-for="(tr, indextr) in data">


            <vs-td :data="data[indextr].name">{{data[indextr].name}}</vs-td>

            <vs-td :data="data[indextr].businessType">{{data[indextr].businessType}}</vs-td>

            <vs-td :data="data[indextr].address.city" v-if="mobileMode==false">{{`${data[indextr].address.city}`}}</vs-td>

            <vs-td :data="data[indextr].address.country" v-if="mobileMode==false">{{data[indextr].address.country}}</vs-td>

            <vs-td>
              <vs-button id="goToProfileButton" @click="goToBusinessProfile(data[indextr].id)">Go to profile</vs-button>
            </vs-td>

            <vs-td :data="data[indextr].role" v-if="isDGAA"> {{data[indextr].role}} </vs-td>

            <vs-td v-if="isDGAA" class="dgaaCheckbox">
              <input type="checkbox" @click="toggleAdmin(data[indextr])">
            </vs-td>


          </vs-tr>
        </template>
      </vs-table>

      <div id="displayingBusinesses">Showing {{searchIndexMin}} - {{searchIndexMax}} of {{businesses.length}} results</div>
    </div>

  </div>
</template>

<script>
import api from "../Api";
import {store} from "../store";

const Search = {
  name: "Search",
  data: function() {
    return {
      tableLoaded: false,
      searchbarUser: "",
      searchbarBusiness: "",
      mobileMode: false,
      errors: [],
      users: [],
      businesses: [],
      searchIndexMin: 1,
      searchIndexMax: 10,
      isDGAA: false
    };
  },

  /**
   * populates the page with with dummy data for testing purposes only
   *
   * api.getSearchResults() queries the test back-end (json-server)
   * at /users which returns a JSON object list of test users which can
   * be filtered by the webpage.
   *
   * remove when test back end works well...
   */
  mounted() {
    if (JSON.parse(sessionStorage.getItem('businessesCache')).length > 0) {
      this.businesses = JSON.parse(sessionStorage.getItem('businessesCache'));
      this.paginator(this.businesses)
    }
    if ( this.getUserRole() === 'DGAA') {
      this.isDGAA = true;
    }
    this.setMobileMode()
  },

  created() {
    window.addEventListener(
        'resize',
        this.setMobileMode
    )

  },


  methods: {

    /**
     * If page reaches certain width set mobile mode on, this removes columns from the table to ensure it fits on the page
     */
    setMobileMode: function() {
      if(window.innerWidth < 1300) {
        this.mobileMode = true
      } else {
        this.mobileMode = false;
      }
    },

    /**
     * Increases search range to be shown on page
     */

    increaseSearchRange: function() {
      this.searchIndexMin += 10;
      if(this.searchIndexMax + 10 > this.users.length) {
        this.searchIndexMax += this.users.length - this.searchIndexMax
      } else {
        this.searchIndexMax += 10;
      }
    },

    /**
     * Increases search range to be shown on page
     */
    decreaseSearchRange: function() {
      this.searchIndexMin -= 10;
      if(this.searchIndexMax % 10 != 0){
        this.searchIndexMax -= this.searchIndexMax % 10;
      } else {
        this.searchIndexMax -= 10;
      }
    },

    getUserRole: function () {
      return store.role;
    },

    /**
     * Go to users profile
     * @param userId id of user that has been clicked
     */
    goToUserProfile(userId) {
      this.$router.push({path: `/users/${userId}`})
    },
    /**
     * Go to business' profile
     * @param bizId id of business that has been clicked
     */
    goToBusinessProfile(bizId) {
      sessionStorage.setItem("businessesCache", JSON.stringify(this.businesses))
      this.$router.push({path: `/businesses/${bizId}`})
    },
    /**
     *
     */
    paginator(data) {
      //Need to set properties of user object so they can be sorted by
      for(let user of data) {
        user.country = user.homeAddress.country;
        user.city = user.homeAddress.city;
      }

      if(data.length < 10) {
        this.searchIndexMin = 1;
        this.searchIndexMax = data.length;
        if(data.length == 0){
          this.searchIndexMin = 0;
        }
      } else {
        this.searchIndexMin = 1;
        this.searchIndexMax = 10;
      }
    },
    /**
     * Searches for the users in the database by calling the API function with an SQL query to find the
     * users based on the input in the search box.
     */
    searchUsers: function () {
      if (this.businesses.length || JSON.parse(sessionStorage.getItem('businessesCache')).length > 0) {
        sessionStorage.setItem("businessesCache", [])
        this.businesses = [];

      }
      if (this.searchbarUser === "") return;
      this.$vs.loading();
      api.searchUsersQuery(this.searchbarUser)
        .then((response) => {
          this.users = response.data;
          this.users = this.users.filter(x => typeof(x) == "object")
          this.paginator(this.users);
        })
        .catch((error) => {
          this.$log.debug(error);
          this.error = "Failed to load users";
        })
        .finally(() => {
          this.$vs.loading.close();
          if(!this.tableLoaded){
            document.getElementsByClassName("vs-pagination--ul")[0].remove(); //remove vuesax table number listing

            //Event listeners for vuesax buttons on table since they're generated afterwards
            document.getElementsByClassName("btn-next-pagination")[0].addEventListener('click', this.increaseSearchRange);
            document.getElementsByClassName("btn-prev-pagination")[0].addEventListener('click', this.decreaseSearchRange);

            this.tableLoaded = true;
        }
      })
    },
    /**
     * Searches for the businesses in the database by calling the API function with an SQL query
     * to find the businesses based on the input in the search box.
     */
    searchBusiness: function() {
      if (this.users.length) {
        this.users = [];
      }
      if (this.searchbarBusiness === "") return;
      this.$vs.loading();
      api.searchBusinessesQuery(this.searchbarBusiness)
         .then((response) => {
           this.businesses = response.data;
           this.businesses = this.businesses.filter(x => typeof(x) == "object")
           this.paginator(this.businesses);
         })
         .catch((error) => {
           this.$log.debug(error);
           this.error = "Failed to load businesses";
         })
         .finally(() => {
           this.$vs.loading.close();
           if(!this.tableLoaded){
             document.getElementsByClassName("vs-pagination--ul")[0].remove(); //remove vuesax table number listing

             //Event listeners for vuesax buttons on table since they're generated afterwards
             document.getElementsByClassName("btn-next-pagination")[0].addEventListener('click', this.increaseSearchRange);
             document.getElementsByClassName("btn-prev-pagination")[0].addEventListener('click', this.decreaseSearchRange);

             this.tableLoaded = true;
           }
         })
    },

    /**
     * makes the checkuser an administrator
     * if they are already, revoke privledges...
     */

    toggleAdmin: function (currentUser) {
      if (currentUser.role == 'USER') {
        api.makeUserAdmin(currentUser.id);
        currentUser.role = 'GAA'
      } else if (currentUser.role == 'GAA') {
        api.revokeUserAdmin(currentUser.id);
        currentUser.role = 'USER'
      }
    },
  },
}

export default Search;
</script>

<style scoped>


#displayingUsers {
  text-align: right;
}

#displayingBusinesses {
  text-align: right;
}


#search {
  font-weight: bold;
  height: 10em;
  display: flex;
  justify-content: center;
  align-items: center;
}

#search input {
  font-size: 24px;
}

.search-input >>> .vs-inputx {
  font-size: 18px;
}

.search-input >>> span {
  font-size: 18px;
  margin: 4px 0;
}

#goToProfileButton {
  cursor: pointer;
  height: 35px;
}

#userTable {
  width: 65%;
  font-family: 'Ubuntu', sans-serif;
  font-weight: bold;
  margin: auto;
  box-shadow: 0 11px 35px 2px rgba(0, 0, 0, 0.14);
  border-radius: 1.5em;
  border-style: solid;
  border-color: white;
  padding: 1em;
}

#businessTable {
  width: 65%;
  font-family: 'Ubuntu', sans-serif;
  font-weight: bold;
  margin: auto;
  box-shadow: 0 11px 35px 2px rgba(0, 0, 0, 0.14);
  border-radius: 1.5em;
  border-style: solid;
  border-color: white;
  padding: 1em;
}

#submitSearchUser {
  margin-left: 0.5em;
  margin-right: 0.5em;
  height: 3em;
}

#submitSearchBusiness {
  margin-left: 0.5em;
  height: 3em;
}

tr {
  font-size: 15px
}

th {
  background: #1F74FF;
  color: white;
}

.main {
  background-color: white;
  top: 1px;
  padding-bottom: 4em;
}



/* For when the screen gets too narrow - mainly for mobile view */
@media screen and (max-width: 1300px) {
  #userTable {
    width: 100%;
  }

  #businessTable {
    width: 100%;
  }

  tr {
    font-size: 10px;

  }

  th {
    background: #1F74FF;
    color: white;
    font-size: 10px
  }
}


</style>
