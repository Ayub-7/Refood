<template>
  <ul id="administrators-list">
    <admin-card/>
  </ul>
</template>

<script>
import Vue from "vue";

Vue.component('admin-card', {
  template: '<li class="card"><div class="admin-name">Elon Musk</div></li>'
})

export default {
  name: "BusinessAdministrators"
}
</script>

<style scoped>

#administrators-list {
  padding: 1em;
}

/* Card CSS */
.card {
  padding: 10px;
  border-radius: 5px;
  box-shadow: 0 3px 6px rgba(0,0,0,0.16), 0 3px 6px rgba(0,0,0,0.23);;
  background: rgba(255, 255, 255, .8);
}


</style>