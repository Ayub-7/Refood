<template>
  <ul id="administrators-list">
    <admin-card/>
    <admin-card/>
    <admin-card/>
  </ul>
</template>

<script>
import Vue from "vue";

Vue.component('admin-card', {
  template: `<li class="card">
                <div class="admin-name">Elon Musk</div>
              </li>`
});

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
  max-width: 40%;

  background-color: #e7e7e7;
  padding: 10px 20px;
  border-radius: 20px;
  border: 2px solid rgba(0, 0, 0, 0.02);
  margin: auto 20px 27px 5px;
}

</style>