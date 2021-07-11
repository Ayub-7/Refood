<template>
  <vs-card class="main">
    <div class="container">
      <div class="title-container">
        <h1 id="title" class="title-left title" >Community Marketplace</h1>
        <div class="title-right">
          <div class="menu-title" style="margin-top: 5px; margin-right: 10px">
            <p v-if="displayType">Grid</p>
            <p v-if="!displayType">List</p>
          </div>
          <label class="switch">
            <input id="display-type-button" v-model="displayType" type="checkbox" @click="displayType=!displayType" checked>
            <span class="slider round"></span>
          </label>
        </div>
      </div>
      <vs-divider></vs-divider>


      <div class="title-container">

        <div class="title-left" >
          <vs-select class="selectExample" v-model="selectSortBy">
            <vs-select-item :key="index" :value="item.value" :text="item.text" v-for="(item, index) in optionsSortBy"/>
          </vs-select>
          <vs-button @click="sortData(selectSortBy);" style="margin: 0 2em 0 0.5em; width: 100px">Sort</vs-button>

        </div>
        <div class="title-right">
          <vs-button @click="openModal" >Add a New Item</vs-button>
        </div>
      </div>

      <vs-divider></vs-divider>

      <vs-tabs alignment="center">
        <vs-tab id="saleTab" label="For Sale" @click="getSectionCards('ForSale')">
          <div>
            <MarketplaceGrid  v-if="displayType" :cardData="cards.slice(itemPerPage*(currentPage-1),currentPage*itemPerPage) " />
            <MarketplaceTable v-if="!displayType" :tableData="cards.slice(itemPerPage*(currentPage-1),currentPage*itemPerPage) " />
          </div>
        </vs-tab>
        <vs-tab id="wantedTab" label="Wanted" @click="getSectionCards('Wanted')">
          <div>
            <MarketplaceGrid v-if="displayType" :cardData="cards.slice(itemPerPage*(currentPage-1),currentPage*itemPerPage) " />
            <MarketplaceTable v-if="!displayType" :tableData="cards.slice(itemPerPage*(currentPage-1),currentPage*itemPerPage) " />
          </div>
        </vs-tab>
        <vs-tab id="exchangeTab" label="Exchange" @click="getSectionCards('Exchange')">
          <div>
            <MarketplaceGrid v-if="displayType" :cardData="cards.slice(itemPerPage*(currentPage-1),currentPage*itemPerPage)" />
            <MarketplaceTable v-if="!displayType" :tableData="cards.slice(itemPerPage*(currentPage-1),currentPage*itemPerPage)" />
          </div>

        </vs-tab>
      </vs-tabs>

      <div class="title-container">
        <div class="title-centre">
          <vs-pagination v-model="currentPage" :total="Math.round(cards.length/itemPerPage +0.4)"/>
        </div>

        <div class="title-right">
          <vs-select class="selectExample" v-model="itemPerPage" @click="currentPage=1;">
            <vs-select-item :key="index" :value="item.value" :text="item.text" v-for="(item, index) in optionsItemsPerPage" />
          </vs-select>
        </div>
      </div>
    </div>

  <MarketplaceAddCard ref="marketplaceAddCard" @submitted="onSuccess"/>
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
      displayType: true,
      currentPage: 1,
      itemPerPage: 10,
      tabIndex: 0,

      currentSection: "ForSale",
      cards: [],

      optionsSortBy:[
        {text:'Title',value:'title'},
        {text:'Date Created',value:'created'},
        {text:'Keywords',value:'keywords'},
      ],
      optionsItemsPerPage:[
        {text:'Showing 10 Per Page',value:'10'},
        {text:'Showing 20 Per Page',value:'20'},
        {text:'Showing 40 Per Page',value:'40'},
        {text:'Showing 80 Per Page',value:'80'},
      ],
      selectSortBy: 'created',
      selectSortByPrevious: '',
      toggleDirection: 1,
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
            this.cards = res.data;

            //Sort by creation date
            this.sortData('created');
          })
          .catch((error) => {
            console.log(error);
          })
          .finally(() => {
            this.$vs.loading.close(`.vs-tabs > .con-vs-loading`);
          });

    },

    /**
     * Reloads the data upon sucessful add card.
     * ForSale, Wanted, Exchange
     *
     * @field tabIndex must track this.tabIndex
     */
    onSuccess(tabIndex) {
      let sectionName = "";
      switch (tabIndex) {
        case 1:
          sectionName = "Wanted"
          break;
        case 2:
          sectionName = "Exchange"
          break;
        default:
          sectionName = "ForSale"
          break;
      }
      this.getSectionCards(sectionName);
    },

    /**
    * Method for opening modal, calls method in child component to open modal
    */
    openModal: function() {
      this.$refs.marketplaceAddCard.openModal();
    },

    /**
     * Sort the cards by the [field] input.
     * Assumes the [field] can be sorted via a simple comparison
     * if the field is anything other than 'created', it will attempt to convert to uppercase before sorting
     *
     * @param field
     */
    sortData: function (field) {
      let direction = this.toggleDirection;
      this.cards = this.cards.sort((cardOne,cardTwo) => (cardOne[field].toUpperCase() < cardTwo[field].toUpperCase()) ? direction : -direction);
      this.toggleDirection = this.toggleDirection*-1;
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

#saleTab {
  color: #1F74FF;
}

#wantedTab {
  color: #1F74FF;
}

#exchangeTab {
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


.title-container {
  display: flex;
  margin: auto;
  padding-bottom: 0.5em;
  padding-top: 1em;
}
.title-left {
  margin-right: auto;
  margin-left: 0;
  display: flex;
}

.title-centre {
  margin-right: auto;
  margin-left: auto;
  display: flex;
}

.title-right{
  margin-right: 0;
  margin-left: auto;
  display: flex;
}


.menu-title {
  margin: auto;
  padding-right: 4px;
  font-size: 20px;
}

.title {
  font-size: 30px;
}




</style>