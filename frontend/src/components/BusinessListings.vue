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

    <!-- ===== GRID VIEW ===== -->
    <div v-if="!tableView" id="grid-view">
      <vs-card class="listing-card" v-for="listing in listings" :key="listing.id" :fixed-height="true">
        <div class="listing-header">
          <vs-image style="background-color: black; min-width: 50px; max-width:50px; max-height: 50px"></vs-image>
          <div style="margin: auto 4px;">
            <div style="font-size: 14px;"><b>{{ listing.productName }}</b></div>
            <div>{{ listing.quantity }}x</div>
          </div>
        </div>
        <div> Closes: {{ listing.closes }}</div>
        <vs-divider style="margin-top: 0"></vs-divider>
        <div>
          {{ listing.moreInfo }}
        </div>
        <div slot="footer" class="grid-card-footer">
          Listed: {{ listing.created }}
        </div>
      </vs-card>
    </div>

    <!-- ===== TABLE VIEW ===== -->
    <div v-else id="table-view">
      <vs-table
          :data="listings"
          noDataText="This business has no listings."
          pagination
          stripe>
        <template slot="thead">
          <vs-th style="border-radius: 8px 0 0 0"> <!-- Product Image Thumbnail --> </vs-th>
          <vs-th sort-key="productName"> Product </vs-th>
          <vs-th sort-key="quantity"> Qty </vs-th>
          <vs-th sort-key="closes"> Closes </vs-th>
          <vs-th sort-key="created"> Listed </vs-th>
          <vs-th style="border-radius: 0 8px 0 0"> Additional Info </vs-th>
        </template>
        <template slot-scope="{data}">
          <vs-tr v-for="listing in data" :key="listing.id">
            <!-- <vs-image width=100 height=100 :src="listing.inventoryItem.product.images[0].filename"></vs-image> TODO: THIS GOES BELOW-->
            <vs-td></vs-td>
            <vs-td>{{ listing.productName }}</vs-td>
            <vs-td>{{ listing.quantity }}x</vs-td>
            <vs-td>{{ listing.closes }}</vs-td>
            <vs-td>{{ listing.created }}</vs-td>
            <vs-td>{{ listing.moreInfo }}</vs-td>
          </vs-tr>
        </template>
      </vs-table>
    </div>

  </div>
</template>

<script>
import api from "../Api";

export default {
  name: "BusinessListings",

  props: {
    businessId: Number,
  },

  data: function() {
    return {
      listings: [],

      sortOptions: ["Listing Date", "Closing Date", "Product Name"], // Not implemented yet.
      selectedSort: null,

      tableView: false, // Default grid-card option.
    }
  },

  methods: {
    /**
     * Retrieves the sale listings of this business.
     */
    getListings: function() {
      api.getBusinessListings(this.businessId)
        .then((res) => {
          this.listings = res.data;
          // Add product name to listing object to make it accessible for table to sort.
          this.listings = this.listings.map(listing => {
            listing.productName = listing.inventoryItem.product.name;
            return listing;
          })
        })
        .catch((error) => {
          this.$log.error(error);
        });
    },


  },

  mounted: function() {
    this.getListings();
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
    height: 225px;
    margin: 0 0.5em 1em 0.5em;
  }

  .listing-header {
    display: flex;
    justify-content: flex-start;
    padding: 4px 0 0 0;
  }

  .grid-card-footer {
    font-size: 12px;
  }

  .listing-card >>> footer {
    margin-left: 0;
    margin-right: auto;
    right: auto;
  }

</style>