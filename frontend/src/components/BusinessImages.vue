<template>
    <div>
        <vs-row style="display: flex; justify-content: flex-end">
            <vs-button icon="add_box" id="add-button" @click="openImageUpload" label="Upload Images">Upload Images</vs-button>
        </vs-row>
        <!-- Image Card -->
        <vs-card v-for="image in images" :key="image.id" id="images-list" style="padding: 0px">
            <ReImage :imagePath="'business_'+business.id+'/'+/[^/]*$/.exec(image.fileName)[0]" :isBusiness="true" class="title-image"></ReImage>
        </vs-card>
        <input type="file" id="fileUpload" ref="fileUpload" style="display: none;" multiple @change="uploadImage($event)"/>
    </div>
</template>
<script>

import ReImage from "./ReImage";
import api from "../Api";

const BusinessImages = {
    name: "BusinessImages",
    components: {ReImage},
    props: {
        images: {
            type: Array,
            default: null,
        },
        primaryImagePath: {
            type: String,
            default: null,
        },
        business: {
            type: Object,
            default: null
        },
    },

    data: function () {
        return {
        }
    },

    mounted() {
        console.log(this.images);
    },

    methods: {
        /**
         * Trigger the file upload box to appear.
         * Used for when the actions dropdown add image action or add image button is clicked.
         */
        openImageUpload: function() {
            this.$refs.fileUpload.click();
        },

        /**
         * Upload business image when image is uploaded on web page
         * @param e Event object which contains file uploaded
         */
        uploadImage: async function(e) {
            //Setup FormData object to send in request
            this.$vs.loading(); //Loading spinning circle while image is uploading (can remove if not wanted)
            for (let image of e.target.files) {
                const fd = new FormData();
                fd.append('filename', image, image.name);
                await api.postBusinessImage(this.business.id, fd)
                    .then(() => { //On success
                        this.$vs.notify({title:`Image for ${this.business.name} was uploaded`, color:'success'});
                        location.reload();
                    })
                    .catch((error) => { //On fail
                        if (error.response.status === 400) {
                            this.$vs.notify({title:`Failed To Upload Image`, text: "The supplied file is not a valid image.", color:'danger'});
                        } else if (error.response.status === 500) {
                            this.$vs.notify({title:`Failed To Upload Image`, text: 'There was a problem with the server.', color:'danger'});
                        }
                    })
                    .finally(() => {
                        this.$vs.loading.close();
                    });
            }
        }

    },
}

export default BusinessImages;
</script>
<style scoped>
#images-list {
    margin-left: auto;
    margin-right: auto;
    display: flex;
    width: 430px;
    height: 320px;
    justify-content: center;
    flex-wrap: wrap;
    margin-top: 15px;
}
#add-button {
    margin-right: 10%;
    margin-top: 15px;
}
.image-card {
    margin: 100px;
    min-width: 200px;
    padding: 4px;
}
.title-image {
    height: 320px;
    width: 430px;
    object-fit: cover;
    display: flex;
    grid-column: 1;
    grid-row: 1 / 3;
    margin-left: auto;
    justify-content: center;
    margin-bottom: 10px;
}
</style>


