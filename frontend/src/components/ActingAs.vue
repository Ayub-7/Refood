<template>
  <div class="userInfo" @focus="showUserBusinesses" @focusout="hideUserBusinesses" tabindex="0">
        <h2 class = "dgaa" v-if="getUserRole() === 'DGAA' || getUserRole() === 'GAA'"><span>{{getUserRole()}}</span></h2>
        <ul class="actInfo" v-if="getActingAsBusinessName() == null">
          <!--v-on:click="showUserBusinesses" -->
          <li class="userStuff">
            <span class ="user">{{getUserName()}}</span>
            <vs-avatar v-if="getUserName() !== null" size="large" style="margin-left: 10px" name="avatar">
              {{getUserName().match(/[A-Z]/g).join('')}}
            </vs-avatar>
          </li>

          <li id="userBusinessPanel">
            <form class="dropdown">
              <h2>Businesses:</h2>
              <ul id="businessList">
                <li v-for="business in getPrimaryBusinesses()" v-bind:href="business.id" :key="business.id" v-on:click="setActingAsBusinessId(business.id)">
                  {{ business.name}}
                </li>
              </ul>
<!--              <label class="label"> Select Business to act as:  </label>-->

<!--              <select class="select" name="acting" placeholder="Business" v-model="buss" @change="setActingAsBusinessId(buss);">-->
<!--                <option value="" disabled selected>Choose business</option>-->
<!--                <option  v-for="business in getPrimaryBusinesses()"-->
<!--                        v-bind:href="business.id"-->
<!--                        :key="business.id" >{{ business.name}}</option>-->
<!--              </select>-->
            </form>
          </li>

        </ul>
      <ul class="actInfo" v-else>
        <li class="business" >
          Logged in as BUSINESS: {{getActingAsBusinessName()}}
        </li>
        <br>
        <li class="user" @click="setActingAsUser()">
          <span class="" style="display: inline;">Act As User: {{ getUserName() }} </span>
        </li>
        </ul>

  </div>
</template>

<script>
import {store, mutations} from "../store";

const actingAs =  {
name: "actingAs",
  data: function () {
    return {
      buss: null,
      loggedInUserId: null,
      userName: null,
      role: null,
      userPrimaryBusinesses: [],
      actingAsBusinessId: null,
      actingAsBusinessName: null
    }
  },
  methods: {
    getUserName() {
      this.userName = store.userName;
      return this.userName;
    },

    getUserRole() {
      this.role = store.role;
      return this.role;
    },

    getPrimaryBusinesses(){
      this.userPrimaryBusinesses = store.userPrimaryBusinesses;
      //console.log(store.userPrimaryBusinesses);
      return store.userPrimaryBusinesses;
    },

    setActingAsBusinessId(businessName){
      const businessId = mutations.getIdByName(businessName);
      mutations.setActingAsBusiness(businessId, businessName)
      this.buss = null;
      this.$router.push({path: `/home`});
    },

    setActingAsUser(){
      mutations.setActingAsUser();
      this.$router.push({path: `/home`});
    },

    getActingAsBusinessName() {
      this.actingAsBusinessName = store.actingAsBusinessName
      return this.actingAsBusinessName;
    },



    showUserBusinesses: function() {
      let x = document.getElementById('userBusinessPanel');
      x.style.display = "block";
      // if (x.style.display === "block") {
      //   console.log("Hello");
      //   window.onload = function(){
      //     document.onclick = function(e){
      //       if(e.target.id !== 'userBusinessPanel'){
      //         //element clicked wasn't the div; hide the div
      //         x.style.display = 'none';
      //       }
      //     };
      //   };
      // }
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
.userInfo {
  display: inline;
}

.actInfo {
  display: inline;
  padding: 0px;
}

li.business {
  display: inline;
}

#userBusinessPanel {
  display: none;
}

.dropdown .select {
  color: #FFFFFF;
  text-align: center;
  font-size: 14px;
  text-decoration: none;
  width: 100%;
  border-radius: 20px;
  padding: 12px 16px;
  border: none;
  background: -webkit-gradient(linear, left top, right top, from(#9C27B0), to(#E040FB));
  background: linear-gradient(to right, #385898, #385898);
  -webkit-box-shadow: 0 0 20px 1px rgb(0 0 0 / 4%);
}

.dropdown .select option {
  color: black;
}

.vs-avatar-content.vs-avatar-content--size.vs-change-color-badge {
  float: right;
  margin-right: 20px;
  margin-top: -9px;
  position: relative;
}

span.user {
  width: 75%;
  float: left;
}

.dropdown {
  margin-top: 30px;
}
</style>