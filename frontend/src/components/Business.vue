<template>

  <div id="container" v-if="this.business != null">
    <!-- Left Side Business Information Panel -->
    <div id="business-container">
      <div id="business-name"  >{{ business.name }}</div>

      <div id="info-container">
        <div id="business-type">{{ business.businessType }}</div>
        <div id="created-date">Created {{ business.created }}</div>
        <div id="address">
          <div id="street-address">{{ business.address }}</div> <!-- Change this soon when address is changed. -->
          <div id="city">Placeholder City</div>
          <div id="region">Placeholder Region</div>
          <div id="country">Placeholder Country</div>
          <div id="postcode">8888</div>
        </div>
        <div id="description">{{ business.description }}</div>
      </div>
    </div>

    <main>
      <!-- Sub Navigation Bar -->
      <nav id="business-navbar">
        <router-link class="business-nav-item" to="/business">Products</router-link>
        <router-link class="business-nav-item" to="/business/administrators">Administrators</router-link>
      </nav>

      <div id="content">
        <router-view></router-view>
      </div>

    </main>
  </div>

</template>


<script>
import api from "@/Api";

const Business = {
  name: "Business",

  // App's initial state.
  data: function() {
    return {
      business: null
    };
  },

  // Executes before component creation.
  mounted() {
    api.getBusinessFromId(this.$route.params.id)
      .then((res) => {
        this.business = res.data;
        console.log(this.business);
      })
  }

}

export default Business
</script>


<style scoped>

#container {
  display: grid;
  grid-template-columns: 25% 1fr;
  grid-template-rows: auto auto;
}

/* Business Info Panel on left side */
#business-container {
  margin: 1em auto;
  border-radius: 1.5em;
  box-shadow: 0px 11px 35px 2px rgba(0, 0, 0, 0.14);
  background-color: #F3EBF6;
}

#business-name {
  grid-row: 1;
  grid-column: 1;

  color: black;
  font-size: 26px;
  padding: 1em;
}

#info-container {
  grid-column: 1;
  grid-row: 2;

  display: grid;
  grid-template-columns: 1fr;
  grid-template-rows: repeat(3, auto) repeat(1, 1fr);
  grid-row-gap: 2em;

  padding-left: 2em;
  padding-bottom: 2em;
  padding-right: 2em;

}

#business-type #created-date #address {

}

#business-type {
  grid-column: 1;
  grid-row: 1;
  font-size: 18px;
}

#created-date {
  grid-column: 1;
  grid-row: 2;
}

#address {
  grid-row: 3;
  height: fit-content;
}

#description {
  grid-row: 4;

}

#business-navbar {
  grid-column: 2;
  grid-row: 1;

  font-size: 18px;

  padding-top: 1em;
  padding-bottom: 1em;

  box-shadow: 0 0 35px 0 rgba(0, 0, 0, 0.14);
  border-radius: 1em;
  border: 2px solid rgba(0, 0, 0, 0.02);

}

/* Right Hand Content Side. */

.business-nav-item {
  text-align: center;
  width: 76%;
  color: rgb(38, 50, 56);
  font-weight: 700;
  font-size: 14px;
  letter-spacing: 1px;

  background-color: #dbe0dd;
  padding: 10px 20px;
  border-radius: 20px;
  border: 2px solid rgba(0, 0, 0, 0.02);
  margin: auto 20px 27px 5px;
}

main {
  margin: 1em 0 1em 1em;
  border-radius: 1.5em;
  box-shadow: 0px 11px 35px 2px rgba(0, 0, 0, 0.14);
  background-color: #F3EBF6;
}

</style>