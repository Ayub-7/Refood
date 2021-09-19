<template>
  <apexchart id="sales-graph-report" width="100%" height="100%" type="bar" :options="options" :series="series"></apexchart>
</template>

<script>
import api from "../Api";

export default {
  name: "BusinessSalesGraph",
  props: ['businessId'],

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
      let yearlyData = {};
      for (let listing of data) {
        let soldDate = new Date(listing.sold);
        if (yearlyData[soldDate.getFullYear()] == null) {
          yearlyData[soldDate.getFullYear()] = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
        }
        yearlyData[soldDate.getFullYear()][soldDate.getMonth()] += +listing.price.toFixed(2);
      }

      let allData = [];
      for (let year of Object.entries(yearlyData)) allData = allData.concat(year[1]); // Flatten object into array.

      // Generates the x-axis labels of each month, for each year.
      const baseMonthCategories = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug',
        'Sep', 'Oct', 'Nov', 'Dec'];
      const numOfYears = Object.keys(yearlyData).length;
      let yearlyMonthCategories = [];
      for (let i = 0; i < numOfYears; i++) {
        yearlyMonthCategories = yearlyMonthCategories.concat(baseMonthCategories.map(month => month + ` ${Object.keys(yearlyData)[i]}`));
      }

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
            formatter: (val) => '$' + val,
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
            formatter: (val) => '$' + val.toFixed(2),
          },
          x: {
            format: "MMMM yyyy"
          }
        },
      };
    },
  },

  mounted: function() {
    this.getSalesData();
  },

}
</script>

<style scoped>

</style>
