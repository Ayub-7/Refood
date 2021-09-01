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
        <vs-button class="options-button" type="border">1 Day</vs-button>
        <vs-button class="options-button" type="border">1 Week</vs-button>
        <vs-button class="options-button" type="border">1 Month</vs-button>
        <vs-button class="options-button" type="border">6 Months</vs-button>
        <vs-button class="options-button" type="border">1 Year</vs-button>
        <vs-button class="options-button" type="border">All</vs-button>
        <div class="options-header" style="font-size: 14px">Custom</div>
        <vs-input type="date" size="small" class="date-input" label="Start" style="grid-row: 6"></vs-input>
        <vs-input type="date" size="small" class="date-input" label="End" style="grid-row: 6"></vs-input>

        <vs-divider style="grid-column: 1/3;"/>
        <div class="options-header">Granularity</div>
        <vs-button class="options-button" type="border">Week</vs-button>
        <vs-button class="options-button" type="border">Month</vs-button>
        <vs-button class="options-button" type="border">Year</vs-button>
        <vs-button class="options-button" type="border">All</vs-button>
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

<!--          <vs-divider style="grid-column: 1/5;"/>-->
      </vs-card>

      <div id="stats-container">
        <div class="stat-box">
          <div class="stat-subheader">Average Sale</div>
          <h2 style="padding-left: 12px">$56.45</h2>
          <div class="sub-header stat-change">
            <vs-icon color="red" icon="arrow_drop_down" class="stat-change-icon"/>
            <div>12.34% from last year</div>
          </div>
          <div class="sub-header" style="padding-left: 12px;">Jan 1 2020 - Dec 31 2020</div>
        </div>
        <div class="stat-box">
          <div class="stat-subheader">Average Price Per Item</div>
          <h2 style="padding-left: 12px">$20.69</h2>
          <div class="sub-header stat-change">
            <vs-icon color="green" icon="arrow_drop_up" class="stat-change-icon"/>
            <div>12.34% from last year</div>
          </div>
          <div class="sub-header" style="padding-left: 12px;">Jan 1 2020 - Dec 31 2020</div>
        </div>
        <div class="stat-box">
          <div class="stat-subheader">Average Sale</div>
          <h2 style="padding-left: 12px">$56.45</h2>
          <div class="sub-header stat-change">
            <vs-icon color="red" icon="arrow_drop_down" class="stat-change-icon"/>
            <div>12.34% from last year</div>
          </div>
          <div class="sub-header" style="padding-left: 12px;">Jan 1 2020 - Dec 31 2020</div>
        </div>
        <div class="stat-box">
          <div class="stat-subheader">Average Sale</div>
          <h2 style="padding-left: 12px">$56.45</h2>
          <div class="sub-header stat-change">
            <vs-icon color="red" icon="arrow_drop_down" class="stat-change-icon"/>
            <div>12.34% from last year</div>
          </div>
          <div class="sub-header" style="padding-left: 12px;">Jan 1 2020 - Dec 31 2020</div>
        </div>
        <div class="stat-box">
          <div class="stat-subheader">Average Sale</div>
          <h2 style="padding-left: 12px">$56.45</h2>
          <div class="sub-header stat-change">
            <vs-icon color="red" icon="arrow_drop_down" class="stat-change-icon"/>
            <div>12.34% from last year</div>
          </div>
          <div class="sub-header" style="padding-left: 12px;">Jan 1 2020 - Dec 31 2020</div>
        </div>
        <div class="stat-box">
          <div class="stat-subheader">Average Sale</div>
          <h2 style="padding-left: 12px">$56.45</h2>
          <div class="sub-header stat-change">
            <vs-icon color="red" icon="arrow_drop_down" class="stat-change-icon"/>
            <div>12.34% from last year</div>
          </div>
          <div class="sub-header" style="padding-left: 12px;">Jan 1 2020 - Dec 31 2020</div>
        </div>
      </div>
    </div>
  </vs-card>
</template>

<script>
export default {
  name: "BusinessSalesReport"
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
