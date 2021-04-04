<template>

  <div id="container" v-if="this.business != null">
    <!-- Left Side Business Information Panel -->
    <div id="business-container">
      <div id="business-name"  >{{ business.name }}</div>

      <div id="info-container">
        <div id="business-type">{{ business.businessType }}</div>
        <div id="created-date">Created {{ business.created.split(' ')[0] }}</div>
        <div id="address">
          <div id="street-address">{{ business.address }}</div> <!-- Change this soon when address is changed. -->
          <div id="city">Placeholder City</div>
          <div id="region">Placeholder Region</div>
          <div id="country">Placeholder Country</div>
          <div id="postcode">8888</div>
        </div>
        <div id="description">{{ business.description }}</div>
      </div>
    </div>

    <main>
      <!-- Sub Navigation Bar -->
      <nav id="business-navbar">
        <router-link class="business-nav-item" :to="{name: `Business`, params:{id: business.id}}">Products</router-link>
        <router-link class="business-nav-item" :to="{name: `BusinessAdministrators`}">Administrators</router-link>
      </nav>

      <div id="content">
        <router-view></router-view>
      </div>

    </main>
  </div>
  <!-- 406 Error: Business with given Id does not exist. -->
  <div id="error" v-else>
    <div id="error-header"> Error 406 </div>
    <div id="error-description" style="font-size: 16px"> This business could not be found :( </div>
  </div>

</template>


<script>
import api from "../Api";

const Business = {
  name: "Business",

  // App's initial state.
  data: function() {
    return {
      business: null,
      adminList: null
    };
  },

  methods: {
    getBusiness: function() {
      api.getBusinessFromId(this.$route.params.id)
        .then((res) => {
          this.business = res.data;
          this.adminList = JSON.parse(JSON.stringify(this.business.administrators)); // It just works?
          console.log(this.business);
        })
        .catch((error) => {
          throw new Error(`ERROR trying to obtain business info from Id: ${error}`);
        })
    }
  },

  mounted() {
    // Retrieve business info.
    this.getBusiness();
  }

}

export default Business;
</script>


<style scoped>

#container {
  display: grid;
  grid-template-columns: 1fr 3fr;
  grid-template-rows: auto auto;
  grid-column-gap: 1em;
}

/* For when the screen gets too narrow - mainly for mobile view */
@media screen and (max-width: 700px) {
  #container {
    display: grid;
    grid-template-columns: 1fr;
    grid-template-rows: auto auto;
  }

  #business-container {
    grid-row: 1;
  }

  main {
    grid-row: 2;
  }

  #business-navbar {
    align-content: center;
  }

}

/* Business Info Panel on left side */
#business-container {
  margin: 1em 0 1em 0;
  border-radius: 1.5em;
  box-shadow: 0 11px 35px 2px rgba(0, 0, 0, 0.14);
  background-color: #F5F5F5;
}

#business-name {
  grid-row: 1;
  grid-column: 1;

  color: black;
  font-size: 26px;
  padding: 1em;
}

#info-container {
  grid-column: 1;
  grid-row: 2;

  display: grid;
  grid-template-columns: 1fr;
  grid-template-rows: repeat(3, auto) repeat(1, 1fr);
  grid-row-gap: 2em;

  padding-left: 2em;
  padding-bottom: 2em;
  padding-right: 2em;

}

#business-type {
  grid-column: 1;
  grid-row: 1;
  font-size: 18px;
}

#created-date {
  grid-column: 1;
  grid-row: 2;
}

#address {
  grid-row: 3;
  height: fit-content;
}

#description {
  grid-row: 4;

}

/* Right Hand Content Side. */
main {
  margin: 1em 0 1em 0;
  border-radius: 1.5em;
  box-shadow: 0 11px 35px 2px rgba(0, 0, 0, 0.14);
  background-color: #F5F5F5;
}

#business-navbar {
  grid-column: 2;
  grid-row: 1;

  font-size: 18px;

  padding-top: 1em;
  padding-bottom: 1em;

  box-shadow: 0 0 35px 0 rgba(0, 0, 0, 0.14);
  border-radius: 1em;
  border: 2px solid rgba(0, 0, 0, 0.02);

}

.business-nav-item {
  text-align: center;
  color: black;
  font-weight: 700;
  font-size: 14px;
  letter-spacing: 1px;
  text-decoration: none;

  padding: 10px 20px;
  margin: 10px;

  background: #dbe0dd linear-gradient(to right, #abd9c1 10%, #fceeb5 50%, #ee786e 100%);
  background-size: 500%;
  border: none;
  border-radius: 5rem;
  box-shadow: 0 .5rem 1rem rgba(0,0,0,.15);
}

.business-nav-item:hover {
  box-shadow: 0 0.25em 1em rgba(0,1,1,.25);
}


/* Error 406 Styling */
#error {
  text-align: center;
  padding: 0 1em 1em 1em;

  margin: 1em 0 1em 0;
  border-radius: 1.5em;
  box-shadow: 0 11px 35px 2px rgba(0, 0, 0, 0.14);
  background-color: #F3EBF6;
}
#error-header {
  font-size: 48px;
  padding: 0.75em 1em 1em 1em;
}


</style>