<template>
  <vs-popup id="addCardModal" title="Add Card To Marketplace" :active.sync="showing">

    <!-- Section selection -->
    <vs-row class="addCardField">
    <vs-col vs-w="2" vs-xs="12" class="addCardHeader">Section <span class="required">*</span></vs-col>
    <vs-select vs-w="10" v-model="section">
        <vs-select-item v-for="section in avaliableSections" :key="section.key" :text="section.key" :value="section.value"/>
    </vs-select>
    </vs-row>

    <!-- Card name -->
    <vs-row class="addCardField">
        <vs-col id="title" vs-w="2" vs-xs="12">
            <div class="addCardHeader" >Title <span class="required">*</span> </div> 
        </vs-col> 
        <vs-col vs-w="9">
            <vs-input v-model="title" class="addCardInput"></vs-input>
        </vs-col>
    </vs-row>

    <!-- Card description -->
    <vs-row class="addCardField">
        <div class="addCardHeader">Description</div>
        <vs-textarea v-model="description" id="description"></vs-textarea>
    </vs-row>

    <!-- Card keywords -->
    <vs-row class="addCardField">
        <vs-col vs-w="2" vs-xs="12">
            <div class="addCardHeader">Keywords</div>
        </vs-col> 
        <vs-col vs-w="9">
            <vs-input v-model="keywords" class="addCardInput"></vs-input>
        </vs-col>
    </vs-row>

    <div id="buttons">
        <vs-button class="addCardButton" @click="addToMarketplace()">Add To Marketplace</vs-button>
        <vs-button class="addCardButton" @click="closeModal()">Cancel</vs-button>
    </div>

  </vs-popup>
</template>

<script>
import api from "@/Api";

export default {


    data: function() {
        return {
            showing: false,
            avaliableSections: [
                {key:'For Sale', value:'ForSale'},
                {key:'Wanted', value:'Wanted'},
                {key:'Exchange', value: 'Exchange'}
            ],
            userSession: null,

            section: null,
            title: '',
            description: '',
            keywords: '',
            
        }
    },


    methods: {
        /** 
        * Template for POST request method
        */
        addToMarketplace() {
            console.log(this.section, this.title, this.description, this.keywords);


            this.closeModal();
        },


      /**
       * obtains the user's account details to create a new card.
       */
      getSession: function () {
        api.checkSession()
            .then((response) => {
              this.userSession = response.data;
            })
            .catch((error) => {
              this.$vs.notify({title:'Error', text:'ERROR trying to obtain user info from session:', color:'danger'});
              this.$log.error("Error checking sessions: " + error);
            });
      },

      /**
       * Creates a new card. of type:
       * (long creatorId, String title, String description, String keywords, MarketplaceSection section)
       *
       *
       * @param card
       *
       * 401 if not logged in, 403 if creatorId, session user Id do not match or if not a D/GAA,
       * 400 if there are errors with data, 201 otherwise

       */
      createNewCard(card) {
        //adapt the test data
        card.creatorId = this.userSession.id;

        api.createCard(this.userSession.id, this.title, this.description, this.keywords, this.section)
            .then((res) => {
              this.$vs.notify({title:'Success', text: `created new card: ${res.data.cardId}`, color:'success'});
            })
            .catch((error) => {
              let errormsg = "ERROR creating new card: ";
              if (error) {
                if (error.response) {
                  if (error.response.status === 401 || error.response.status === 403) {
                    this.$vs.notify({title: 'Error', text: errormsg + 'user account error', color: 'danger'});
                  }

                  if (error.response.status === 400) {
                    this.$vs.notify({title: 'Error', text: errormsg + 'invalid data', color: 'danger'});
                  }
                } else {
                  this.$vs.notify({
                    title: 'Error',
                    text: 'ERROR trying to obtain user info from session:',
                    color: 'danger'
                  });
                }
              }
            });
      },

        /** 
        * Closes modal by setting showing to false (which is linked to Card :active-sync)
        */
        closeModal() {
            this.showing = false;
        },

        /** 
        * Opens modal by setting showing to true (linked to :active-sync), also resets form data before opening
        */
        openModal() {
            this.resetData();
          this.getSession();

          this.showing = true;
        },

        /**
        * Method for reseting form data, gets called when modal closes 
        */
        resetData() {

            this.section = null;
            this.title = '';
            this.description = '';
            this.keywords = '';
        }
    }
}
</script>

<style>

.addCardField {
    margin-bottom: 25px;
    margin-top: 5px;
}

.addCardHeader {
    font-size: 17px;
}

.addCardInput {
    width: 80%;
    font-size: 20px;
}


.addCardButton {
    margin: 5px;
    width: 150px;
}

.required {
    color: red;
}

#buttons {
    text-align: center;
}


#description {
    height: 150px;
    max-height: 150px;
    min-height: 150px;
}


</style>