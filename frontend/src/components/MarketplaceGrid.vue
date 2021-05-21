<template>
    <div>
    <vs-card id="cardContainer">
        <div>
          <vs-row id="marketRow">
            <!-- Change vs-lg to 2 if you want 6 per row or 3 if you want 4 per row -->
            <vs-col id="marketCard" type="flex" vs-justify="center" vs-align="center" vs-lg="3" vs-sm="12" v-for="card in cardData" :key="card.id">
              <div style="margin: 10px; width: 90%;">
                <vs-card actionable>
                  <div slot="media" id="cardHeader">
                    <img id="marketImage" :src="require('../../public/ProductShoot.jpg')" />
                  </div>
                  <div>
                    <div id="cardTitle">{{card.title}}</div>
                    <!-- Need to add limit or something to description -->
                    <div id="cardDescription">{{card.description}}</div>
                    <vs-row>
                      <div id="keywordWrapper">
                      <vs-col type="flex" v-for="keyword in card.keywords" :key="keyword.id" vs-w="3">
                        <div id="cardKeywords">#{{keyword.name}}</div>
                      </vs-col>
                      </div>
                    </vs-row>
                  </div>
                </vs-card>
              </div>
            </vs-col>
          </vs-row>
        </div>
    </vs-card>
    </div>
</template>

<script>
export default {
    props: ['cardData']

}
</script>

<style>

/* CARD STYLING */

#marketImage {
  width: 100%;
  height: auto;
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

#cardTitle {
  height: 40px;
}

#cardDescription {
  margin-top: 10px;
  height: 120px;
  overflow-y: scroll;
}

#cardKeywords {
  color: #1F74FF;
  font-size: 15px;
}

#keywordWrapper {
  margin-top: 10px;
  height:50px;
  width: 100%;
  overflow-y: scroll;
}


</style>