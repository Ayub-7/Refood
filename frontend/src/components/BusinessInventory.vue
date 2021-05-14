<template>
  <vs-card id="container">

    <div id="header-container">
      <div id="title"> Inventory </div>
      <div id="header-buttongroup">
        <vs-button class="header-button" @click="$router.push(`/businesses/${$route.params.id}/products`)">Product Catalogue</vs-button>
        <vs-button class="header-button">New Inventory Product</vs-button>
        <vs-button @click="newListingPopup = true" class="header-button">New Item Listing</vs-button>
        <!-- todo: remove this new item listing button when testing done
              - want to make it so there's an action button for each inventory item, with the ability to add a new listing attached.-->
      </div>
    </div>

    <!-- NEW LISTING MODAL -->
    <vs-popup :active.sync="newListingPopup"
              title="Create a new listing">
      <div class="new-listing-modal">
        <h3 id="listing-product-id">
          ABC-123
        </h3>
        <h3 id="listing-product-name">
          Bean of cans - Placeholder Name
        </h3>
        <vs-divider id="listing-divider"></vs-divider>

        <div id="listing-qty">
          <label style="font-size: 13.6px">Quantity</label>
          <vs-input-number v-model="qty"></vs-input-number> <!-- todo: max based on current inventory amount -->
        </div>
        <div id="listing-price">
          <span style="margin: auto 0 4px 0; font-size: 14px">$</span>
          <vs-input type="number" v-model="price" label="Price" ></vs-input>
        </div>
        <div id="listing-closes">
          <vs-input type="date" v-model="closes" label="Close Date"></vs-input>
        </div>
        <div id="listing-moreInfo">
          <vs-textarea class="textarea" v-model="moreInfo" label="More Info"></vs-textarea>
        </div>
        <div id="listing-button-group">
          <vs-button color="success" style="width: 100px;">Create</vs-button>
          <vs-button color="danger" style="width: 100px;" @click="newListingPopup = false">Cancel</vs-button>
        </div>
      </div>
    </vs-popup>

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

  .new-listing-modal {
    display: grid;
    grid-template-columns: 1fr 1fr;
    grid-template-rows: repeat(auto-fit, 1fr);
    grid-row-gap: 0.5em;
  }

  .vs-popup-primary >>> header {
    background-color: #1F74FF;
    color: #FFFFFF;
  }

  .textarea >>> textarea {
    resize: none;
    min-height: 100px;
    max-height: 100px;
  }

  #listing-product-id {
    grid-row: 1;
    grid-column: 1;

    text-align: center;
  }

  #listing-product-name {
    grid-row: 1;
    grid-column: 2;

    text-align: center;
  }

  #listing-divider {
    grid-row: 2;
    grid-column: 1 / 3;
  }

  #listing-qty {
    margin: auto;
    grid-column: 2;
    grid-row: 3;
  }

  #listing-price {
    margin: auto;
    grid-column: 1;
    grid-row: 3;

    display: flex;
  }

  #listing-closes {
    margin: auto;
    padding-left: 8px;
    grid-column: 1;
    grid-row: 4;
  }

  #listing-moreInfo {
    margin: 0.75em auto auto auto;
    width: 90%;
    grid-row: 5;
    grid-column: 1 / 3;
  }

  #listing-button-group {
    margin: 0.75em auto auto auto;
    width: 90%;
    grid-row: 6;
    grid-column: 2;

    display: flex;
    justify-content: space-around;
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