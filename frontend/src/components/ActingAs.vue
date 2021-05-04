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

              <vs-dropdown-menu>
                <vs-dropdown-item class="dropdown-item" @click="setActingAsUser()" v-if="getActingAsBusinessName()">
                  <div class="dropdown-item-name" >{{ getUserName() }} </div>
                  <vs-avatar class="dropdown-item-avatar" v-if="getUserName() !== null" size="small">
                  </vs-avatar>
                </vs-dropdown-item>

                <vs-dropdown-group vs-label="Businesses" id="businessList">
                  <vs-dropdown-item class="dropdown-item" v-for="business in getBusinesses()" :key="business.id" v-on:click="setActingAsBusinessId(business.id, business.name)">
                    <div class="dropdown-item-name">{{business.name}} <span v-if="business.primaryAdministratorId === loggedInUserId">(P)</span></div>
                    <vs-avatar class="dropdown-item-avatar" icon="store" size="small"></vs-avatar>
                  </vs-dropdown-item>
                </vs-dropdown-group>
              </vs-dropdown-menu>
            </vs-dropdown>
        </div>

  </div>
</template>

<script>
import {store, mutations} from "../store";

const actingAs =  {
name: "actingAs",
  data: function () {
    return {
      loggedInUserId: null,
      userName: null,
      role: null,
      userBusinesses: [],
      actingAsBusinessId: null,
      actingAsBusinessName: null
    }
  },
  methods: {
    getUserName() {
      this.userName = store.userName;
      this.loggedInUserId = store.loggedInUserId;
      return this.userName;
    },

    getUserRole() {
      this.role = store.role;
      return this.role;
    },

    getBusinesses(){
      this.userBusinesses = store.userBusinesses;
      return store.userBusinesses;
    },

    setActingAsBusinessId(businessId, businessName){
      mutations.setActingAsBusiness(businessId, businessName)
      this.$router.push({path: `/home`}).catch(() => {console.log("NavigationDuplicated Warning: same route.")});
      // Prominent vue-router contributor suggests to catch error and do nothing with it.
      // @see https://github.com/vuejs/vue-router/issues/2872
    },

    setActingAsUser(){
      mutations.setActingAsUser();
      this.$router.push({path: `/home`}).catch(() => {console.log("NavigationDuplicated Warning: same route.")});
    },

    getActingAsBusinessName() {
      this.actingAsBusinessName = store.actingAsBusinessName
      return this.actingAsBusinessName;
    },

    showUserBusinesses: function() {
      let x = document.getElementById('userBusinessPanel');
      x.style.display = "block";
    },

    hideUserBusinesses: function() {
      let x = document.getElementById('userBusinessPanel');
      x.style.display = "none";
    }
  },
}
export default actingAs;
</script>

<style scoped>

.acting-display {
  display: flex;
  min-width: 150px;
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