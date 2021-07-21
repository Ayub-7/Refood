<template>
    <div id="grid-container" style="margin: auto">
        <div>
          <vs-row id="marketRow">
            <!-- Change vs-lg to 2 if you want 6 per row or 3 if you want 4 per row -->
            <vs-col id="marketCard" type="flex" vs-justify="center" vs-align="center" vs-lg="3" vs-sm="12" v-for="card in cardData" :key="card.id">
              <div style="margin: 10px; width: 90%;" @click="openCardModal(card)">
                <!-- Marketplace Card -->
                <vs-card>
                  <div slot="media" id="cardHeader">
                    <!-- Default image for now -->
                    <img id="marketImage" src="../../public/ProductShoot.jpg" alt="Product image"/>
                  </div>
                  <div>
                    <div v-if="showSection" class="section">{{displaySection(card.section)}}</div>
                    <div id="cardCreationDate">{{card.created}}</div>
                    <div id="cardUserName" v-if="card.user.firstName">{{card.user.firstName+" "+card.user.lastName}}</div>
                    <div id="cardUserAddress" v-if="card.user.homeAddress">{{MarketplaceCommon.getGeneralAddress(card.user.homeAddress)}}</div>417
                    <div id="cardTitle">{{card.title}}</div>
                    <!-- Need to add limit or something to description -->
                    <div id="cardDescription">{{card.description}}</div>
                    <!-- Keyword display -->
                      <div id="keywordWrapper">
                        <div id="cardKeywords"  v-for="keyword in card.keywords.split(' ')" :key="keyword" >#{{keyword}}</div>
                      </div>
                  </div>
                </vs-card>
              </div>
            </vs-col>
          </vs-row>
        </div>
      <CardModal id="cardModal" ref="cardModal" v-show="selectedCard != null" @deleted="notifyOfDeletion" :selectedCard='selectedCard' />
    </div>
</template>

<script>
import CardModal from './CardModal.vue'
import MarketplaceCommon from "./MarketplaceCommon";

export default {
  data: function() {
    return {
      selectedCard: null,
      MarketplaceCommon
    }
  },
  components: {
    CardModal
  },
  props: {
      cardData: {
        type: Array,
      },
      showSection: {
        default: false,
        type: Boolean,
      }
  },
  watch: {
    "cardData": function (val) {
      this.cards = MarketplaceCommon.checkCardList(val);
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
     * Method for notifying the marketplace component that a card has been deleted
     */
    notifyOfDeletion: function() {
      this.$emit('cardRemoved');
    }
    },

    /**
     * Displays the section - checks if it is 'ForSale', if so, return the string with a space, return normally otherwise.
     * @param section card section.
     */
    displaySection: function(section) {
      if (section === "ForSale") return "For Sale";
      return section;
    },


}


</script>

<style>

#card-modal-message-button {
  margin-left: 5px;
}

/* CARD STYLING */

#marketImage {
  width: 100%;
  height: auto;
}

.section {
  font-size: 12px;
  color: gray;
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
