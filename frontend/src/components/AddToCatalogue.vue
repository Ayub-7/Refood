<template>
  <div class="card">
    <h3 class="card-header">Add a Product to your Catalogue</h3>
    <form>
      <div id="info-field">
        <div id="product-name">
          <vs-input
              class="form-control"
              type="text"
              label-placeholder="Product name (required)"
              v-model="productname"/>
        </div>
        <div id="product-id">
          <vs-input
              class="form-control"
              type="text"
              label-placeholder="Product ID (required)"
              v-model="productid"/>
        </div>
        <div id="manufacturer">
          <vs-input
              class="form-control"
              type="text"
              label-placeholder="Manufacturer"
              v-model="manufacturer"/>
        </div>
        <div id="description">
          <vs-textarea
              class="form-control"
              type="text"
              label="Description"
              v-model="description"/>
        </div>
        <div id="rrp">
          <p id="rrp-title">Recommended Retail Price:</p>
          <currency-input
              class="form-control"
              id="currency-box"
              label="Price"
              v-model="rrp"
              :options="{
                locale: 'en-NZ',
                currency: 'NZD',
                valueRange: { min: 0 },
                precision: 2,
                distractionFree: {
                  hideCurrencySymbol: true,
                  hideGroupingSymbol: true
                },
                autoSign: true,
                useGrouping: true
              }"
          />
        </div>
      </div>
      <button
          type="button"
          class="add-button"
          @click="checkForm()">Add Item to Catalogue</button>
    </form>
  </div>
</template>

<script>
import CurrencyInput from "@/components/CurrencyInput";
import api from "@/Api";
import {mutations} from "@/store";

const AddToCatalogue = {
  name: "AddToCatalogue",
  data: function () {
    return {
      errors: [],
      productid: "",
      productname: "",
      description: "",
      manufacturer: null,
      rrp: null,
    };
  },
  methods: {
    /**
     * The function checks the inputs of the registration form to ensure they are in the right format.
     * The function also updates the errors list that will be displayed on the page if at least one of the input boxes
     * is in the wrong format.
     */
    checkForm: function() {
      this.errors = [];
      if (this.productname.length === 0) {
        this.errors.push(this.productname);
      }

      if (this.productid.length === 0) {
        this.errors.push(this.productid);
      }

      if (this.errors.length >= 1) {
        this.$vs.notify({title:'Failed to create catalogue item', text:'Required fields are missing.', color:'danger'});
      }
    },
    /**
     * Creates a POST request when user submits form, using the createUser function from Api.js
     */
    createItem: function() {

      //Use createUser function of API to POST user data to backend
      //AT THE MOMENT BACKEND IS JUST A JSON-SERVER, THE SERVER IS RUN USING testUser.json AS A JSON-SERVER ON PORT 9499
      //https://www.npmjs.com/package/json-server
      if(this.errors.length == 0){

        //const homeAddress = {



        api.createProduct()
            .then((response) => {
              this.$log.debug("New item created:", response.data);
              // window.location.replace("http://localhost:9500/Users?id=" + response.data.id);
              mutations.setUserLoggedIn(response.data.userId, response.data.role); //Store user info into program state, used for later calls
              //LOAD USER PAGE, USING ROUTER
              this.$router.push({name: 'UserPage', params: {id: response.data.userId}})
              //this.$router.push({path: `/users/${response.data.id}`});
            }).catch((error) => {
          if(error.response){
            console.log(error.response.status);
            console.log(error.response.message);
            this.errors.push("Email already in use");
          }
          this.$log.debug("Error Status:", error)
        });
      }},
    components: {CurrencyInput}

  }
}
export default AddToCatalogue;

</script>

<style scoped>

</style>