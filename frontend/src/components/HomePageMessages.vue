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

        <div class="message-detail-container">
          <vs-icon icon="send"></vs-icon>
          <div id="message-detail-sent">
            {{currentMessage.sent}}
          </div>
        </div>

        <div id="message-detail-message">
          {{currentMessage.description}}
        </div>
      </div>

    </vs-popup>
    <div v-for="message in messages" :key="message.id">
      <vs-card id="message-notification-card">
          <div id="message-notification-container">
            <div id="message-text">New message from {{users[message.sender.id || message.sender].firstName}} {{users[message.sender.id || message.sender].lastName}} about {{message.card.title}}</div>
            <vs-button class="message-button" @click="openDetailedModal(message)">Expand</vs-button>
            <vs-button class="message-button" @click="deleteMessage(message.id)">Delete</vs-button>
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


      openDetailedModal: function(card) {
        this.currentMessage = card;
        this.detailedView = true;
      }
      
    }
}
</script>

<style>

#message-notification-container {
  display: flex;
  justify-content: space-between;
  margin-right: 10px;
}

#message-text {
  width: 80%;
  font-size: 14px;
  height: 30px;
  line-height: 30px;
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
  text-align: center;
  margin-top: 20px;
  font-size: 15px;
}

#message-detail-sent {
  font-size: 14px;
  margin-left: 5px;
}

.message-detail-header {
  font-size: 20px;
  margin-left: 5px;
}


</style>