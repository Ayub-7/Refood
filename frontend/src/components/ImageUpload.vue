<template>
    <div>
        <!-- Using label for button since can't rename button text -->
        <label ref="loadableButton" @click="showModal=true" id='imageSelectBtn' class='button'>Upload image</label>
            <!-- Add user to business as admin modal -->
        <Modal v-if="showModal">
            <div slot="header">
                Upload images for product:
            </div>

            <div slot="body">
                <vs-select class="business-dropdown" v-model="selectedProduct">
                    <vs-select-item v-for="product in products" :key="product.id" :text="`${product.id}: ${product.name}`" :value="product.id"/>
                </vs-select>
            </div>

            <div id="modal-footer" slot="footer">
                <button class="modal-button modal-cancel-button" @click="showModal=false">
                Cancel
                </button>
                <label ref="loadableButton" id='imageUploadBtn' for="fileUpload" class="button">Upload image</label>
                <input ref="fileUpload" id="fileUpload" type="file" multiple @change="uploadImage($event)">
                <!-- <div v-for> -->
                <!-- @change='uploadImage($event)' -->
            </div>
        </Modal>


    </div>
</template>

<script>
import api from "../Api";
import Modal from "./Modal";


const ImageUpload = {
  name: "ImageUpload",
  props: ['businessId', 'products'],
  components: {
      Modal
    },
  data: function() {
    return {
        selectedProduct: null,
        showModal: false,
        selectedImages: [],
    }
  },

  methods: {

    /**
     * Upload product image when image is uploaded on web page
     * @param e Event object which contains file uploaded
     */

    uploadImage: function(e) {
            console.log('yo')
            //Get image file from event
            //const image = e.target.files[0];
            
            //reset file path value since images would stay in input (meaning you couldn't upload same image twice)
            // document.getElementById("fileUpload").value = null;
            this.showModal = false;
            //Setup FormData object to send in request
            this.$vs.loading(); //Loading spinning circle while image is uploading (can remove if not wanted)
            for (let image of e.target.files) {
                const fd = new FormData();
                fd.append('filename', image, image.name);


                api.postProductImage(this.businessId, this.selectedProduct, fd)
                    .then(() => { //On success
                        this.$vs.notify({title:`Image for ${this.selectedProduct} was uploaded`, color:'success'});
                    }).catch((error) => { //On fail
                        if (error.response.status === 400) {
                            this.$vs.notify({title:`Image failed to upload`, color:'danger'});
                        } else if (error.repsonse.status === 500) {
                            this.$vs.notify({title:`Image cannot be uploaded, there is problem with the server`, color:'danger'});
                        }
                    }).finally(() => {
                        this.$vs.loading.close();
                        location.reload();
                    })
            }
    }
  },

}

export default ImageUpload
</script>

<style scoped>

    #fileUpload {
        visibility: hidden;
        width: 0px;
    }

    .button{
        background: #1F74FF;
        color: white;
        padding: 0.8em;
        border-radius: 20px;
        cursor: pointer; 
    }

    

    #imageSelectBtn:hover {
        background: #30487c
    }


    .business-dropdown {
  text-align: center;
  font-size: 14px;
  text-decoration: none;

  width: 100%;
  padding: 12px 16px;
}

#modal-footer {
  margin: auto;
}

.modal-button:hover {
  box-shadow: 0 0.25em 1em rgba(0,1,1,.25);
}

.modal-ok-button {
  text-align: center;
  color: black;

  width: 100px;
  margin: 0 1em;
  padding: 10px 20px;
  background: #dbe0dd linear-gradient(to right, #abd9c1 10%, #fceeb5 50%, #ee786e 100%);
  background-size: 500%;
  border: none;
  border-radius: 5rem;
  box-shadow: 0 .5rem 1rem rgba(0,0,0,.15);

}

.modal-cancel-button {
  text-align: center;
  color: black;

  width: 100px;
  margin: 0 1em;
  padding: 10px 20px;
  background: #dbe0dd linear-gradient(to left, #abd9c1 10%, #fceeb5 50%, #ee786e 100%);
  background-size: 500%;
  border: none;
  border-radius: 5rem;
  box-shadow: 0 .5rem 1rem rgba(0,0,0,.15);
}


</style>