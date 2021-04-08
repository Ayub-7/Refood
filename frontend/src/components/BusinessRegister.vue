<template>
  <div class="card" id="body">
    <h3 class="card-header text-center">Create a ReFood Business Account</h3>
    <div class="card-body">
        <div class="form-row">
          <div class="form-group col-md-6">
            <input type="text" class="form-control" placeholder="Enter Name of Business" name="businessName"  required>
          </div>
          <div class="form-group col-md-6">
            <input type="text" class="form-control" placeholder="Enter Business Address" name="Address">
          </div>

          <div class="form-group col-md-6">
            <textarea type="text" class="form-control" placeholder="Enter Business Description" name="Description" ></textarea>
          </div>

          <div class="form-group col-md-6">
            <input type="tel" class="form-control" placeholder="Enter Business Phone number" name="phonenumber" >
          </div>

          <form class="dropdown">
            <label> Select Business Type :   </label>
            <select class="select" name="type">
              <option value = "Accommodation and Food Services" > Accommodation and Food Services
              </option>
              <option value = "Retail Trade"> Retail Trade
              </option>
              <option value = " Charitable organisation">  Charitable organisation
              </option>
              <option value = "Non-profit organisation"> Non-profit organisation
              </option>
            </select>
          </form>


          <div class="form-group col-md-6">
            <button type="button" class="register-button" @click="checkForm(); createUserInfo()">Register</button>
          </div>

        </div>
      </div>
  </div>
</template>

<script>
import Vue from 'vue';
import Dropdown from 'bp-vuejs-dropdown';

// global
Vue.use(Dropdown);

export default {
name: "BusinessRegister"
}
</script>

<style scoped>
/* Dropdown Button */


.dropdown {
  cursor: pointer;
  border-radius: 5em;
  color: #fff;
  background: linear-gradient(to right, #9C27B0, #E040FB);
  border: 0;
  padding-left: 40px;
  padding-right: 40px;
  padding-bottom: 10px;
  padding-top: 10px;
  font-family: 'Ubuntu', sans-serif;
  margin-left: 50%;
  font-size: 13px;
  box-shadow: 0 0 20px 1px rgba(0, 0, 0, 0.04);
}

.register-button {
  cursor: pointer;
  border-radius: 5em;
  color: #fff;
  background: linear-gradient(to right, #9C27B0, #E040FB);
  border: 0;
  padding-left: 40px;
  padding-right: 40px;
  padding-bottom: 10px;
  padding-top: 10px;
  font-family: 'Ubuntu', sans-serif;
  margin-left: 35%;
  margin-bottom: 100%;
  font-size: 13px;
  box-shadow: 0 0 20px 1px rgba(0, 0, 0, 0.04);
}
/* The container <div> - needed to position the dropdown content */


/* Dropdown Content (Hidden by Default) */
.dropdown-content {
  display: none;
  position: absolute;
  background-color: #f1f1f1;
  margin-left: 80px;
  margin-right: 80px;
  min-width: 160px;
  box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
  z-index: 1;
}

/* Links inside the dropdown */
.dropdown-content a {
  color: black;
  padding: 3px 6px;
  text-decoration: none;
  display: block;
}

/* Change color of dropdown links on hover */
.dropdown-content a:hover {background-color: #ddd;}

/* Show the dropdown menu on hover */
.dropdown:hover .dropdown-content {display: block;}

/* Change the background color of the dropdown button when the dropdown content is shown */
.dropdown:hover .dropbtn {background-color: #3e8e41;}

.form-group col-md-6{
  color: rgb(38, 50, 56);
  font-weight: 700;
  font-size: 14px;
  letter-spacing: 1px;
  background: rgba(136, 126, 126, 0.04);
  padding: 10px 20px;
  border: none;
  border-radius: 20px;
  outline: none;
  box-sizing: border-box;
  border: 2px solid rgba(0, 0, 0, 0.02);

  text-align: center;
  margin-bottom: 27px;
  font-family: 'Ubuntu', sans-serif;
}

.card {
  background-color: #FFFFFF;
  width: 700px;
  height: 400px;
  margin: 1em auto;
  border-radius: 2.5em;
  outline: none;
  border: none;
}

.card-header {
  font-family: 'Ubuntu', sans-serif;
  font-weight: bold;
  font-size: 23px;
  color: #8C55AA;
}

.form-control {
  width: 76%;
  font-weight: 700;
  font-size: 14px;
  letter-spacing: 1px;
  background: rgba(136, 126, 126, 0.04);
  padding: 10px 5px;
  border: none;
  border-radius: 20px;
  outline: none;
  box-sizing: border-box;
  border: 2px solid rgba(0, 0, 0, 0.02);
  margin-bottom: 50px;
  margin-left: 46px;
  text-align: center;
  margin-bottom: 27px;
  font-family: 'Ubuntu', sans-serif;
}

</style>