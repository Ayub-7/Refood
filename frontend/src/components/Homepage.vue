<template>
  <!-- -->
  <div id="container" v-if="getLoggedInUserId() != null">
      <!-- Welcome message that greets user with their name, or a business administrator with the business name -->
      <div id="name-container">
        <!-- Shows a different greeting message depending on who the user is acting as -->
        <div v-if="getBusinessId() != null" class="name" id="business-name">
          Welcome to your home page, {{getBusinessName()}}!
        </div>
        <div v-else class="name" id="name">
          Welcome to your home page, {{getUserName()}}!
        </div>
      </div>

      <div id="sidebar-container">
        <!-- Left navigation with links to profile pages, or the product catalogue -->
        <div id="left-nav" class="sub-container">
          <!-- Shows a different nav depending on who the user is acting as -->
          <div class="userinfo-container" v-if="getBusinessId()">
          <div id="businfo-content">
            <!-- When each list item is clicked, redirects to the relevant page in the application -->
            <vs-button class="left-nav-item" id="bus-profile-btn" @click.native='goToProfile()'>Business Profile</vs-button>
            <vs-button class="left-nav-item" id="bus-catalogue-btn" @click.native='goToProductCatalogue()'>Product Catalogue</vs-button>
            <vs-button class="left-nav-item" @click.native='goToSalesHistory()'>Sales History</vs-button>
          </div>
          </div>
          <div class="userinfo-container" v-else>
            <div id="userinfo-content">
              <vs-button class="left-nav-item" id="user-profile-btn" @click.native='goToProfile()'>Profile</vs-button>
              <vs-button class="left-nav-item" id="marketplace-btn" :to="'/marketplace'" >Marketplace</vs-button>
              <vs-button class="left-nav-item" id="cards-btn" @click="openMarketModal()">My Cards</vs-button>
            </div>
          </div>
        </div>
        <!-- Watchlist div, will show users 'Favourited' products and businesses when further features have been implemented -->
        <div id="watchlist-container" class="sub-container">
          <div style="margin-top: 1px; margin-left: -20px" class="message-detail-container message">
            <vs-icon icon="favorite_border" class="msg-icon"></vs-icon>
            <div id="message-detail-message">
              Watchlist
            </div>
            {{likes}}
          </div>
          <vs-divider style="margin-top: -30px"></vs-divider>
          <div v-for="(item, index) in likedItem" :key="item.id" >
            <vs-card style="margin-top: -40px" id="message-notification-card" actionable>
              <div id="likes-notification-container">
                <vs-row v-if="item.likes != 0">
                  <vs-col vs-type="flex" vs-align="center" vs-justify="space-between">
                    <vs-tooltip text="View Listing">
                      <vs-icon id="view-icon" icon="visibility" class="msg-icon" @click="viewListing(item.inventoryItem.product.business.id, item.id)"></vs-icon>
                    </vs-tooltip>
                    <vs-tooltip text="Unlike">
                      <vs-icon id="like-icon" icon="favorite" class="msg-icon" color="red" @click="unlike(item.id, index); item.likes = 0"></vs-icon>
                    </vs-tooltip>
                  </vs-col>
                </vs-row>
                <vs-row v-else>
                  <vs-col vs-type="flex" vs-align="center" vs-justify="space-between">
                    <vs-icon id="view-unlike-icon" icon="visibility" class="msg-icon" @click="viewListing(item.inventoryItem.product.business.id, item.id)"></vs-icon>
                    <vs-icon id="unlike-icon" icon="favorite_border" class="msg-icon" color="red" ></vs-icon>
                  </vs-col>
                </vs-row>
                <div id="product-name">{{item.inventoryItem.product.name}}</div>
                <div id="product-seller"><strong>Seller: </strong>{{item.inventoryItem.product.business.name}}</div>
                <div id="product-closes" slot="footer"><strong>Closes: </strong>{{item.closes}}</div>
              </div>
            </vs-card>
          </div>
        </div>
      </div>

        <!-- Main element that will display the user a personalized news feed when further features have been implemented -->
        <main>
          <nav id="newsfeed-navbar">
            <div class="newsfeed-title">
              <span style="display: inline-block; vertical-align: middle;">
                <vs-icon icon="feed" />
              </span>
               News Feed
            </div>
          </nav>
          <vs-divider style="padding: 0 1em;"/>
          <HomePageMessages v-if="getBusinessId() == null" :currency="currencySymbol"></HomePageMessages>
        </main>

    <vs-popup title="Your Cards" :active.sync="showMarketModal" id="market-card-modal">
      <div v-if="cards.length > 0" class="container">
        <MarketplaceGrid @cardRemoved="getUserCards(userId)" :cardData="cards.slice((currentCardPage-1)*4, currentCardPage*4)" showSection></MarketplaceGrid>
        <vs-pagination :max="5" :total="Math.ceil(cards.length/4)" v-model="currentCardPage"></vs-pagination>
      </div>
      <!-- If the user has no active cards -->
      <div v-else class="container">
        This user has no active cards on the marketplace right now.
      </div>
    </vs-popup>
  </div>
