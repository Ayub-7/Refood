<template>
  <div id="form-outer">
    <vs-button @click="addNewInv=true" class="header-button">New Inventory Listing</vs-button>
    <vs-popup classContent="popup-example"  title="Add new inventory item" :active.sync="addNewInv">
      <div class="form-group required vs-col" vs-order="1" id="firstColModal" >
        <div class="row">
          <label for="prodId">Product *</label>
          <vs-select id="prodId" class="selectExample" v-model="invenForm.prodId" v-on:change="autofill">
            <vs-select-item :value="product.id" :text="product.name" v-for="product in products" v-bind:href="product.id" :key="product.id"/>
          </vs-select>
        </div>
        <div class="row">
          <label for="pricePerItem">Price per item *</label>
          <vs-input
              :danger="(errors.includes(invenForm.pricePerItem))"
              danger-text="Price per item must be greater than zero and numeric."
              class="inputx"
              id="pricePerItem"
              placeholder="Price per item"
              v-model="invenForm.pricePerItem"
              v-on:change="updateTotalPrice"/>
        </div>
        <div class="row">
          <label for="totalPrice">Total price *</label>
          <vs-input
              :danger="(errors.includes(invenForm.totalPrice))"
              danger-text="Total price must be greater than zero and numeric."
              class="inputx"
              id="totalPrice"
              placeholder="Price per item"
              v-model="invenForm.totalPrice"/>
        </div>
        <div class="row">
          <label for="quantity">Quantity *</label>
          <vs-input-number
              :danger="(errors.includes(invenForm.quantity))"
              danger-text="Quantity must be greater than zero."
              min="0"
              :step="1"
              id="quantity"
              v-model="invenForm.quantity"
              v-on:change="updateTotalPrice"/>
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
              v-model="invenForm.bestBefore"/>
        </div>
        <div class="row">
          <label for="listingExpiry">Listing expiry *</label>
          <vs-input
              :danger="(errors.includes('past-expiry'))"
              danger-text="Expiry date is required and cannot be in past"
              type="date"
              id="listingExpiry"
              class="inputx"
              v-model="invenForm.listExpiry"/>
        </div>
        <div class="row">
          <label for="manufactureDate">Manufacture date</label>
          <vs-input
              :danger="(errors.includes('future-manu'))"
              danger-text="Date cannot be in the future"
              type="date"
              id="manufactureDate"
              class="inputx"
              v-model="invenForm.manufactureDate"/>
        </div>
        <div class="row">
          <label for="sellBy">Sell by</label>
          <vs-input
              :danger="(errors.includes('past-sell'))"
              danger-text="Date cannot be in past"
              type="date"
              id="sellBy"
              class="inputx"
              v-model="invenForm.sellBy"/>
        </div>
      </div>
      <div class="form-group required vs-col" align="center" id="addButton" @click="addInventory()">
        <vs-button>Add product</vs-button>
      </div>
    </vs-popup>
  </div>
</template>

<script>
import api from "../Api.js";
import {store} from "../store.js";

