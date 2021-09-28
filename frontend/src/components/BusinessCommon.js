import {store} from "../store";
import axios from "axios";

export default {

    /**
     * A function to check for a business registration or modification form
     * @param businessName Name of the business
     * @param description Description of the business
     * @param country The business' country
     * @param businessType The type of the business
     * @returns Will return an empty list if there are no errors,
     * otherwise, will return the list of errors
     */
    businessCheckForm: function(businessName, description, country, businessType, ) {
        let errors = [];

        if (businessName.length === 0) {
            errors.push('businessName');
        }

        if (description != null) {
            if (description.length > 140) {
                errors.push('description');
            }
        }

        if (country.length === 0) {
            errors.push('country');
        }

        if (!this.checkAge()){
            errors.push('dob');
        }

        if (!businessType) {
            errors.push('businessType');
        }
        return errors;
    },


    /**
     * Returns the years since the user was born. No rounding is done in the function.
     * @param enteredDate The user's birthdate
     * @returns {boolean} Whether the user is old enough, 16, to register a business.
     */
    checkAge: function() {
        let enteredDate = store.userDateOfBirth;
        let years = new Date(new Date() - new Date(enteredDate)).getFullYear() - 1970;
        return (years >= 16);
    },

    /**
     * Retrieve a list of suggested cities using the photon open api.
     */
    getCitiesFromPhoton: function() {
        if (this.city.length >= this.minNumberOfCharacters) {

            this.suggestCities = true;
            axios.get(`https://photon.komoot.io/api/?q=${this.city}&osm_tag=place:city&lang=en`)
                .then( res => {
                    this.suggestedCities = res.data.features.map(location => location.properties.name);
                    this.suggestedCities = this.suggestedCities.filter(city => city != null);
                })
                .catch( error => {
                    console.log("Error with getting cities from photon." + error);
                });
        }
        else {
            this.suggestCities = false;
        }
    },

    /**
     * Retrieve a list of suggested countries using the photon open api.
     */
    getCountriesFromPhoton: function(country, minNumOfChars) {
        if (country >= minNumOfChars) {

            let suggestedCountries = []
            axios.get(`https://photon.komoot.io/api/?q=${country}&osm_tag=place:country&lang=en`)
                .then( res => {
                    suggestedCountries = res.data.features.map(location => location.properties.country);
                    console.log("Bababooey")
                    return {'0': false,
                        '1': suggestedCountries};
                })
                .catch( error => {
                    console.log("Bababooey")

                    this.$log.error(error)
                });
        }
        else {
            return {'0': false,
                    '1': []};
        }
    },
}