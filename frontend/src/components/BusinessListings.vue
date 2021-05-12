<template>
  <div id="listings-container">
    <div id="header-container">
      <vs-select id="header-sort" v-model="selectedSort" label="Sort">
        <vs-select-item v-for="sort in sortOptions" :key="sort" :value="sort" :text="sort"/>
      </vs-select>

      <div style="display: inline-flex; margin: auto 0 0.5em 0;">
        <div style="padding: 0 1em; font-size: 14px;"> View </div>
        <vs-switch v-model="tableView" style="margin: auto 0;"></vs-switch>
      </div>
    </div>
    <vs-divider></vs-divider>

    <div v-if="!tableView" id="grid-view">
      <vs-card>
        <div style="display: flex; justify-content: flex-start">
          <vs-image style="background-color: black;"></vs-image>
          <div style="margin: auto;">
            <div>Product Name</div>
            <div>30x</div>
          </div>
        </div>
        <vs-divider></vs-divider>
        <div>
          Seller may be willing to consider near offers.
        </div>
        <div slot="footer" class="grid-card-footer">
          Listed: 2021-05-12s
        </div>
      </vs-card>
    </div>

  </div>
</template>

<script>

export default {
  name: "BusinessListings",

  data: function() {
    return {
      sortOptions: ["Listing Date", "Closing Date", "Product Name"], // Not implemented yet.
      selectedSort: null,

      tableView: false, // Default grid-card option.
    }
  },

}
</script>

<style scoped>
  #listings-container {
    display: flex;
    flex-direction: column;
    margin: 1em auto;
  }

  #header-container {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
  }

  #grid-view {
    display: grid;
    grid-template-rows: repeat(auto-fit, auto);
    grid-template-columns: 1fr 1fr 1fr 1fr;
  }

  .grid-card-footer {
    font-size: 12px;
  }

</style>