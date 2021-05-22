<template>
    <div>
    <vs-card id="cardContainer">
        <div>
          <vs-row id="marketRow">
            <!-- Change vs-lg to 2 if you want 6 per row or 3 if you want 4 per row -->
            <vs-col id="marketCard" type="flex" vs-justify="center" vs-align="center" vs-lg="3" vs-sm="12" v-for="card in cardData" :key="card.id">
              <div style="margin: 10px; width: 90%;">
                <!-- Marketplace Card -->
                <vs-card actionable>
                  <div slot="media" id="cardHeader">
                    <!-- Default image for now -->
                    <img id="marketImage" :src="require('../../public/ProductShoot.jpg')" />
                  </div>
                  <div>
                    <div id="cardTitle">{{card.title}}</div>
                    <!-- Need to add limit or something to description -->
                    <div id="cardDescription">{{card.description}}</div>
                    <!-- Keyword display -->
                      <div id="keywordWrapper">
                        <div id="cardKeywords"  v-for="keyword in card.keywords" :key="keyword.id" >#{{keyword.name}}</div>
                      </div>
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
  height: 50px;
}

#cardDescription {
  margin-top: 10px;
  height: 120px;
  overflow-y: scroll;

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
  overflow-y: scroll;
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