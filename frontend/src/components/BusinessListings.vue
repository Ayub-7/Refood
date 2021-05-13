<template>
  <div id="listings-container">
    <div id="header-container">
      <vs-select id="header-sort" v-model="selectedSort" label="Sort">
        <vs-select-item v-for="sort in sortOptions" :key="sort" :value="sort" :text="sort"/>
      </vs-select>

      <div style="display: inline-flex; margin: auto 0 0.5em 0;">
        <div style="padding: 0 1em; font-size: 14px;"> View </div>
        <vs-switch id="table-switch" v-model="tableView" style="margin: auto 0; width: 50px">
          <span slot="on">Table</span>
          <span slot="off">Grid</span>
        </vs-switch>
      </div>
    </div>
    <vs-divider></vs-divider>

    <!-- GRID VIEW -->
    <div v-if="!tableView" id="grid-view">
      <vs-card class="listing-card">
        <div class="listing-header">
          <vs-image style="background-color: black; width:50px; height: 50px"></vs-image>
          <div style="margin: auto 4px;">
            <div style="font-size: 16px;">Product Name</div>
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

    <!-- TABLE VIEW -->
    <div v-else id="table-view">
      <vs-table
          :data="listings"
          noDataText="This business has no listings."
          pagination
          stripe>
        <template slot="thead">
          <vs-th style="border-radius: 8px 0 0 0"> <!-- Product Image Thumbnail --> </vs-th>
          <vs-th> Product </vs-th>
          <vs-th> Qty </vs-th>
          <vs-th> Closes </vs-th>
          <vs-th> Listed </vs-th>
          <vs-th style="border-radius: 0 8px 0 0"> Additional Info </vs-th>
        </template>
        <template slot-scope="{data}">
          <vs-tr v-for="listing in data" :key="listing"> <!-- todo: connect data with table -->
            <vs-td> </vs-td>
            <vs-td> </vs-td>
            <vs-td> </vs-td>
            <vs-td> </vs-td>
            <vs-td> </vs-td>
            <vs-td> </vs-td>
            <vs-td> </vs-td>
          </vs-tr>
        </template>
      </vs-table>
    </div>

  </div>
</template>

<script>

export default {
  name: "BusinessListings",

  data: function() {
    return {
      listings: [],

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

  /* === LISTING CARD ==== */
  #grid-view {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-around;
    margin: auto;
  }

  .listing-card {
    width: 200px;
    margin: 0 0.5em 1em 0.5em;
  }

  .listing-header {
    display: flex;
    justify-content: flex-start;
    padding: 4px 0 0 0;
  }

  .grid-card-footer {
    font-size: 12px;
    padding-bottom: 8px;
  }

</style>