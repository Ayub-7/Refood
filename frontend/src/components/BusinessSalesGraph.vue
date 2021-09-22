<template>
  <div>
    <vs-card id="options-container">
      <div id="period-change-container">
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
                   style="grid-column: 3;">
          1 Week
        </vs-button>
        <vs-button v-bind:class="[{'active-button': activePeriodButton === '1-m'}, 'options-button']"
                   type="border"
                   style="grid-column: 1;">
          1 Month
        </vs-button>
        <vs-button v-bind:class="[{'active-button': activePeriodButton === '6-m'}, 'options-button']"
                   type="border"
                   style="grid-column: 3;">
          6 Months
        </vs-button>
        <vs-button v-bind:class="[{'active-button': activePeriodButton === '1-y'}, 'options-button']"
                   type="border"
                   style="grid-column: 1;">
          1 Year
        </vs-button>
        <vs-button v-bind:class="[{'active-button': activePeriodButton === 'all'}, 'options-button']"
                   type="border"
                   style="grid-column: 3;">
          All
        </vs-button>
      </div>
      <div id="custom-period-container">
        <div class="options-header">Custom</div>
        <vs-input type="date" size="small" class="date-input" style="grid-column: 1" v-model="dateStart" label="Start"
                  danger-text="Date can not be in the future or after the end date"/>
        <p style="margin: auto auto 0">-</p>
        <vs-input type="date" size="small" class="date-input" v-model="dateEnd" label="End"
                  danger-text="Date can not be in the future or after the end date" style="grid-column: 3"/>
        <vs-button type="border" size="small" style="grid-column: 1/4; width: 100px; margin: auto;">Go</vs-button>

      </div>
      <div id="granularity-container">
        <div class="options-header">Summary Interval</div>
        <vs-button v-bind:class="[{'active-button': activeGranularityButton === 'd'}, 'options-button']"
                   type="border"
                   style="grid-column: 1;"
                   @click="getSalesData('day')">
          Day
        </vs-button>
        <vs-button id="week-granularity" v-bind:class="[{'active-button': activeGranularityButton === 'w'}, 'options-button']"
                   type="border"
                   style="grid-column: 3;"
                   @click="getSalesData('week')">
          Week
        </vs-button>
        <vs-button id="month-granularity" v-bind:class="[{'active-button': activeGranularityButton === 'm'}, 'options-button']"
                   type="border"
                   style="grid-column: 1;"
                   @click="getSalesData('month')">
          Month
        </vs-button>
        <vs-button id="year-granularity" v-bind:class="[{'active-button': activeGranularityButton === 'y'}, 'options-button']"
                   type="border"
                   style="grid-column: 3;"
                   @click="getSalesData('year')">
          Year
        </vs-button>
      </div>
    </vs-card>
    <apexchart id="sales-graph-report" width="100%" height="90%" type="bar" :options="options" :series="series"></apexchart>
  </div>
</template>

<script>
import api from "../Api";

