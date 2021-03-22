<template>
  <div id="app" >
    <div class="topbar">
      <table>
        <tr>
          <th>
            <router-link class="title" to="/">Register</router-link>
          </th>
          <th>
            <router-link class="title" to="/Login">Login</router-link>
          </th>
          <th>
            <router-link class="title" to="/Search">Search</router-link>
          </th>
        </tr>
      </table>
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

.topbar {
  padding-bottom: 20px;
  padding-top: 20px;
  background: linear-gradient(to right, #9C27B0, #E040FB);
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