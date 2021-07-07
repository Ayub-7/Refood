<template>
    <div id="grid-container" style="margin: auto">
        <div>
          <vs-row id="marketRow">
            <!-- Change vs-lg to 2 if you want 6 per row or 3 if you want 4 per row -->
            <vs-col id="marketCard" type="flex" vs-justify="center" vs-align="center" vs-lg="3" vs-sm="12" v-for="card in cardData" :key="card.id">
              <div style="margin: 10px; width: 90%;">
                <!-- Marketplace Card -->
                <vs-card actionable>
                  <div slot="media" id="cardHeader">
                    <!-- Default image for now -->
                    <img id="marketImage" src="../../public/ProductShoot.jpg" alt="Product image"/>
                  </div>
                  <div>
                    <div id="cardTitle">{{card.title}}</div>
                    <!-- Need to add limit or something to description -->
                    <div id="cardDescription">{{card.description}}</div>
                    <!-- Keyword display -->
                      <div id="keywordWrapper">
                        <div id="cardKeywords"  v-for="keyword in getKeywords(card.keywords)" :key="keyword.id" >#{{keyword.name}}</div>
                      </div>
                  </div>
                </vs-card>
              </div>
            </vs-col>
          </vs-row>
        </div>
    </div>
</template>

<script>
export default {
  props: ['cardData'],
  methods: {

    getKeywords: function(keywords) {
      keywords = keywords.split(" ");
      let tmpKeywords = [];

      for(let i=0;i<keywords.length;i++) {
        let keyword = {};
        keyword.key = i;
        keyword.name = keywords[i];
        tmpKeywords.push(keyword);
      }
      return tmpKeywords; // keywords.split(" ")
    }
  }
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
