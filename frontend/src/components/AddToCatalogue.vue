<template>
  <div class="card">
    <h3 class="card-header">Add a Product to your Catalogue</h3>
    <form>
      <div id="info-field">
        <div id="fullname">
          <vs-input
              type="text"
              label-placeholder="Product name (required)"
              v-model="fullname"/>
        </div>
        <div id="description">
          <vs-textarea
            type="text"
            label="Description"
            name="description"
            v-model="description"/>
        </div>
        <div id="manufacturer">
          <vs-input
            type="text"
            label-placeholder="Manufacturer"
            v-model="manufacturer"/>
        </div>
        <p>Recommended Retail Price:</p>
        <div id="rrp">
          <currency-input
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
        @click="checkForm">Add Item to Catalogue</button>
    </form>
  </div>
</template>

<script>
import CurrencyInput from "@/components/CurrencyInput";

export default {
  name: "AddToCatalogue",
  components: {CurrencyInput}
}
</script>

<style scoped>

</style>