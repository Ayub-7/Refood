<template>
  <div id="form-outer" v-if="product != null">
    <vs-popup id="inventory-modal" title="Add New Inventory Entry" :active.sync="addNewInv">
      <!-- Product Image -->
      <div id="header-row">
        <div style="margin: auto; cursor: pointer;">
          <vs-tooltip text="Click here for more product details">
            <img @click="showFullProduct = true" v-if="product.primaryImagePath != null && isDevelopment()" class="image" v-bind:src="require('../../../backend/src/main/resources/media/images/businesses/' + getImgUrl(product))" alt="Product image"/>
            <img @click="showFullProduct = true" v-if="product.primaryImagePath != null && !isDevelopment()" class="image" alt="Product Image" v-bind:src="getImgUrl(product)"/>
            <img @click="showFullProduct = true" v-if="!product.primaryImagePath && isDevelopment()" class="image" src="ProductShoot.jpg" alt="Product image"/>
            <img @click="showFullProduct = true" v-if="!isDevelopment() && !product.primaryImagePath" class="image" :src="getImgUrl(true)" alt="Product image"/>
          </vs-tooltip>
        </div>
        <div id="product-name">

            <div class="sub-header">Product</div>
            <div style="font-size: 18px; text-align: center; font-weight: bold">{{ product.name }}</div>
            <div>{{ product.id }}</div>
            <vs-tooltip :text="product.description" title="Description" position="bottom">
              <vs-icon size="1rem" icon="info"></vs-icon>
            </vs-tooltip>
        </div>
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

    <!-- SHOW FULL PRODUCT INFO MODAL -->
    <!-- Accessed by pressing the image of the product. -->
    <vs-popup id="full-product-modal" title="Full Product" :active.sync="showFullProduct">
        <div>
          <img v-if="product.primaryImagePath != null && isDevelopment()" class="full-image" v-bind:src="require('../../../backend/src/main/resources/media/images/businesses/' + getImgUrl(product))" alt="Product image"/>
          <img v-if="product.primaryImagePath != null && !isDevelopment()" class="full-image" alt="Product Image" v-bind:src="getImgUrl(product)"/>
          <img v-if="!product.primaryImagePath && isDevelopment()" class="full-image" src="ProductShoot.jpg" alt="Product image"/>
          <img v-if="!isDevelopment() && !product.primaryImagePath" class="full-image" :src="getImgUrl(true)" alt="Product image"/>
        </div>

        <div style="font-size: 13pt; height:100%; line-height: 1.5; display:flex; flex-direction: column;">
          <div style="display: flex; flex-direction: column; justify-content: space-between">
            <div style="font-size: 16px; font-weight: bold;  text-align: justify; word-break: break-all;">{{ product.name }} </div>
            <p>{{ product.id }}</p>
          </div>
          <vs-divider></vs-divider>
          <div style="font-size: 16px; font-weight: bold; height: 24px;">{{ product.manufacturer }} </div>
          <p style="font-size: 14px; margin-bottom: 8px;">Created: {{ product.created }} </p>
          <div style="height: 75px; font-size: 14px; overflow-y: auto; ">{{ product.description }} </div>
        </div>

        <div style="font-size: 25pt; font-weight: bold; margin: auto 0" >{{currency + " " +  product.recommendedRetailPrice }} </div>
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
      currency: "$",
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

      showFullProduct: false,
    }

  },
  methods: {
    /**
     * Opens the modal, prefills any inputs that need to prefilled, and clears any other already filled in inputs.
     */
    open: function(product, currency) {
      this.errors = [];
      this.invenForm.sellBy = '';
      this.invenForm.bestBefore = '';
      this.invenForm.manufactureDate = '';
      this.invenForm.listExpiry = '';
      this.invenForm.quantity = 1;

      this.product = product;
      this.currency = currency;
      this.invenForm.pricePerItem = product.recommendedRetailPrice;
      this.invenForm.prodId = product.id;
      this.updateTotalPrice();

      this.addNewInv = true;
    },

    /**
     * Checks if the current web environment is in development mode.
     */
    isDevelopment() {
      return (process.env.NODE_ENV === 'development')
    },

    /**
     * Retrieves the image url link for the given product.
     * @param product the product to retrieve the image for.
     * @return a string link to the product image, or the default image if it doesn't have a product.
     **/
    getImgUrl(product) {
      if (product === true && process.env.NODE_ENV !== 'staging') {
        return '/prod/ProductShoot.jpg';
      } else if (product === true) {
        return '/test/ProductShoot.jpg';
      } else if (product.primaryImagePath != null && process.env.NODE_ENV !== 'development' && process.env.NODE_ENV !== 'staging') {
        return '/prod/prod_images/' + product.primaryImagePath.toString().replace("\\", "/")
      } else if (product.primaryImagePath != null && process.env.NODE_ENV !== 'development') {
        return '/test/prod_images/' + product.primaryImagePath.toString().replace("\\", "/")
      } else if (product.primaryImagePath != null) {
        return product.primaryImagePath.toString().replace("\\", "/")
      } else {
        return '../../public/ProductShoot.jpg'
      }
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

#inventory-modal {
  z-index: 100;
}

#header-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
}

.image {
  margin: auto;
  width: 150px;
  height: 100px;
  object-fit: cover;
  border-radius: 4px;
}

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

/* === FULL PRODUCT MODAL === */

#full-product-modal > .vs-popup {
  width: 350px;
}

.full-image {
  height: 210px;
  border-radius: 4px 4px 0 0;
  object-fit: cover;
  margin-left: 4px;
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