import {store} from "../store";

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
}