<template>
  <div v-if="currentProduct != null">
    <vs-popup title="Modify Inventory Entry" :active.sync="modifyInv" id="modify-modal">
      <div id="header-row">
        <div id="image-container">
          <vs-tooltip text="Click here for more product details">
            <vs-tooltip :text="currentProduct.description" title="Description" position="bottom">
              <img @click="showFullProduct = true" v-if="currentProduct.primaryImagePath != null && isDevelopment()" class="image" v-bind:src="require('../../../backend/src/main/resources/media/images/businesses/' + getImgUrl(currentProduct))" alt="Product image"/>
              <img @click="showFullProduct = true" v-if="currentProduct.primaryImagePath != null && !isDevelopment()" class="image" alt="Product Image" v-bind:src="getImgUrl(currentProduct)"/>
              <img @click="showFullProduct = true" v-if="!currentProduct.primaryImagePath && isDevelopment()" class="image" src="ProductShoot.jpg" alt="Product image"/>
              <img @click="showFullProduct = true" v-if="!isDevelopment() && !currentProduct.primaryImagePath" class="image" :src="getImgUrl(true)" alt="Product image"/>
            </vs-tooltip>
          </vs-tooltip>
        </div>
        <div id="product-name">
          <div class="sub-header">Product</div>
            <vs-select id="product-select" v-model="currentProduct">
              <vs-select-item :value="product" :text="`[${product.id}] ${product.name}`" v-for="product in products" :key="product.id"/>
            </vs-select>
        </div>
      </div>
      <vs-divider/>

      <div class="vs-col" vs-order="1" id="first-col-modal">
        <div class="row">
          <label for="pricePerItem">Price per item</label>
          <vs-input
              type="number"
              :danger="(errors.includes('pricePerItem'))"
              danger-text="Price per item must be greater than zero and numeric."
              id="pricePerItem"
              :min="0"
              v-model="invenForm.pricePerItem"/>
        </div>
        <div class="row">
          <label for="total-price">Total Price</label>
          <vs-input
              type="number"
              :danger="(errors.includes('totalPrice'))"
              danger-text="Total price must be greater than zero and numeric."
              id="total-price"
              :min="0"
              v-model="invenForm.totalPrice"/>
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
      <div class="vs-col" vs-order="2" id="second-col-modal">
        <div class="row">
          <label for="bestBefore">Best before</label>
          <vs-input
              :danger="(errors.includes('past-best'))"
              danger-text="Date cannot be in past"
              type="date"
              id="bestBefore"
              v-model="invenForm.bestBefore"/>
        </div>
        <div class="row">
          <label for="listingExpiry">Listing expiry</label>
          <vs-input
              :danger="(errors.includes('past-expiry'))"
              danger-text="Expiry date is required and cannot be in past"
              type="date"
              id="listingExpiry"
              v-model="invenForm.listExpiry"/>
        </div>
        <div class="row">
          <label for="manufactureDate">Manufacture date</label>
          <vs-input
              :danger="(errors.includes('future-manu'))"
              danger-text="Date cannot be in the future"
              type="date"
              id="manufactureDate"
              v-model="invenForm.manufactureDate"/>
        </div>
        <div class="row">
          <label for="sellBy">Sell by</label>
          <vs-input
              :danger="(errors.includes('past-sell'))"
              danger-text="Date cannot be in past"
              type="date"
              id="sellBy"
              v-model="invenForm.sellBy"/>
        </div>
      </div>
      <div class="vs-col" id="add-button" @click="updateInventory()">
        <vs-button>Update Inventory Entry</vs-button>
      </div>
      <div class="vs-col" id="cancel-button" @click="modifyInv=false">
        <vs-button type="border">Cancel</vs-button>
      </div>
    </vs-popup>

    <!-- SHOW FULL PRODUCT INFO MODAL -->
    <!-- Accessed by pressing the image of the product. -->
    <vs-popup id="full-product-modal" title="Full Product" :active.sync="showFullProduct" >
      <div>
        <img v-if="currentProduct.primaryImagePath != null && isDevelopment()" class="full-image" v-bind:src="require('../../../backend/src/main/resources/media/images/businesses/' + getImgUrl(currentProduct))" alt="Product image"/>
        <img v-if="currentProduct.primaryImagePath != null && !isDevelopment()" class="full-image" alt="Product Image" v-bind:src="getImgUrl(currentProduct)"/>
        <img v-if="!currentProduct.primaryImagePath && isDevelopment()" class="full-image" src="ProductShoot.jpg" alt="Product image"/>
        <img v-if="!isDevelopment() && !currentProduct.primaryImagePath" class="full-image" :src="getImgUrl(true)" alt="Product image"/>
      </div>

      <div style="font-size: 13pt; height:100%; line-height: 1.5; display:flex; flex-direction: column;">
        <div style="display: flex; flex-direction: column; justify-content: space-between">
          <div style="font-size: 16px; font-weight: bold;  text-align: justify; word-break: break-all;">{{ currentProduct.name }} </div>
          <p>{{ currentProduct.id }}</p>
        </div>
        <vs-divider></vs-divider>
        <div style="font-size: 16px; font-weight: bold; height: 24px;">{{ currentProduct.manufacturer }} </div>
        <p style="font-size: 14px; margin-bottom: 8px;">Created: {{ currentProduct.created }} </p>
        <div style="height: 75px; font-size: 14px; overflow-y: auto; ">{{ currentProduct.description }} </div>
      </div>

      <div style="font-size: 25pt; font-weight: bold; margin: auto 0" >{{currency + currentProduct.recommendedRetailPrice }} </div>
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
      currentProduct: null,
      item: null,
      invenForm: {
        prodId: '',
        pricePerItem: 0.0,
        totalPrice: 0.0,
        quantity: 0,
        bestBefore: '',
        listExpiry: '',
        manufactureDate: '',
        sellBy: ''
      },

      modifyInv: false,
      products: [],
      errors: [],

      showFullProduct: false,
      currency: "$",
    }
  },

  methods: {
    /**
     * Called to open the modal, setting fields to the current inventory item's values.
     * Typically called from an outside component.
     */
    open: function(inventory, currency) {
      this.modifyInv = true;

      this.item = inventory;
      this.currency = currency;
      this.getBusinessProducts();
      this.setCurrentItem(this.item);
    },

    /**
     * Checks if the current web environment is in development mode.
     */
    isDevelopment() {
      return (process.env.NODE_ENV === 'development');
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
     * Checks the form for valid input fields.
     * @return boolean true if there are no errors, false otherwise.
     */
    checkForm: function() {
      this.errors = [];

      const today = new Date();
      const invalidChars = /^([a-zA-Z0-9\u0600-\u06FF\u0660-\u0669\u06F0-\u06F9 _.-]+)$/;
      if (!invalidChars.test(this.invenForm.prodId)) {
        this.errors.push("invalid-chars");
      }

      if (isNaN(this.invenForm.pricePerItem)) {
        this.errors.push('pricePerItem');
      }

      if (this.invenForm.totalPrice == null) {
        this.errors.push('totalPrice');
      }

      let dateInPast = function(firstDate, secondDate) {
        return firstDate.setHours(0, 0, 0, 0) <= secondDate.setHours(0, 0, 0, 0);
      }

      let dateInFuture = function(firstDate, secondDate) {
        return firstDate.setHours(0, 0, 0, 0) >= secondDate.setHours(0, 0, 0, 0);
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

      if (this.invenForm.totalPrice <= 0.0) {
        this.errors.push('totalPrice');
      }

      let timestamp;
      let dateObject;
      if (this.invenForm.bestBefore !== '') {
        timestamp = Date.parse(this.invenForm.bestBefore);
        dateObject = new Date(timestamp)
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

      if (this.errors.includes(this.invenForm.quantity)) {
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
     * Retrieves the product catalogue of the business.
     */
    getBusinessProducts() {
      api.getBusinessProducts(store.actingAsBusinessId)
          .then((response) => {
            this.$log.debug("Data loaded: ", response.data);
            this.products = response.data;

            // Done to make the select dropdown properly find the correct product object and set as default.
            this.currentProduct = this.products.find(product => {
              return product.id === this.item.productId;
            });
          })
          .catch((error) => {
            this.$log.debug(error);
            this.error = "Failed to load products";
          });
    },

    /**
     * Makes a call to the server to update the inventory with the new modified details (when the inputs are valid).
     */
    updateInventory: function() {
      this.invenForm.prodId = this.currentProduct.id;
      this.checkForm();
      if (this.errors.length === 0) {
        api.modifyInventory(store.actingAsBusinessId, this.item.id, this.invenForm.prodId, this.invenForm.quantity, this.invenForm.pricePerItem, this.invenForm.totalPrice, this.invenForm.manufactureDate, this.invenForm.sellBy, this.invenForm.bestBefore, this.invenForm.listExpiry)
            .then((response) => {
              this.$log.debug("Inventory item updated:", response.data);
              //this.inventory.push(response.data);
              this.addNewInv = false;
              this.modifyInv = false;
              this.$emit('submitted');
              this.$vs.notify( {
                title: `Item successfully modified`,
                color: 'success'
              });
            })
            .catch((error) => {
              if (error.response) {
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
              }
              this.$log.debug("Error Status:", error)
        });
      }
    },

    /**
     * Sets the input fields to the current inventory item values.
     * @param item the item object to get the values from.
     */
    setCurrentItem(item) {
      if (item !== undefined) {
        this.invenForm.prodId = item.productId;
        this.invenForm.manufactureDate = item.manufactured;
        this.invenForm.sellBy = item.sellBy;
        this.invenForm.bestBefore = item.bestBefore;
        this.invenForm.listExpiry = item.expires;
        this.invenForm.quantity = item.quantity;
        this.invenForm.pricePerItem = item.pricePerItem;
        this.invenForm.totalPrice = item.totalPrice;
      }
    },

    /**
     * Updates the total price value when either quantity or price per item changes.
     */
    updateTotalPrice: function() {
      this.invenForm.totalPrice = (this.invenForm.quantity * this.invenForm.pricePerItem).toFixed(2);
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

#modify-modal {
  z-index: 100;
}

#product-select {
  width: 275px;
}

#first-col-modal {
  margin-right: 160px;
  margin-left: 5px;
}

.row {
  margin-bottom: 15px;
}

.vs-popup--header {
  background-color: #1F74FF;
  color: #FFFFFF;
}

.vs-popup-primary >>> header {
  background-color: #1F74FF;
  color: #FFFFFF;
}

#cancel-button {
  margin-left: 4px;
}

#image-container {
  margin: auto;
  cursor: pointer;

  transition: .5s ease;
  backface-visibility: hidden;
}

#image-container:hover {
  opacity: 0.5;
}


@media screen and (max-width: 630px) {
  #header-row {
    display: flex;
    flex-direction: column;
  }

  #add-button {
    margin: 0 auto;
  }

  #cancel-button {
    margin: 0.5em auto;
  }
}


</style>