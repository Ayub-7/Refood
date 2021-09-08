<template>
  <vs-card id="report-container">
    <div id="header-container">
      <vs-icon icon="summarize" size="32px"></vs-icon>
      <div id="title">Sales Report</div>
      <div id="title-business"> - {{getBusinessName()}}</div>
    </div>
    <vs-divider/>

    <div id="content-container">
      <!-- FILTER/GRANULARITY PICKER -->
      <vs-card id="options-container">
        <div class="options-header" style="display: flex; justify-content: center">
          <span style="padding-right: 4px">Period</span>
          <vs-tooltip text="Adjusts the length of time of each statistic">
            <vs-icon icon="info" size="14px"/>
          </vs-tooltip>
        </div>
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
        <vs-input type="date" size="small" class="date-input" style="grid-column: 1" v-model="dateStart" label="Start" :danger="(errors.includes('past-min-date'))"
                  danger-text="Date can not be in the future or after the end date"/>
        <p style="margin: auto auto 0">-</p>
        <vs-input type="date" size="small" class="date-input" v-model="dateEnd" label="End" :danger="(errors.includes('past-min-date'))"
                  danger-text="Date can not be in the future or after the end date" style="grid-column: 3"/>
        <vs-button type="border" size="small" style="grid-column: 1/4; width: 100px; margin: auto;">Go</vs-button>

        <vs-divider style="grid-column: 1/4; margin: 0 auto;"/>

        <div class="options-header">Summary Interval</div>
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
        <h3>Summary</h3>
        <vs-divider style="margin-top: 4px"/>
        <div v-if="activeGranularityButton==='all'">
          <div class="row-summary-container">
            <h2 class="summary-header">{{currentYearReport.title}}</h2>
            <div class="summary-subheader">NUMBER OF SALES</div>
            <div>{{currentYearReport.totalSales}}</div>
            <div class="summary-subheader">AVG ITEMS PER SALE</div>
            <div>{{currency + currentYearReport.averagePricePerItem}}</div>
            <div class="summary-subheader">TOTAL SALE VALUE</div>
            <div>{{currency + currentYearReport.totalSaleValue}}</div>
            <div class="summary-subheader">AVG SALE VALUE</div>
            <div>{{currency + currentYearReport.averageSale}}</div>
          </div>
        </div>
        <div v-else id="summary-list" v-for="(summary, index) in summaries" :key="index" >
          <div class="row-summary-container">
            <h2 class="summary-header">{{ summary.title }}</h2>
            <div class="summary-subheader">NUMBER OF SALES</div>
            <div>{{ summary.totalSales }}</div>
            <div class="summary-subheader">AVG ITEMS PER SALE</div>
            <div>{{ summary.averageItemsPerSale }}</div>
            <div class="summary-subheader">TOTAL SALE VALUE</div>
            <div>{{currency + summary.totalSaleValue}}</div>
            <div class="summary-subheader">AVG SALE VALUE</div>
            <div>{{currency + summary.averagePricePerItem}}</div>
          </div>
        </div>
      </vs-card>

      <div id="stats-container">
        <div class="stat-box">
          <div class="stat-subheader">Average Sale</div>
          <h2 style="padding-left: 12px">{{currency + currentYearReport.averageSale}}</h2>
          <div class="sub-header stat-change">
            <vs-icon id="iconAverageSale" color="red" icon="arrow_drop_down" class="stat-change-icon"/> <!-- decrease icon -->
            <div>{{increaseFromLastYear(currentYearReport.averageSale, lastYearReport.averageSale)}}% from last year</div>
          </div>
          <div class="sub-header stat-date"> {{this.formatDate(dateStart)}} - {{this.formatDate(dateEnd)}}</div>
        </div>
        <div class="stat-box">
          <div class="stat-subheader">Average Price Per Item</div>
          <h2 style="padding-left: 12px">{{currency + currentYearReport.averagePricePerItem}}</h2>
          <div class="sub-header stat-change">
            <vs-icon color="green" icon="arrow_drop_up" class="stat-change-icon"/> <!-- increase icon -->
            <div>{{increaseFromLastYear(currentYearReport.averagePricePerItem, lastYearReport.averagePricePerItem)}}% from last year</div>
          </div>
          <div class="sub-header stat-date" style="padding-left: 12px;"> {{this.formatDate(dateStart)}} - {{this.formatDate(dateEnd)}}</div>
        </div>
        <div class="stat-box">
          <div class="stat-subheader">Average Items Per Sale</div>
          <h2 style="padding-left: 12px">{{currency + currentYearReport.averageItemsPerSale}}</h2>
          <div class="sub-header stat-change">
            <vs-icon color="red" icon="arrow_drop_down" class="stat-change-icon"/>
            <div>{{increaseFromLastYear(currentYearReport.averageItemsPerSale, lastYearReport.averageItemsPerSale)}}% from last year</div>
          </div>
          <div class="sub-header stat-date"> {{this.formatDate(dateStart)}} - {{this.formatDate(dateEnd)}}</div>
        </div>
        <div class="stat-box">
          <div class="stat-subheader">Total Sale Value</div>
          <h2 style="padding-left: 12px">{{currency + currentYearReport.totalSaleValue}}</h2>
          <div class="sub-header stat-change">
            <vs-icon color="red" icon="arrow_drop_down" class="stat-change-icon"/>
            <div>{{increaseFromLastYear(currentYearReport.totalSaleValue, lastYearReport.totalSaleValue)}}% from last year</div>
          </div>
          <div class="sub-header stat-date" style="padding-left: 12px;"> {{this.formatDate(dateStart)}} - {{this.formatDate(dateEnd)}}</div>
        </div>
        <div class="stat-box">
          <div class="stat-subheader">Total Items Sold</div>
          <h2 style="padding-left: 12px">{{currentYearReport.totalItems}}</h2>
          <div class="sub-header stat-change">
            <vs-icon color="red" icon="arrow_drop_down" class="stat-change-icon"/>
            <div>{{increaseFromLastYear(currentYearReport.totalItems, lastYearReport.totalItems)}}% from last year</div>
          </div>
          <div class="sub-header stat-date" style="padding-left: 12px;"> {{this.formatDate(dateStart)}} - {{this.formatDate(dateEnd)}}</div>
        </div>
        <div class="stat-box">
          <div class="stat-subheader">Total Sales</div>
          <h2 style="padding-left: 12px">{{currentYearReport.totalSales}}</h2>
          <div class="sub-header stat-change">
            <vs-icon color="red" icon="arrow_drop_down" class="stat-change-icon"/>
            <div>{{increaseFromLastYear(currentYearReport.totalSales, lastYearReport.totalSales)}}% from last year</div>
          </div>
          <div class="sub-header stat-date" style="padding-left: 12px;"> {{this.formatDate(dateStart)}} - {{this.formatDate(dateEnd)}}</div>
        </div>
      </div>
    </div>
  </vs-card>
