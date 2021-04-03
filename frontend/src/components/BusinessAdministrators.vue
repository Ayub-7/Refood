<template>
  <ul id="administrators-list">
    <admin-card v-for="user in admins" :key="user.id" v-bind:user="user"/>
  </ul>
</template>

<script>
import Vue from "vue";

// Administrator's Card
Vue.component('admin-card', {
  template: `<li class="card">
                <button class="admin-name" @click="navigateToUser(user.id)">{{user.firstName}} {{user.middleName}} {{user.lastName}}</button>
              </li>`,

  props: ['user'],

  methods: {
    navigateToUser: function(id) {
      this.$router.push({path: `/users/${id}`})
    }

  }
});

const BusinessAdministrators = {
  name: "BusinessAdministrators",

  data: function() {
    return {
      admins: null
    }
  },

  mounted() {
    this.admins = this.$parent.adminList;
  },

}

export default BusinessAdministrators;
</script>

<style scoped>

#administrators-list {
  padding: 1em;
}

/* Card CSS */
.card {
  max-width: 40%;

  background-color: #F5F5F5;
  padding: 10px 20px;
  border-radius: 20px;
  border: 2px solid rgba(0, 0, 0, 0.02);
  margin: auto 20px 27px 5px;
  box-shadow: 0 .5rem 1rem rgba(0,0,0,.15);
}

.admin-name {
  align-content: start;
}

</style>