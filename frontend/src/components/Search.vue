<template>
  <div class="main" id="body">
  <div id="search">
    
    <input type="search" placeholder="Search for user" name="searchbar" v-model="searchbar">
    <vs-button type="border" @click="searchUsers">Search</vs-button>
  </div>


  <div v-if="users.length" id="userTable">
    <vs-table :data="users" pagination max-items="10">
      <template slot="thead" id="tableHeader">

        <vs-th sort-key="id">
          ID
        </vs-th>
        <vs-th sort-key="firstName">
          First name
        </vs-th>
        <vs-th sort-key="lastName">
          Last name
        </vs-th>
        <vs-th sort-key="city">
          City
        </vs-th>
        <vs-th sort-key="country">
          Country
        </vs-th>
        <vs-th sort-key="email">
          Email
        </vs-th>
        <vs-th>
        </vs-th>

        <vs-th v-if="isDGAA">Is Admin</vs-th>
        <vs-th class="dgaaCheckbox" v-if="isDGAA">Toggle Admin</vs-th>



      </template>

      <template slot-scope="{data}">
        <vs-tr :key="indextr" v-for="(tr, indextr) in data" @click="test()">

          <vs-td :data="data[indextr].id"> {{data[indextr].id}} </vs-td>

          <vs-td :data="data[indextr].firstName">{{data[indextr].firstName}}</vs-td>

          <vs-td :data="data[indextr].firstName">{{data[indextr].lastName}}</vs-td>

          <vs-td :data="data[indextr].city">{{`${data[indextr].city}`}}</vs-td>

          <vs-td :data="data[indextr].country">{{`${data[indextr].country}`}}</vs-td>

          <vs-td :data="data[indextr].email">{{data[indextr].email}}</vs-td>

          <vs-td>
            <div id="goToProfileButton" @click="goToProfile(data[indextr].id)">Go to profile</div>
          </vs-td>

          <vs-td :data="data[indextr].role" v-if="isDGAA"> {{data[indextr].role}} </vs-td>

          <vs-td v-if="isDGAA" class="dgaaCheckbox">
            <input type="checkbox" @click="toggleAdmin(data[indextr])">
          </vs-td>


        </vs-tr>
      </template>
    </vs-table>
  </div>

  </div>
</template>

<script>
import api from "../Api";
import {store} from "../store"

const Search = {
  name: "Search",
  data: function() {
    return {
      selected: [],
      errors: [],
      toggle: [1,1,1,1,1,1,1,1],
      searchbar: "",
      searchbarResults: "",
      users: [],
      filteredUsers: [],
      reducedUsers: [],
      enableTable: false,
      resultTrack: "",
      userSearchIndexMin: 0,
      userSearchIndexMax: 10,
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
    if ( this.getUserRole() === 'DGAA') {
      this.isDGAA = true;
    }
  },


  methods: {
    getUserRole: function () {
      return store.role;
    },

    goToProfile(userId) {
      this.$router.push({path: `/users/${userId}`})
    },
    /**
     * Searches for the users in the database by calling the API function with an SQL query to find the
     * users based on the input in the search box.
     */
    searchUsers: function () {
      if (this.searchbar.length > 0) {
        this.enableTable = true;
        this.resultTrack = this.searchbar;
        console.log(this.searchbar);
        api
            .searchQuery(this.searchbar)
            .then((response) => {
              console.log(response)
              this.$log.debug("Data loaded: ", response.data);
              this.users = response.data;
              this.users = this.users.filter(x => typeof(x) == "object")

              //Need to set properties of user object so they can be sorted by
              for(let user of this.users) {
                user.country = user.homeAddress.country;
                user.city = user.homeAddress.city; 
              }

              console.log(this.users)
              //this.filteredUsers = response.data;
            })
            .catch((error) => {
              this.$log.debug(error);
              this.error = "Failed to load users";
            })
            .finally(() => (this.loading = false));
      } else {
        this.errors.push("Please enter input the user you want to search for");
      }
    },

    /**
     * makes the checkuser an administrator
     * if they are already, revoke privledges...
     */

    toggleAdmin: function (currentUser) {
      if (currentUser.role == 'USER') {
        //currentUser.id = true;
        api.makeUserAdmin(currentUser.id);
        currentUser.role = 'GAA'
        //console.log("admin true"+currentUser.id+currentUser.firstName)
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

#search {
  font-family: 'Ubuntu', sans-serif;
  font-weight: bold;
  height: 10em;
  display: flex;
  justify-content: center;
  align-items: center;
}


#search input {
  font-size: 30px;
}

#goToProfileButton {
  background:#3B5998;
  text-align: center;
  color: white;
  border-radius: 1.5em;
  cursor: pointer;
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

tr {
  font-size: 15px
}

th {
  background: #3B5998;
  color: white;
}

.main1{
  top:-100px;
}

.title {
  padding-top: 40px;
  color: #3B5998;
  font-family: 'Ubuntu', sans-serif;
  font-weight: bold;
  font-size: 23px;
}

.main {
  background-color: white;
  top: 1px;
}


.prevNextSearchButton {
  cursor: pointer;
  border-radius: 5em;
  color: #fff;
  background: linear-gradient(to right, #9C27B0, #E040FB);
  border: 0;
  padding-left: 20px;
  padding-right: 20px;
  padding-bottom: 5px;
  padding-top: 5px;
  font-family: 'Ubuntu', sans-serif;
  margin-top: 10px;
  font-size: 13px;
  box-shadow: 0 0 20px 1px rgba(0, 0, 0, 0.04);
}



.displaying {
  padding-top: 15px;
}


.headingButton {
  cursor: pointer;
  border-radius: 5em;
  color: #fff;
  background: #3B5998;
  border: 0;
  padding-left: 20px;
  padding-right: 20px;
  padding-bottom: 10px;
  padding-top: 10px;
  font-family: 'Ubuntu', sans-serif;
  margin-left: 35%;
  font-size: 13px;
  box-shadow: 0 0 20px 1px rgba(0, 0, 0, 0.04);
}

.profile-text-inner {
  margin: 7em auto;
  width: 90%;
  height: 80%;
}

</style>