export default {
  name: "BusinessSalesGraph",
  props: {
    businessId: {
      type: Number,
    },
    currencySymbol: {
      type: String,
      default: "$",
    }
  },

  data: function() {
    return {
      activeGranularityButton: "m",

      series: [{
        data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
      }],

      options: {
        chart: {
          id: 'sales-graph-report',
        },
        title: {
          style: {
            fontSize: "18px",
            fontWeight: "bold",
            fontFamily: "Ubuntu, sans-serif",
          }
        },
        colors: ["#1F74FF"],
        dataLabels: {
          enabled: false,
        },
        noData: {
          text: "No sales data available"
        }
      },
    }
  },

  methods: {
    /**
     * Retrieve's the business' bought listing data.
     */
    getSalesData: function() {
      api.getBusinessSales(this.businessId)
        .then((res) => {
          let type = this.getNextFinestGranularity(res.data);
          this.displaySalesData(res.data, type);
        })
        .catch((error) => {
          this.$log.error(error);
        });
    },

    getNextFinestGranularity: function(data) {
      if (data.length > 0) {
        let sorted = data.sort((a,b) => new Date(b.sold) - new Date(a.sold));
        let min = sorted[0];
        let max = sorted[sorted.length-1];
        let diff = (new Date(min.sold) - new Date(max.sold)) / (1000*60*60*24);

        if (diff < 7) return "day";
        if (diff < 30) return "week";
        if (diff < 365) return "month";
        else return "year";
      }

      return "month";
    },

    /**
     * Calculate and displays sales data.
     * Currently creates and displays total monthly sales data.
     * @param data bought listings sales data
     */
    displaySalesData: function(data, type) {
      // Categorises and sums up data, splitting each bought listing into it's respective year and month.
      let processedData;
      let allData = [];
      let categories = [];
      let barFormat = "category";
      let labelFormat = "dd-MMM";

      if (type.toLowerCase() === "year") {
        this.activeGranularityButton = "y";
        processedData = this.totalYearlyRevenue(data);
        // Flatten object into array.
        for (let year of Object.entries(processedData)) allData = allData.concat(year[1]);
        // Generates the x-axis labels of each month, for each year.
        categories = Object.keys(processedData);
      } else if (type.toLowerCase() === "month") {
        this.activeGranularityButton = "m";
        barFormat = "datetime";
        labelFormat = "MMMM yyyy";
        processedData = this.totalMonthlyRevenue(data);
        // Flatten object into array.
        for (let year of Object.entries(processedData)) allData = allData.concat(year[1]);
        // Generates the x-axis labels of each month, for each year.
        categories = this.generateMonthLabels(Object.keys(processedData)); //For Monthly Revenue
        // Removes months with no sales up to the first month of sales.
        let i = 0;
        while (i < allData.length) {
          if (allData[i] > 0) {
            allData = allData.slice(i);
            categories = categories.slice(i);
            break;
          }
          i++;
        }
      } else if (type.toLowerCase() === "week") {
        this.activeGranularityButton = "w";
        barFormat = "datetime";
        labelFormat = "dd-MMM";
        processedData = this.totalWeeklyRevenue(data);
        // Flatten object into array.
        for (let year of Object.entries(processedData)) {
          let entries = Object.entries(year[1]).map(week => week[1]);
          allData = allData.concat(entries);
        }
        // Generates the x-axis labels of each month, for each year.
        categories = this.generateWeekLabels(processedData);
      }
      else if (type.toLowerCase() === "day") {
        this.activeGranularityButton = "d";
        barFormat = "datetime";
        labelFormat = "dd-MMM";
        processedData = this.totalDailyRevenue(data);
        categories = this.generateDayLabels(processedData);

        for (let day of Object.entries(processedData)) {
          allData.push(day[1]);
        }

      }
      // Updates the plot options and inputs.
      // Reassigning entire variable allows chart to properly update and play animations.
      this.series = [{
        name: 'Total Value',
        data: allData,
      }];

      this.options = {
        title: {
          text: "Monthly Total Sales",
        },
        yaxis: {
          decimalsInFloat: 2,
          labels: {
            formatter: (val) => this.currencySymbol + val,
            style: {
              fontSize: "12px",
            }
          }
        },
        xaxis: {
          type: barFormat,
          categories: categories,
          labels: {
            datetimeFormatter: {
              year: 'yyyy',
              month: "MMM",
            },
          }
        },
        tooltip: {
          y: {
            formatter: (val) => this.currencySymbol + val.toFixed(2),
          },
          x: {
            format: labelFormat
          }
        },
      };
    },

    /**
     * Categorises and sums up data, splitting each bought listing into it's respective year and month.
     * @param data the bought listing data (i.e. the sold listings).
     * @return object where each key is a year, and the value is an array of size 12, each index representing a month,
     * and the value at each index representing the total revenue/value of listings sold.
     */
    totalMonthlyRevenue: function(data) {
      let processedData = {};
        for (let listing of data) {
          let soldDate = new Date(listing.sold);
          if (processedData[soldDate.getFullYear()] == null) {
            processedData[soldDate.getFullYear()] = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
          }
          processedData[soldDate.getFullYear()][soldDate.getMonth()] += +listing.price.toFixed(2);
        }
      return processedData;
    },

    /**
     * Categorises and sums up data, splitting each bought listing into it's respective year and week.
     * @param data the bought listing data (i.e. the sold listings).
     * @return object where each key is a year, and the value is an array of size 52, each index representing a week,
     * and the value at each index representing the total revenue/value of listings sold.
     */
    totalWeeklyRevenue: function(data) {
      let weeklyData = {};
      for (let listing of data) {
        let soldDate = new Date(listing.sold);

        let dayNum = soldDate.getDay();
        let dateCopy = soldDate;
        dateCopy.setDate(soldDate.getDate() - dayNum + 1);
        let mondayString = dateCopy.getFullYear() + "-" + (dateCopy.getMonth() + 1) + "-" + dateCopy.getDate();

        if (weeklyData[dateCopy.getFullYear()] == null) {
          weeklyData[dateCopy.getFullYear()] = {};
        }

        if (weeklyData[soldDate.getFullYear()][mondayString] == null) {
          weeklyData[soldDate.getFullYear()][mondayString] = 0;
        }
        weeklyData[soldDate.getFullYear()][mondayString] += +listing.price.toFixed(2);
      }

      return weeklyData;

    },
    /**
     * Categorises and sums up data, splitting each bought listing into it's respective year.
     * @param data the bought listing data (i.e. the sold listings).
     * @return object where each key is a year, and the value is an array of size 1,
     * which contains the sales of that year.
     */
    totalYearlyRevenue: function(data) {
      let processedData = {};
      for (let listing of data) {
        let soldDate = new Date(listing.sold);
        if (processedData[soldDate.getFullYear()] == null) {
          processedData[soldDate.getFullYear()] = 0;
        }
        processedData[soldDate.getFullYear()] += +listing.price.toFixed(2);
      }
      return processedData;
    },
    /**
     * Categorises and sums up data, splitting each bought listing into it's respective year and day.
     * @param data the bought listing data (i.e. the sold listings).
     * @return object where each key is a year, and the value is an array of size 365, each index representing a day,
     * and the value at each index representing the total revenue/value of listings sold.
     */
    totalDailyRevenue: function(data) {
      let processedData = {};
      for (let listing of data) {
        let soldDate = new Date(listing.sold);
        let soldString = soldDate.getFullYear() + "-" + (soldDate.getMonth() + 1) + "-" + soldDate.getDate();

        if (processedData[soldString] == null) {
          processedData[soldString] = 0;
        }
        processedData[soldString] += +listing.price.toFixed(2);
      }

      return processedData;
    },

    /**
     * Generates an array of strings containing each month for every given year.
     * Used to generate the x-axis labels.
     * @param years an array of years to generate labels for.
     * @returns {*[]} an array of every month of each year; e.g. 'Apr 2021', 'May 2021'...
     */
    generateMonthLabels: function(years) {
      const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug',
        'Sep', 'Oct', 'Nov', 'Dec'];

      const numOfYears = years.length;
      let yearlyMonthCategories = [];
      for (let i = 0; i < numOfYears; i++) {
        yearlyMonthCategories = yearlyMonthCategories.concat(months.map(month => month + ` ${years[i]}`));
      }

      return yearlyMonthCategories;
    },

    /**
     * Helper function that generates x-axis labels to represent weeks
     * @param data series used to populate the graph
     * @returns {[]} an array of x-axis labels
     */
    generateWeekLabels: function(data) {
        let labels = [];

        for (let year of Object.keys(data)) {
          labels = labels.concat(Object.keys(data[year]));
        }
        return labels;
    },
    /**
     * Helper function that generates x-axis labels to represent days
     * @param data series used to populate the graph
     * @returns {[]} an array of x-axis labels
     */
    generateDayLabels: function(processedData) {
      return Object.entries(processedData).map(day => day[0]);
    }
  },

  mounted: function() {
    this.getSalesData();
  },

}
</script>

<style scoped>
/* === OPTIONS CONTAINER === */
#options-container {
  width: 100%;
  height: fit-content;
}

#options-container >>> .vs-card--content {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  grid-gap: 1em 2px;
  padding-bottom: 16px;
}

#period-change-container, #custom-period-container, #granularity-container {
  display: grid;
  grid-row-gap: 1em;
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

</style>
