<template>
  <div id="notifications">
    <div>
      <vs-dropdown vs-trigger-click>
        <div id="notificationContainer">
          <vs-avatar icon="notifications" size="30" name="avatar" />
          <span id="dropdownButtonName">{{ this.notifications.length}}</span>
        </div>
        <vs-dropdown-menu>
            
          <vs-dropdown-group vs-label="Notifications" id="cardList" v-if="notifications.length > 0">
            <div class="dropdown-item" v-for="notification in notifications" :key="notification.cardId">
                <div class="dropdown-item-name">

                    <!-- If card is expired -->
                    <div class="cardContainer" v-if="notification.status == expired">
                        <div id="text">
                        {{notification.title}} has expired
                        </div>
                        <div id="buttons"> 
                            <vs-button class="notificationButtons" @click="extendCard(notification.cardId, notification.title)">Extend</vs-button> 
                            <vs-button class="notificationButtons" @click="deleteCard(notification.cardId, notification.title)">Delete card</vs-button>
                        </div>
                    </div>

                    <!-- If card is deleted -->
                    <div class="cardContainer" v-else-if="notification.status == deleted">
                        {{notification.title}} has been removed
                    </div>
                
                </div>
            </div>
          </vs-dropdown-group>
          <vs-dropdown-group vs-label="No notifications" v-else>
          </vs-dropdown-group>
        </vs-dropdown-menu>
      </vs-dropdown>
    </div>
  </div>
</template>

<script>
import api from '../Api';

import { mutations, store } from '../store';

export default {

    data: function() {
        return {
          
          expired: 'Expired',
          deleted: 'Deleted',

        };
    },

    methods: {
      extendCard(cardId, title) {
        api.extendCardDisplayPeriod(cardId)
        .then(() => {
          this.getNotifications();
          this.$vs.notify({title:'Card Extended', text:`Card ${title} was extended`, color:'success'});
        }).catch((error) => {
          console.log(error);
        })
      },

      deleteCard(cardId, title) {
        api.deleteCard(cardId)
        .then(() => {
          this.getNotifications();
          this.$vs.notify({title:'Card Deleted', text:`Card ${title} was deleted`, color:'success'});
        }).catch((error) => {
          console.log(error);
        })
      },


      getNotifications() {
        api.getNotifications(store.loggedInUserId)
        .then((response) => {
            mutations.setNotifications(response.data);
        })
      }
    },

    computed: {
        notifications() {
            return store.notifications
        }
    }

}
</script>

<style>

.dropdown-item >>> a {
  display: flex;
  text-align: center;
}

.dropdown-item-name {
  margin: auto;
  min-width: 100px;
  font-size: 12px;
  margin-top: 5px;
}

#cardList {
  width: 450px;
  max-height: 350px;
  overflow-y: auto;
}

#text {
    max-width: 65%;
}

#dropdownButtonName {
  font-size: 16px;
  text-align: right;
  position: absolute;
  bottom: 0;
  left: 70%;
  font-size: 14px;
  color: white;
}

#notificationContainer {
  display: flex;
  position: relative
  /* min-width: 150px; */
}

#notifications {
    margin-right: 5px;
}


.cardContainer {
  display: flex;
  justify-content: space-between;
  border-radius: 10px;
  padding: 0.25em;
}



</style>
