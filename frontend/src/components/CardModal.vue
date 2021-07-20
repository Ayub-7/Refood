<template>
  <vs-popup id="card-modal" :title="selectedCard.title" :active.sync="showing">
    <div id="card-modal-name"><vs-icon icon="face" class="inline"></vs-icon>
      <p class="inline text">{{selectedCard.user.firstName}} {{selectedCard.user.lastName}}</p>
    </div>
    <div id="card-modal-location"><vs-icon icon="location_on" class="inline"></vs-icon>
      <p class="inline text">{{selectedCard.user.homeAddress.suburb}} {{selectedCard.user.homeAddress.city}} {{selectedCard.user.homeAddress.country}}</p>
    </div>
    <div id="card-modal-closing"><vs-icon icon="event"></vs-icon>
      <p class="inline text">Closes on {{toStringDate(selectedCard.displayPeriodEnd)}}</p>
    </div>
    <div id="card-modal-description">{{selectedCard.description}}</div>
    <div id="card-modal-keywords" v-for="keyword in selectedCard.keywords.split(' ')" :key="keyword" >#{{keyword}}</div>
    <vs-divider></vs-divider>
    <div id="card-modal-bottom">
      <div id="card-modal-listed">Listed: {{toStringDate(selectedCard.created)}}</div>
      <!-- Add delete button if user is card owner -->
      <div v-if="selectedCard.user.id == userId">
        <vs-button id="card-modal-message-button" @click="deleteCard()">Delete</vs-button>
      </div>
      <vs-button id="card-modal-message-button">Message</vs-button>
    </div>

  </vs-popup>
</template>

<script>
import api from "../Api";

export default {
  name: "CardModal",
  props: ['selectedCard'],
  data: function() {
    return {
      showing: false,
      userId: null
    }
  },
  methods:
      {
        /**
         * Opens modal by setting showing to true (linked to :active-sync), also resets form data before opening
         */
        openModal: function() {
          this.showing = true;
        },
        /**
         * Converts seconds to date
         */
        toStringDate: function (timestamp) {
          const dateFull = new Date(timestamp);
          return dateFull.toDateString();
        },

        /**
         * Obtain the current logged in user's ID
         */
        getUserId: function() {
          api.checkSession()
              .then((response) => {
                this.userId = response.data.id;
              })
              .catch((error) => {
                this.$log.error("Error checking sessions: " + error);
                this.$vs.notify({title:'Error', text:'ERROR trying to obtain user info from session:', color:'danger'});
              });
        },

        /**
         * Preconditions: Must be logged in
         * Postconditions: The card will be deleted
         * Allows the user to delete a card
         */
        deleteCard: function() {
          api.deleteCard(this.selectedCard.id)
          .then(() => {
            this.$emit('deleted');
            this.showing = false;
            this.$vs.notify({title:'Success', text:'Card deleted', color:'success'});
          }).catch((error) => {
            this.$log.error("Error deleting card: " + error);
            this.$vs.notify({title:'Error', text:'ERROR deleting card', color:'danger'});
          });
        }
      },
  mounted: function(){
    this.getUserId();
  }
}
</script>

<style scoped>

.inline {
  display: inline-block;
  position: relative;
}

.text {
  top: -5px;
  left: 5px;
  font-size: large;
}

#card-modal-keywords {
  color: #1F74FF;
  font-size: 15px;
  padding: 2px;
  float: left;
}

#card-modal-bottom {
  display: flex;
  margin-left: 20px;
  flex-wrap: wrap;
}

#card-modal-listed {
  position: relative;
  flex: 50%;
  font-size: large;
  top: 7px;
}

#card-modal-message-button {
  flex: 0%;
}

</style>