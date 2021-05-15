<template>
  <vs-card id="container">

    <div id="header-container">
      <div id="title"> Inventory </div>
      <div id="header-buttongroup">
        <vs-button class="header-button" @click="$router.push(`/businesses/${$route.params.id}/products`)">Product Catalogue</vs-button>
        <vs-button @click="addNewInv=true" class="header-button">New Inventory Listing</vs-button>
        <vs-popup classContent="popup-example"  title="Add a product to your inventory" :active.sync="addNewInv">
          <div class="form-group required vs-col" vs-order="1" id="firstColModal">
            <div class="row">
              <label for="prodId">Product ID</label>
              <vs-input
                  :danger="(errors.includes(prodId))"
                  danger-text="Product ID is required."
                  type="text"
                  class="inputx"
                  id="prodId"
                  placeholder="Product ID"
                  v-model="prodId"/>
            </div>
            <div class="row">
              <label for="pricePerItem">Price per item</label>
              <vs-input
                  :danger="(errors.includes(pricePerItem))"
                  danger-text="Price per item must be greater than zero and numeric."
                  class="inputx"
                  id="pricePerItem"
                  placeholder="Price per item"
                  v-model="pricePerItem"/>
            </div>
            <div class="row">
              <label for="quantity">Quantity</label>
              <vs-input-number
                  :danger="(errors.includes(quantity))"
                  danger-text="Quantity must be greater than zero."
                  min="0"
                  :step="1"
                  id="quantity"
                  v-model="quantity"/>
            </div>
            <div class="row">
              <label for="description">Description</label>
              <vs-textarea
                  id="description"
                  v-model="invDescription">
              </vs-textarea>
            </div>
          </div>
          <div class="form-group required vs-col" vs-order="2" id="secondColModal">
            <div class="row">
              <label for="bestBefore">Best before</label>
              <vs-input
                  :danger="(errors.includes('past-best'))"
                  danger-text="Date cannot be in past"
                  type="date"
                  id="bestBefore"
                  class="inputx"
                  v-model="bestBefore"/>
            </div>
            <div class="row">
              <label for="listingExpiry">Listing expiry</label>
              <vs-input
                  :danger="(errors.includes('past-expiry'))"
                  danger-text="Expiry date is required and cannot be in past"
                  type="date"
                  id="listingExpiry"
                  class="inputx"
                  v-model="listExpiry"/>
            </div>
            <div class="row">
              <label for="manufactureDate">Manufacture date</label>
              <vs-input
                  :danger="(errors.includes('past-manu'))"
                  danger-text="Date cannot be in past"
                  type="date"
                  id="manufactureDate"
                  class="inputx"
                  v-model="manufactureDate"/>
            </div>
            <div class="row">
              <label for="sellBy">Sell by</label>
              <vs-input
                  :danger="(errors.includes('past-sell'))"
                  danger-text="Date cannot be in past"
                  type="date"
                  id="sellBy"
                  class="inputx"
                  v-model="sellBy"/>
            </div>
          </div>
          <div class="form-group required vs-col" align="center" id="addButton" @click="addInventory; checkForm()">
            <vs-button>Add product</vs-button>
          </div>
        </vs-popup>
      </div>
    </div>

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
import api from "../Api";
// import axios from "axios";
import {store} from "../store";

