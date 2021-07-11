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
      <vs-button id="card-modal-message-button">Message</vs-button>
    </div>

  </vs-popup>
</template>

<script>
export default {
  name: "CardModal",
  props: ['selectedCard'],
  data: function() {
    return {
      showing: false,
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
        }

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