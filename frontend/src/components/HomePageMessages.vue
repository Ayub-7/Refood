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
    <div v-for="message in messages" :key="message.id">
      <vs-card id="message-notification-card" actionable>
          <div id="message-notification-container"  @click="openDetailedModal(message)">
            <div id="message-text">New message from {{users[message.sender.id || message.sender].firstName}} {{users[message.sender.id || message.sender].lastName}} about {{message.card.title}}</div>
            <vs-button color="danger" id="delete-btn" class="message-button" @click.stop.prevent="deleteMessage(message.id)">X</vs-button>
          </div>
      </vs-card>
    </div>


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
          console.log(this.messages)
          for (let message of this.messages) {
            this.users[message.sender.id] = message.sender;
          }
          console.log(this.users)
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
          console.log("sup");
          //the server can return either the sender object or it's id
          //we resolve which it is so we are always posting to the correct user
          let senderId=null;
          if (originalMessage.sender.id) {
            senderId=originalMessage.sender.id;
          } else {
            senderId=originalMessage.sender;
          }

          api.postMessage(senderId, originalMessage.card.id, message)
              .then((res) => {
                this.$vs.notify({title: 'Reply Sent!', text: `ID: ${res.data.messageId}`, color: 'success'});
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
       * Simply check a blank message is not sent
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

#message-notification-container {
  display: flex;
  margin-right: 10px;
}



#message-text {


  width: 100%;
  font-size: 14px;
  /* height: 100%; */
  /* margin-bottom: 15px; */
  line-height: 30px;
}

#delete-btn {
  padding: 0.5em;
  font-size: 14px;
  width: 35px;
}


#message-notification-card {
  width: 95%;
  margin: auto;
  margin-top: 5px;
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

#message-detail-sent {
  font-size: large;
}

.message {
  margin-top: 25px;
}

#message-detail-modal button {
  padding: 10px 30px;
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
    text-align: center;
    height: 100%;
  }

  #delete-btn{
    margin: auto;
    width: 25px;
    font-size: 12px;
  }
}


This is a card I am selllThis is a card I am selll
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