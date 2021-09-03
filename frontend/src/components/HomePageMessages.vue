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

    <!-- === NEWSFEED ITEMS === -->
    <div v-for="item in feedItems" :key="item.fid">
      <!-- CARD MESSAGE -->
      <vs-card v-if="item.card" id="message-notification-card" class="notification-card" actionable>
        <div @click="openDetailedModal(item)">
          <div style="display: flex; justify-content: space-between">
            <p class="sub-header">MARKETPLACE - {{item.sent}}</p>
            <vs-button color="danger" id="delete-btn" class="message-button delete-button" @click.stop.prevent="deleteMessage(item.id)" icon="close"></vs-button>
          </div>
          <div id="message-notification-container">
            <div id="message-text">New message from {{users[item.sender.id || item.sender].firstName}} {{users[item.sender.id || item.sender].lastName}} about <strong>{{item.card.title}}</strong></div>
          </div>
        </div>
      </vs-card>

      <!-- USER BOUGHT LISTING NOTIFICATION -->
      <div v-else-if="item.boughtListing && item.boughtListing.buyer === currentUserId" @mouseenter="markAsRead(item)" class="bought-listing-container">
        <vs-card v-bind:class="[{'unread-notification': item.viewStatus === 'Unread'}, 'notification-card', 'bought-listing-notification']">
          <div class="pln-top-row">
            <p class="sub-header">BOUGHT LISTING - {{ item.created }}</p>
          </div>
          <h3>{{ item.boughtListing.product.name }}</h3>
          <h5>{{ item.boughtListing.product.business.name }}</h5>
          <div class="pln-bottom-row">
            <h4>
              {{ currency }}
              {{ item.boughtListing.price }}
            </h4>
            <div>
              Collect your purchase at <strong>{{ createAddressString(item.boughtListing.product.business.address) }}</strong>
            </div>
          </div>
        </vs-card>
      </div>

      <!-- USER LIKED PURCHASED LISTING NOTIFICATIONS -->
      <div v-else-if="item.boughtListing && item.boughtListing.buyer !== currentUserId" @mouseenter="markAsRead(item)" class="liked-listing-container">
        <vs-card v-bind:class="[{'unread-notification': item.viewStatus === 'Unread'}, 'liked-listing-notification', 'notification-card']">
          <div class="pln-top-row">
            <p class="sub-header">LIKED LISTING - {{ item.created }}</p>
          </div>
          <div class="lln-description">
            <strong>{{ item.boughtListing.product.name }}</strong>, by {{ item.boughtListing.product.business.name }} was purchased by someone else, and is no longer available.
          </div>
        </vs-card>
      </div>

      <!-- NEW LIKED LISTING NOTIFICATIONS -->
      <div v-else-if="item.listing" @mouseenter="markAsRead(item)" class="liked-listing-container">
        <vs-card v-bind:class="[{'unread-notification': item.viewStatus === 'Unread'}, 'liked-listing-notification', 'notification-card']">
          <p class="sub-header">{{ item.status.toUpperCase() }} LISTING - {{ item.created }}</p>
          <div style="display: flex">
            <div class="lln-description">
              <span v-if="item.status === 'Liked'">You have liked <strong>{{ item.listing.inventoryItem.product.name }}</strong>.</span>
              <span v-else>You have unliked <strong>{{ item.listing.inventoryItem.product.name }}</strong>.</span>
            </div>
            <div class="lln-button-group">
              <vs-button id="view-listing-button" class="lln-delete-button view-listing-button" @click="goToListing(item.listing)"> View Listing </vs-button>
            </div>
          </div>
        </vs-card>
      </div>
    </div>

  </div>
</template>

<script>
import api from "../Api";
import { store } from '../store';

