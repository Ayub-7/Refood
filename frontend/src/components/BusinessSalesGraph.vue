<template>
  <div style="height: 400px">
    <div v-if="toggleSales">
      <vs-button  class="toggle-button" id="toggle-sales" @click="toggleSales = !toggleSales; getTotalSales()">
        <vs-icon style="float: left; margin-right: 10px">
          shopping_bag
        </vs-icon>
        <p style="white-space: nowrap; margin-top: 5px; margin-right:15px ">
          See Total Sales
        </p>
      </vs-button>
    </div>
    <div v-else>
      <vs-button style="display: block;" class="toggle-button" id="toggle-sales-value" @click="toggleSales = !toggleSales; getTotalRevenue()">
        <vs-icon style="float: left; margin-right: 10px">
          price_change
        </vs-icon>
        <p style="white-space: nowrap; margin-top: 5px; margin-right:15px ">
          See Total Value
        </p>
      </vs-button>
    </div>
    <apexchart id="sales-graph-report" width="100%" height="80%" type="bar" :options="options" :series="series"></apexchart>
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
      toggleSales: true,
      boughtListings: [],
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
     * retrieve total sales
     */
    getTotalSales: function () {
      let yearlyDataSales = this.totalMonthlySales(this.boughtListings);
      let yearlySales =  this.displaySalesData(yearlyDataSales);
      this.series = [{
        name: 'Total Sales',
        data: yearlySales,
      }];
    },

    /**
     * retrieve total Revenue
     */
    getTotalRevenue: function () {
      // Categorises and sums up data, splitting each bought listing into it's respective year and month.
      let yearlyData = this.totalMonthlyRevenue(this.boughtListings);
      let yearlyRevenue = this.displaySalesData(yearlyData);

      this.series = [{
        name: 'Total Value',
        data: yearlyRevenue,
      }];

    },

    /**
     * Retrieve's the business' bought listing data.
     */
    getSalesData: function() {
      api.getBusinessSales(this.businessId)
        .then((res) => {
          this.boughtListings = res.data;
          this.getTotalRevenue();
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
    displaySalesData: function(yearlyData) {
      let allData = [];
      for (let year of Object.entries(yearlyData)) allData = allData.concat(year[1]); // Flatten object into array.

      // Generates the x-axis labels of each month, for each year.
      let yearlyMonthCategories = this.generateMonthLabels(Object.keys(yearlyData));

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
          type: "datetime",
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
            formatter: (val) => {
              if (this.toggleSales) {
                return this.currencySymbol + val.toFixed(2);
              } else {
                return val;
              }
            }
          },
          x: {
            format: "MMMM yyyy"
          }
        },
      };
      return allData;
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
     * Categorises and sums up data, splitting each bought listing into it's respective year and month.
     * @param data the bought listing data (i.e. the sold listings).
     * @return object where each key is a year, and the value is an array of size 12, each index representing a month,
     * and the value at each index representing the total revenue/value of listings sold.
     */
    totalMonthlySales: function(data) {
      let yearlyData = {};
      for (let listing of data) {
        let soldDate = new Date(listing.sold);
        if (yearlyData[soldDate.getFullYear()] == null) {
          yearlyData[soldDate.getFullYear()] = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
        }
        yearlyData[soldDate.getFullYear()][soldDate.getMonth()] += 1;
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
    }

  },

  mounted: function() {
    this.getSalesData();
  },

}
</script>

<style scoped>

.toggle-button {
  /*margin-right: 10px;*/
  margin-left: 5px;
  margin-bottom: 7px;
  padding-right: 30px;
}

</style>
