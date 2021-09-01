<template>
  <vs-card id="container">
    <div id="header-container">
      <vs-icon icon="summarize" size="32px"></vs-icon>
      <div id="title">Sales Report</div>
      <div id="title-business" v-if="this.business != null"> - {{this.business.name}}</div>
    </div>
    <vs-divider/>

    <div id="content-container">
      <!-- FILTER/GRANULARITY PICKER -->
      <vs-card id="options-container">
        <div class="options-header">Period</div>
        <vs-button class="options-button" type="border">1 Day</vs-button>
        <vs-button class="options-button" type="border">1 Week</vs-button>
        <vs-button class="options-button" type="border">1 Month</vs-button>
        <vs-button class="options-button" type="border">6 Months</vs-button>
        <vs-button class="options-button" type="border">1 Year</vs-button>
        <vs-button class="options-button" type="border">All</vs-button>
        <div class="options-header" style="font-size: 14px">Custom</div>
        <vs-input class="date-input" v-model="dateStart" label="Start" :danger="(errors.includes('past-min-date'))"
                  danger-text="Date can not be in the future or after the end date" type="date" style="grid-row: 6"/>
        <vs-input class="date-input" v-model="dateEnd" label="End" :danger="(errors.includes('past-min-date'))"
                  danger-text="Date can not be in the future or after the end date" type="date" style="grid-row: 6"/>

        <vs-divider style="grid-column: 1/3;"/>
        <div class="options-header">Granularity</div>
        <vs-button class="options-button" type="border">Week</vs-button>
        <vs-button class="options-button" type="border">Month</vs-button>
        <vs-button class="options-button" type="border">Year</vs-button>
        <vs-button class="options-button" type="border">All</vs-button>
      </vs-card>

      <!-- for each in month list
      AVG SALE VALUE - should we keep naming consistent?
      -->
      <vs-card id="summary-container" v-for="summary in summaries" :key="summary.id" >

        <div class="row-summary-container">
          <h2 class="summary-header">January 2020</h2>
          <div class="summary-subheader">NUMBER OF SALES</div>
          <div>1106</div>
          <div class="summary-subheader">AVG ITEMS PER SALE</div>
          <div>3</div>

          <div class="summary-subheader">TOTAL SALE VALUE</div>
          <div>{{this.currency}}5202.92</div>
          <div class="summary-subheader">AVG SALE VALUE</div>
          <div>{{this.currency}}58.92</div>
        </div>

<!--          <vs-divider style="grid-column: 1/5;"/>-->
      </vs-card>

      <div id="stats-container">
        <div class="stat-box">
          <div class="stat-subheader">Average Sale</div>
          <h2 style="padding-left: 12px">{{currency + averageSale}}</h2>
          <div class="sub-header stat-change">
            <vs-icon color="red" icon="arrow_drop_down" class="stat-change-icon"/>
            <div>12.34% from last year</div>
          </div>
          <div class="sub-header" style="padding-left: 12px;"> {{this.formatDate(dateStart)}} - {{this.formatDate(dateEnd)}}</div>
        </div>
        <div class="stat-box">
          <div class="stat-subheader">Average Price Per Item</div>
          <h2 style="padding-left: 12px">{{currency + averagePricePerItem}}</h2>
          <div class="sub-header stat-change">
            <vs-icon color="green" icon="arrow_drop_up" class="stat-change-icon"/>
            <div>12.34% from last year</div>
          </div>
          <div class="sub-header" style="padding-left: 12px;"> {{this.formatDate(dateStart)}} - {{this.formatDate(dateEnd)}}</div>
        </div>
        <div class="stat-box">
          <div class="stat-subheader">Average Items Per Sale</div>
          <h2 style="padding-left: 12px">{{currency + averageItemsPerSale}}</h2>
          <div class="sub-header stat-change">
            <vs-icon color="red" icon="arrow_drop_down" class="stat-change-icon"/>
            <div>12.34% from last year</div>
          </div>
          <div class="sub-header" style="padding-left: 12px;"> {{this.formatDate(dateStart)}} - {{this.formatDate(dateEnd)}}</div>
        </div>
        <div class="stat-box">
          <div class="stat-subheader">Total Sale Value</div>
          <h2 style="padding-left: 12px">{{currency + totalSaleValue}}</h2>
          <div class="sub-header stat-change">
            <vs-icon color="red" icon="arrow_drop_down" class="stat-change-icon"/>
            <div>12.34% from last year</div>
          </div>
          <div class="sub-header" style="padding-left: 12px;"> {{this.formatDate(dateStart)}} - {{this.formatDate(dateEnd)}}</div>
        </div>
        <div class="stat-box">
          <div class="stat-subheader">Total Items Sold</div>
          <h2 style="padding-left: 12px">{{totalItems}}</h2>
          <div class="sub-header stat-change">
            <vs-icon color="red" icon="arrow_drop_down" class="stat-change-icon"/>
            <div>12.34% from last year</div>
          </div>
          <div class="sub-header" style="padding-left: 12px;"> {{this.formatDate(dateStart)}} - {{this.formatDate(dateEnd)}}</div>
        </div>
        <div class="stat-box">
          <div class="stat-subheader">Total Sales</div>
          <h2 style="padding-left: 12px">{{totalSales}}</h2>
          <div class="sub-header stat-change">
            <vs-icon color="red" icon="arrow_drop_down" class="stat-change-icon"/>
            <div>12.34% from last year</div>
          </div>
          <div class="sub-header" style="padding-left: 12px;"> {{this.formatDate(dateStart)}} - {{this.formatDate(dateEnd)}}</div>
        </div>
      </div>
    </div>
  </vs-card>