export default {

  props: {
    currency: {
      type: String,
      default: "$",
    }
  },

  data() {
    return {
      messaging: false,
      showing: false,
      message: '',
      errors: [],
      users: {},
      detailedView: false,
      currentMessage: null,

      currentUserId: null,

      messages: [],
      listingNotifications: [],
      feedItems: [],
    }
  },

  mounted() {
    this.currentUserId = store.loggedInUserId;
    this.getMessages();
    this.getListingNotifications();
  },

  methods: {
    /**
     * Marks a listing notification as read.
     * @param notification the notification object to update.
     */
    markAsRead: function(notification) {
      if (notification.viewStatus === "Unread") {
        api.updateListingNotificationViewStatus(notification.id, "Read")
          .then((res) => {
            this.$log.debug(res);
            notification.viewStatus = "Read";
          })
          .catch((error) => {
            this.$log.debug(error);
          });
      }
    },

    /**
     * Marks a listing notification as important
     * @param notification the notification object to update.
     */
    markAsImportant: function(notification) {
      if (notification.viewStatus != "Important") {
        api.updateListingNotificationViewStatus(notification.id, "Important")
            .then((res) => {
              this.$log.debug(res);
              notification.viewStatus = "Important";
            })
            .catch((error) => {
              this.$log.debug(error);
            });
      } else {
        api.updateListingNotificationViewStatus(notification.id, "Important")
            .then((res) => {
              this.$log.debug(res);
              notification.viewStatus = "Read";
            })
            .catch((error) => {
              this.$log.debug(error);
            });
      }
    },

    /**
     * Combines the different news feed item types into a single list.
     * Sorts the list by newest messages first.
     */
    combineFeedMessages: function() {
      this.feedItems = this.messages.concat(this.listingNotifications);
      // Set a overall unique id for each feed item. Prevent any overlapping ids which may cause update errors.
      this.feedItems = this.feedItems.map((item, index) => {
        item.fid = index;
        return item;
      });
      this.feedItems.sort(function(a, b) {
        return new Date(b.created) - new Date(a.created);
      });
    },

    /**
     * Calls the backend to retrieve all of the messages for the current user.
     */
    getMessages: function() {
      api.getMessages(this.currentUserId)
        .then((response) => {
          this.messages = response.data;
          for (let message of this.messages) {
            this.users[message.sender.id] = message.sender;
          }

          this.messages = this.messages.map(message => {
            // Map the sent date to a new created attribute - to be used for sorting.
            message.created = message.sent;
            return message;
          });
        })
        .catch((error) => {
          this.$log.error("Error getting messages: " + error);
          this.$vs.notify({title:`Could not get messages`, text: "There was an error getting messages", color:'danger'});
        });
    },

    /**
     * Calls the backend to delete a given message's id.
     * @param messageId the unique id of the message to be deleted.
     */
    deleteMessage: function(messageId) {
      api.deleteMessage(messageId)
        .then((response) => {
          this.$vs.notify({
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
     * Retrieves the notifications relating to purchased listings.
     */
    getListingNotifications: function() {
      api.getListingNotifications(store.loggedInUserId)
        .then((res) => {
          this.listingNotifications = res.data;
          this.combineFeedMessages();
        })
        .catch((error) => {
          this.$log.debug(error);
          if (error && error.response) {
            this.$vs.notify({title: `Error ${error.response}`,
                              text: "There was a problem getting your newsfeed.",
                              color: "danger"});
          }
        });
    },

    /**
     * Sends user message by calling POST messages
     * @param originalMessage the object of the originally sent message.
     * @param message the text string to send back.
     */
    sendMessage(originalMessage, message) {
      if (this.checkMessage(message)) {
        //the server can return either the sender object or it's id
        //we resolve which it is so we are always posting to the correct user
        let senderId = null;
        if (originalMessage.sender.id) {
          senderId = originalMessage.sender.id;
        } else {
          senderId = originalMessage.sender;
        }
        api.postMessage(senderId, originalMessage.card.id, message)
          .then(() => {
            this.$vs.notify({title: 'Reply Sent!', color: 'success'});
            //reset the message after success
            this.message = "";
            this.errors = [];
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

    /**
     * Opens the expanded message information modal, allowing the user to view the full message content.
     * @param message the message to expand.
     */
    openDetailedModal: function(message) {
      this.currentMessage = message;
      this.detailedView = true;
      this.showing = true;
    },

    /**
     * Creates and returns a string of a given address to display on the page.
     * @param address the address entity to concatenate information from.
     * @return location string.
     */
    createAddressString: function(address) {
      let location = "";
      if (address.streetNumber) location += address.streetNumber + " ";
      if (address.streetName) location += address.streetName + ", ";
      if (address.suburb) location += address.suburb + ", ";
      if (address.city) location += address.city + ", ";
      if (address.region) location += address.region + ", ";
      if (address.country) location += address.country;

      return location;
    },

    /**
     * Redirects the page to the given full listing page.
     * @param listing the listing page to redirect the browser to.
     */
    goToListing: function(listing) {
      this.$router.push(`/businesses/${listing.inventoryItem.product.business.id}/listings/${listing.id}`);
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
.notification-card {
  margin: 1em;
  width: auto;
}

.delete-button {
  width: 25px!important;
  height: 25px!important;
}

.delete-button >>> i.material-icons {
  font-size: 20px;
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
.unread-notification {
  box-shadow: 0 0 4px red!important;
}

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
  margin: auto 0;
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

  .lln-delete-button {
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

</style>
