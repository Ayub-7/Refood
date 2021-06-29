<template>
  <vs-popup id="addCardModal" title="Add Card To Marketplace" :active.sync="showingAddCardModal">

    <!-- Section selection -->
    <vs-row class="addCardField">
    <vs-col vs-w="2" class="addCardHeader">Section</vs-col>
    <vs-select vs-w="10" v-model="section">
        <vs-select-item v-for="section in avaliableSections" :key="section" :text="section" :value="section"/>
    </vs-select>
    </vs-row>

    <!-- Card name -->
    <vs-row class="addCardField">
        <vs-col id="title" vs-w="2">
            <div class="addCardHeader" >Title</div>
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
        <vs-col vs-w="2">
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
export default {
    props: ['showingAddCardModal'],


    data: function() {
        return {
            avaliableSections: ['For Sale', 'Wanted', 'Exchange'],

            section: 'For Sale',
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
        * Method to close modal, emits 'close' event to parent component which then sets 'showingAddCardModal' to false
        * Also clears form
        */
        closeModal() {
            this.$emit('closeModal');
            setTimeout(this.resetData, 500);
        },

        /**
        * Method for reseting form data, gets called when modal closes 
        */
        resetData() {

            this.section = 'For Sale';
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

#buttons {
    text-align: center;
}


#description {
    height: 150px;
}

</style>