</template>

<script>
import api from "../Api";
import axios from "axios";
const moment = require('moment');

export default {
  name: "BusinessSalesReport",

  data: function() {
    return {
      currency: "$",
      businessId: '',
      business: [],
      salesHistory: [],
      averageSale: 0,
      averagePricePerItem: 0,
      averageItemsPerSale: 0,
      totalSaleValue: 0,
      totalItems: 0,
      totalSales: 0,
      dateStart: null,
      dateEnd: null,
      summaries: [],
      errors: []
    }
  },


  mounted: function() {
    this.businessId = this.$route.params.id;
    this.getSalesHistory();
    this.getBusiness();
    this.getSession();

    let currentDate = new Date();
    //we gotta set date to a string or the bloody thing complains
    this.dateStart = "Jan 01, " + currentDate.getFullYear();
    this.dateEnd = "Dec 31, " + currentDate.getFullYear();
  },

  methods: {
    /**
     * Calls get sales history
     */
    getSalesHistory: function () {
      api.getBusinessListingNotifications(this.businessId)
          .then((res) => {
            this.salesHistory = res.data;

            console.log(this.salesHistory);

            //only once we have obtained the data, calculate the variables
            this.calculateReport();
            this.calculateMonthlySaleSummaries();
          })
          .catch(err => {
            console.log(err)
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
     * Get the business name to populate the title
     * TODO: do we use this OR store.businessname?
     */
    getBusiness: function () {
      api.getBusinessFromId(this.$route.params.id)
          .then((res) => {
            this.business = res.data;
          })
          .catch((error) => {
            if (error) {
              if (error.response.status === 406) {
                this.$vs.notify({title:'Error', text:'This business does not exist.', color:'danger', position:'top-center'})
              }
            }
            this.$log.error(`ERROR trying to obtain business info from Id: ${error}`);
          });
    },

    /**
     * calculates all the variables at once
     * much more efficient than having a loop for every parameter...
     */
    calculateReport: function() {
      let totalPrice = 0;
      let totalQuantity = 0;
      if(this.salesHistory) {
        for(let i=0;i<this.salesHistory.length;i++) {
          totalPrice += this.salesHistory[i].boughtListing.price;
          totalQuantity += this.salesHistory[i].boughtListing.quantity;
        }
      }
      console.log(this.salesHistory)

      this.averageSale = Number(totalPrice/this.salesHistory.length).toFixed(2);
      this.averagePricePerItem = Number(totalPrice / totalQuantity).toFixed(2);
      this.averageItemsPerSale = Number(totalQuantity / this.salesHistory.length).toFixed(2);
      this.totalSaleValue = Number(totalPrice).toFixed(2);
      this.totalItems = Number(totalQuantity).toFixed(2);
      this.totalSales = this.salesHistory.length;
    },

    calculateSummary: function(salesHistory) {
      let summary= {};
      let totalPrice = 0;
      let totalQuantity = 0;
      if(salesHistory) {
        for(let i=0;i<salesHistory.length;i++) {
          totalPrice += salesHistory[i].boughtListing.price;
          totalQuantity += salesHistory[i].boughtListing.quantity;
        }
      }
      console.log(salesHistory)

      summary.averageSale = Number(totalPrice/salesHistory.length).toFixed(2);
      summary.averagePricePerItem = Number(totalPrice / totalQuantity).toFixed(2);
      summary.averageItemsPerSale = Number(totalQuantity / salesHistory.length).toFixed(2);
      summary.totalSaleValue = Number(totalPrice).toFixed(2);
      summary.totalItems = Number(totalQuantity).toFixed(2);
      summary.totalSales = salesHistory.length;

      return summary;
    },

    setDatePeriod: function() {
      return this.salesHistory.filter(function (d) {
        return d >= this.dateStart && d <= this.dateEnd;
      });
    },


    calculateMonthlySaleSummaries: function() {
      for (let i=0;i<12;i++) {
        let start = new Date(i+'-01-'+this.dateStart.getFullYear())
        let end = new Date(i+1+'-01-'+this.dateStart.getFullYear())

        let monthlyHistory = this.salesHistory.filter(function (salesItem) {
          return salesItem >= start && salesItem <= end;
         });
        let summary = this.calculateSummary(monthlyHistory);
        this.summaries.push(summary);
      }
      console.log(this.summaries);
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
  display: flex;
  justify-content: space-around;
}

/* OPTIONS CONTAINER */
#options-container {
  max-width: 300px;
}

#options-container >>> .vs-card--content {
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-gap: 1em 4px;
  padding-bottom: 16px;
}

.options-header {
  grid-column: 1/3;
  text-align: center;
  font-weight: 500;
  font-size: 18px;
}

.options-button {
  width: 100px;
  margin: auto;
}

.date-input {
  width: 140px;
  margin: auto;
}

/* MONTH/WEEK/YEAR REPORT CONTAINER */
#summary-container {
  /*border: black 1px solid;*/
  max-width: 600px;
  margin: 0 2em;
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

</style>
