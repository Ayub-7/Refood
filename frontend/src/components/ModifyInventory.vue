<template>
  <vs-popup classContent="popup-example"  title="Modify Inventory Product">
    <div class="form-group required vs-col" vs-order="1" id="firstColModal">
      <div class="row">
        <label for="prodId">Product</label>
        <vs-input
            v-model="product"
        />
      </div>
      <div class="row">
        <label for="pricePerItem">Price per item</label>
        <vs-input
            :danger="(errors.includes(pricePerItem))"
            danger-text="Price per item must be greater than zero and numeric."
            class="inputx"
            id="pricePerItemEdit"
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
            id="quantityEdit"
            v-model="quantity"/>
      </div>
      <div class="row">
        <label for="description">Description</label>
        <vs-textarea
            width="200px"
            height="50px"
            class="description-textarea"
            id="descriptionEdit"
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
            id="bestBeforeEdit"
            class="inputx"
            v-model="bestBefore"/>
      </div>
      <div class="row">
        <label for="listingExpiry">Listing expiry</label>
        <vs-input
            :danger="(errors.includes('past-expiry'))"
            danger-text="Expiry date is required and cannot be in past"
            type="date"
            id="listingExpiryEdit"
            class="inputx"
            v-model="listExpiry"/>
      </div>
      <div class="row">
        <label for="manufactureDate">Manufacture date</label>
        <vs-input
            :danger="(errors.includes('past-manu'))"
            danger-text="Date cannot be in past"
            type="date"
            id="manufactureDateEdit"
            class="inputx"
            v-model="manufactureDate"/>
      </div>
      <div class="row">
        <label for="sellBy">Sell by</label>
        <vs-input
            :danger="(errors.includes('past-sell'))"
            danger-text="Date cannot be in past"
            type="date"
            id="sellByEdit"
            class="inputx"
            v-model="sellBy"/>
      </div>
    </div>
    <div class="form-group required vs-col" align="center" id="addButtonEdit">
      <vs-button v-on:click="submitModify">Modify product</vs-button>
    </div>
  </vs-popup>
</template>

<script>
export default {
  name: "ModifyInventory",
  data() {
  return{
    errors: [],
    pricePerItem: 0.0,
    totalPrice: 0.0,
    quantity: 0,
    invDescription: '',
    bestBefore: '',
    listExpiry: '',
    manufactureDate: '',
    sellBy: '',
  }
  },
  methods: {

  }
}

</script>