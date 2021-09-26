<template>
    <div>
        <vs-button icon="add_box" id="add-button"></vs-button>
        <!-- Image Card -->
        <div id="images-list">
        <div v-for="image in images" :key="image.id">
            <ReImage :imagePath="image.filename"></ReImage>
        </div>
        </div>
    </div>
</template>
<script>

import ReImage from "./ReImage";

const BusinessImages = {
    name: "BusinessImages",
    components: {ReImage},
    props: {
        images: Array,
        primaryImagePath: String,
    },

    data: function () {
        return {
            primaryImagePath: this.primaryImagePath,
            images: this.images,
        }
    },

    methods: {}
}

export default BusinessImages;
</script>
<style scoped>
#images-list {
    padding: 1em;
    display: flex;
    flex-wrap: wrap;
}
#add-button {
    float: right;
    margin-right: 10%;
    margin-top: 15px;
}
</style>


