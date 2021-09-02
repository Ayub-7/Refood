<template>
  <vs-card id="container">
    <div id="header-container">
      <vs-icon icon="summarize" size="32px"></vs-icon>
      <div id="title">Sales Report</div>
    </div>
    <vs-divider/>

    <div id="content-container">
      <!-- FILTER/GRANULARITY PICKER -->
      <vs-card id="options-container">
        <div class="options-header">Period</div>
        <vs-button v-bind:class="[{'active-button': activePeriodButton === '1-d'}, 'options-button']"
                   type="border"
                   style="grid-column: 1;"
                   @click="onPeriodChange('1-d')">
          1 Day
        </vs-button>
        <vs-button v-bind:class="[{'active-button': activePeriodButton === '1-w'}, 'options-button']"
                   type="border"
                   style="grid-column: 3;"
                   @click="onPeriodChange('1-w')">
          1 Week
        </vs-button>
        <vs-button v-bind:class="[{'active-button': activePeriodButton === '1-m'}, 'options-button']"
                   type="border"
                   style="grid-column: 1;"
                   @click="onPeriodChange('1-m')">
          1 Month
        </vs-button>
        <vs-button v-bind:class="[{'active-button': activePeriodButton === '6-m'}, 'options-button']"
                   type="border"
                   style="grid-column: 3;"
                   @click="onPeriodChange('6-m')">
          6 Months
        </vs-button>
        <vs-button v-bind:class="[{'active-button': activePeriodButton === '1-y'}, 'options-button']"
                   type="border"
                   style="grid-column: 1;"
                   @click="onPeriodChange('1-y')">
          1 Year
        </vs-button>
        <vs-button v-bind:class="[{'active-button': activePeriodButton === 'all'}, 'options-button']"
                   type="border"
                   style="grid-column: 3;"
                   @click="onPeriodChange('all')">
          All
        </vs-button>
        <div class="options-header" style="font-size: 14px">Custom</div>
        <vs-input type="date" size="small" class="date-input" label="Start" style="grid-column: 1"/>
        <p style="margin: auto auto 0">-</p>
        <vs-input type="date" size="small" class="date-input" label="End" style="grid-column: 3"/>
        <vs-button type="border" size="small" style="grid-column: 1/4; width: 100px; margin: auto;">Go</vs-button>

        <vs-divider style="grid-column: 1/4; margin: 0 auto;"/>

        <div class="options-header">Granularity</div>
        <vs-button v-bind:class="[{'active-button': activeGranularityButton === 'w'}, 'options-button']"
                   type="border"
                   style="grid-column: 1;"
                   @click="onGranularityChange('w')">
          Week
        </vs-button>
        <vs-button v-bind:class="[{'active-button': activeGranularityButton === 'm'}, 'options-button']"
                   type="border"
                   style="grid-column: 3;"
                   @click="onGranularityChange('m')">
          Month
        </vs-button>
        <vs-button v-bind:class="[{'active-button': activeGranularityButton === 'y'}, 'options-button']"
                   type="border"
                   style="grid-column: 1;"
                   @click="onGranularityChange('y')">
          Year
        </vs-button>
        <vs-button v-bind:class="[{'active-button': activeGranularityButton === 'all'}, 'options-button']"
                   type="border"
                   style="grid-column: 3;"
                   @click="onGranularityChange('all')">
          All
        </vs-button>
      </vs-card>

      <vs-card id="summary-container">
        <div class="row-summary-container">
          <h2 class="summary-header">January 2020</h2>
          <div class="summary-subheader">NUMBER OF SALES</div>
          <div>1106</div>
          <div class="summary-subheader">AVG ITEMS PER SALE</div>
          <div>3</div>

          <div class="summary-subheader">TOTAL SALE VALUE</div>
          <div>$5202.92</div>
          <div class="summary-subheader">AVG SALE VALUE</div>
          <div>$58.92</div>
        </div>
      </vs-card>

      <div id="stats-container">
        <div class="stat-box">
          <div class="stat-subheader">Average Sale</div>
          <h2 style="padding-left: 12px">$56.45</h2>
          <div class="sub-header stat-change">
            <vs-icon color="red" icon="arrow_drop_down" class="stat-change-icon"/> <!-- decrease icon -->
            <div>12.34% from last year</div>
          </div>
          <div class="sub-header stat-date">Jan 1 2020 - Dec 31 2020</div>
        </div>
        <div class="stat-box">
          <div class="stat-subheader">Average Price Per Item</div>
          <h2 style="padding-left: 12px">$20.69</h2>
          <div class="sub-header stat-change">
            <vs-icon color="green" icon="arrow_drop_up" class="stat-change-icon"/> <!-- increase icon -->
            <div>12.34% from last year</div>
          </div>
          <div class="sub-header stat-date">Jan 1 2020 - Dec 31 2020</div>
        </div>
        <div class="stat-box">
          <div class="stat-subheader">Average Items Per Sale</div>
          <h2 style="padding-left: 12px">$56.45</h2>
          <div class="sub-header stat-change">
            <vs-icon color="red" icon="arrow_drop_down" class="stat-change-icon"/>
            <div>12.34% from last year</div>
          </div>
          <div class="sub-header stat-date">Jan 1 2020 - Dec 31 2020</div>
        </div>
        <div class="stat-box">
          <div class="stat-subheader">Total Sale Value</div>
          <h2 style="padding-left: 12px">$564545.45</h2>
          <div class="sub-header stat-change">
            <vs-icon color="red" icon="arrow_drop_down" class="stat-change-icon"/>
            <div>12.34% from last year</div>
          </div>
          <div class="sub-header stat-date">Jan 1 2020 - Dec 31 2020</div>
        </div>
        <div class="stat-box">
          <div class="stat-subheader">Total Items Sold</div>
          <h2 style="padding-left: 12px">696</h2>
          <div class="sub-header stat-change">
            <vs-icon color="red" icon="arrow_drop_down" class="stat-change-icon"/>
            <div>12.34% from last year</div>
          </div>
          <div class="sub-header stat-date">Jan 1 2020 - Dec 31 2020</div>
        </div>
        <div class="stat-box">
          <div class="stat-subheader">Total Sales</div>
          <h2 style="padding-left: 12px">$56.45</h2>
          <div class="sub-header stat-change">
            <vs-icon color="red" icon="arrow_drop_down" class="stat-change-icon"/>
            <div>12.34% from last year</div>
          </div>
          <div class="sub-header stat-date">Jan 1 2020 - Dec 31 2020</div>
        </div>
      </div>
    </div>
  </vs-card>
