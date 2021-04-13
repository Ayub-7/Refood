<template>
  <div class="userInfo">
    <div style="  display: flex;  justify-content: right; text-align: right">
        <h2 class = "dgaa" v-if="this.userRole == 'DGAA' || this.userRole == 'GAA'"><span>{{this.userRole}}</span></h2>
        <div>
          {{this.userName}}
        </div>
        <ul id="businesses">
          <li id = "business" v-for="business in this.userPrimaryBusinesses"
               v-bind:href="business.id"
               :key="business.id">
            <p>{{ business.name }} </p>
          </li>
        </ul>
      <!-- <img src="../profile-pic.jpeg" alt="Profile Pic" style="height: 10%; width: 10%; margin-left: 10px">-->
      <p>profile pic</p>
    </div>
  </div>
</template>

<script>
import {store} from "../store";

const actingAs =  {
name: "actingAs",
  data: function () {
    return {
      userName: null,
      userRole: null,
      userPrimaryBusinesses: [],
      actingAsBusinessId: null,
      actingAsBusinessName: null,
    }
  },
  methods: {

  },
  mounted: function () {
    this.userName = store.userName;
    this.userRole = store.role;
    this.userPrimaryBusinesses = store.userPrimaryBusinesses;
    this.actingAsBusinessId = store.actingAsBusinessId;
    this.actingAsBusinessName = store.actingAsBusinessName;
  }
}
export default actingAs;
</script>

<style scoped>
/*.userInfo {*/
/*  color: white;*/
/*  font-weight: 700;*/
/*  font-size: 14px;*/
/*  letter-spacing: 1px;*/
/*  background: #385898;*/
/*  !*padding: 10px 20px;*!*/
/*  border-radius: 20px;*/
/*  outline: none;*/
/*  box-sizing: border-box;*/
/*  border: 2px solid rgba(0, 0, 0, 0.02);*/
/*  margin-left: 5px;*/
/*  margin-right: 20px;*/
/*  margin-bottom: 27px;*/
/*  font-family: 'Ubuntu', sans-serif;*/
/*  padding-top: 20px;*/
/*  text-align: right*/
/*}*/

/*.dgaa {*/
/*  color: rgb(38, 50, 56);*/
/*  background: #dbe0dd;*/
/*  text-align: center;*/
/*  font-size: 23px;*/
/*  right: 0px;*/
/*  font-weight: 600;*/
/*  position: relative;*/
/*  border-radius: 20px;*/
/*  width: 100px;*/
/*  font-family: 'Ubuntu', sans-serif;*/
/*}*/

.businesses {

}
</style>