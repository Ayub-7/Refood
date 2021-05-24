<template>
  <div id="form-outer">
    <vs-button @click="modifyInv=true" class="header-button">Edit</vs-button>
    <vs-popup classContent="popup-example"  title="Modify Inventory Item" :active.sync="modifyInv">
      <div class="form-group required vs-col" vs-order="1" id="firstColModal">
        <div class="row">
          <label for="prodId">Product</label>
          <vs-select id="prodId" class="selectExample" v-model="invenForm.prodId" disabled>
            <vs-select-item :value="product.id" :disabld="product.id = item.prodId" :text="product.name" v-for="product in products" v-bind:href="product.id" :key="product.id"/>
          </vs-select>
        </div>
        <div class="row">
          <label for="pricePerItem">Price per item</label>
          <vs-input
              :danger="(errors.includes(invenForm.pricePerItem))"
              danger-text="Price per item must be greater than zero and numeric."
              class="inputx"
              id="pricePerItem"
              placeholder="Price per item"
              v-model="invenForm.pricePerItem"/>
        </div>
        <div class="row">
          <label for="quantity">Quantity</label>
          <vs-input-number
              :danger="(errors.includes(invenForm.quantity))"
              danger-text="Quantity must be greater than zero."
              min="0"
              :step="1"
              id="quantity"
              v-model="invenForm.quantity"/>
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
          <label for="listingExpiry">Listing expiry</label>
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
              :danger="(errors.includes('past-manu'))"
              danger-text="Date cannot be in past"
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
      <div class="form-group required vs-col" align="center" id="addButton" @click="updateInventory(); checkForm()">
        <vs-button>Update product</vs-button>
      </div>
      <div class="form-group required vs-col" align="center" id="cancelButton" @click="modifyInv=false">
        <vs-button>Cancel</vs-button>
      </div>
    </vs-popup>
  </div>
</template>

<script>
import api from "../Api";
import {store} from "../store";

export default {
  name: "ModifyInventory",
  data() {
    return {
      invenForm: {
        prodId: '',
        pricePerItem: 0.0,
        quantity: 0,
        bestBefore: '',
        listExpiry: '',
        manufactureDate: '',
        sellBy: ''
      },
      modifyInv: false,
      products: [],
      errors: [],
    }
  },
  props: ['item'],
  methods: {
    checkForm: function() {
      this.errors = [];
      var invalidChars = /[^a-zA-Z/ -\d]/i;
      if (this.invenForm.prodId.match(invalidChars)) {
        this.errors.push("invalid-chars");
      }

      var today = new Date();
      var regex = /^[0-9]*(?:\.\d{1,2})?$/;
      if( !regex.test(this.invenForm.pricePerItem) ) {
        this.errors.push(this.invenForm.pricePerItem);
      }

      var dateInPast = function(firstDate, secondDate) {
        if(firstDate.setHours(0,0,0,0) <= secondDate.setHours(0,0,0,0)) {
          return true;
        }

        return false;
      }
      if (this.invenForm.bestBefore === '' && this.invenForm.sellBy === '' && this.invenForm.manufactureDate === ''
          && this.listExpiry === '') {
        this.errors.push('no-dates');
      }

      if (this.invenForm.listExpiry === '') {
        this.errors.push('past-expiry');
      }

      if (this.invenForm.prodId.length === 0 || this.errors.includes('invalid-chars')) {
        this.errors.push(this.invenForm.prodId);
      }
      if (this.invenForm.pricePerItem <= 0.0) {
        this.errors.push(this.pricePerItem);
      }
      if (this.invenForm.bestBefore !== '') {
        var timestamp = Date.parse(this.bestBefore);
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
        if (dateInPast(dateObject, today) === false) {
          this.errors.push('future-date');
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
      if (this.errors.includes(this.invenForm.quantity)) {
        this.$vs.notify({
          title: 'Failed to create inventory item',
          text: 'Quantity must be greater than zero.',
          color: 'danger'
        });
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
    updateInventory: function() {
      console.log(store.actingAsBusinessId, this.item.id, this.invenForm.prodId, this.invenForm.quantity, this.invenForm.pricePerItem, this.invenForm.totalPrice, this.invenForm.manufactureDate, this.invenForm.sellBy, this.invenForm.bestBefore, this.invenForm.listExpiry);
      if (this.errors.length === 0) {
        api.modifyInventory(store.actingAsBusinessId, this.item.id, this.invenForm.prodId, this.invenForm.quantity, this.invenForm.pricePerItem, this.invenForm.totalPrice, this.invenForm.manufactureDate, this.invenForm.sellBy, this.invenForm.bestBefore, this.invenForm.listExpiry)
            .then((response) => {
              this.$log.debug("Inventory item updated:", response.data);
              //this.inventory.push(response.data);
              this.addNewInv = false;
              this.$emit('submitted');
              this.$vs.notify( {
                title: `Item successfully modified`,
                color: 'success'
              });
            }).catch((error) => {
          if (error.response) {
            console.log(error);
            if (error.response.status === 400) {
              console.log(error.response);
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
  },
  mounted(){
    this.getBusinessProducts();
    this.invenForm.prodId = this.item.productId;
    this.invenForm.manufactureDate = this.item.manufactured;
    this.invenForm.sellBy = this.item.sellBy;
    this.invenForm.bestBefore = this.item.bestBefore;
    this.invenForm.listExpiry = this.item.expires;
    this.invenForm.quantity = this.item.quantity;
    this.invenForm.pricePerItem = this.item.pricePerItem;
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