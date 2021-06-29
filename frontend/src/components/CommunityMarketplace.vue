<template>
  <vs-card class="main">
    <div class="profile-text-inner">
      <!-- TODO: Remove when actual button is added -->
      <vs-button @click="openModal">Add to market (test for now)</vs-button>
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
      <vs-tabs alignment="center">
        <vs-tab label="For Sale">
          <div>
            <MarketplaceGrid v-if="displaytype" :cardData="testData" />
            <MarketplaceTable v-if="!displaytype" :tableData="testData" />
          </div>
        </vs-tab>
        <vs-tab label="Wanted">
          <div>
            <MarketplaceGrid v-if="displaytype" :cardData="testData.slice(1, 4)" />
            <MarketplaceTable v-if="!displaytype" :tableData="testData.slice(1, 4)" />
          </div>
        </vs-tab>
        <vs-tab label="Exchange">
          <div>
            <MarketplaceGrid v-if="displaytype" :cardData="testData.slice(1,2)" />
            <MarketplaceTable v-if="!displaytype" :tableData="testData.slice(1,2)" />
          </div>
          
        </vs-tab>
      </vs-tabs>
    </div>
  <MarketplaceAddCard :showingAddCardModal="showingAddCardModal" @closeModal="closeModal" />

  </vs-card>

</template>

<script>
import MarketplaceGrid from './MarketplaceGrid.vue'
import MarketplaceTable from './MarketplaceTable.vue'
import MarketplaceAddCard from './MarketplaceAddCard.vue'
export default {
  name: "CommunityMarketplace",
  components: {
    MarketplaceGrid, MarketplaceTable, MarketplaceAddCard
  },

  data: () => {
    return {
      showingAddCardModal: false,


      displaytype: true,
      // TEST DATA FOR NOW, ONCE PROPER IMPLEMATION OF CARDS IS MADE THIS CAN BE REMOVED
      testData: [
        {
          id: 1,
          title: 'Beans',
          description: 'I need to get rid of these beans someone please buy them $1000 ono' ,
          keywords: [
            {
              id: 1,
              name: 'beans'
            }
          ]
        },
        {
          id: 2,
          title: 'Waste Food',
          description: 'We have a lot of waste food that needs to be sold ',
          keywords: [
            {
              id: 2,
              name: 'waste'
            }
          ]
        },
        {
          id: 3,
          title: 'Silvia s14 SR20DET',
          description: '21k flat no cheaper first in first serve S14 Facelift •SR20DET (TURBO) •Body has done 199xxxkm •Engine had a rebuild done at 197xxxkm •New reconditioned TURBO  •Has current WOF REG AND CERT  •Lowered on adjustable suspension •On mags Any questions please ask',
          keywords: [
            {
              id: 3,
              name: 'carr'
            },
            {
              id: 4,
              name: 'vroom'
            }
          ]
        },

        {
          id: 4,
          title: 'Bad Rats: the Rats\' Revenge Steam Key',
          description: 'Bad Rats: The Rats\' Revenge is a 2009 puzzle video game developed by Invent4 Entertainment. Over a string of levels, the player places a set of rats and static objects to guide a ball towards a trap that kills a cat',
          keywords: [
            {
              id: 3,
              name: 'rats'
            },
            {
              id: 4,
              name: 'game'
            },
            {
              id: 5,
              name: 'steam'
            }
          ]
        },

      ]
    }
  },
  methods: {
    /**
    * Method for closing modal, sets variable to false. This gets called on a custom event 'closeModal' that is triggered in MarketplaceAddCard 
    */
    closeModal: function() {
      this.showingAddCardModal = false;
    },

    /**
    * Method for opening modal by setting state variable
    */
    openModal: function() {
      //Have to set to false first because vuesax pop-ups have an issue where if you click the X the activesync stays true
      this.showingAddCardModal = false;
      this.showingAddCardModal = true;
    }
  },
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