</template>

<script>
import api from "../Api";
import {mutations, store} from "../store"
import HomePageMessages from "./HomePageMessages.vue";
import MarketplaceGrid from "./MarketplaceGrid";
import ListingDetail from "./ListingDetail";
import axios from "axios";

const Homepage = {
  name: "Homepage",
  components: {ListingDetail, HomePageMessages, MarketplaceGrid},
  data: function () {
    return {
      unliked: false,
      showListing: false,
      likes: 0,
      likedItem: [],
      userId: null,
      businesses: [],
      actingAsBusinessId: null,
      business: null,
      showMarketModal: false,
      cards: [],
      currentCardPage: 1,
      user: null,
      currencySymbol: "$",
    }
  },
  /**
   * Sets the userId variable equal to the userId from the store when the component
   * is first rendered, then gets the users details from the backend using the API
   */
  mounted() {
    this.checkUserSession();
    this.getLikes(this.userId);
  },

  methods: {
    /**
     * Sets display currency based on the user's home country.
     * @param country country for which currency is going to be retrieved
     */
    setCurrency(country) {
      axios.get(`https://restcountries.eu/rest/v2/name/${country}`)
        .then(response => {
          this.currencySymbol = response.data[0].currencies[0].symbol;
        })
        .catch(err => {
          this.$log.debug(err);
      });
    },

    /**
     * open listing
     */
    viewListing: function (businessId, listingId) {
      this.$router.push({name: "Listing", params: {businessId: businessId, listingId: listingId}})
    },

    /**
     * unlike an item
     */
    unlike: function (id, index) {
      this.likes -= 1;
      this.likedItem.splice(index, 1)
      api.removeLikeFromListing(id)
      .then(() => {
        this.$vs.notify({title: "Successfully unliked listing", color: "success"});
      })
      .catch((error) => {
        if (error.response) {
          this.$vs.notify({title: "Error unliking listing", color: "danger"});
        }
        this.$log.debug(error);
      })

    },
    /**
     * Retrieves all the cards that the user has liked.
     */
    getLikes: function(userId) {
      api.getUserLikedListings(userId)
        .then((res) => {
          this.likedItem = res.data;
          this.likes = res.data.length;
        })
        .catch((error) => {
          if (error.response) {
            this.$vs.notify({title: "Error retrieving likes", color: "danger"});
          }
        })
    },

    /**
     * Retrieves all the cards that the user has created.
     */
    getUserCards: function(id) {
      this.$vs.loading({
        container: ".vs-popup",
      });
      this.cards = [];
      api.getUserCards(id)
        .then((res) => {
          this.cards = res.data;
          for(let i = 0; i < this.cards.length; i++){
            if(!this.cards[i].user.homeAddress){
              this.cards[i].user = this.user;
            }
          }
        })
        .catch((error) => {
          if (error.response) {
            this.$vs.notify({title: "Error retrieving cards", color: "danger"});
          }
          this.$log.debug(error);
        })
        .finally(() => {
          this.$vs.loading.close(`.vs-popup > .con-vs-loading`);
        });
    },

    /**
     * Gets user info from backend and sets userFirstname property (for welcome message)
     * Also sets the users details in store.js, so the users session can be maintained
     * as they navigate throughout the applications and 'act' as a business
     *
     * @param userId ID of user who is currently viewing page.
     */
    getUserDetails: function(userId) {
      api.getUserFromID(userId)
          .then((response) => {
            this.user = response.data;
            this.businesses = JSON.parse(JSON.stringify(this.user.businessesAdministered));
            this.userLoggedIn = true;
            /* Sets user details in store.js */
            mutations.setUserDateOfBirth(response.data.dateOfBirth);
            mutations.setUserName(response.data.firstName + " " + response.data.lastName);
            mutations.setUserCountry(response.data.homeAddress.country);
            mutations.setUserBusinesses(this.businesses);
          }).catch((err) => {
        if (err.response.status === 401) {
          this.$vs.notify({title:'Unauthorized Action', text:'You must login first.', color:'danger'});
          this.$router.push({name: 'LoginPage'});
        }
        this.$log.error(err);
      })
    },

    /**
     * Show the modal box (marketplace activity).
     * Having a separate function to just open the modal is good for testing.
     */
    openMarketModal: function() {
      this.showMarketModal = true;
      api.checkSession()
        .then(() => {
          this.getUserCards(this.user.id);
        })
        .catch((error) => {
          this.$vs.notify({title:'Error getting session info', text:`${error}`, color:'danger'});
        });
    },

    /**
     * Close the pop-up box with no consequences.
     */
    closeModal: function() {
      this.showModal = false;
    },

    /**
     * Sends an api request to get a business object from a business Id
     * Sets this components business variable to this object
     *
     * @param id business id
     */
    getBusiness: function(id) {
      api.getBusinessFromId(id)
          .then((res) => {
            this.business = res.data;
          })
          .catch((error) => {
            this.$log.error(error);
          })
    },

    /**
     * Gets the business id from the store, for the business the user is acting as
     *
     * @return busId the business id
     */
    getBusinessId: function() {
      let busId = store.actingAsBusinessId;
      this.actingAsBusinessId = busId;
      if(busId){
        this.getBusiness(busId);
      }
      return busId;
    },

    /**
     * Gets the name of the business the user is acting as from the store
     */
    getBusinessName: function() {
      return store.actingAsBusinessName;
    },

    /**
     * Gets the username of the logged in user from the store
     */
    getUserName: function() {
      return store.userName;
    },

    /**
     * Gets the logged in users id from the store, and assigns this components
     * userId variable equal to it.
     */
    getLoggedInUserId: function() {
      this.userId = store.loggedInUserId;
      return this.userId;
    },

    /**
     * Redirects the user to either the business profile page, if acting as a business,
     * or the user profile page, if acting as an individual
     */
    goToProfile: function() {
      if(this.getBusinessId() != null){
        this.$router.push({path: `/businesses/${this.getBusinessId() }`});
      } else {
        this.$router.push({path: `/users/${this.getLoggedInUserId()}`});
      }
    },

    /**
     * Redirects the user to the product catalogue page, if acting as a business
     */
    goToProductCatalogue: function() {
      this.$router.push({path: `/businesses/${this.getBusinessId()}/products`});
    },

    goToSalesHistory: function() {
      this.$router.push({path: `/businesses/${this.getBusinessId()}/sales-history`});
    },

    checkUserSession: function() {
      api.checkSession()
        .then((response) => {
          this.getUserDetails(response.data.id);
        })
        .catch((error) => {
          this.$log.error("Error checking sessions: " + error);
          this.$vs.notify({title:'Error', text:'ERROR trying to obtain user info from session:', color:'danger'});
        });
    }
  },
}

