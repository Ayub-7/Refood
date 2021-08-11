<template>
  <div id="body">
    <vs-popup title="Message" :active.sync="detailedView">
      <div v-if="currentMessage != null" id="message-detail-modal">
        <div class="message-detail-container">
          <vs-icon icon="face"></vs-icon>
          <div class="message-detail-header">
            {{users[currentMessage.sender.id || currentMessage.sender].firstName}} {{users[currentMessage.sender.id || currentMessage.sender].lastName}}
          </div>
        </div>

        <div class="message-detail-container">
          <vs-icon icon="store"></vs-icon>
          <div class="message-detail-header">
            {{currentMessage.card.title}}
          </div>
        </div>

        <div class="message-detail-container message">
        <vs-icon icon="question_answer" class="msg-icon"></vs-icon>
        <div id="message-detail-message">
          {{currentMessage.description}}
        </div>
        </div>

        <vs-divider></vs-divider>
        <div id="card-modal-bottom">
          <div class="message-detail-delivered">
            <vs-icon icon="send"></vs-icon>
            <div id="message-detail-sent">
              {{currentMessage.sent}}
            </div>
          </div>
        <vs-button id="reply-btn" class="card-modal-message-button" v-if="messaging===false" @click="messaging=true">Reply</vs-button>
        <vs-button class="card-modal-message-button"  @click="messaging=false; message = ''; errors=[];" v-else>Cancel</vs-button>
        </div>
        <transition name="slide" v-if="showTransition">
          <div id="card-modal-message" v-if="messaging">
            <vs-textarea style="margin-bottom: 50px" v-model="message" id="message-input"
                         :counter="250"
            />
            <div v-if="(errors.includes('bad-content'))" style="color: red">Message cannot be blank or too long</div>
            <div v-if="(errors.includes('invalid-card'))" style="color: red">There was something wrong with the card</div>
            <vs-button id="card-modal-message-send" @click="sendMessage(currentMessage, message)">Send Reply</vs-button>

          </div>
        </transition>
      </div>

    </vs-popup>


      <vs-card v-for="message in messages" :key="message.id" id="message-notification-card" class="notification-card" actionable>
          <div @click="openDetailedModal(message)">
            <div style="display: flex; justify-content: space-between">
              <p class="sub-header">MARKETPLACE - {{message.sent}}</p>
              <vs-button color="danger" id="delete-btn" class="message-button delete-button" @click.stop.prevent="deleteMessage(message.id)" icon="close"></vs-button>
            </div>
            <div id="message-notification-container">
              <div id="message-text">New message from {{users[message.sender.id || message.sender].firstName}} {{users[message.sender.id || message.sender].lastName}} about {{message.card.title}}</div>
            </div>
         </div>
      </vs-card>

    <!-- PURCHASE LISTING NOTIFICATIONS -->
    <vs-card class="notification-card">
      <div class="pln-top-row">
        <p class="sub-header">PURCHASED LISTING - 2021-08-08</p>
        <div style="display: flex;">
          <vs-button color="danger" icon="close" class="pln-delete-button delete-button"></vs-button>
        </div>
      </div>
      <h2>Honda Civic</h2>
      <h5>Company Name</h5>
      <div class="pln-bottom-row">
        <div style="display: flex; margin-top: auto;">
          <h2>
            $69.99
          </h2>
          <div style="padding-left: 1em">
            Collect at Ilam Road, Christchurch, New Zealand
          </div>
        </div>
        <div>
          <vs-button>View Listing</vs-button>
        </div>
      </div>
    </vs-card>

    <!-- LIKED LISTING NOTIFICATIONS -->
    <vs-card class="liked-listing-notification notification-card">
      <p class="sub-header">LIKED LISTING - 2021-08-08</p>
      <div class="lln-description">
        <b>Honda Civic</b>, by Company Name was purchased by someone else, and is no longer available.
      </div>
      <div class="lln-button-group">
        <vs-button>View Listing</vs-button>
        <vs-button color="danger" icon="close" class="lln-delete-button delete-button"></vs-button>
      </div>

    </vs-card>

  </div>

</template>

<script>

import api from "../Api";
import { store } from '../store';

