import Vue from 'vue';


export const store = Vue.observable({
    loggedInUserId: null,
    role: null,
    userPrimaryBusinesses: [],

});


export const mutations = {
    setUserLoggedIn(userId, role) {
        store.loggedInUserId = userId;
        store.role = role;
    },

    userLogout() {
        store.loggedInUserId = null;
    },

    setUserRole(role) {
        store.role = role;
    },

    setUserPrimaryBusinesses(newBusinesses) {
        store.userPrimaryBusinesses = newBusinesses;
    }
}