</template>

<script>
export default {
  name: "BusinessSalesReport",

  data: function() {
    return {
      // Used to determine which setting is currently selected - prevents re-clicking, and highlights the active button.
      activePeriodButton: "",
      activeGranularityButton: "",
    }
  },

  methods: {

    /**
     * Something happens when this function is called. (todo: do something here).
     * @param period string of the selected period.
     */
    onPeriodChange: function(period) {
      this.activePeriodButton = period;// Changes the period button to be selected and disabled.
    },

    /**
     * Something happens when this function is called. (todo: do something here).
     * @param period string of the selected granularity.
     */
    onGranularityChange: function(period) {
      this.activeGranularityButton = period; // Changes the granularity button to be selected and disabled.
    }
  }

}
</script>

<style scoped>

#container {
  background-color: white;
  width: 75%;
  margin: 1em auto;
}

#header-container {
  display: flex;
  margin: auto;
  padding-bottom: 0.5em;
  padding-top: 1em;
}

#title {
  font-size: 30px;
  margin: auto auto auto 4px;
}

#content-container {
  display: grid;
  grid-template-columns: 1fr 3fr auto;
  grid-column-gap: 1em;
  justify-content: space-around;

}

/* === OPTIONS CONTAINER === */
#options-container {
  width: fit-content;
  height: fit-content;
}

#options-container >>> .vs-card--content {
  display: grid;
  grid-template-columns: 150px 4px 150px;
  grid-gap: 1em 2px;
  padding-bottom: 16px;
}

.options-header {
  grid-column: 1/4;
  text-align: center;
  font-weight: 500;
  font-size: 18px;
}

.options-button {
  width: 100px;
  margin: auto;
}

.active-button {
  background-color: rgb(31,116,255)!important;
  color: white;
  pointer-events: none;
}

.date-input {
  width: 136px;
  margin: auto;
}

/* MONTH/WEEK/YEAR REPORT CONTAINER */
#summary-container {
  overflow-y: scroll;
  height: 685px;
}

.row-summary-container {
  display: grid;
  grid-template-columns: 2fr 1fr 2fr 1fr;
  grid-column-gap: 1em;
  padding-bottom: 1em;
}

.summary-header {
  grid-column: 1/5;
}

.summary-subheader {
  font-size: 12px;
  text-align: right;
  color: gray;
}

/* STATS CONTAINER */
#stats-container {
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-column-gap: 1em;
  grid-row-gap: 1em;
  height: fit-content;
}

.stat-box {
  border: 1px solid rgba(0, 0, 0, 0.16);
  border-radius: 12px;
  height: fit-content;
  width: fit-content;
  padding: 12px 12px 12px 0;

  display: flex;
  flex-direction: column;
}

.stat-change {
  display: inline-flex;
  padding-left: 8px;
}

.stat-change-icon {
  margin: auto 0;
  font-size: 20px;
}

.stat-subheader {
  font-weight: bold;
  font-size: 12px;
  padding-left: 12px;
}

.stat-date {
  padding-left: 12px;
  font-size: 10px;
}

@media screen and (max-width: 1600px) {
  #stats-container {
    grid-template-columns: 1fr;
  }
}

@media screen and (max-width: 1310px) {
  #content-container {
    grid-template-columns: auto 2fr;
  }

  #stats-container {
    grid-column: 1/3;
    grid-template-columns: repeat(auto-fit, 160px);
    justify-content: space-around;
  }
}

@media screen and (max-width: 975px) {
  #content-container {
    grid-template-columns: 1fr;
    grid-row-gap: 1em;
  }

  #options-container {
    margin: 0 auto;
  }

  #summary-container{
    grid-column: 1;
  }

  #stats-container{
    grid-column: 1;
  }
}

</style>
