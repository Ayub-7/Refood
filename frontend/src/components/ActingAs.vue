<template>
  <div class="userInfo">
        <h2 class = "dgaa" v-if="getUserRole() === 'DGAA' || getUserRole() === 'GAA'"><span>{{getUserRole()}}</span></h2>
        <ul class="actInfo" v-if="getActingAsBusinessName() == null">
          <li class="user" >
            Logged in as {{getUserRole()}} {{getUserName()}}
          </li>
          <li>
            <form class="dropdown">
              <label class="label"> Select Business to act as:  </label>
              <select class="select" name="acting" v-model="buss" @click="setActingAsBusinessId(buss) ; Redirect()">
                <option  v-for="business in getPrimaryBusinesses()"
                        v-bind:href="business.id"
                        :key="business.id" >{{ business.name}}</option>
              </select>
            </form>
          </li>

        </ul>
      <ul class="actInfo" v-else>
        <li class="business" >
          Logged in as BUSINESS: {{getActingAsBusinessName()}}
        </li>
        <li class="user" @click="setActingAsUser()">
          <p>Act As User: {{ getUserName() }} </p>
        </li>
        </ul>
      <!-- <img src="../profile-pic.jpeg" alt="Profile Pic" style="height: 10%; width: 10%; margin-left: 10px">-->
      <p>profile pic</p>
  </div>
</template>

<script>
import {store, mutations} from "../store";

const actingAs =  {
name: "actingAs",
  data: function () {
    return {
      buss:null,
      actingAsBusinessId: null,
      actingAsBusinessName: null,
    }
  },
  methods: {
    getUserName() {
      return store.userName;
    },

    getUserRole() {
      return store.role;
    },

    getPrimaryBusinesses(){
      return store.userPrimaryBusinesses;
    },

    setActingAsBusinessId(businessName){
      //console.log("hi");
      const businessId = mutations.getIdByName(businessName);
      mutations.setActingAsBusiness(businessId, businessName)
    },
    Redirect() {
      this.$router.push({path: `/businesses/${store.actingAsBusinessId}`});
    },

    setActingAsUser(){
      mutations.setActingAsUser();
      this.$router.push({path: `/users/${store.loggedInUserId}`});
    },

    getActingAsBusinessName(){
      return store.actingAsBusinessName;
    }
  },
  // mounted: function () {
  //   this.userName = store.userName;
  //   this.userRole = store.role;
  //   this.userPrimaryBusinesses = store.userPrimaryBusinesses;
  //   this.actingAsBusinessId = store.actingAsBusinessId;
  //   this.actingAsBusinessName = store.actingAsBusinessName;
  // }
}
export default actingAs;
</script>

<style scoped>
.userInfo {
  display: inline;
}

.actInfo {
  display: inline;
}

li.business {
  display: inline;
}
</style>