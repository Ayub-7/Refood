import Vue from 'vue';


export const store = Vue.observable({
    loggedInUserId: null,
    role: null,
    userDateOfBirth: null,
    userName: null,
    userPrimaryBusinesses: [],
    actingAsBusinessId: null,
    actingAsBusinessName: null,
    productToAlterId: null
});


//UNCOMMENT TO USE APP WITHOUT LOGGING IN 
// export const store = Vue.observable({
//     loggedInUserId: 26,
//     role: null,
//     userPrimaryBusinesses: [
//         {
//             "id": 7,
//             "administrators": [
//                 26
//             ],
//             "name": "Business2",
//             "primaryAdministratorId": 26,
//             "description": "Test Business 2",
//             "address": {
//                 "streetNumber": "620",
//                 "streetName": "Sutherland Lane",
//                 "city": "Dalai",
//                 "region": null,
//                 "country": "China",
//                 "postcode": null
//             },
//             "businessType": "Retail Trade",
//             "created": "2021-04-11 04:06:14"
//         }
//     ],

// });


export const mutations = {
    setUserLoggedIn(userId, role) {
        store.loggedInUserId = userId;
        store.role = role;
    },

    userLogout() {
        store.loggedInUserId = null;
        store.userName = null;
        store.userAge = null;
        store.userPrimaryBusinesses = null;
        store.actingAsBusinessId = null;
        store.actingAsBusinessName = null;
    },



    getIdByName(name) {
        for (const names of store.userPrimaryBusinesses){
            if (names.name === name){
                return names.id;
            }
        }
    },
    setUserRole(role) {
        store.role = role;
    },

    setUserName(userName) {
        store.userName = userName;
    },

    setUserDateOfBirth(userDateOfBirth) {
        store.userDateOfBirth = userDateOfBirth;
    },

    setUserPrimaryBusinesses(newBusinesses) {
        store.userPrimaryBusinesses = newBusinesses;
    },

    setActingAsBusiness(businessId, businessName){
        store.actingAsBusinessId = businessId;
        store.actingAsBusinessName = businessName;
    },

    setActingAsUser(){
        store.actingAsBusinessId = null;
        store.actingAsBusinessName = null;
    },

    setProductToAlter(productId) {
        store.productToAlterId = productId;
    }

}