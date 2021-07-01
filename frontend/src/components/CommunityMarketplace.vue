<template>
  <vs-card class="main">
      <!-- TODO: Remove when actual button is added -->
      <div class="container">
        <vs-button @click="openModal">Add to market (test for now)</vs-button>
        <div style="display: flex; margin: auto; padding-bottom: 1em;">
        <div id="title" style="font-size: 30px; margin: auto 8px;" >Community Marketplace</div>
        <div style="margin-right: 0; margin-left: auto; display: flex">
          <div class="title" style="margin-top: 5px; margin-right: 10px">
            <p v-if="displayType">Grid</p>
            <p v-if="!displayType">List</p>
          </div>
          <label class="switch" >
            <input id="display-type-button" v-model="displayType" type="checkbox" @click="displayType=!displayType" checked>
            <span class="slider round"></span>
          </label>
        </div>
      </div>
      <vs-divider></vs-divider>
      <vs-tabs alignment="center">
        <vs-tab label="For Sale" @click="getSectionCards('ForSale')">
          <div>
            <MarketplaceGrid v-if="displayType" :cardData="cards" />
            <MarketplaceTable v-if="!displayType" :tableData="cards" />
          </div>
        </vs-tab>
        <vs-tab label="Wanted" @click="getSectionCards('Wanted')">
          <div>
            <MarketplaceGrid v-if="displayType" :cardData="cards" />
            <MarketplaceTable v-if="!displayType" :tableData="cards" />
          </div>
        </vs-tab>
        <vs-tab label="Exchange" @click="getSectionCards('Exchange')">
          <div>
            <MarketplaceGrid v-if="displayType" :cardData="cards" />
            <MarketplaceTable v-if="!displayType" :tableData="cards" />
          </div>

        </vs-tab>
      </vs-tabs>
    </div>
  <MarketplaceAddCard ref="marketplaceAddCard" />

  </vs-card>

</template>

<script>
import MarketplaceGrid from './MarketplaceGrid.vue'
import api from "../Api";
import MarketplaceTable from './MarketplaceTable.vue'
import MarketplaceAddCard from './MarketplaceAddCard.vue'
export default {
  name: "CommunityMarketplace",
  components: {
    MarketplaceGrid, MarketplaceTable, MarketplaceAddCard
  },

  data: () => {
    return {
      displayType: true,
      currentSection: "ForSale",
      cards: [],

    }
  },

  methods: {
    getSectionCards: function(section) {
      this.$vs.loading({
        container: ".vs-tabs",
      });
      this.cards = [];
      api.getCardsBySection(section)
        .then((res) => {
          this.cards = res.data.slice(0, 100); // todo: TEMPORARY UNTIL WE CAN PAGINATE THE DATA COMING IN.
        })
        .catch((error) => {
          console.log(error);
        })
        .finally(() => {
          this.$vs.loading.close(`.vs-tabs > .con-vs-loading`);
        });
    },
    /**
     * Method for opening modal, calls method in child component to open modal
     */
    openModal: function() {
      this.$refs.marketplaceAddCard.openModal();
    }
  },

  mounted() {
    api.checkSession()
      .then(() => {
        this.getSectionCards("ForSale");
      })
      .catch((error) => {
        this.$vs.notify({title:'Error getting session info', text:`${error}`, color:'danger'});
      });
  }

}

</script>

<style scoped>

vs-tab {
  color: #1F74FF;
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

.container {
  margin: 1em;
}

.main {
  background-color: white;
  width: 75%;
  margin: 1em auto;
}

.vs-divider {
  margin-bottom: 0px;
}

.switch {
  position: relative;
  display: inline-block;
  width: 60px;
  height: 34px;
}

.switch input {
  opacity: 0;
  width: 0;
  height: 0;
}

.slider {
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #ccc;
  -webkit-transition: .4s;
  transition: .4s;
}

.slider:before {
  position: absolute;
  content: "";
  height: 26px;
  width: 26px;
  left: 4px;
  bottom: 4px;
  background-color: white;
  -webkit-transition: .4s;
  transition: .4s;
}

input:checked + .slider {
  background-color: #2196F3;
}

input:focus + .slider {
  box-shadow: 0 0 1px #2196F3;
}

input:checked + .slider:before {
  -webkit-transform: translateX(26px);
  -ms-transform: translateX(26px);
  transform: translateX(26px);
}

/* Rounded sliders */
.slider.round {
  border-radius: 34px;
}

.slider.round:before {
  border-radius: 50%;
}

#header-container {
  display: flex;
  justify-content: space-between;
  padding-top: 4px;
}

#header-buttongroup {
  display: inline-flex;
  justify-content: space-around;
}





</style>