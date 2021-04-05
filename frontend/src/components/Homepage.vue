<template>
  <!-- -->
  <div id="body" v-if="this.$store.state.userId != null">
  <div class="main">
      <h1 id="pagetitle"> Home </h1>
      <!-- Example Content class -->
      <div class="content">
          <!-- Example post or story on home page -->
          <h2 class="contentHeader">Header</h2>
          <p class="contentBody">Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris sit amet sem magna. Nullam vestibulum condimentum laoreet. Integer et velit semper, blandit tellus ut, viverra ipsum. Aenean egestas dictum maximus. Curabitur egestas neque ex, eget laoreet ex blandit vel. Nullam ut augue nec odio hendrerit luctus accumsan in erat. Donec viverra congue laoreet. Mauris non eleifend ex. Pellentesque scelerisque sodales augue in sodales. Aenean sed tellus a enim mollis mollis. Nunc lobortis eros sit amet ligula mollis placerat.</p>
      </div>
  </div>
  </div>
</template>

<script>
//import api from "../Api";
const Homepage = {
  name: "Homepage",
  data: function () {
    return {}
  },

  methods: {}
  
}
export default Homepage;
</script>

<style scoped>
#body {
  background-color: #F3EBF6;
  font-family: 'Ubuntu', sans-serif;
}

#pagetitle {
  text-align: center;
  background-color: #E1BEE7;
  border-radius: 0.5em;
}

.content {
    text-align: center;
    padding-top: 3em;

}

.contentHeader {
    background: #E1BEE7;
    margin-left: 100px;
    margin-right: 100px;
    border-radius: 0.5em;
}

.contentBody {
    font-size: 15px;
    margin-left: 2em;
    margin-right: 2em;
}


.main {
  background-color: #FFFFFF;
  width: 1000px;
  height: 1000px;
  margin: 7em auto;
  border-radius: 1.5em;
  box-shadow: 0px 11px 35px 2px rgba(0, 0, 0, 0.14);
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