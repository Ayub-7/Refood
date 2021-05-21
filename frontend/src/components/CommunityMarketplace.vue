<template>
  <div>
    <vs-tabs alignment="center">
      <vs-tab label="For Sale">
        <vs-card id="cardContainer">
        <div>
          <vs-row id="marketRow">
            <vs-col id="marketCard" type="flex" vs-justify="center" vs-align="center" vs-lg="3" vs-sm="12" v-for="card in testData" :key="card.id">
              <div style="margin: 10px;">
                <vs-card actionable :fixed-height="true">
                  <div slot="media" id="cardHeader">
                    <img id="marketImage" :src="require('../../public/ProductShoot.jpg')" />
                  </div>
                  <div>
                    <div id="cardTitle">{{card.title}}</div>
                    <div id="cardDescription">{{card.description}}</div>
                  </div>
                </vs-card>
              </div>
            </vs-col>
          </vs-row>
        </div>
        </vs-card>
      </vs-tab>
      <vs-tab label="Wanted"><div></div></vs-tab>
      <vs-tab label="Exchange"><div></div></vs-tab>
    </vs-tabs>
  </div>
</template>

<script>
export default {
  name: "CommunityMarketplace",

  data: () => {
    return {
      testData: [
        {
          id: 1,
          title: 'Marketplace Item 1',
          description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque interdum ut mi et tempor. Morbi nec molestie eros. Donec ornare. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque interdum ut mi et tempor. Morbi nec molestie eros. Donec ornare.'
        },
        {
          id: 2,
          title: 'Marketplace Item 2',
          description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque interdum ut mi et tempor. Morbi nec molestie eros. Donec ornare.'
        },
        {
          id: 3,
          title: 'Marketplace Item 3',
          description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque interdum ut mi et tempor. Morbi nec molestie eros. Donec ornare.'
        },
        {
          id: 4,
          title: 'Marketplace Item 1',
          description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque interdum ut mi et tempor. Morbi nec molestie eros. Donec ornare.'
        },
        {
          id: 5,
          title: 'Marketplace Item 2',
          description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque interdum ut mi et tempor. Morbi nec molestie eros. Donec ornare.'
        },
        {
          id: 6,
          title: 'Marketplace Item 3',
          description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque interdum ut mi et tempor. Morbi nec molestie eros. Donec ornare.'
        }
      ]
    }
  }
}

</script>

<style scoped>

vs-tab {
  color: #1F74FF;
}

#marketImage {
  width: 100%;
  height: auto;
}

#marketCard {
  margin: 0px;
}

#cardTitle {
  font-weight: bold;
  font-size: 17px;
}

#cardContainer {
  padding: 15px;
  width: 90%;
  margin: auto;
}

#marketRow {
}


</style>