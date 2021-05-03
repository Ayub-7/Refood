<template>
  <div class="userInfo" @focus="showUserBusinesses" @focusout="hideUserBusinesses" tabindex="0">
        <h2 class = "dgaa" v-if="getUserRole() === 'DGAA' || getUserRole() === 'GAA'"><span>{{getUserRole()}}</span></h2>
        <ul class="actInfo" v-if="getActingAsBusinessName() == null">
          <!--v-on:click="showUserBusinesses" -->
          <li class="userStuff">
            <span class ="user">{{getUserName()}}</span>
            <vs-avatar v-if="getUserName() !== null" size="30" style="margin-left: 10px" name="avatar">
            </vs-avatar>
          </li>

          <li id="userBusinessPanel">
              <ul id="businessList">
                <li v-for="business in getPrimaryBusinesses()" v-bind:href="business.id" :key="business.id" v-on:click="setActingAsBusinessId(business.id, business.name)">
                  <span class="user small" style="display: inline; font-size: 12px; padding-top: 5px"> {{ business.name}} </span>
                  <vs-avatar class="v-small" v-if="getUserName() !== null" icon="store" style="transform: translate(0%, -20%) scale(0.7) !important; right: 0px;">
                  </vs-avatar>
                </li>
              </ul>
          </li>

        </ul>
      <ul class="actInfo" v-else>
        <li class="business" >
          <span class ="user">{{getActingAsBusinessName()}}</span>
          <vs-avatar v-if="getUserName() !== null" icon="store" size="30" style="margin-left: 10px" name="avatar">
          </vs-avatar>
        </li>

        <li id="userBusinessPanel" class="user" @click="setActingAsUser()">
          <span class="user small" style="display: inline; font-size: 12px; padding-top: 5px">{{ getUserName() }} </span>
          <vs-avatar class="v-small" v-if="getUserName() !== null" icon="person" style="transform: translate(0%, -20%) scale(0.7) !important;">
          </vs-avatar>
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

    setActingAsBusinessId(businessId, businessName){
      mutations.setActingAsBusiness(businessId, businessName)
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
  margin-bottom: -15px;
  border-top: 1px solid black;
}

/*.dropdown .select {*/
/*  color: #FFFFFF;*/
/*  text-align: center;*/
/*  font-size: 14px;*/
/*  text-decoration: none;*/
/*  width: 100%;*/
/*  border-radius: 20px;*/
/*  padding: 12px 16px;*/
/*  border: none;*/
/*  background: -webkit-gradient(linear, left top, right top, from(#9C27B0), to(#E040FB));*/
/*  background: linear-gradient(to right, #385898, #385898);*/
/*  -webkit-box-shadow: 0 0 20px 1px rgb(0 0 0 / 4%);*/
/*}*/

.dropdown .select option {
  color: black;
}

span.user {
  width: auto;
  float: left;
  font-size: 16px;
  padding: 10px 5px 0px 15px;
}

</style>