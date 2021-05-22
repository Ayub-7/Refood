<template>
  <div>
    <vs-tabs alignment="center">
      <vs-tab label="For Sale">
        <div>
          <MarketplaceGrid :cardData="testData" />
        </div>
      </vs-tab>
      <vs-tab label="Wanted">
        <div>
          <MarketplaceGrid :cardData="testData.slice(1, 4)" />
        </div>
      </vs-tab>
      <vs-tab label="Exchange">
        <div>
          <MarketplaceGrid :cardData="testData.slice(1,2)" />
        </div>
      </vs-tab>
    </vs-tabs>
  </div>
</template>

<script>
import MarketplaceGrid from './MarketplaceGrid.vue'
export default {
  name: "CommunityMarketplace",
  components: {
    MarketplaceGrid
  },

  data: () => {
    return {
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


</style>