</template>

<script>
import api from "../Api";
import axios from "axios";
import {store} from "../store";
const moment = require('moment');

export default {
  name: "BusinessSalesReport",

  data: function() {
    return {
      // Used to determine which setting is currently selected - prevents re-clicking, and highlights the active button.
      activePeriodButton: "",
      activeGranularityButton: "all",
      currency: "$",
      actingAsBusinessId: '',
      business: [],
      salesHistory: [],
      dateStart: null,
      dateEnd: null,
      currentYearReport: {},
      lastYearReport: {},
      summaries: [
         {
          title: "January 2020",
          averageSale: 100.00,
          averagePricePerItem: 58.92,
          averageItemsPerSale: 3,
          totalSaleValue: 5202.92,
          totalSales: 1106
        },
        {
          title: "February 2020",
          averageSale: 100.00,
          averagePricePerItem: 58.92,
          averageItemsPerSale: 3,
          totalSaleValue: 5202.92,
          totalSales: 1106
        },
        {
          title: "March 2020",
          averageSale: 100.00,
          averagePricePerItem: 58.92,
          averageItemsPerSale: 3,
          totalSaleValue: 5202.92,
          totalSales: 1106
        }
      ],
      errors: []
    }
  },


  mounted: function() {
    this.getSession();

    this.getSalesHistory();

    let currentDate = new Date();
    //we gotta set date to a string or the bloody thing complains
    this.dateStart = "Jan 01, " + currentDate.getFullYear();
    this.dateEnd = "Dec 31, " + currentDate.getFullYear();
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
      if (period === 'w') {
        //filter weeks
        this.granularityWeeks()
        console.log(period)
      } else if (period === 'm') {
        //filter month
        this.granularityMonths()
        console.log(period)
      } else if (period === 'y') {
        //filter year
        this.granularityYears()
        console.log(period)
      }
      this.activeGranularityButton = period; // Changes the granularity button to be selected and disabled.

    },

    /**
     * filter weeks granularity
     */
    granularityWeeks: function () {
      console.log("weeks");
    },

    /**
     * filter weeks granularity
     */
    granularityMonths: function () {
      console.log("months");
    },

    /**
     * filter weeks granularity
     */
    granularityYears: function () {
      console.log("years");
    },

    /**
     * Calculates the percentage increase from last year
     * If last year had no sales, return 100% increase
     *
     * @param thisyear The current year's figure
     * @param lastyear The previous year's figure
     */
    increaseFromLastYear(thisYear, lastYear) {
      if (lastYear === 0) {
        return 100;
      }
      return (thisYear - lastYear) / lastYear;
    },

    /**
     * Calls getBusinessListingNotifications to populate the page's sales history
     */
    getSalesHistory: function () {
      api.getBusinessListingNotifications(this.actingAsBusinessId)
          .then((res) => {
            this.salesHistory = res.data;

            //only once we have obtained the data, calculate the variables
            this.calculateReport();
          })
          .catch(err => {
            this.$log.debug(err);
          });
    },

    formatDate: function(date) {
      return moment(new Date(date)).format('MMM DD, YYYY');
    },

    /**
     * Calls get session endpoint to get user country, if successful calls setCurrency ()
     */
    getSession: function() {
      api.checkSession()
          .then((response) => {
            this.setCurrency(response.data.homeAddress.country)
          })
          .catch(err => {
            this.$log.debug(err);
          });
    },
    /**
     * return the name of the business currently administering
     * also, sets the accting as business id to update the sales report
     */
    getBusinessName: function() {
      if (store.actingAsBusinessId !== this.actingAsBusinessId) {
        this.actingAsBusinessId = store.actingAsBusinessId;

        this.getSalesHistory();
      }

      return store.actingAsBusinessName;
    },

    /**
     * calculateReport
     * Populates the report data metrics
     * First separates the salesHistory into the selected period and that same period from the year before
     * Then it calculates the summary report from both
     *
     * Later these are used in the business metrics and % increases from last year
     */
    calculateReport: function() {
      let start = moment(new Date(this.dateStart));
      let end = moment(new Date(this.dateEnd));
      let currentYearSalesHistory = this.salesHistory.filter(sale => moment(sale).isBetween(start, end));

      start = start.subtract(1,'year');
      end = end.subtract(1,'year');
      let lastYearSalesHistory = this.salesHistory.filter(sale => moment(sale).isBetween(start, end));

      this.currentYearReport = this.calculateSummary(currentYearSalesHistory, "Current Period's Report");
      this.lastYearReport = this.calculateSummary(lastYearSalesHistory, "Last period's Report");
    },

    /**
     * calculates the summary object given a list of sales in that period
     * @param salesHistory List of sales in the given period. ie All the sales in january
     * @param title Title of the summary ie "January 2020"
     */
    calculateSummary: function(salesHistory, title) {
      let summary= {};
      let totalPrice = 0;
      let totalQuantity = 0;
      summary.title = title
      if(salesHistory.length > 0) {
        for(let i=0;i<salesHistory.length;i++) {
          totalPrice += salesHistory[i].boughtListing.price;
          totalQuantity += salesHistory[i].boughtListing.quantity;
        }
        summary.averageSale = Number(totalPrice/salesHistory.length).toFixed(2);
        summary.averagePricePerItem = Number(totalPrice / totalQuantity).toFixed(2);
        summary.averageItemsPerSale = Number(totalQuantity / salesHistory.length).toFixed(2);
        summary.totalSaleValue = Number(totalPrice).toFixed(2);
        summary.totalItems = Number(totalQuantity).toFixed(2);
        summary.totalSales = salesHistory.length;

      } else {
        summary.averageSale = 0.00;
        summary.averagePricePerItem = 0.00;
        summary.averageItemsPerSale = 0;
        summary.totalSaleValue = 0.00;
        summary.totalItems = 0.00;
        summary.totalSales = 0;
      }
      return summary;
    },

    /**
     * Sets display currency based on the user's home country.
     */
    setCurrency: function (country) {
      axios.get(`https://restcountries.eu/rest/v2/name/${country}`)
          .then(response => {
            this.currency = response.data[0].currencies[0].symbol;
          })
          .catch(err => {
            this.$log.debug(err);
          });
    },
  },
}
</script>

<style scoped>

#report-container {
  background-color: white;
  width: 100%;
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
  margin-top: 4px;
  margin-right: 8px;
}
#title-business {
  font-size: 30px;
  font-weight: bold;
  margin-top: 4px;
  margin-left: 0px;
  margin-right: auto;
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
}

#summary-container >>> .vs-card--content {
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
