<template>
  <vs-card id="container">

    <div id="header-container">
      <div id="title"> Inventory </div>
      <div id="header-buttongroup">
        <vs-button class="header-button" @click="$router.push(`/businesses/${$route.params.id}/products`)">Product Catalogue</vs-button>
        <vs-button class="header-button">New Inventory Listing</vs-button>
      </div>
    </div>

    <vs-divider></vs-divider>
    <!-- Table View -->
    <vs-table id="table"
              :data="this.inventory"
              noDataText="You don't have any inventory."
              :pagination="true"
              :maxItems="10"
              :notSpacer="true"
              stripe>
      <template class="table-head" slot="thead" >
        <vs-th style="border-radius: 8px 0 0 0"> <!-- Image Column --> </vs-th>
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
        <vs-td style="height: 80px;">
          <img v-if="inventory.product.primaryImagePath" style="width:auto; height: 100%;   border-radius: 1em;" v-bind:src="require('../../../backend/src/main/resources/media/images/businesses/' + getImgUrl(product))"/>
          <img v-if="!inventory.product.primaryImagePath" style="width: auto; height: 100%;   border-radius: 1em;" v-bind:src="require('../../public/ProductShoot.jpg')"/>
        </vs-td>
          <vs-td id="productIdCol" :data="inventory.productId"> {{inventory.productId}} </vs-td>
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
export default {
  name: "BusinessInventory",

  data: function() {
    return {
      inventory: [],
    }
  },

  mounted() {
    this.getBusinessInventory();
  },

  methods: {
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
    width: 110px;
  }


  

</style>