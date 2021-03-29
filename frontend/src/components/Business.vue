<template>
  <div id="container">
    <div id="business-header">Cool Business</div>

    <div id="business-info-container">
      <div id="business-type">Accommodation and Food Services</div>
      <div id="created-date">Created 2021-03-29</div>
      <div id="address">
        <div id="street-address">123 Real Street</div>
        <div id="city">Christchurch</div>
        <div id="region">Canterbury</div>
        <div id="country">New New Zealandia</div>
        <div id="postcode">8888</div>
      </div>
      <div id="description">Very long description about this very cool business.</div>
    </div>

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
  grid-template-rows: auto 1fr;

  margin: 1em auto;
  border-radius: 1.5em;
  box-shadow: 0px 11px 35px 2px rgba(0, 0, 0, 0.14);
  background-color: #F3EBF6;
}

#business-header {
  grid-row: 1;
  grid-column:  1 / 3; /* Column span 2 columns */

  color: black;
  font-size: 24px;
  padding: 1em;
}

#business-info-container {
  grid-column: 1;
  grid-row: 2;

  display: grid;
  grid-template-columns: 1fr;
  grid-template-rows: repeat(2, auto) repeat(2, 1fr);
  grid-row-gap: 1em;

  padding-left: 2em;
}

#business-type {
  grid-column: 1;
  grid-row: 1;
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

</style>