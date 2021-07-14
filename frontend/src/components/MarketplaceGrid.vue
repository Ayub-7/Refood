<template>
    <div id="grid-container" style="margin: auto">
        <div>
          <vs-row id="marketRow">
            <!-- Change vs-lg to 2 if you want 6 per row or 3 if you want 4 per row -->
            <vs-col id="marketCard" type="flex" vs-justify="center" vs-align="center" vs-lg="3" vs-sm="12" v-for="card in cards" :key="card.id">
              <div style="margin: 10px; width: 90%;" @click="openCardModal(card)">
                <!-- Marketplace Card -->
                <vs-card>
                  <div>
                    <div id="cardCreationDate">{{card.created}}</div>

                    <div id="cardUserName" v-if="card.user.firstName">{{card.user.firstName+" "+card.user.lastName}}</div>
                    <div id="cardUserAddress" v-if="card.user.homeAddress">{{getGeneralAddress(card.user.homeAddress)}}</div>


                    <div id="cardTitle">{{card.title}}</div>
                    <!-- Need to add limit or something to description -->
                    <div id="cardDescription">{{card.description}}</div>
                    <!-- Keyword display -->
                      <div v-if="card.keywords" id="keywordWrapper">
                        <div id="cardKeywords"  v-for="keyword in getKeywords(card.keywords)" :key="keyword.id" >#{{keyword.name}}</div>
                      </div>
                  </div>
                </vs-card>
              </div>
            </vs-col>
          </vs-row>
        </div>
      <CardModal id="cardModal" ref="cardModal" v-if="selectedCard != null" :selectedCard='selectedCard' />
    </div>
</template>

<script>
import CardModal from './CardModal.vue'

import api from "../Api";

export default {
  props: ['cardData'],
  data: function () {
    return {
      cards: this.cardData,
      selectedCard: null
    }
  },
  components: {
    CardModal
  },
  watch: {
    "cardData": function(val) {
      this.cards = this.checkCardList(val);
    }
  },
  methods: {

    /**
     * Method for opening card modal, calls method in child component to open modal
     */
    openCardModal: function(card) {
      this.selectedCard = card;
      this.$refs.cardModal.openModal();
    },
    /**
     * Converts the space separated keywords to a JSON object recognized by the keywordWrapper
     *
     * @param keywords  space separated keywords
     * @returns {[]}    JSON object
     */

    getKeywords: function(keywords) {
      keywords = keywords.trimEnd().split(/\s+/);
      let tmpKeywords = [];

      for(let i=0;i<keywords.length;i++) {
        let keyword = {};
        keyword.key = i;
        keyword.name = keywords[i];
        tmpKeywords.push(keyword);
      }
      return tmpKeywords;
    },

    /**
     * Translates the user's address into a general address string, listing only the country, city and suburb.\
     *
     * @param homeAddress   User's home address
     * @returns String      General address string
     */

    getGeneralAddress: function (homeAddress) {
      let addressStr = "";
      if(homeAddress.country) {
        addressStr += homeAddress.country;
      }

      if(homeAddress.city) {
        addressStr += ", " + homeAddress.city;
      }

      if(homeAddress.suburb) {
        addressStr += ", " + homeAddress.suburb;
      }

      return addressStr
    },

    /**
     * Checks the data integrity of the list of cards
     * ie every card includes a user
     * if not, it will get one from the database and update the card list
     *
     * assumes the user id attribute is not null.
     *
     */
    checkCardList: function(cards) {
      cards.forEach(function (card, index) {
        if (!card.user.firstName) {
          api.getUserFromID(card.user)
              .then((response) => {
                cards[index].user = response.data;
              }).catch((err) => {
            if (err.response.status === 406) {
              this.$vs.notify({title:'User not found: '+card.user, text:'This user does not exist.', color:'danger'});
            }
            throw new Error(`Error trying to get user info from id: ${err}`);
          });

        }
      });
      return cards;
    }
  },
}


</script>

<style>

/* CARD STYLING */

#marketImage {
  width: 100%;
  height: auto;
}

#cardCreationDate {
  font-weight: lighter;
  font-size: 10px;
  height: 20px;
}

#cardUserName {
  font-size: 10px;
  height: 15px;
}

#cardUserAddress {
  font-size: 10px;
  height: 40px;
}

#cardTitle {
  font-weight: bold;
  font-size: 17px;
  height: 50px;
}

#cardDescription {
  margin-top: 10px;
  height: 120px;
  overflow-y: auto;
}

#cardKeywords {
  color: #1F74FF;
  font-size: 15px;
  padding: 2px;
  float: left;
}

#keywordWrapper {
  margin-top: 10px;
  width: 100%;
  height: 50px;
  overflow-y: auto;
}


/* REMOVE AUTO SCROLL HIDING, SO USER KNOWS IF PARAGRAPH IS LONGER THAN CARD SIZE */

::-webkit-scrollbar {
  -webkit-appearance: none;
  width: 5px;
}

::-webkit-scrollbar-thumb {
  border-radius: 4px;
  background-color: rgba(0, 0, 0, .5);
  box-shadow: 0 0 1px rgba(255, 255, 255, .5);
}


</style>
