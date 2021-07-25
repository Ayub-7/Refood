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

      <vs-button class="card-modal-edit-button" @click="setPrefills()" v-if="editing===false && (userId === selectedCard.user.id || userRole === 'DGAA')">Edit Card</vs-button>
      <!-- Add delete button if user is card owner -->
      <vs-button id="card-modal-delete-button" @click="deleteCard()" v-if="selectedCard.user.id == userId || userRole === 'DGAA'" style="margin-left: 10px;">Delete</vs-button>
      <vs-button class="card-modal-message-button" @click="messaging=true" v-else-if="messaging===false && userId !== selectedCard.user.id">Message</vs-button>
      <vs-button class="card-modal-message-button"  @click="messaging=false; editing=false" v-else>Cancel</vs-button>
    </div>

    <transition name="slide" v-if="showTransition">
    <div id="card-modal-message" v-if="messaging">
      <vs-textarea v-model="message" id="message-input"></vs-textarea>
      <vs-button id="card-modal-message-send" @click="sendMessage(selectedCard.id, message)">Send Message</vs-button>
    </div>
    </transition>

    <!-- EDIT CARD -->
    <transition name="slide" v-if="showTransition">
      <div id="card-modal-edit" v-if="editing">
        <vs-row class="addCardField">
          <vs-col vs-w="2" vs-xs="12" class="addCardHeader">Section <span class="required">*</span></vs-col>
          <vs-select vs-w="10" v-model="section" :danger="editErrors.section.error"
                     :danger-text="editErrors.section.message">
            <vs-select-item v-for="section in availableSections" :key="section.key" :text="section.key" :value="section.value"/>
          </vs-select>
        </vs-row>

        <vs-row class="addCardField">
          <vs-col id="title" vs-w="2" vs-xs="12">
            <div class="addCardHeader" >Title <span class="required">*</span> </div>
          </vs-col>
          <vs-col vs-w="9">
            <vs-textarea :class="[{'textarea-danger': editErrors.title.error}, 'addCardInput', 'title-input']" v-model="title" rows="1" :counter="50" @keydown.enter.prevent></vs-textarea>
            <transition name="fade">
              <div v-show="editErrors.title.error" class="edit-error">{{editErrors.title.message}}</div>
            </transition>
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
import api from "../Api";

export default {
  name: "CardModal",
  props: ['selectedCard'],
  data: function() {
    return {
      showing: false,
      userId: null,
      userRole: null,
      messaging: false,
      message: '',
      editing: false,
      title: '',
      keywordList: [],
      description: '',
      section: '',


      editErrors: {
        title: {error: false, message: "There is a problem with the card title"},
        section: {error: false, message: "You must choose a section"},
      },
      availableSections: [
        {key:'For Sale', value:'ForSale'},
        {key:'Wanted', value:'Wanted'},
        {key:'Exchange', value: 'Exchange'}
      ],
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
          this.section = this.selectedCard.section;
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
          this.getUserId();
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
                this.userRole = response.data.role;
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
        resetState: function() {
          this.message = '';
          this.title = '';
          this.keywords = [];
          this.description = '';
          this.editing = false;
          this.messaging = false;
        },

        /**
         * Saves the the changed input fields of an edited card - provided that the fields are valid.
         * todo: send info to backend.
         */
        saveCardEdit: function() {
          if (this.validateCardEdit()) {
            this.title = this.title.trim(); // Removing any whitespace before and after.
            this.$vs.notify({title: "Success", text: "Card successfully edited.", color:"success"});
          }
          else {
            this.$vs.notify({title: "Error saving changes", text: "Please check the input fields and try again.", color: "danger"});
          }
        },

        /**
         * Validates the input fields of the user's card editing.
         */
        validateCardEdit: function() {
          let valid = true;

          if (this.editErrors.title.error) {
            valid = false;
          }

          if (this.editErrors.section.error) {
            valid = false;
          }

          return valid;
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
    /**
     * Watches the title value for any changes, and checks validity of it.
     */
    title: function() {
      if (this.title.length < 1) {
        this.editErrors.title.error = true;
        this.editErrors.title.message = "A valid card title is required";
      }
      else if (this.title.trim().length === 0) {
        this.editErrors.title.error = true;
        this.editErrors.title.message = "A valid card title is required";
      }
      else if (this.title.length > 50) {
        this.editErrors.title.error = true;
        this.editErrors.title.message = "Card title is too long";
      }
      else {
        this.editErrors.title.error = false;
      }
    },

    /**
     * Makes sure the section doesn't somehow become null.
     */
    section: function() {
      this.editErrors.section.error = this.section == null || this.section === "";
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
  margin-left: 20px;
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
#card-modal-edit {
  margin-top: 8px;
}

.title-input >>> textarea {
  max-height: 33px;
  min-height: 33px;
}

.title-input { /* Is being used. */
  margin-bottom: 0;
}

.edit-error {
  font-size: 10px;
  position: absolute;
  color: #FF4757;
  margin-left: 8px;
}

</style>