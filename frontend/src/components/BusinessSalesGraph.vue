<template>
  <apexchart width="100%" height="100%" type="bar" :options="salesDatax" :series="salesDatay"></apexchart>
</template>

<script>

export default {
  name: "BusinessSalesGraph",
  props: ['salesDatay', 'salesDatax'],
data: function () {
    return {
      // Used to determine which setting is currently selected - prevents re-clicking, and highlights the active button.
      activePeriodButton: "",
      activeGranularityButton: "",
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
      errors: [],
    }
  },


  mounted: function () {
  },

  methods: {
    /**
     * Something happens when this function is called. (todo: do something here).
     * @param period string of the selected period.
     */
    onPeriodChange: function (period) {
      this.activePeriodButton = period;// Changes the period button to be selected and disabled.
    },

    /**
     * Something happens when this function is called. (todo: do something here).
     * @param period string of the selected granularity.
     */
    onGranularityChange: function (period) {
      this.activeGranularityButton = period; // Changes the granularity button to be selected and disabled.
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
  }
}
</script>

<style scoped>

#report-container {
  background-color: white;
  width: 100%;
  margin: 1em auto;
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