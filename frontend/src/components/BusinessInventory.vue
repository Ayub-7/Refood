<template>
  <vs-card id="container">

    <div id="header-container">
      <div id="title"> Inventory </div>
      <div id="header-buttongroup">
        <vs-button class="header-button" @click="$router.push(`/businesses/${$route.params.id}/products`)">Product Catalogue</vs-button>
        <vs-button class="header-button">New Inventory Product</vs-button>
        <vs-button @click="newListingPopup = true" class="header-button">New Item Listing</vs-button> <!-- todo: remove this when testing done -->
      </div>
    </div>

    <vs-prompt :active.sync="newListingPopup" title="Add a new listing">
      <div>
        <label>Qty</label><vs-input-number v-model="qty"></vs-input-number> <!-- todo: max based on current inventory amount -->
        <vs-input class="new-listing-input" v-model="qty" label-placeholder="Qty"></vs-input>
        <vs-input class="new-listing-input" v-model="price" label-placeholder="Price"></vs-input>
        <vs-input class="new-listing-input" v-model="moreInfo" label-placeholder="More Info"></vs-input>
        <vs-input class="new-listing-input" v-model="closes" label-placeholder="Close Date"></vs-input>
      </div>
    </vs-prompt>

    <vs-divider></vs-divider>
    <!-- Table View -->
    <vs-table id="table"
              data="inventory"
              noDataText="You don't have any inventory."
              :pagination="true"
              :maxItems="10"
              stripe>
      <template class="table-head" slot="thead" >
        <vs-th style="border-radius: 8px 0 0 0"> <!-- Image Column --> </vs-th>
        <vs-th> ID </vs-th>
        <vs-th> Product </vs-th>
        <vs-th> Manufacturer </vs-th>
        <vs-th> Sell By </vs-th>
        <vs-th> Best Before </vs-th>
        <vs-th> Expires </vs-th>
        <vs-th> Qty </vs-th>
        <vs-th> Price Per Item </vs-th>
        <vs-th> Total Price </vs-th>
        <vs-th style="border-radius: 0 8px 0 0"> <!-- Action Button Column --> </vs-th>
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
          <vs-td> </vs-td>
          <vs-td> </vs-td>
          <vs-td> </vs-td>
          <vs-td> </vs-td>
        </vs-tr>
      </template>
    </vs-table>
  </vs-card>
</template>

<script>
export default {
  name: "BusinessInventory",

  data: function() {
    return {
      inventory: [],

      newListingPopup: false,
      qty: 0,
      price: 0.00,
      moreInfo: "",
      created: "",
      closes: "",
    }
  },


}
</script>

<style scoped>
  #container {
    width: 75%;
    margin: 1em auto;

  }

  /* ===== PAGE HEADER ===== */
  #header-container {
    display: flex;
    justify-content: space-between;
  }

  #title {
    font-size: 30px;
    margin: auto 8px;
  }

  #header-buttongroup {
    display: inline-flex;
    justify-content: space-around;
  }

  .header-button {
    margin: 0 1em;
  }

  /* ===== NEW LISTING DIALOG/PROMPT ====== */
  .new-listing-input {
    padding: 1em 0;
  }


  /* ===== INVENTORY TABLE ===== */
  .table-head {
    border-radius: 1em;
  }

  #table {
    margin: 1em;
  }

  @media screen and (max-width: 850px) {
    #header-container {
      flex-direction: column;
    }

    #title {
      padding: 0.5em 0 1em 0.5em;
    }
  }

</style>