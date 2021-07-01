<template>
  <ul id="administrators-list">
    <!-- Admin Card -->
    <li class="card" v-for="user in admins" :key="user.id" v-bind:user="user" @click="navigateToUser(user.id)">
      <div class="admin-name">
        <strong v-if="primaryAdminId === user.id"> {{user.firstName}} {{user.middleName}} {{user.lastName}} </strong>
        <div v-else> {{user.firstName}} {{user.middleName}} {{user.lastName}} </div>
      </div>
      <vs-icon icon="close" class="removeAdminButton" v-if="isPrimaryAdmin()" v-on:click.stop="removeUserAsAdmin(user)"></vs-icon>
    </li>
  </ul>
</template>

<script>
import api from "../Api";
import {store} from "../store"

const BusinessAdministrators = {
  name: "BusinessAdministrators",

  props: {
    admins: Array,
    pAdminId: Number,
  },

  data: function() {
    return {
      primaryAdminId: this.pAdminId,
    }
  },

  methods: {
    /**
     * Remove selected admin user from business.
     * @param user
     */
    removeUserAsAdmin: function (user) {
      api.removeUserAsBusinessAdmin(this.$route.params.id, user.id)
          .then(() => {
            this.admins = this.admins.filter((admin) => admin.id !== user.id);
            this.$vs.notify({
              title: `Successfully removed user`,
              text: `${user.firstName} was removed as an administrator.`,
              color: 'success'
            });
          })
          .catch((error) => {
            if (error.response.status === 400) {
              this.$vs.notify({
                title: `Failed to remove user`,
                text: `${user.firstName} is the primary administrator.`,
                color: 'danger'
              });
            }
            throw new Error(`ERROR trying to remove user as admin: ${error}`);
          });
    },

    /**
     * Navigate page to the user that was clicked on.
     */
    navigateToUser: function (id) {
      this.$router.push({path: `/users/${id}`})
    },

    /**
     * Checks if the current user is also the primary administrator of this business.
     * @returns {boolean} true if the user is the primary admin, else false.
     */
    isPrimaryAdmin: function () {
      return store.loggedInUserId === this.primaryAdminId;
    },
  },
}

export default BusinessAdministrators;
</script>

<style scoped>

#administrators-list {
  padding: 1em;
  display: flex;
  flex-wrap: wrap;

}

/* Card CSS */
.card {
  min-width: 150px;

  display: inline-flex;
  flex-direction: row;
  cursor: pointer;

  background-color: transparent;
  padding: 10px 20px;
  border-radius: 4px;
  border: 2px solid rgba(0, 0, 0, 0.02);
  margin: 0.5em 1em;
  box-shadow: 0 .5rem 1rem rgba(0,0,0,.15);
}

.card:hover {
  box-shadow: 0 0.5em 1em rgba(0,1,1,.25);
}

.admin-name {
  align-content: center;
  padding: 0 0.5em;
}

.removeAdminButton {
  margin: auto;
  opacity: 0.3;
  padding: 0 4px;
}

.removeAdminButton:hover {
  opacity: 1;
}

</style>