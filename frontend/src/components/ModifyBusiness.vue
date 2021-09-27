<template>
  <div class="card">
    <h3 class="card-header">Modify business</h3>
    <form autocomplete="off">
      <vs-input id="business-name"
                :danger="this.errors.includes('businessName')"
                type="text"
                class="form-control"
                label="Business Name *"
                v-model="businessName"/>
      <vs-select
          width="90%"
          id="business-type"
          :danger="this.errors.includes('businessType')"
          class="form-control"
          label="Business Type *"
          v-model="businessType"
          autocomplete >
        <vs-select-item v-for="type in availableBusinessTypes" :key="type" :text="type" :value="type"/>
      </vs-select>

      <vs-textarea type="text" class="form-control text-areas" label="Business Description" v-model="description" :counter="140"/>

      <vs-divider style="grid-row: 4;"></vs-divider>
      <div class="label-control">Address</div>

      <div id="address-container">
        <div id="street-number">
          <vs-input v-model="streetNumber" class="form-control" label="Street Number"></vs-input>
        </div>
        <div id="street-address">
          <vs-input v-model="streetAddress" class="form-control" label="Street Address"></vs-input>
        </div>
        <div id="suburb">
          <vs-input v-model="suburb" class="form-control" label="Suburb"></vs-input>
        </div>
        <div id="postcode">
          <vs-input v-model="postcode" class="form-control" label="Postcode"></vs-input>
        </div>
        <div id="city">
          <!-- If wanting to test/check suggested item tiles, remove blur. -->
          <vs-input autocomplete="none" @blur="suggestCities = false;" v-model="city" @input="getCitiesFromPhoton()" class="form-control" label="City"></vs-input>
          <ul v-if="this.suggestCities" class="suggested-box">
            <li v-for="suggested in this.suggestedCities" @mousedown="setCity(suggested)" :key="suggested" :value="suggested" class="suggested-item">{{suggested}}</li>
          </ul>
        </div>
        <div id="region">
          <vs-input v-model="region" class="form-control" label="Region"></vs-input>
        </div>
        <div id="country">
          <vs-input @blur="suggestCountries = false;" :danger="this.errors.includes('country')" @input="getCountriesFromPhoton()" v-model="country" class="form-control" label="Country *"></vs-input>
          <ul v-if="this.suggestCountries" class="suggested-box">
            <li v-for="suggested in this.suggestedCountries" @mousedown="setCountry(suggested)" :key="suggested" :value="suggested" class="suggested-item">{{suggested}}</li>
          </ul>
        </div>
      </div>

      <vs-button class="register-button" @click="checkForm(); createBusinessInfo()">Register</vs-button>
    </form>
  </div>
</template>
