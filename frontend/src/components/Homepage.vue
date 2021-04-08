<template>
  <!-- -->
  <div id="body" v-if="this.$store.state.userId != null">
      <div id="welcomeHeader">
       <h1 id="pageTitle"> Welcome, {{this.userFirstName}}! </h1>
       <h1 id="profileLink" @click='goToProfilePage()' style='cursor: pointer'>Profile Page</h1>
      </div>
  </div>
</template>

<script>
import api from "../Api";
const Homepage = {
    name: "Homepage",
    data: function () {
        return {
            userFirstName: null
        }
    },

    methods: {
      getUserDetails: function(userId) {
        api.getUserFromID(userId)
          .then((response) => {
            this.userFirstName = `${response.data.firstName}`
          }).catch((err) => {
            throw new Error(`Error trying to get user info from id: ${err}`)
          })
      },

      goToProfilePage: function() {
          this.$router.push({path: `/users/${this.$store.state.userId}`});
      }
    },

    mounted: function () {
        let userId = this.$store.state.userId
        this.getUserDetails(userId);
    },
  
}
export default Homepage;
</script>

<style scoped>
#body {
  display: grid;
  /* background-color: #F3EBF6; */
  font-family: 'Ubuntu', sans-serif;
  padding: 3em;
}

#welcomeHeader {
  display: grid;
  grid-template-columns: 80% 1fr;
  text-align: center;
  background-color: #F3EBF6;
}

#pageTitle {
  border-radius: 10px;
  background-color: #F3EBF6;
  font-size: 60px

}

#profileLink {
  border-radius: 10px;
  background-color: #f3e3f9;
  margin-left: 5px;
  font-size: 30px;
  padding-top: 5%;
  box-shadow: 0px 3px 8px#cfcfcf;
}







a {
  text-shadow: 0px 0px 3px rgba(117, 117, 117, 0.12);
  color: #E1BEE7;
  text-decoration: none
}
@media (max-width: 600px) {
  .main {
    border-radius: 0px;
  }
}
</style>