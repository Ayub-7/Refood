<template>
  <div id="app" >
    <div class="topbar">
      <table>
        <tr>
          <th>
            <router-link class="title" to="/">Register</router-link>
          </th>
          <th>
            <router-link class="title" to="/login">Login</router-link>
          </th>
          <th>
            <router-link class="title" to="/search">Search</router-link>
<<<<<<< HEAD
          </th>
          <th>
            <router-link :to="{name: 'UserPage', params: {id: this.$store.state.userId}}" v-if="this.$store.state.userId != null" class="title">Profile</router-link>
          </th>
          <th>
            <router-link :to="{path: '/login'}" v-if="this.$store.state.userId != null" class="title">
            <span @click="$store.commit('setUserId', null); $store.commit('setUserRole', null)" class="title">Logout</span>
            </router-link>

=======
>>>>>>> U5-Business-Accounts
          </th>
        </tr>
      </table>
      <h2 class = "dgaa" v-if="this.$store.state.userRole == 'DGAA' || this.$store.state.userRole == 'GAA'"><span>{{this.$store.state.userRole}}</span></h2>
    </div>

    <router-view></router-view>
    <footer class="info">
      <h4>REFOOD 2021</h4>
    </footer>
  </div>
</template>

<script>
import Register from "./components/Register";
import Login from "@/components/Login.vue";
// @click="goToUserPage()"
//import Students from "./components/Students";
// Vue app instance
// it is declared as a reusable component in this case.
// For global instance https://vuejs.org/v2/guide/instance.html
// For comparison: https://stackoverflow.com/questions/48727863/vue-export-default-vs-new-vue
const app = {
  name: "app",
  components: {
    // list your components here to register them (located under 'components' folder)
    // https://vuejs.org/v2/guide/components-registration.html
    Login, Register
  },
  // app initial state
  // https://vuejs.org/v2/guide/instance.html#Data-and-Methods
  data: () => {
    return {};
  },

};

// make the 'app' available
export default app;
</script>

<style scoped>

.dgaa {
  color: rgb(38, 50, 56);
  background: #dbe0dd;
  text-align: center;
  font-size: 23px;
  right: 0px;
  font-weight: 600;
  position: relative;
  border-radius: 20px;
  width: 100px;
  font-family: 'Ubuntu', sans-serif;
}

.topbar {
  padding-bottom: 25px;
  display: flex;
  flex-direction: row;
  padding-top: 20px;
  background: linear-gradient(to right, #9C27B0, #E040FB);
  border: 2px solid rgba(0, 0, 0, 0.02);
}

.title {
  width: 76%;
  color: rgb(38, 50, 56);
  font-weight: 700;
  font-size: 14px;
  letter-spacing: 1px;
  background: #dbe0dd;
  padding: 10px 20px;
  border: none;
  border-radius: 20px;
  outline: none;
  box-sizing: border-box;
  border: 2px solid rgba(0, 0, 0, 0.02);
  margin-bottom: 50px;
  margin-left: 5px;
  margin-right: 20px;
  text-align: center;
  margin-bottom: 27px;
  font-family: 'Ubuntu', sans-serif;
}

[v-cloak] {
  display: none;
}

</style>