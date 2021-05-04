import { shallowMount, createLocalVue } from '@vue/test-utils';
import Business from '../components/BusinessRegister';
import Vuesax from 'vuesax';
import {store} from "../store";

let wrapper;
const localVue = createLocalVue();
localVue.use(Vuesax);

let $vs = {
    notify: jest.fn()
}

//Mock user
const mockUser = {
    "id": 5,
    "firstName": "Rayna",
    "middleName": "YEP",
    "lastName": "Dalgety",
    "nickname": "Universal",
    "bio": "zero tolerance task-force",
    "email": "rdalgety3@ocn.ne.jp",
    "dateOfBirth": "1999-02-28",
    "phoneNumber": "+7 684 622 5902",
    "homeAddress": "44 Ramsey Court",
    "created": "2021-04-05 00:11:04",
    "role": "USER",
    "businessesAdministered": []
}

const $router = {
    push: jest.fn()
}
const checkAgeMethod = jest.spyOn(Business.methods, 'checkAge');
beforeEach(() => {
    wrapper = shallowMount(Business, {
        propsData: {},
        mocks: {$vs, store, $router},
        stubs: [],
        methods: {},
        localVue,
    });
});

afterEach(() => {
    wrapper.destroy();
});

describe('Age check tests', () => {
    test('Permitted age to register business', () => {
        store.userDateOfBirth = "1998-04-01";
        expect(wrapper.vm.checkAge()).toBe(true);
    });

    test('Forbidden age to register business', () => {
        store.userDateOfBirth = "2006-04-01";
        expect(wrapper.vm.checkAge()).toBe(false);
    });
});


//TESTS TO CHECK LOGIN ERROR HANDLING
describe('Business Register error checking', () => {


    beforeEach(() => {
        checkAgeMethod.mockImplementation(() => {
            return true;
        });
    });

    test('Handles empty Register', () => {
        wrapper.vm.checkForm();
        expect(wrapper.vm.errors.length).toBe(3);
    });

    test('Handles only name', () => {
        wrapper.vm.businessName = 'bestBusiness';
        wrapper.vm.checkForm();
        expect(wrapper.vm.errors.length).toBe(2);
    })
    test('Handles only a type', () => {
        wrapper.vm.businessType = 'Charitable Organisation';
        wrapper.vm.checkForm();
        expect(wrapper.vm.errors.length).toBe(2);
    })

    test('Handles old enough user', () => {
        wrapper.vm.checkForm();
        expect(wrapper.vm.errors.length).toBe(3);
    })
});

describe('Business Register user age checking', () => {
    beforeEach(() => {
        checkAgeMethod.mockImplementation(() => {
            return false;
        });
    });

    test('Handles to young user', () => {
        wrapper.vm.checkForm();
        expect(wrapper.vm.errors.includes('dob')).toBe(true);
    })
});

describe('Successful business registration', () => {
    beforeEach(() => {
        checkAgeMethod.mockImplementation(() => {
            return true;
        });
    });

    test('Handles empty Register', () => {
        wrapper.vm.businessName = 'bestBusiness';
        wrapper.vm.businessType = 'Charitable Organisation';
        wrapper.vm.country = 'Kazakhstan';
        wrapper.vm.createBusinessInfo();
        expect(wrapper.vm.errors.length).toBe(0);
    });
});

describe('Set country', () => {
   test("Setting country", () => {
       expect(wrapper.vm.country.length).toEqual(0);
       wrapper.vm.setCountry("Israel");
       expect(wrapper.vm.country.length).toEqual(6);
       expect(wrapper.vm.country).toEqual("Israel");
   })
});

describe('Set city', () => {
    test("Setting city", () => {
        expect(wrapper.vm.city.length).toEqual(0);
        wrapper.vm.setCity("Almaty");
        expect(wrapper.vm.city.length).toEqual(6);
        expect(wrapper.vm.city).toEqual("Almaty");
    })
});

describe('Recommend countries', () => {
    test("Successful recommendation", () => {
        wrapper.vm.country = "Rus";
        expect(wrapper.vm.suggestCountries).toBeFalsy();
        wrapper.vm.getCountriesFromPhoton();
        expect(wrapper.vm.suggestCountries).toBeTruthy();
    })

    test("Unsuccessful recommendation", () => {
        wrapper.vm.country = "Ru";
        expect(wrapper.vm.suggestCountries).toBeFalsy();
        wrapper.vm.getCountriesFromPhoton();
        expect(wrapper.vm.suggestCountries).toBeFalsy();
    })
});

describe('Recommend cities', () => {
    test("Successful recommendation", () => {
        wrapper.vm.city = "Cai";
        expect(wrapper.vm.suggestCities).toBeFalsy();
        wrapper.vm.getCitiesFromPhoton();
        expect(wrapper.vm.suggestCities).toBeTruthy();
    })

    test("Unsuccessful recommendation", () => {
        wrapper.vm.city = "Ca";
        expect(wrapper.vm.suggestCities).toBeFalsy();
        wrapper.vm.getCitiesFromPhoton();
        expect(wrapper.vm.suggestCities).toBeFalsy();
    })
});