export default {
  name: "AddToInventory",
  data() {
    return {
      invenForm: {
        prodId: '',
        pricePerItem: 0.0,
        totalPrice: 0.0,
        quantity: 0,
        bestBefore: '',
        listExpiry: '',
        manufactureDate: '',
        sellBy: '',
      },
      addNewInv: false,
      item: null,
      products: [],
      errors: [],
    }
  },
  methods: {
    checkForm: function() {
      this.errors = [];
      var today = new Date();
      const invalidChars = /^([a-zA-Z0-9\u0600-\u06FF\u0660-\u0669\u06F0-\u06F9 _.-]+)$/;
      if (!invalidChars.test(this.invenForm.prodId)) {
        this.errors.push("invalid-chars");
      }

      if((typeof this.invenForm.pricePerItem) !== "number") {
        this.errors.push(this.invenForm.pricePerItem);
      }

      var dateInPast = function(firstDate, secondDate) {
        if(firstDate.setHours(0,0,0,0) <= secondDate.setHours(0,0,0,0)) {
          return true;
        }

        return false;
      }

      var dateInFuture = function(firstDate, secondDate) {
        if(firstDate.setHours(0,0,0,0) >= secondDate.setHours(0,0,0,0)) {
          return true;
        }

        return false;
      }

      if (this.invenForm.bestBefore === '' && this.invenForm.sellBy === '' && this.invenForm.manufactureDate === ''
          && this.invenForm.listExpiry === '') {
        this.errors.push('no-dates');
      }

      if (this.invenForm.listExpiry === '') {
        this.errors.push('past-expiry');
      }

      if (this.invenForm.prodId.length === 0 || this.errors.includes('invalid-chars')) {
        this.errors.push(this.invenForm.prodId);
      }
      if (this.invenForm.pricePerItem <= 0.0) {
        this.errors.push(this.invenForm.pricePerItem);
      }
      if (this.invenForm.bestBefore !== '') {
        var timestamp = Date.parse(this.invenForm.bestBefore);
        var dateObject = new Date(timestamp)
        if (dateInPast(dateObject, today) === true) {
          this.errors.push('past-date');
          this.errors.push('past-best');
        }
      }
      if (this.invenForm.listExpiry !== '') {
        timestamp = Date.parse(this.invenForm.listExpiry);
        dateObject = new Date(timestamp)
        if (dateInPast(dateObject, today) === true) {
          this.errors.push('past-date');
          this.errors.push('past-expiry');
        }
      }
      if (this.invenForm.manufactureDate !== '') {
        timestamp = Date.parse(this.invenForm.manufactureDate);
        dateObject = new Date(timestamp)
        if (dateInFuture(dateObject, today) === true) {
          this.errors.push('past-date');
          this.errors.push('future-manu');
        }
      }
      if (this.invenForm.sellBy !== '') {
        timestamp = Date.parse(this.invenForm.sellBy);
        dateObject = new Date(timestamp)
        if (dateInPast(dateObject, today) === true) {
          this.errors.push('past-date');
          this.errors.push('past-sell');
        }
      }
      if (this.invenForm.quantity <= 0) {
        this.errors.push(this.invenForm.quantity);
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

      if (this.errors.includes('future-date')) {
        this.$vs.notify({
          title: 'Failed to create inventory item',
          text: 'Dates cannot be in the future',
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
      if (this.errors.length > 0) {
        return false;
      }
      return true;
    },
    updateTotalPrice: function() {
      console.log(this.invenForm.quantity);
      this.invenForm.totalPrice = this.invenForm.quantity * this.invenForm.pricePerItem;
    },
    autofill: function() {
      if (this.invenForm.prodId !== '') {
        let prodInd = 0;
        while (prodInd < this.products.length) {
          if (this.products[prodInd]["id"] === this.invenForm.prodId) {
            break;
          }
          prodInd++;
        }
        this.invenForm.pricePerItem = this.products[prodInd]["recommendedRetailPrice"];
        this.updateTotalPrice();
      }
    },
    addInventory: function() {
      if (this.checkForm()) {
        console.log("DEBUG: adding to inventory...")
        api.createInventory(store.actingAsBusinessId, this.invenForm.prodId, this.invenForm.quantity, this.invenForm.pricePerItem, this.invenForm.totalPrice, this.invenForm.manufactureDate, this.invenForm.sellBy, this.invenForm.bestBefore, this.invenForm.listExpiry)
            .then((response) => {
              this.$log.debug("New catalogue item created:", response.data);
              //this.inventory.push(response.data);
              this.addNewInv = false;
              this.$emit('submitted');
              this.$vs.notify( {
                title: `Item successfully added to the business' inventory`,
                color: 'success'
              });
            }).catch((error) => {
          if (error.response) {
            console.log(error);
            if (error.response.status === 400) {
              if (this.getActingAsBusinessId() == null) {
                this.$vs.notify({
                  title: 'Failed to add an inventory item',
                  text: 'User is not a business administrator',
                  color: 'danger'
                });
              } else {
                this.$vs.notify({
                  title: 'Failed to add an inventory item',
                  text: 'Incomplete form, or the product does not exist.',
                  color: 'danger'
                });
              }
            } else if (error.response.status === 403) {
            this.$vs.notify({
              title: 'Failed to add an inventory item',
              text: 'You do not have the rights to access this business',
              color: 'danger'
            });
          } else if (error.response.status === 404) {
            this.$vs.notify({
              title: 'Failed to add an inventory item',
              text: 'There was a problem adding the inventory item to the database',
              color: 'danger'
            });
          }
          console.log(error.response.status);
        }
          this.$log.debug("Error Status:", error);
        })
      }
    },
    getBusinessProducts() {
      api.getBusinessProducts(store.actingAsBusinessId)
          .then((response) => {
            this.$log.debug("Data loaded: ", response.data);
            this.products = response.data;
          })
          .catch((error) => {
            this.$log.debug(error);
            this.error = "Failed to load products";
          });
    },

    /**
     * Gets business id user is acting as
     **/
    getActingAsBusinessId() {
      return store.actingAsBusinessId;
    },
  },
  mounted(){
    this.getBusinessProducts();
  }
}
</script>


<style>
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

.vs-popup--header {
  background-color: #1F74FF;
  color: #FFFFFF;
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


</style>