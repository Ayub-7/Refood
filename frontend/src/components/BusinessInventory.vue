<template>
  <vs-card id="container">

    <div id="header-container">
      <div id="title"> Inventory </div>
      <div id="header-buttongroup">
        <vs-button class="header-button" @click="$router.push(`/businesses/${$route.params.id}/products`)">Product Catalogue</vs-button>
        <vs-button @click="popupActivo2=true" class="header-button">New Inventory Listing</vs-button>
        <!-- TEST -->
        <vs-popup classContent="popup-example"  title="Lorem ipsum dolor sit amet" :active.sync="popupActivo2">
          <vs-input class="inputx" placeholder="Placeholder" v-model="value1"/>
          <vs-input disabled class="inputx" placeholder="Disabled" v-model="value2"/>

          <p>
            Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut
          </p>
          <vs-select
              label="Figuras"
              v-model="select1"
          >
            <vs-select-item :key="index" :value="item.value" :text="item.text" v-for="item,index in options1" />
          </vs-select>
          <vs-button @click="popupActivo3=true" color="primary" type="filled">Open Inner Popup</vs-button>
          <vs-popup title="Inner popup" :active.sync="popupActivo3">
            <p>
              Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
              <br><br>
              Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
              <br><br>
              Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
            </p>
          </vs-popup>
        </vs-popup>
        <!-- TEST -->
      </div>
    </div>

    <vs-divider></vs-divider>
    <!-- Table View -->
    <vs-table id="table"
              data="inventory"
              noDataText="You don't have any inventory."
              :pagination="true"
              :maxItems="10"
              stripe>
      <template class="table-head" slot="thead" >
        <vs-th style="border-radius: 8px 0 0 0"> <!-- Image Column --> </vs-th>
        <vs-th> ID </vs-th>
        <vs-th> Product </vs-th>
        <vs-th> Manufacturer </vs-th>
        <vs-th> Sell By </vs-th>
        <vs-th> Best Before </vs-th>
        <vs-th> Expires </vs-th>
        <vs-th> Qty </vs-th>
        <vs-th> Price Per Item </vs-th>
        <vs-th> Total Price </vs-th>
        <vs-th style="border-radius: 0 8px 0 0"> <!-- Action Button Column --> </vs-th>
      </template>
      <template slot-scope="{data}">
        <vs-tr v-for="listing in data" :key="listing"> <!-- todo: connect data with table -->
          <vs-td> </vs-td>
          <vs-td> </vs-td>
          <vs-td> </vs-td>
          <vs-td> </vs-td>
          <vs-td> </vs-td>
          <vs-td> </vs-td>
          <vs-td> </vs-td>
          <vs-td> </vs-td>
          <vs-td> </vs-td>
          <vs-td> </vs-td>
          <vs-td> </vs-td>
        </vs-tr>
      </template>
    </vs-table>

  </vs-card>
</template>

<script>
export default {
  name: "BusinessInventory",
  data(){
    return {
      inventory: [],
      select1:3,
      options1:[
        {text:'IT',value:0},
        {text:'Blade Runner',value:2},
        {text:'Thor Ragnarok',value:3},
      ],
      value1:'',
      value2:'',
      popupActivo2:false,
      popupActivo3:false
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
  }

  #table {
    margin: 1em;
  }

</style>