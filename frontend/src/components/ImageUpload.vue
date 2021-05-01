<template>
    <div>
        <!-- Using label for button since can't rename button text -->
        <label ref="loadableButton" id='imageSelectBtn' for="fileUpload" class="btn">Upload Image</label>
        <input id="fileUpload" type="file" @change="uploadImage($event)">

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
    uploadImage: function(e) {
        
        //Get image file from event
        const image = e.target.files[0];
        //Setup FormData object to send in request
        const fd = new FormData();
        fd.append('filename', image, image.name);

        this.$vs.loading(); //Loading spinning circle while image is uploading (can remove if not wanted)
        api.postProductImage(this.businessId, this.productId, fd)
            .then(() => { //On success
                this.$vs.notify({title:`Image for ${this.productId} was uploaded`, color:'success'});
            }).catch((error) => { //On fail
                if (error.response.status === 400) {
                    this.$vs.notify({title:`Image failed to upload`, color:'danger'});
                } else if (error.repsonse.status === 500) {
                    this.$vs.notify({title:`Image cannot be uploaded, there is problem with the server`, color:'danger'});
                }
            }).finally(() => {
                this.$vs.loading.close();
                document.getElementById("fileUpload").value = null; //reset file path value since images would stay in input (meaning you couldn't upload same image twice)
            })   
        

    },
  },

}

export default ImageUpload
</script>

<style scoped>

    #fileUpload {
        visibility: hidden;
    }

    #imageSelectBtn {
        background: #3B5998;
        color: white;
        padding: 0.8em;
        border-radius: 20px;
        cursor: pointer; 
    }

    #imageSelectBtn:hover {
        background: #30487c
    }


</style>