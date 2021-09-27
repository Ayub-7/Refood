<template>
  <div class="card">
        <form autocomplete="off">
          <vs-input id="business-name"
                    type="text"
                    class="form-control"
                    label="Business Name *"
                    v-model="businessName"/>
          <vs-select
              width="90%"
              id="business-type"
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
                <li v-for="suggested in this.suggestedCities" :key="suggested" :value="suggested" class="suggested-item">{{suggested}}</li>
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

          <vs-button class="register-button">Register</vs-button>
        </form>
  </div>
</template>

<script>
export default {
  name: "ModifyBusiness",
  data: function () {
    return {
      availableBusinessTypes: [],
      errors: [],
      businessName: "",

      streetNumber: "",
      streetAddress: "",
      suburb: "",
      postcode: "",
      city: "",
      region: "",
      country: "",

      description: "",
      businessType: null,

      suggestCities: false,
      suggestedCities: [],

      suggestCountries: false,
      suggestedCountries: [],
      minNumberOfCharacters: 3,
      user: null
    };
  },
  methods: {

  }

}
</script>

<style scoped>

.suggested-box {
  position: absolute;
  display: inline-block;
  list-style: none;
  width: 300px;
}

.suggested-item {
  cursor: pointer;
  position: relative;
  margin: 0 0 0 1em;

  border: 2px solid rgba(0, 0, 0, 0.02);
  padding: 0.5em 1em;
  background: white;
  z-index: 99;
}

.suggested-item:hover {
  background: lightgray;
}

.label-control {
  font-weight: 700;
  font-size: 16px;
  margin: auto auto 0.5em auto;
}

.register-button {
  margin: 1em auto;
  width: 150px;
}

.card {
  max-width: 800px;
  background-color: white;
  margin: 1em auto;
  padding: 0.5em 0 0.5em 0;
  border-radius: 4px;
  border: 2px solid rgba(0, 0, 0, 0.02);
  box-shadow: 0 .5rem 1rem rgba(0, 0, 0, .15);

  display: grid;
  grid-template-columns: 1fr;
  grid-template-rows: auto auto;
  grid-row-gap: 1em;

}

.card-header {
  grid-row: 1;

  text-align: center;
  font-weight: bold;
  font-size: 24px;
  color: #1F74FF;

  margin: 0;
  padding: 1em 0;

}

form {
  grid-row: 2;

  margin: 0 4em;

  display: grid;
  grid-template-columns: 1fr;
  grid-template-rows: repeat(5, auto);
  grid-row-gap: 4px;

}

.form-control {
  margin: 0.25em auto;
  width: 90%;
}

#business-name {
  padding: 0;
}

/* ===== ADDRESS CONTAINER ===== */
#address-container {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  grid-template-rows: repeat(auto-fit, auto);
  margin: 0 1.5em;
}

input {
  margin: 0;
}

#street-number {
  grid-row: 1;
  grid-column: 1;
}

#street-address {
  grid-row: 1;
  grid-column: 2;
}

#suburb {
  grid-row: 1;
  grid-column: 3;
}

#city {
  grid-row: 2;
  grid-column: 2 / 4;
  margin: auto 0;
}

#city >>> .vs-input {
  width: 95%;
}

#region {
  grid-row: 3;
  grid-column: 1;
}

#country {
  grid-row: 3;
  grid-column: 2 / 4;
}

#country >>> .vs-input {
  width: 95%;
}

#postcode {
  grid-row: 2;
  grid-column: 1;
}

.text-areas {
  margin-top: 1em;
}

.text-areas >>> h4 {
  font-size: 14px;
  font-weight: 400;
}

.text-areas >>> textarea {
  max-width: 500px;
  min-height: 70px;
  max-height: 70px;
}

@media screen and (max-width: 825px) {
  .card {
    width: 90%;
  }
}

@media screen and (max-width: 600px) {
  #address-container {
    display: grid;
    grid-template-columns: 1fr;
    grid-template-rows: repeat(auto-fit, auto);
    margin: 0 1.5em;
  }

  #street-number {
    grid-column: 1;
    grid-row: 1;
  }

  #street-address {
    grid-column: 1;
    grid-row: 2;
  }

  #suburb {
    grid-column: 1;
    grid-row: 3;
  }

  #postcode {
    grid-column: 1;
    grid-row: 4;
  }

  #city {
    grid-column: 1;
    grid-row: 5;
  }

  #city >>> .vs-input {
    margin: 0 1em;
    width: auto;
  }

  #region {
    grid-column: 1;
    grid-row: 6;
  }

  #country {
    grid-column: 1;
    grid-row: 7;
  }

  #country >>> .vs-input {
    margin: 0 1em;
    width: auto;
  }

}

</style>
