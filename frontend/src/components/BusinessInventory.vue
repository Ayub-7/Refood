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

    <!-- ===== NEW LISTING MODAL ===== -->
    <vs-popup :active.sync="newListingPopup"
              title="Create a new listing">
      <div class="new-listing-modal">
        <div id="listing-product-id">
          <div class="sub-header">Product ID</div>
          <div style="font-size: 18px;">ABC-123</div>
        </div>
        <div id="listing-product-name">
          <div class="sub-header">Product Name</div>
          <div style="font-size: 18px;">Bean of Cans - Placeholder Name</div>
        </div>
        <vs-divider id="listing-divider"></vs-divider>
        <div id="listing-price">
          <span :class="{currencyAfterError: newListingErrors.price.error, currencyBeforeError: !newListingErrors.price.error}">{{currency}}</span>
          <vs-input type="number" v-model="price"
                    label="Price" min="0"
                    :danger="newListingErrors.price.error"
                    :danger-text="newListingErrors.price.message"></vs-input>
        </div>
        <div id="listing-qty">
          <div>
            <label style="font-size: 13.6px">Quantity</label>
            <vs-input-number
                v-model="quantity"
                :max="10"></vs-input-number> <!-- todo: max based on current inventory amount -->
          </div>
          <div v-show="newListingErrors.quantity.error" style="font-size: 10px; color: #FF4757; text-align: center; position: absolute">{{ newListingErrors.quantity.message }}</div>
        </div>
        <div id="listing-closes">
          <vs-input type="datetime-local"
                    v-model="closes"
                    :danger="newListingErrors.closes.error"
                    :danger-text="newListingErrors.closes.message"
                    label="Close Date"></vs-input>
        </div>
        <div id="listing-moreInfo">
          <vs-textarea class="textarea"
                       v-model="moreInfo"
                       label="More Info"></vs-textarea>
        </div>
        <div id="listing-button-group">
          <vs-button id="create-button" color="success" @click="createNewListing()" style="width: 100px;">Create</vs-button>
          <vs-button id="cancel-button" color="danger" style="width: 100px;" @click="closeNewListing()">Cancel</vs-button>
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
import axios from "axios";
import {store} from "../store";

export default {
  name: "BusinessInventory",

  data: function() {
    return {
      inventory: [],
      currency: "$",

      // New Sale Listing Variables
      newListingPopup: false,
      newListingErrors: {
        price: {error: false, message: "Price cannot be a negative number."},
        quantity: {error: false, message: "Please enter a positive quantity."},
        closes: {error: false, message: "Please enter a valid date."}
      },
      quantity: 0,
      price: 0.00,
      moreInfo: "",
      closes: "", // todo: should default to the expiry date of selected item.
    }
  },

  methods: {
    /**
     * Validates the fields for a new public listing.
     * @return true if all of the required fields meet the requirements, false otherwise.
     */
    validateNewListing: function() {
      Object.values(this.newListingErrors).forEach(input => input.error = false);

      let isValid = true;
      if (this.price < 0) {
        this.price = 0.00;
        this.newListingErrors.price.error = true;
        isValid = false;
      }
      if (this.quantity < 1) { // In theory this shouldn't occur (because vs-input-number component will set it to the min/max allowed).
        this.quantity = 0;
        this.newListingErrors.quantity.error = true;
        isValid = false;
      }
      if (this.closes === "" || this.closes == null) {
        this.newListingErrors.closes.error = true;
        this.newListingErrors.closes.message = "Please enter a valid date.";
        isValid = false;
      }
      if (new Date(this.closes) < Date.now()) {
        this.newListingErrors.closes.error = true;
        this.newListingErrors.closes.message = "Please enter a future date.";
        isValid = false;
      }

      return isValid;
    },

    /**
     * Checks if the new list form is valid, then creates a new listing to be sent to backend if true.
     */
    createNewListing: function() {
      if (this.validateNewListing()) {
        alert("It validated. Replace me with functionality.");
      }
    },

    /**
     * Closes the new listing modal, and reset error fields.
     */
    closeNewListing: function() {
      this.newListingPopup = false;
      Object.values(this.newListingErrors).forEach(input => input.error = false);
    },

    /**
     * Sets display currency based on the user's home country.
     * User home country is taken from the store.
     */
    setCurrency: function () {
      let country = store.userCountry;
      axios.get(`https://restcountries.eu/rest/v2/name/${country}`)
        .then(response => {
          this.currency = response.data[0].currencies[0].symbol;
        }).catch(err => {
          this.$log.debug(err);
      });
    },
  },

  mounted: function() {
    this.setCurrency();
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
    padding-top: 4px;
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
    padding-left: 12px;
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

  .sub-header {
    font-size: 12px;
    color: gray;
  }

  .currencyBeforeError {
    margin: auto 4px 4px 0;
  }

  .currencyAfterError {
    margin: auto 4px 32px 0;
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

  @media screen and (max-width: 450px) {
    .new-listing-modal {
      display: grid;
      grid-template-columns: 1fr;
      grid-template-rows: repeat(auto-fit, 1fr);
      grid-row-gap: 0.5em;
    }

    #listing-product-id {
      grid-row: 1;
      grid-column: 1;
    }

    #listing-product-name {
      grid-row: 2;
      grid-column: 1;

    }

    #listing-divider {
      grid-row: 3;
      grid-column: 1;
    }

    #listing-qty {
      grid-column: 1;
      grid-row: 4;
      margin-bottom: 1em;
    }

    #listing-price {
      grid-column: 1;
      grid-row: 5;
    }

    #listing-closes {
      grid-column: 1;
      grid-row: 6;
    }

    #listing-moreInfo {
      grid-row: 7;
      grid-column: 1;
    }

    #listing-button-group {
      grid-row: 8;
      grid-column: 1;

      display: flex;
      justify-content: space-around;
    }

    .textarea >>> textarea {
      resize: none;
      min-height: 100px;
      max-height: 100px;
      max-width: 100px;
    }

  }

</style>