import { shallowMount } from '@vue/test-utils';
import Business from '../Business';

let wrapper;

// Mock busibess admin
const mockAdmin = {
    "id": 9,
    "firstName": "Joete",
    "middleName": "YEP",
    "lastName": "Stopps",
    "nickname": "Multi-layered",
    "bio": "responsive capacity",
    "email": "jstopps7@flickr.com",
    "dateOfBirth": "1984-10-14",
    "phoneNumber": "+36 694 564 9090",
    "homeAddress": "34 Mendota Avenue",
    "created": "2021-04-03 23:29:50",
    "role": "USER",
    "businessesAdministered": [
        1
    ]
}

// Mock Business
const mockBusiness = {
        "id": 1,
        "administrators": [
        {
            "id": 9,
            "firstName": "Joete",
            "middleName": "YEP",
            "lastName": "Stopps",
            "nickname": "Multi-layered",
            "bio": "responsive capacity",
            "email": "jstopps7@flickr.com",
            "dateOfBirth": "1984-10-14",
            "phoneNumber": "+36 694 564 9090",
            "homeAddress": "34 Mendota Avenue",
            "created": "2021-04-03 23:29:50",
            "role": "USER",
            "businessesAdministered": [
                1
            ]
        }
    ],
        "name": "Business1",
        "description": "Test Business 1",
        "address": "123 Test Street",
        "businessType": "Accommodation and Food Services",
        "created": "2021-04-03 23:29:50"
    }


// Mocking $route
const $route = {
    params: {
        id: 1
    }
};

beforeEach(() => {
    wrapper = shallowMount(Business, {
        propsData: {},
        mocks: {$route},
        stubs: ['router-link', 'router-view'],
        methods: {},
    });
    wrapper.setData({business: mockBusiness})
});

afterEach(() => {
    wrapper.destroy();
});

describe('Business tests', () => {
    const getBusinessMethod = jest.spyOn(Business.methods, 'getBusiness');
    getBusinessMethod.mockResolvedValue(mockBusiness);

    test('is a Vue instance', () => {
        expect(wrapper.isVueInstance).toBeTruthy();
    });

    test('has business', () => {
        expect(wrapper.vm.$route.params).toHaveProperty('id', 1);
        expect(wrapper.vm.business).toBeTruthy();
    });
});

describe("Successful login", () => {
    beforeEach(() => {
       wrapper.vm.user = mockAdmin;
    })

    test('business page loads successfully', () => {
        expect(wrapper.find('#business-name').exists()).toBe(true)
        expect(wrapper.find('#business-navbar').exists()).toBe(true)
    });
})