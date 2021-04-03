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
 * Declare all available services here
 */
import axios from 'axios'

const SERVER_URL = process.env.VUE_APP_SERVER_ADD;

const instance = axios.create({  
  baseURL: SERVER_URL,
  timeout: 1000  
});


export default {
    /**
     * Attempt to authenticate a user account with a username and password
     * @param username The user's username input
     * @param password The user's password input
     * @param token Authentication token added to API request header
     * @returns {Promise<AxiosResponse<any>>}
     */
    login: (email, password) => instance.post('login', {email, password}, {withCredentials: true}),

  // user POST create new user account data
    /**
     * Create a new user by storing their data to the database
     * @param firstName Their firstname
     * @param lastName Their lastname
     * @param middleName Their middlename (OPTIONAL)
     * @param nickname Their nickname (OPTIONAL)
     * @param bio Their bio (OPTIONAL)
     * @param email Their email
     * @param dateOfBirth Their date of birth
     * @param phoneNumber Their phone
     * @param homeAddress Their home address
     * @param hashedPassword Their password hashed using password-hash library
     * @param registerDate Their registration date
     * @returns {Promise<AxiosResponse<any>>}
     */
    createUser: async(firstName, lastName, middleName, nickname, bio, email, dateOfBirth, phoneNumber, homeAddress, password, registerDate) =>
  instance.post('users', {firstName, lastName, middleName, nickname, bio, email, dateOfBirth, phoneNumber, homeAddress, password, registerDate}),

    /**
     * Get a specific user via their unique ID number
     * @param userId The user's unique ID number
     * @returns {Promise<AxiosResponse<any>>}
     */
    getUserFromID: (userId) => instance.get(`users/${userId}`, {withCredentials: true}),

    /**
     * Get all users from the database.
     * @returns {Promise<AxiosResponse<any>>}
     */
    getAll: () => instance.get('Users', {
        transformResponse: [function (data) {
            return data? JSON.parse(data)._embedded.students : data;
        }]
    }),

    /**
     * Search for one or more users from the database from a given input.
     * @param input Any information relating to the user. (e.g: firstname, lastname, ...etc)
     * @returns {Promise<AxiosResponse<any>>}
     */
    //searchQuery: async(input) => instance.post('users/search', input),

    /**
     * Query search results that uses searchQuery function
     * @returns {Promise<AxiosResponse<any>>}
     */
    searchQuery: (query) => instance.get(`/users/search?searchQuery="${query}"`,{withCredentials: true}),

    /**
     * Method (frontend) to let a DGAA user make a user an GAA admin user.
     * @param id user id to be made admin.
     */
    makeUserAdmin: async(id) =>
        instance.put('/users/'+id+'/makeAdmin',{},{withCredentials: true}),


    /**
     * Method (frontend) to let a DGAA user revoke GAA admin status from another user. Reverts the user back to USER role.
     * @param id user id to revoke admin user role.
     */
    revokeUserAdmin: async(id) =>
    instance.put('/users/'+id+'/revokeAdmin',{}, {withCredentials: true}),
}