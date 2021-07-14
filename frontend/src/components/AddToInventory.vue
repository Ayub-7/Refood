<template>
  <div id="form-outer" v-if="product != null">
    <vs-popup title="Add New Inventory Entry" :active.sync="addNewInv">
      <div id="product-name">
        <div class="sub-header">Product</div>
        <div style="font-size: 18px; text-align: center; font-weight: bold">{{ product.name }}</div>
        <div>{{ product.id }}</div>
      </div>
      <vs-divider/>

      <div class="vs-col" vs-order="1" id="first-col-modal" >
        <div class="row">
          <label for="pricePerItem">Price per item *</label>
          <vs-input
              type="number"
              :danger="(errors.includes('pricePerItem'))"
              danger-text="Price per item must be greater than zero and numeric."
              id="pricePerItem"
              v-model="invenForm.pricePerItem"/>
        </div>
        <div class="row">
          <label for="totalPrice">Total price *</label>
          <vs-input
              type="number"
              :danger="(errors.includes(invenForm.totalPrice))"
              danger-text="Total price must be greater than zero and numeric."
              id="totalPrice"
              v-model="invenForm.totalPrice"/>
        </div>
        <div class="row">
          <label for="quantity">Quantity *</label>
          <vs-input-number
              :danger="(errors.includes(invenForm.quantity))"
              danger-text="Quantity must be greater than zero."
              :min="1"
              :step="1"
              id="quantity"
              v-model="invenForm.quantity"/>
        </div>
      </div>
      <div class="vs-col" vs-order="2" id="second-col-modal">
        <div class="row">
          <label for="bestBefore">Best Before</label>
          <vs-input
              :danger="(errors.includes('past-best'))"
              danger-text="Date cannot be in past"
              type="date"
              id="bestBefore"
              v-model="invenForm.bestBefore"/>
        </div>
        <div class="row">
          <label for="expiryDate">Expiry Date *</label>
          <vs-input
              :danger="(errors.includes('past-expiry'))"
              danger-text="Expiry date is required and cannot be in past"
              type="date"
              id="expiryDate"
              v-model="invenForm.listExpiry"/>
        </div>
        <div class="row">
          <label for="manufactureDate">Manufacture Date</label>
          <vs-input
              :danger="(errors.includes('future-manu'))"
              danger-text="Date cannot be in the future"
              type="date"
              id="manufactureDate"
              v-model="invenForm.manufactureDate"/>
        </div>
        <div class="row">
          <label for="sellBy">Sell By</label>
          <vs-input
              :danger="(errors.includes('past-sell'))"
              danger-text="Date cannot be in past"
              type="date"
              id="sellBy"
              v-model="invenForm.sellBy"/>
        </div>
      </div>
      <div class="required vs-col" align="center" id="addButton">
        <vs-button @click="addInventory">Add To Inventory</vs-button>
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
      product: null,
      invenForm: {
        prodId: '',
        pricePerItem: 0.0,
        totalPrice: 0.0,
        quantity: 1,
        bestBefore: '',
        listExpiry: '',
        manufactureDate: '',
        sellBy: '',
      },

      addNewInv: false,
      errors: [],
    }

  },
  methods: {
    /**
     * Opens the modal, prefills any inputs that need to prefilled.
     */
    open: function(product) {
      this.errors = [];
      this.product = product;
      this.invenForm.pricePerItem = product.recommendedRetailPrice;
      this.invenForm.prodId = product.id;

      this.addNewInv = true;
    },

    /**
     * Checks the form inputs, validating the inputted values.
     * @return boolean true if no errors found, false otherwise.
     */
    checkForm: function() {
      this.errors = [];
      var today = new Date();
      const invalidChars = /^([a-zA-Z0-9\u0600-\u06FF\u0660-\u0669\u06F0-\u06F9 _.-]+)$/;
      if (!invalidChars.test(this.invenForm.prodId)) {
        this.errors.push("invalid-chars");
      }

      if(isNaN(this.invenForm.pricePerItem)) {
        this.errors.push('pricePerItem');
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
        this.errors.push('pricePerItem');
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
          this.errors.push('future-date');
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

    /**
     * Updates the total price value when either quantity or price per item changes.
     */
    updateTotalPrice: function() {
      this.invenForm.totalPrice = (this.invenForm.quantity * this.invenForm.pricePerItem).toFixed(2);
    },

    /**
     * Saves the inventory to the backend (if input fields are valid).
     */
    addInventory: function() {
      if (this.checkForm()) {
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
            })
            .catch((error) => {
              if (error.response) {
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
            }
            this.$log.debug("Error Status:", error);
            });
      }
    },

    /**
     * Gets business id user is acting as
     **/
    getActingAsBusinessId() {
      return store.actingAsBusinessId;
    },

  },

  watch: {
    "invenForm.quantity": function() {
      this.updateTotalPrice();
    },

    "invenForm.pricePerItem": function() {
      this.updateTotalPrice();
    },
  }
}
</script>


<style>
#product-name {
  grid-row: 1;
  grid-column: 2;

  text-align: center;
  margin: auto;
}

.sub-header {
  font-size: 12px;
  color: gray;
}

#first-col-modal {
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

.textarea >>> textarea {
  resize: none;
  max-width: 200px;
  min-height: 100px;
  max-height: 100px;
}

@media screen and (max-width: 630px) {
  .vs-popup--content {
    display: flex;
    flex-direction: column;
  }

  div#first-col-modal {
    margin: 0 auto;
  }

  #second-col-modal {
    margin: 0 auto;
  }

}


</style>