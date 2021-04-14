/*
 * Created on Wed Feb 10 2021
 *
 * The Unlicense
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or distribute
 * this software, either in source code form or as a compiled binary, for any
 * purpose, commercial or non-commercial, and by any means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors of this
 * software dedicate any and all copyright interest in the software to the public
 * domain. We make this dedication for the benefit of the public at large and to
 * the detriment of our heirs and successors. We intend this dedication to be an
 * overt act of relinquishment in perpetuity of all present and future rights to
 * this software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to <https://unlicense.org>
 */

/**
 * Main entry point for your Vue app
 */
import Vue from 'vue';
import VueRouter from 'vue-router';
import Vuex from 'vuex';
import createPersistedState from "vuex-persistedstate";
import App from './App.vue';
import VueLogger from 'vuejs-logger';
import Vuesax from 'vuesax';

import Login from "@/components/Login";
import BusinessRegister from "@/components/BusinessRegister";
import Register from "./components/Register";
import Users from "@/components/Users.vue";
import Search from "@/components/Search.vue";
import Business from "@/components/Business.vue";
import BusinessAdministrators from "@/components/BusinessAdministrators";
import Homepage from "@/components/Homepage"

Vue.config.productionTip = false

import 'vuesax/dist/vuesax.css'
import 'material-icons/iconfont/material-icons.css'; // used with vuesax.

const options = {
  isEnabled: true,
  logLevel : 'debug',
  stringifyArguments : false,
  showLogLevel : true,
  showMethodName : false,
  separator: '|',
  showConsoleColors: true
};

Vue.use(VueLogger, options);
Vue.use(VueRouter);
Vue.use(Vuex);
Vue.use(Vuesax);

//Store data used to maintain state in program
const store = new Vuex.Store({

  plugins: [createPersistedState({
    storage: window.sessionStorage,
  })],

  state: {
    userId: null,
    viewingUserId: null,
    userRole: null,
    businessId: null,
    businessName: null,
    userPrimaryBusinesses: []
  },

  mutations: {
    resetState (state) {
      state.userId = null;
      state.viewingUserId = null;
      state.userRole = null;
      state.userPrimaryBusinesses = [];
    },

    setUserId (state, newUserId) {
      state.userId = newUserId;
    },

    setViewUserId (state, newUserId) {
      state.viewingUserId = newUserId;
    },

    setUserRole (state, newUserRole) {
      state.userRole = newUserRole;
    },

    setUserPrimaryBusinesses (state, newBusinesses) {
      state.userPrimaryBusinesses = newBusinesses;
    },

    setBusinessId (state, newBusinessId) {
      state.businessId = newBusinessId;
    },

    setBusinessName (state, newBusinessName) {
      state.businessName = newBusinessName;
    },

  }
})


const routes = [
  {path: '/', component: Homepage},
  {path: '/login', component: Login},
  {path: '/businesses', component: BusinessRegister},
  {name: 'LoginPage', path: '/login', component: Login},
  {path: '/Register', component: Register},
  {name: 'UserPage', path: '/users/:id', component: Users},
  {path: '/search', component: Search},
  {
    path: '/businesses/:id',
    name: 'Business',
    component: Business,
    children: [
          {
            path: 'administrators',
            name: 'BusinessAdministrators',
            component: BusinessAdministrators,
          }
        ]
  },

];

const router = new VueRouter({
  routes,
  mode: 'history'
});

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store: store,
  render: h => h(App)
});