export default Homepage;
</script>

<style scoped>
#message-notification-card {
  min-height: 100px;
}

.con-vs-card >>> .vs-card--content {
  margin-bottom: 4px;
}

#product-name {
  text-align: left;
  font-size: 12px;
  font-weight: bold;
}

#product-closes {
  font-size: 12px;
}

#view-icon {
  font-size: 16px;
  margin-left: -1px;
  cursor: pointer;
  transition: font-size 0.3s;
}

#view-unlike-icon {
  font-size: 16px;
  margin-left: -1px;
  cursor: pointer;
  transition: font-size 0.3s;
}

#view-icon:hover {
  transition: font-size 0.3s;
  font-size: 20px!important;
}

#like-icon:hover {
  transition: font-size 0.3s;
  font-size: 20px!important;
}

#like-icon {
  font-size: 16px;
  cursor: pointer;
  transition: font-size 0.3s;
}

#unlike-icon {
  font-size: 13px;
  margin-left: 150px;
  cursor: pointer
}

#market-card-modal >>> .vs-popup {
  width: 1200px;
}

#market-card-modal {
  z-index: 100;
}

#cards-btn {
  padding-left: 0;
  padding-right: 0;
}

#container {
  display: grid;
  grid-template-columns: 0.5fr 1fr 4fr 0.5fr;
  grid-template-rows: 1fr auto;
  grid-column-gap: 1em;
  margin: auto;
  padding: 0 2em;
}

