<template>
  <vs-card id="container">

    <div id="header-container">
      <div id="title"> Inventory </div>
      <div id="header-buttongroup">
        <vs-button class="header-button" @click="$router.push(`/businesses/${$route.params.id}/products`)">Product Catalogue</vs-button>
        <vs-button @click="addNewInv=true" class="header-button">New Inventory Listing</vs-button>
        <vs-button @click="newListingPopup = true" class="header-button">New Item Listing</vs-button>
        <!-- todo: remove this new item listing button when testing done
              - want to make it so there's an action button for each inventory item, with the ability to add a new listing attached.-->

        <vs-popup classContent="popup-example"  title="Add a product to your inventory" :active.sync="addNewInv">
        <div class="form-group required vs-col" vs-order="1" id="firstColModal">
            <div class="row">
              <label for="prodId">Product</label>
              <vs-select id="prodId" class="selectExample" v-model="prodId" v-on:change="autofill">
                <vs-select-item :value="product.id" :text="product.name" v-for="product in products" v-bind:href="product.id" :key="product.id"/>
              </vs-select>
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
                  width="200px"
                  height="50px"
                  class="description-textarea"
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
          <div class="form-group required vs-col" align="center" id="addButton" @click="addInventory(); checkForm()">
            <vs-button>Add product</vs-button>
          </div>
        </vs-popup>
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
              v-model="selected"
              @selected="handleSelect"
              :data="this.inventory"
              noDataText="You don't have any inventory."
              :pagination="true"
              :maxItems="5"
              stripe>
      <template class="table-head" slot="thead" >
        <vs-th sort-key="productId"> ID </vs-th>
        <vs-th sort-key="productName"> Product </vs-th>
        <vs-th class="dateInTable" sort-key="manufactured"> Manufactured </vs-th>
        <vs-th class="dateInTable" sort-key="sellBy"> Sell By </vs-th>
        <vs-th class="dateInTable" sort-key="bestBefore"> Best Before </vs-th>
        <vs-th class="dateInTable" sort-key="expires"> Expires </vs-th>
        <vs-th sort-key="quantity"> Qty </vs-th>
        <vs-th id="pricePerItemCol"  sort-key="pricePerItem"> Price Per Item </vs-th>
        <vs-th sort-key="totalPrice"> Total Price </vs-th>
        <vs-th style="border-radius: 0 8px 0 0"> <!-- Action Button Column --> </vs-th>
      </template>
      <template slot-scope="{data}">
        <vs-tr v-for="inventory in data" :key="inventory.id"> <!-- todo: connect data with table -->
          <vs-td id="productIdCol" :data="inventory.productId"> 
          {{inventory.productId}} 
          <div style="height: 80px">
          <img v-if="inventory.product.primaryImagePath" style="width:auto; height: 100%;   border-radius: 1em;" v-bind:src="require('../../../backend/src/main/resources/media/images/businesses/' + getImgUrl(product))"/>
          <img v-if="!inventory.product.primaryImagePath" style="width: auto; height: 100%;   border-radius: 1em;" v-bind:src="require('../../public/ProductShoot.jpg')"/>
          </div>
            </vs-td>
          <vs-td :data="inventory.productName"> {{inventory.productName}} </vs-td>
          <vs-td :data="inventory.manufactured"> {{inventory.manufactured}} </vs-td>
          <vs-td :data="inventory.sellBy"> {{inventory.sellBy}} </vs-td>
          <vs-td :data="inventory.bestBefore">{{inventory.bestBefore}} </vs-td>
          <vs-td :data="inventory.expires">{{inventory.expires}} </vs-td>
          <vs-td :data="inventory.quantity">{{inventory.quantity}} </vs-td>
          <vs-td :data="inventory.pricePerItem">{{inventory.pricePerItem}} </vs-td>
          <vs-td :data="inventory.totalPrice">{{inventory.totalPrice}}</vs-td>
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

  data: function() {
    return {
      errors: [],
      inventory: [],
      products: [],
      prodId:'',
      addNewInv:false,
      pricePerItem: 0.0,
      totalPrice: 0.0,
      quantity: 0,

      invDescription: '',
      bestBefore: '',
      listExpiry: '',
      manufactureDate: '',
      sellBy: '',
      
      newListingPopup: false,
      qty: 0,
      price: 0.00,
      moreInfo: "",
      created: "",
      closes: ""
    }
  },

  mounted() {
    api.getBusinessProducts(this.$route.params.id)
        .then((response) => {
          this.$log.debug("Data loaded: ", response.data);
          this.products = response.data;
        })
        .catch((error) => {
          this.$log.debug(error);
          this.error = "Failed to load products";
        });

    this.getBusinessInventory();
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
        console.log(store.actingAsBusinessId, this.prodId, this.quantity, this.pricePerItem, this.totalPrice, this.manufactureDate, this.sellBy, this.bestBefore, this.listExpiry)
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
    },
    autofill: function() {
      if (this.prodId !== '') {
        let prodInd = 0;
        while (prodInd < this.products.length) {
          if (this.products[prodInd]["id"] === this.prodId) {
            break;
          }
          prodInd++;
        }
        console.log(this.products[prodInd])
        this.pricePerItem = this.products[prodInd]["recommendedRetailPrice"];
        this.invDescription = this.products[prodInd]["description"];
      }
    },

    /** 
     * Calls API get businessInventory function, also adds new fields to inventory object for sorting and sets default order
    */
    getBusinessInventory() {
      api.getBusinessInventory(this.$route.params.id)
      .then((response) => {
        this.inventory = response.data;
        console.log(this.inventory)
        for(let inventoryItem of this.inventory) {
          //Issue with sorting using object properties, so pulled required properties into inventory object
          inventoryItem['productName'] = inventoryItem.product.name;
          inventoryItem['productId'] = inventoryItem.product.id;
        }

        //Default ordering is product name, so all similar products will be next to each other
        this.inventory = this.inventory.sort((productOne, productTwo) => (productOne.name < productTwo.name) ? 1 : -1)

      }).catch((err) => {
        if(err.response.status == 401) {
          this.$router.push({path: '/login'})
        }
      })
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
    max-width: 200px;
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

  th {
    background: #1F74FF;
    color: white;
    font-size: 12px;
  }

  #table {
    margin: 0.5em;
    white-space: nowrap;
  }

  #productIdCol {
    font-size: 10px;
  }

  #pricePerItemCol {
    font-size: 11px;
  }

  td {
    font-size: 12px;
    min-width: 80px
  }

  .dateInTable{
    width: 130px;
  }


  

  /* ===== INVENTORY ADDING MODAL ===== */
  #firstColModal {
    margin-right: 160px;
    margin-left: 5px;
  }

  .description-textarea >>> textarea {
    resize: none;
    min-height: 50px;
    max-height: 50px;
  }

  .row {
    margin-bottom: 15px;
  }
  .addButton {
    align-self: center;
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