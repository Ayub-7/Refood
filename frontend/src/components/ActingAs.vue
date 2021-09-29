<template>
  <div class="userInfo">
    <h2 class = "dgaa" v-if="getUserRole() === 'DGAA' || getUserRole() === 'GAA'"><span>{{getUserRole()}}</span></h2>

    <div>
      <vs-dropdown vs-trigger-click>
        <div v-if="getActingAsBusinessName() == null" class="acting-display">
          <span class="user">{{getUserName()}}</span>
          <vs-avatar v-if="getUserName() !== null" size="30" name="avatar"></vs-avatar>
        </div>
        <div v-else class="acting-display">
          <span class="user">{{getActingAsBusinessName()}}</span>
          <vs-avatar v-if="getUserName() !== null" icon="store" size="30" name="avatar">
          </vs-avatar>
        </div>

        <vs-dropdown-menu class="user-menu">
          <vs-dropdown-item class="dropdown-item" @click="setActingAsUser()" v-if="getActingAsBusinessName()">
            <div class="dropdown-item-name" >{{ getUserName() }} </div>
            <vs-avatar class="dropdown-item-avatar" v-if="getUserName() !== null" size="small">
            </vs-avatar>
          </vs-dropdown-item>

          <vs-dropdown-group vs-label="Businesses" id="businessList">
            <vs-dropdown-item class="dropdown-item" v-for="business in getBusinesses()" :key="business.id" v-on:click="setActingAsBusinessId(business.id, business.name)">
              <div class="dropdown-item-name">{{business.name}} <span v-if="business.primaryAdministratorId === loggedInUserId">(P)</span></div>
              <ReImage v-if="business.primaryThumbnailPath !== null" :imagePath="business.primaryThumbnailPath" :isThumbnail="true" style="border: 1px solid #ddd; border-radius: 50%; padding: 2px;"></ReImage>
              <vs-avatar v-else class="dropdown-item-avatar" icon="store" size="small"></vs-avatar>
            </vs-dropdown-item>
          </vs-dropdown-group>
        </vs-dropdown-menu>
      </vs-dropdown>
    </div>
  </div>
</template>

<script>
import {store, mutations} from "../store";
import api from "../Api";
import ReImage from "./ReImage";

const actingAs =  {
  name: "actingAs",
  components: {ReImage},
  data: function () {
    return {
      loggedInUserId: null,
      userName: null,
      role: null,
      userBusinesses: [],
      actingAsBusinessId: null,
      actingAsBusinessName: null,
    }
  },
  methods: {
    /**
     * Retreive the current userName and loggedInUserId of acting as account
     * @returns the acting name
     */
    getUserName() {
      this.userName = store.userName;
      this.loggedInUserId = store.loggedInUserId;
      return this.userName;
    },

    /**
     * Retreives the acting account
     * @returns role of the acting account
     */
    getUserRole() {
      this.role = store.role;
      return this.role;
    },

    /**
     * Retrieve the businesses administrated by currnet acting as account, or all business less the current business
     * if currently acting as a business account
     * @returns filtered list of businesses
     */
    getBusinesses(){
      this.userBusinesses = store.userBusinesses;
      let filteredBusinesses = [];
      for (let business of this.userBusinesses) {
        if (business.id !== store.actingAsBusinessId) {
          filteredBusinesses.push(business);
        }
      }
      return filteredBusinesses;
    },

    /**
     * Set the acting as business id and name and store it in store.js
     * @param businessId id of acting as business account
     * @param businessName name of acting as business account
     */
    setActingAsBusinessId(businessId, businessName){
      api.actAsBusiness(businessId)
          .then(() => {
            this.refreshCachedItems();
            mutations.setActingAsBusiness(businessId, businessName)
            this.$router.push({path: `/home`}).catch(() => {console.log("NavigationDuplicated Warning: same route.")});
          }).catch((error) => {
        if(error.response) {
          this.$log.debug("Error Status:", error.response.status, ":", error.response.message)

        }
        this.$log.debug("Error Status:", error)
      });

      // Prominent vue-router contributor suggests to catch error and do nothing with it.
      // @see https://github.com/vuejs/vue-router/issues/2872
    },

    /**
     * Set the acting as to the logge in user, and update store.js file
     */
    setActingAsUser(){
      api.actAsBusiness(0)
          .then(() => {
            this.refreshCachedItems();
            mutations.setActingAsUser();
            this.$router.push({path: `/home`}).catch(() => {console.log("NavigationDuplicated Warning: same route.")});
          }).catch((error) => {
        if(error.response) {
          this.$log.debug("Error Status:", error.response.status, ":", error.response.message)
        }

        this.$log.debug("Error Status:", error)
      });
    },

    /**
     * Empty the business cache, when switching acting as to user account
     */
    refreshCachedItems() {
      if (sessionStorage.getItem('businessesCache') !== null) {
        sessionStorage.removeItem("businessesCache");
      }
    },

    /**
     * Retrieve the acting as business name from store
     * @returns {String} name of the acting business
     */
    getActingAsBusinessName() {
      this.actingAsBusinessName = store.actingAsBusinessName
      return this.actingAsBusinessName;
    },

    /**
     * Display the businesses administrated by the user
     */
    showUserBusinesses: function() {
      let x = document.getElementById('userBusinessPanel');
      x.style.display = "block";
    },

    /**
     * Hide the businesses administrated by the user
     */
    hideUserBusinesses: function() {
      let x = document.getElementById('userBusinessPanel');
      x.style.display = "none";
    }
  },
}
export default actingAs;
</script>

<style scoped>

.userInfo >>> .vs-con-dropdown {
  cursor: pointer;
}

.acting-display {
  display: flex;
  min-width: 150px;
  cursor: pointer;
}

span.user {
  font-size: 16px;
  margin: auto 0 auto auto;
  text-align: right;
}

.dropdown-item >>> a {
  display: flex;
  text-align: center;
}

.dropdown-item-name {
  margin: auto;
  min-width: 100px;
}

</style>
