<template>
    <div>
        <label id='imageSelectBtn' for="fileUpload" class="btn">Upload Image</label>
        <input id="fileUpload" type="file" @change="uploadImage">
    </div>
</template>

<script>
import api from "../Api";


const ImageUpload = {
  name: "ImageUpload",
  props: ['businessId', 'productId'],

  data: function() {
    return {
    }
  },

  methods: {
    /**
     * Upload product image when image is uploaded on web page
     * @param e Event object which contains file uploaded
     */
    uploadImage(e) {
        //Get image file from event
        const image = e.target.files[0];

        //Setup FormData object to send in request
        const fd = new FormData();
        fd.append('filename', image, image.name);

        api.postProductImage(this.businessId, this.productId, fd)
            .then(() => { //On success
                this.$vs.notify({title:`Image was successfully uploaded`, color:'success'});
            }).catch((error) => { //On fail
                if (error.response.status === 400) {
                    this.$vs.notify({title:`Image failed to upload`, color:'danger'});
                }
            })     
    },
  },

}

export default ImageUpload
</script>

<style scoped>
    /* #imageUpload {
        color: white;
        width: 400px;
        padding: 0.5em;
    } */

    #fileUpload {
        visibility: hidden;
    }

    #imageSelectBtn {
        background: #3B5998;
        color: white;
        padding: 0.8em;
        border-radius: 20px;
    }


</style>