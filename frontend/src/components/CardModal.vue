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
      <vs-button class="card-modal-message-button" @click="messaging=true" v-if="messaging==false">Message</vs-button>
      <vs-button class="card-modal-message-button" @click="messaging=false" v-else>Cancel</vs-button>
    </div>

    <transition name="slide" v-if="showTransition">
    <div id="card-modal-message" v-if="messaging">
      <vs-textarea v-model="message" id="message-input"></vs-textarea>
      <vs-button id="card-modal-message-send" @click="sendMessage(selectedCard.id, message)">Send Message</vs-button>
    </div>
    </transition>


  </vs-popup>
</template>

<script>
export default {
  name: "CardModal",
  props: ['selectedCard'],
  data: function() {
    return {
      showing: false,
      messaging: false,
      message: ''
    }
  },
  methods:
      {
        /**
         * Opens modal by setting showing to true (linked to :active-sync), also resets form data before opening
         */
        openModal: function() {
          this.resetState();
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
         * Sends user message by calling POST messages
         * TODO: to be implemented
         * @param cardId ID of card whose owner the user is going to message
         */
        sendMessage(cardId, message) {
          console.log("Implement Me", cardId, message);
        },


        /**
         * Resets state of messaging information
         */
        resetState() {
          this.message = '';
          this.messaging = false;
        }

      },

  computed: {
    /**
     * Weird computed property to stop closing transition from happening when opening modal
     */
    showTransition: function() {
      return this.showing || !this.messaging;
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

.card-modal-message-button {
  flex: 0%;
}

#card-modal-message {
  margin-top: 10px;
}

#card-modal-message >>> textarea {
  max-height: 100px;
  min-height: 100px;
}

#card-modal-message-send {
  float: right;
}



/* Taken from https://codepen.io/kdydesign/pen/VrQZqx */
.slide-enter-active {
   -moz-transition-duration: 0.3s;
   -webkit-transition-duration: 0.3s;
   -o-transition-duration: 0.3s;
   transition-duration: 0.3s;
   -moz-transition-timing-function: ease-in;
   -webkit-transition-timing-function: ease-in;
   -o-transition-timing-function: ease-in;
   transition-timing-function: ease-in;
}

.slide-leave-active {
   -moz-transition-duration: 0.3s;
   -webkit-transition-duration: 0.3s;
   -o-transition-duration: 0.3s;
   transition-duration: 0.3s;
   -moz-transition-timing-function: cubic-bezier(0, 1, 0.5, 1);
   -webkit-transition-timing-function: cubic-bezier(0, 1, 0.5, 1);
   -o-transition-timing-function: cubic-bezier(0, 1, 0.5, 1);
   transition-timing-function: cubic-bezier(0, 1, 0.5, 1);
}

.slide-enter-to, .slide-leave {
   max-height: 250px;
   overflow: hidden;
}

.slide-enter, .slide-leave-to {
   overflow: hidden;
   max-height: 0;
}
</style>