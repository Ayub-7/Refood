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
              stripe>
      <template class="table-head" slot="thead" >
        <vs-th style="border-radius: 8px 0 0 0"> <!-- Image Column --> </vs-th>
        <vs-th sort-key="id"> ID </vs-th>
        <vs-th sort-key="name"> Product </vs-th>
        <vs-th sort-key="manufacturer"> Manufacturer </vs-th>
        <vs-th sort-key="sellBy"> Sell By </vs-th>
        <vs-th sort-key="bestBefore"> Best Before </vs-th>
        <vs-th sort-key="expires"> Expires </vs-th>
        <vs-th sort-key="quantity"> Qty </vs-th>
        <vs-th> Price Per Item </vs-th>
        <vs-th> Total Price </vs-th>
        <vs-th style="border-radius: 0 8px 0 0"> <!-- Action Button Column --> </vs-th>
      </template>
      <template slot-scope="{data}">
        <vs-tr v-for="inventory in data" :key="inventory.id"> <!-- todo: connect data with table -->
        <vs-td style="height: 100px;">
                        <img v-if="inventory.product.primaryImagePath" style="width:auto; height: 100%;   border-radius: 1em;" v-bind:src="require('../../../backend/src/main/resources/media/images/businesses/' + getImgUrl(product))"/>
                        <img v-if="!inventory.product.primaryImagePath" style="width: auto; height: 100%;   border-radius: 1em;" v-bind:src="require('../../public/ProductShoot.jpg')"/>
        </vs-td>
          <vs-td :data="inventory.product.id"> {{inventory.product.id}} </vs-td>
          <vs-td :data="inventory.product.name"> {{inventory.product.name}} </vs-td>
          <vs-td :data="inventory.product.manufacturer"> {{inventory.product.manufacturer}} </vs-td>
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
    api.getBusinessInventory(this.$route.params.id)
    .then((response) => {
      this.inventory = response.data;
    }).catch((err) => {
      if(err.response.status == 401) {
        this.$router.push({path: '/login'})
      }
    })
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

</style>