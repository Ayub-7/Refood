<template>
<div id="form-outer">
<!--  <vs-button v-if="isCreate" @click="addNewInv=true" class="header-button">New Inventory Listing</vs-button>-->
<!--  <vs-button v-if="!isCreate" @click="addNewInv=true" class="header-button1">Edit</vs-button>-->
  <vs-popup classContent="popup-example"  :title="title" :active.sync="addNewInv">
  <div class="form-group required vs-col" vs-order="1" id="firstColModal">
    <div class="row">
      <label for="prodId">Product</label>
      <vs-select id="prodId" class="selectExample" v-model="invenForm.prodId">
        <vs-select-item :value="product.id" :text="product.name" v-for="product in products" v-bind:href="product.id" :key="product.id"/>
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
  <div class="form-group required vs-col" align="center" id="addButton" v-if="isCreate" @click="addInventory(); checkForm()">
    <vs-button>Add product</vs-button>
  </div>
  <div class="form-group required vs-col" align="center" id="updateButton" v-if="!isCreate" @click="updateInventory(); checkForm()">
    <vs-button>Update product</vs-button>
  </div>
  <div class="form-group required vs-col" align="center" id="cancelButton" @click="addToInv = false">
    <vs-button>Cancel</vs-button>
  </div>
  </vs-popup>
</div>
</template>

<script>
export default {
  name: "InventoryForm",
  prop: ['isCreate', 'item', 'addNewInv'],
  data() {
  return{
    invenForm: {
      prodId: '',
      pricePerItem: 0.0,
      quantity: 0,
      bestBefore: '',
      listExpiry: '',
      manufactureDate: '',
      sellBy: ''
    },
    title: '',
    isCreate: null,
    addNewInv: true,
    //addNewInv: false,
    item: null,
    products: [],
    errors: [],
  }
  },
  methods: {
    closeModal(){

    },
    updateInventory(){

    },
    checkForm(){

    },
    addInventory(){

    }
  },
  mounted(){
    console.log(this.addNewInv);
    if(this.isCreate){
      this.title = "Add a product to your inventory";
    } else {
      this.title = "Modify Inventory Product";
    }

    //prefill values if editing
    if(this.item){
      this.invenForm.prodId = this.item.productId;
      this.invenForm.pricePerItem = this.item.pricePerItem;
      this.invenForm.quantity= this.item.quantity;
      this.invenForm.bestBefore= this.item.bestBefore;
      this.invenForm.listExpiry = this.item.expires;
      this.invenForm.manufactureDate= this.item.manufactured;
      this.invenForm.sellBy =  this.item.sellBy;
    }
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