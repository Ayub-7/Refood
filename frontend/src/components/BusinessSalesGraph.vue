<template>
  <apexchart id="sales-graph-report" width="100%" height="100%" type="bar" :options="options" :series="series"></apexchart>
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
          this.displaySalesData(res.data);
        })
        .catch((error) => {
          this.$log.error(error);
        });
    },

    /**
     * Calculate and displays sales data.
     * Currently creates and displays total monthly sales data.
     * @param data bought listings sales data
     */
    displaySalesData: function(data) {
      // Categorises and sums up data, splitting each bought listing into it's respective year and month.
      let yearlyData = this.totalWeeklyRevenue(data);

      // let today = new Date("2021-07-15");
      // let daynum = today.getDay();
      // let monday = today.setDate(today.getDate()-daynum+1);


      let allData = [];
      for (let year of Object.entries(yearlyData)) allData = allData.concat(year[1]); // Flatten object into array.

      // // Generates the x-axis labels of each month, for each year.
      // let yearlyMonthCategories = this.generateMonthLabels(Object.keys(yearlyData)); //For Monthly Revenue
      // let yearlyMonthCategories = Object.keys(yearlyData);
      let yearlyMonthCategories = this.generateWeekLabels(yearlyData);

      // Removes months with no sales up to the first month of sales.
      let i = 0;
      while (i < allData.length) {
        if (allData[i] > 0) {
          allData = allData.slice(i);
          yearlyMonthCategories = yearlyMonthCategories.slice(i);
          break;
        }
        i++;
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
          type: "category",
          categories: yearlyMonthCategories,
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
            format: "MMMM yyyy"
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
      let yearlyData = {};
        for (let listing of data) {
          let soldDate = new Date(listing.sold);
          if (yearlyData[soldDate.getFullYear()] == null) {
            yearlyData[soldDate.getFullYear()] = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
          }
          yearlyData[soldDate.getFullYear()][soldDate.getMonth()] += +listing.price.toFixed(2);
        }
      return yearlyData;
    },

    /**
     * NOT WORKING YET, TODO: FIX THIS!
     * @param data
     * @returns {{}}
     */
    totalWeeklyRevenue: function(data) {
      let weeklyData = {};
      for (let listing of data) {
        let soldDate = new Date(listing.sold);
        if (weeklyData[soldDate.getFullYear()] == null) {
          weeklyData[soldDate.getFullYear()] = {};
        }

        let dayNum = soldDate.getDay();
        let dateCopy = soldDate;
        dateCopy.setDate(soldDate.getDate() - dayNum + 1);
        let mondayString = (dateCopy.getMonth() + 1) + "-" + dateCopy.getDate();
        // let oneJan = new Date(soldDate.getFullYear(),0,1);
        // let numberOfDays = Math.floor((soldDate - oneJan) / (24 * 60 * 60 * 1000));
        // let result = Math.ceil(( soldDate.getDay() + 1 + numberOfDays) / 7);

        if (weeklyData[soldDate.getFullYear()][mondayString] == null) {
          weeklyData[soldDate.getFullYear()][mondayString] = 0;
        }
        weeklyData[soldDate.getFullYear()][mondayString] += +listing.price.toFixed(2);
      }

      console.log(weeklyData)
      return weeklyData;
    },

    totalYearlyRevenue: function(data) {
      let yearlyData = {};
      for (let listing of data) {
        let soldDate = new Date(listing.sold);
        if (yearlyData[soldDate.getFullYear()] == null) {
          yearlyData[soldDate.getFullYear()] = 0;
        }
        yearlyData[soldDate.getFullYear()] += +listing.price.toFixed(2);
      }
      return yearlyData;
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

    generateWeekLabels: function(data) {
        let labels = [];

        for (let year of Object.keys(data)) {
          labels = labels.concat(Object.keys(data[year]));
        }
        return labels;
    },



  },

  mounted: function() {
    this.getSalesData();
  },

}
</script>

<style scoped>

</style>
