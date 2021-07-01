<template>
  <vs-card class="main">
    <div class="profile-text-inner">
      <div style="display: flex; margin: auto; padding-bottom: 1em;">
        <div id="title" style="font-size: 30px; margin: auto 8px;" >Community Marketplace</div>
        <div style="margin-right: 0; margin-left: auto; display: flex">
          <div class="title" style="margin-top: 5px; margin-right: 10px">
            <p v-if="displaytype">Grid</p>
            <p v-if="!displaytype">List</p>
          </div>
          <label class="switch" >
            <input v-model="displaytype" type="checkbox" checked>
            <span class="slider round"></span>
          </label>
        </div>
      </div>
      <vs-divider></vs-divider>
      <div>Sort By: </div>
      <vs-button @click="openModal">Add to market (test for now)</vs-button>
      <vs-divider></vs-divider>

      <vs-tabs alignment="center">
        <vs-tab label="For Sale">
          <div>
            <MarketplaceGrid v-if="displaytype" v-bind:cardData=sectionForSale.slice(itemPerPage*(currentPage-1),currentPage*itemPerPage) />
            <MarketplaceTable v-if="!displaytype" v-bind:tableData=sectionForSale.slice(itemPerPage*(currentPage-1),currentPage*itemPerPage) />
          </div>
          <div class="center">
          </div>
        </vs-tab>
        <vs-tab label="Wanted">
          <div>
            <MarketplaceGrid v-if="displaytype" v-bind:cardData=sectionWanted.slice(itemPerPage*(currentPage-1),currentPage*itemPerPage) />
            <MarketplaceTable v-if="!displaytype" v-bind:tableData=sectionWanted.slice(itemPerPage*(currentPage-1),currentPage*itemPerPage) />
          </div>
        </vs-tab>
        <vs-tab label="Exchange">
          <div>
            <MarketplaceGrid v-if="displaytype" v-bind:cardData=sectionExchange.slice(itemPerPage*(currentPage-1),currentPage*itemPerPage) />
            <MarketplaceTable v-if="!displaytype" v-bind:tableData=sectionExchange.slice(itemPerPage*(currentPage-1),currentPage*itemPerPage) />
          </div>

        </vs-tab>
      </vs-tabs>
        <vs-pagination v-model="currentPage" :total="sectionForSale.length/itemPerPage"/>
    </div>
  <MarketplaceAddCard ref="marketplaceAddCard" />
  </vs-card>



</template>

<script>
import MarketplaceGrid from './MarketplaceGrid.vue'
import MarketplaceTable from './MarketplaceTable.vue'
import MarketplaceAddCard from './MarketplaceAddCard.vue'
import api from "../Api";

export default {
  name: "CommunityMarketplace",
  components: {
    MarketplaceGrid, MarketplaceTable, MarketplaceAddCard
  },

  data: () => {
    return {
      displaytype: true,
      sectionForSale: "",
      sectionWanted: "",
      sectionExchange: "",
      currentPage: 1,
      itemPerPage: 12,

      //test data for create card
      newCardTest: {
        "creatorId": "2",
        "title": "1989 S13 Silvia RB25DET",
        "description": "No wof reg, send it",
        keywords: [
          {
            id: 6,
            name: 'Nissan'
          },
          {
            id: 7,
            name: 'Silvia'
          },
          {
            id: 8,
            name: 'RB25DET'
          },
          {
            id: 9,
            name: 'S13'
          }],
        section: "ForSale"
      },
    }
  },

  methods: {
    /**
    * Method for opening modal, calls method in child component to open modal
    */
    openModal: function() {
      this.$refs.marketplaceAddCard.openModal();
    },


    getCards: function (section) {
      api.getCards(section)
          .then((response) => {
            return response.data;
          })
          .catch((error) => {
            this.$vs.notify({title:'Error', text:'ERROR getting cards:', color:'danger'});
            this.$log.error("Error" + error);
          });
    },

    getAllCards: function () {
      api.getCards('ForSale')
          .then((response) => {
            console.log(response.data)
            this.sectionForSale = response.data;
          })
          .catch((error) => {
            this.$vs.notify({title:'Error', text:'ERROR getting cards:', color:'danger'});
            this.$log.error("Error" + error);
          });

      api.getCards('Wanted')
          .then((response) => {
            this.sectionWanted = response.data;
          })
          .catch((error) => {
            this.$vs.notify({title:'Error', text:'ERROR getting cards:', color:'danger'});
            this.$log.error("Error" + error);
          });

      api.getCards('Exchange')
          .then((response) => {
            this.sectionExchange = response.data;
          })
          .catch((error) => {
            this.$vs.notify({title:'Error', text:'ERROR getting cards:', color:'danger'});
            this.$log.error("Error" + error);
          });
    }
  },


  mounted() {
    this.getAllCards();
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

.profile-text-inner {
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