<template>

  <div id="container">
    <!-- Left Side Business Information Panel -->
    <div id="business-container">
      <div id="business-name">Cool Business</div>

      <div id="info-container">
        <div id="business-type">Accommodation and Food Services</div>
        <div id="created-date">Created 2021-03-29</div>
        <div id="address">
          <div id="street-address">123 Real Street</div>
          <div id="city">Christchurch</div>
          <div id="region">Canterbury</div>
          <div id="country">New New Zealandia</div>
          <div id="postcode">8888</div>
        </div>
        <div id="description">Very long description about this very cool business. Very long description about this very cool business. Very long description about this very cool business.</div>
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
export default {
  name: "Business"
}
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