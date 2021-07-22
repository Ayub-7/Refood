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

      <vs-button class="card-modal-edit-button" @click="setPrefills()" v-if="editing===false && userId === selectedCard.user.id">Edit Card</vs-button>
      <vs-button class="card-modal-message-button" @click="messaging=true" v-else-if="messaging===false && userId !== selectedCard.user.id">Message</vs-button>
      <vs-button class="card-modal-message-button"  @click="messaging=false; editing=false" v-else>Cancel</vs-button>
    </div>

    <transition name="slide" v-if="showTransition">
    <div id="card-modal-message" v-if="messaging">
      <vs-textarea v-model="message" id="message-input"></vs-textarea>
      <vs-button id="card-modal-message-send" @click="sendMessage(selectedCard.id, message)">Send Message</vs-button>
    </div>
    </transition>

    <transition name="slide" v-if="showTransition">
      <div id="card-modal-edit" v-if="editing">
        <vs-row class="addCardField">
          <vs-col id="title" vs-w="2" vs-xs="12">
            <div class="addCardHeader" >Title <span class="required">*</span> </div>
          </vs-col>
          <vs-col vs-w="9">
            <vs-textarea :class="[{'textarea-danger': editErrors.title.error}, 'addCardInput', 'title-input']" v-model="title" rows="1" :counter="50" ></vs-textarea>
            <div v-show="editErrors.title.error" class="edit-error">{{editErrors.title.message}}</div>
          </vs-col>
        </vs-row>
        <vs-row class="addCardField">
          <div class="addCardHeader">Description</div>
          <vs-textarea v-model="description" id="description"></vs-textarea>
        </vs-row>
        <vs-row class="addCardField">
          <vs-col vs-w="2" vs-xs="12">
            <div class="addCardHeader">Keywords</div>
          </vs-col>
          <vs-col vs-w="9">
            <vs-chips color="rgb(145, 32, 159)" placeholder="New Keyword" v-model="keywordList">
              <vs-chip v-for="keyword in keywordList" v-bind:key="keyword.value" @click="remove(keyword)"
                       closable>{{keyword}}
              </vs-chip>
            </vs-chips>
          </vs-col>
        </vs-row>
        <vs-button color="success" id="card-modal-edit-save" @click="saveCardEdit">Save</vs-button>
      </div>
    </transition>

  </vs-popup>
</template>

<script>
import api from "../Api.js";

export default {
  name: "CardModal",
  props: ['selectedCard'],
  data: function() {
    return {
      showing: false,
      messaging: false,
      message: '',
      editing: false,
      title: '',
      keywordList: [],
      description: '',

      userId: -1,

      editErrors: {
        title: {error: false, message: "There is a problem with the card title."},
      },
    }
  },
  methods:
      {
        /**
         * removes keyword
         */
        remove(item) {
          this.keywordList.splice(this.keywordList.indexOf(item), 1)
        },
        /**
         * sets prefilled entries for edit card modal
         */
        setPrefills: function() {
          this.editing = true;
          this.title = this.selectedCard.title;
          this.description = this.selectedCard.description;
          if (this.selectedCard.keywords === '') {
            this.keywordList = [];
          } else {
            this.keywordList = this.selectedCard.keywords.match(/.*?[\s]+?/g);
          }
        },
        /**
         * Opens modal by setting showing to true (linked to :active-sync), also resets form data before opening
         */
        openModal: function() {
          this.resetState();
          this.showing = true;
          this.getCurrentUserId();
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
         * Retrieves and sets the userId to the current user.
         * Used to determine if the owner of the card is the current user.
         */
        getCurrentUserId: function() {
          api.checkSession()
            .then((res) => {
              this.userId = res.data.id;
            })
            .catch((error) => {
              this.$log.debug(error);
            });
        },

        /**
         * Resets state of messaging information
         */
        resetState() {
          this.message = '';
          this.title = '';
          this.keywords = [];
          this.description = '';
          this.editing = false;
          this.messaging = false;
        },

        saveCardEdit() {
          this.validateCardEdit();
          console.log("yep");
        },

        validateCardEdit() {
          Object.values(this.editErrors).forEach(input => input.error = false);

          if (this.title.length < 1) {
            this.editErrors.title.error = true;
            this.editErrors.title.message = "A card title is required.";
          }
          else if (this.title.length > 50) {
            this.editErrors.title.error = true;
            this.editErrors.title.message = "Card title is too long.";
          }
        }
      },

  computed: {
    /**
     * Weird computed property to stop closing transition from happening when opening modal
     */
    showTransition: function() {
      return this.showing || !this.messaging;
    }
  },

  watch: {
    title: function() {
      if (this.title.length < 1) {
        this.editErrors.title.error = true;
        this.editErrors.title.message = "A card title is required.";
      }
      else if (this.title.length > 50) {
        this.editErrors.title.error = true;
        this.editErrors.title.message = "Card title is too long.";
      }
      else {
        this.editErrors.title.error = false;
      }
    }
  }

}
</script>

<style scoped>

#cardModal >>> .con-vs-popup {
  z-index: 10000;
}

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

.card-modal-message-button, .card-modal-edit-button {
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

/* === EDIT CARD === */
.title-input >>> textarea {
  max-height: 33px;
  min-height: 33px;
}

.title-input {
  margin-bottom: 0;
}

.edit-error {
  font-size: 10px;
  position: absolute;
  color: #FF4757;
  margin-left: 8px;
}

</style>