export default {
  name: "BusinessInventory",
  data(){
    return {
      errors: [],
      inventory: [],
      prodId:'',
      addNewInv:false,
      pricePerItem: 0.0,
      totalPrice: 0.0,
      quantity: 0,
      invDescription: '',
      bestBefore: '',
      listExpiry: '',
      manufactureDate: '',
      sellBy: ''

    }
  },
  methods: {
    /**
     * TODO: FOR AYUB
     */
    checkForm: function() {
      this.errors = [];
      var invalidChars = /[^a-zA-Z/ -\d]/i;
      if (this.prodId.match(invalidChars)) {
        this.errors.push("invalid-chars");
      }

      var today = new Date();
      var regex = /^[0-9]*(?:\.\d{1,2})?$/;
      if( !regex.test(this.pricePerItem) ) {
        this.errors.push(this.pricePerItem);
      }

      var dateInPast = function(firstDate, secondDate) {
        if(firstDate.setHours(0,0,0,0) <= secondDate.setHours(0,0,0,0)) {
          return true;
        }

        return false;
      }
      if (this.bestBefore === '' && this.sellBy === '' && this.manufactureDate === ''
          && this.listExpiry === '') {
        this.errors.push('no-dates');
      }

      if (this.listExpiry === '') {
        this.errors.push('past-expiry');
      }

      if (this.prodId.length === 0 || this.errors.includes('invalid-chars')) {
        this.errors.push(this.prodId);
      }
      if (this.pricePerItem <= 0.0) {
        this.errors.push(this.pricePerItem);
      }
      if (this.bestBefore !== '') {
        var timestamp = Date.parse(this.bestBefore);
        var dateObject = new Date(timestamp)
        if (dateInPast(dateObject, today) === true) {
          this.errors.push('past-date');
          this.errors.push('past-best');
        }
      }
      if (this.listExpiry !== '') {
        timestamp = Date.parse(this.listExpiry);
        dateObject = new Date(timestamp)
        if (dateInPast(dateObject, today) === true) {
          this.errors.push('past-date');
          this.errors.push('past-expiry');
        }
      }
      if (this.manufactureDate !== '') {
        timestamp = Date.parse(this.manufactureDate);
        dateObject = new Date(timestamp)
        if (dateInPast(dateObject, today) === true) {
          this.errors.push('past-date');
          this.errors.push('past-manu');
        }
      }
      if (this.sellBy !== '') {
        timestamp = Date.parse(this.sellBy);
        dateObject = new Date(timestamp)
        if (dateInPast(dateObject, today) === true) {
          this.errors.push('past-date');
          this.errors.push('past-sell');
        }
      }
      if (this.quantity <= 0) {
        this.errors.push(this.quantity);
      }
      if (this.invDescription.length > 25) {
        this.errors.push('no-desc');
      }
      if (this.errors.includes('no-dates')) {
        this.$vs.notify({
          title: 'Failed to create inventory item',
          text: 'Date is Required.',
          color: 'danger'
        });
      }
      if (this.errors.includes('past-date')) {
        this.$vs.notify({
          title: 'Failed to create inventory item',
          text: 'Dates cannot be in the past.',
          color: 'danger'
        });
      }
      if (this.errors.includes('no-desc')) {
        this.$vs.notify({
          title: 'Failed to create inventory item',
          text: 'Description (MAX 25 CHARS).',
          color: 'danger'
        });
      }
      if (this.errors.includes(this.quantity)) {
        this.$vs.notify({
          title: 'Failed to create inventory item',
          text: 'Quantity must be greater than zero.',
          color: 'danger'
        });
      }

    },
    addInventory: function() {
      if (this.errors.length === 0) {
        api.createInventory(store.actingAsBusinessId, this.prodId, this.quantity, this.pricePerItem, this.totalPrice, this.manufactureDate, this.sellBy, this.bestBefore, this.listExpiry)
          .then((response) => {
            this.$log.debug("New catalogue item created:", response.data);
            this.inventory.push(response.data);
          }).catch((error) => {
            if (error.response) {
              console.log(error);
              if (error.response.status === 400) {
                this.$vs.notify( {
                  title: 'Failed to add an inventory item',
                  text: 'Incomplete form, or the product does not exist.',
                  color: 'danger'
                });
              } else if (error.response.status === 403) {
                this.$vs.notify( {
                  title: 'Failed to add an inventory item',
                  text: 'You do not have the rights to access this business',
                  color: 'danger'
                });
              }
              console.log(error.response.status);
            }
          this.$log.debug("Error Status:", error)
        })
      }
    }
  }
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

  /* ===== INVENTORY TABLE ===== */
  .table-head {
    border-radius: 1em;
  }

  th {
    background: #1F74FF;
    color: white;
  }

  #table {
    margin: 1em;
  }

  /* ===== INVENTORY ADDING MODAL ===== */
  #firstColModal {
    margin-right: 160px;
    margin-left: 5px;
  }
  .row {
    margin-bottom: 15px;
  }
  .addButton {
    align-self: center;
  }
</style>