/* Top Name Container */
#name-container {
  grid-column: 2 / 4;
  grid-row: 1;
  text-align: center;
  background-color: white;
  padding: 15px 0 15px 0;
  border-radius: 4px;
  margin: 8px 0 0 0;

  box-shadow: 0px 4px 25px 0px rgba(0,0,0,.1);
}

.name {
  font-size: 32px;
  padding: 0.5em 0;
  line-height: 1.2em;
}

#business-name {
  font-size: 32px;
  margin: auto;
}

.newsfeed-title {
  font-size: 24px;
  padding: 4px 0 0 0.5em;
}

/* Side-bar panel on left side */
#sidebar-container {
  grid-column: 2;
  grid-row: 2;

  display: grid;
  grid-template-columns: 1fr;
  grid-template-rows: repeat(3, auto) repeat(1, 1fr);
  grid-row-gap: 1em;
}

.sub-container {
  padding: 2em;
  border-radius: 4px;
  box-shadow: 0px 4px 25px 0px rgba(0,0,0,.1);
  background-color: #FFFFFF;
}

#watchlist-container {
  grid-column: 1;
  grid-row: 3;
  display: grid;
  grid-template-columns: 1fr;
  grid-template-rows: repeat(1, auto) repeat(1, 1fr);
  grid-row-gap: 2em;
}

#watchlist-header-container {
  display: flex;
  justify-content: space-between;
}

.watchlist-title {
  font-size: 18px;
  margin-left: 4px;
  transition: 0.3s;
}

/* News feed styles. */
main {
  grid-column: 3;
  grid-row: 2;

  margin: 1em 0 1em 0;
  border-radius: 4px;
  box-shadow: 0 11px 35px 2px rgba(0, 0, 0, 0.14);
  background-color: #FFFFFF;
}

#newsfeed-navbar {
  grid-column: 2;
  grid-row: 1;
  font-size: 18px;
  padding-top: 1em;
  padding-bottom: 1em;
  box-shadow: 0 0 35px 0 rgba(0, 0, 0, 0.14);
  border-radius: 4px;
  border: 2px solid rgba(0, 0, 0, 0.02);
}

/* left navigation panel styling */

#left-nav {
  grid-row: 2;
}

.left-nav-item {
  width: 100%;
  margin: 0.5em auto;
  text-align: center;
  letter-spacing: 1px;
}

@media screen and (max-width: 1200px) {
  .watchlist-title {
    transition: 0.3s;
    font-size: 16px;
    margin-left: 0;
  }
}

/* For when the screen gets too narrow - mainly for mobile view */
@media screen and (max-width: 700px) {
  #container {
    display: grid;
    grid-template-columns: 1fr;
    grid-template-rows: auto auto auto;
    margin: auto;
    padding: 0 2em;
  }

  #name-container {
    grid-column: 1;
    grid-row: 1;
  }

  #sidebar-container {
    grid-column: 1;
    grid-row: 2;
  }

  main {
    grid-column: 1;
    grid-row: 3;
  }

  #newsfeed-navbar {
    align-content: center;
  }

  .watchlist-title {
    font-size: 18px;
    margin-left: 4px;
    transition: 0.3s;
  }

}
</style>