export default {
    data() {
      return {
        messages: [],
        messaging: false,
        showing: false,
        message: '',
        errors: [],
        users: {},
        detailedView: false,
        currentMessage: null,
      }
    },

    mounted() {
      this.getMessages();
    },

    methods: {
      getMessages: function() {
        api.getMessages(store.loggedInUserId)
        .then((response) => {
          this.messages = response.data;
          for (let message of this.messages) {
            this.users[message.sender.id] = message.sender;
          }
        }).catch((error) => {
          this.$log.error("Error getting messages: " + error);
          this.$vs.notify({title:`Could not get messages`, text: "There was an error getting messages", color:'danger'});

        })
      },

      deleteMessage: function(messageId) {
        api.deleteMessage(messageId)
            .then((response) => {
              this.$vs.notify( {
                title: `Message Deleted`,
                text: response.data.sender.firstName +" "+response.data.sender.lastName+ ": "+ response.data.description,
                color: 'success'
              });
              this.getMessages();
            })
            .catch((error) => {
              this.$vs.notify({
                title: 'Failed to delete the message',
                color: 'danger'
              });
              this.$log.debug("Error Status:", error);
            });
      },

      /**
       * Sends user message by calling POST messages
       * @param cardId ID of card whose owner the user is going to message
       */
      sendMessage(originalMessage, message) {
        if (this.checkMessage(message)) {
          //the server can return either the sender object or it's id
          //we resolve which it is so we are always posting to the correct user
          let senderId=null;
          if (originalMessage.sender.id) {
            senderId=originalMessage.sender.id;
          } else {
            senderId=originalMessage.sender;
          }

          api.postMessage(senderId, originalMessage.card.id, message)
              .then(() => {
                this.$vs.notify({title: 'Reply Sent!', color: 'success'});
                //reset the message after success
                this.message = "";
                this.errors=[];
              })
              .catch((error) => {
                this.$log.debug(error);
                this.$vs.notify({title: 'Error sending message', text: `${error}`, color: 'danger'});
              });
        }

      },
      /**
       * Check the message contents
       * Simply check a blank message is not sent and the message is under the maximum character limit
       */
      checkMessage() {

        if (this.message == null || this.message === "") {
          this.errors.push('bad-content');

          this.$vs.notify({title:'Error sending message', text:`No message content`, color:'danger'});
          return false;
        }

        if (this.message.length > 250) {
          this.errors.push('bad-content');

          this.$vs.notify({title:'Error sending message', text:`Message is too long. Consider sending it in parts.`, color:'danger'});
          return false;
        }

        return true;
      },


      openDetailedModal: function(message) {
        this.currentMessage = message;
        this.detailedView = true;
        this.showing = true;
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

<style>
.notification-card {
  margin: 1em;
  width: auto;
}

.delete-button {
  width: 25px!important;
  height: 25px!important;
}

.delete-button > i.material-icons {
  font-size: 18px;
}

#message-notification-container {
  display: flex;
  margin-right: 10px;
}

#message-text {
  width: 100%;
  font-size: 14px;
}

#delete-btn {
  padding: 0.5em;
  font-size: 14px;
  width: 35px;
}

.message-detail-container {
  display: flex;
  margin-top: 5px;
}

#message-detail-message {
  font-size: 15px;
  word-wrap: break-word;
  width: 92%;
  float: right;
  margin-left: 5px;
}

#message-detail-sent {
  font-size: 14px;
  margin-left: 5px;
}

.message-detail-header {
  font-size: 20px;
  margin-left: 5px;
}

#card-modal-bottom {
  display: flex;
  margin-bottom: 10px;
  flex-wrap: wrap;
}

#card-modal-message-send {
  float: right;
}

.message-detail-delivered {
  position: relative;
  flex: 50%;
  font-size: large;
  top: 7px;
  display: flex;
}

.message {
  margin-top: 25px;
}

#message-detail-modal button {
  padding: 10px 30px;
}

.sub-header {
  font-size: 12px;
  color: gray;
}

/* === PURCHASE LISTING NOTIFICATION === */

.pln-top-row {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}

.pln-delete-button {
  margin-left: 1em;
}

.pln-bottom-row {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}

.liked-listing-notification > .vs-card--content {
  display: grid;
  grid-template-columns: 3fr 1fr;
  grid-template-rows: auto auto;
}

.lln-description {
  grid-row: 2;
}

.lln-button-group {
  grid-row: 1/3;
  grid-column: 2;

  margin: auto 0 auto auto;
  display: flex;
  flex-direction: row;
}

.lln-delete-button {
  margin: auto 0 auto 1em;
}


@media only screen and (max-width: 1250px){
  #message-notification-container {
    display: grid;
  }

  #message-notification-card {
    height: 100%;
  }

  #message-text {
    margin-bottom: 10px;
    height: 100%;
  }

  #delete-btn{

  }

  .lln-button-group {
    flex-direction: column-reverse;
  }

  .lln-delete-button {
    margin-left: auto;
    margin-bottom: 4px;
  }

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

/* .msg-icon {
  margin-top: 6%;
} */
</style>
