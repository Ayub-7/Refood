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
        xaxis: {
          categories: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August',
            'September', 'October', 'November', 'December']
        },
        colors: ["#1F74FF"],
        dataLabels: {
          enabled: false,
        }
      },
    }
  },

  methods: {
    getData: function() {
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
     * Currently displays data by month - regardless of year.
     * @param data bought listings sales data
     */
    displaySalesData: function(data) {
      let result = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
      for (let listing of data) {
        let soldDate = new Date(listing.sold);
        result[soldDate.getMonth()] += +listing.price.toFixed(2);
      }

      this.series = [{ // Reassign entire variable to make chart properly update and animations to play.
        name: 'Total Value',
        data: result,
      }];


      this.options = {
        title: {
          text: "Monthly Total Sales for 2021",
          style: {
            fontSize: "18px",
            fontWeight: "bold",
            fontFamily: "Ubuntu, sans-serif",
          }
        },
        yaxis: {
          decimalsInFloat: 2,
          labels: {
            formatter: (val) => '$' + val,
          }
        },
        tooltip: {
          y: {
            formatter: (val) => '$' + val.toFixed(2),
          }
        },
      };
    },
  },

  mounted: function() {
    this.getData();
  },

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
