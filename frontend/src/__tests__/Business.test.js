import { mount, createLocalVue } from '@vue/test-utils';
import Business from '../components/Business';
import Vuesax from 'vuesax';
import api from '../Api';

let wrapper;
let localVue = createLocalVue();
localVue.use(Vuesax);

// Mock business admin
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
        "created": "2021-04-03 23:29:50",
        "images": []
}

const wishlist = [
    {
        id: 1,
        businessId: 1,
        userId: 1,
        muted: false,
    }
]


// Mocking $route
const $route = {
    params: {
        id: 1
    }
};

const $vs = {
    notify: jest.fn(),
};

const $log = {
    error: jest.fn(),
    debug: jest.fn(),
}

api.checkSession = jest.fn().mockResolvedValue({data: {id: 1}});
api.getBusinessFromId = jest.fn().mockResolvedValue({data: mockBusiness});
api.getUserFromID = jest.fn().mockResolvedValue({data: mockAdmin});
api.getUsersWishlistedBusinesses = jest.fn().mockImplementation(() => {
    return Promise.resolve({status: 200, data: wishlist});
});
api.removeBusinessFromWishlist = jest.fn().mockImplementation(() => {
    return Promise.resolve({status: 200});
});
api.addBusinessToWishlist = jest.fn().mockImplementation(() => {
    return Promise.resolve({status: 201});
});

beforeEach(() => {
    wrapper = mount(Business, {
        propsData: {},
        mocks: {$route, $vs, $log},
        stubs: ['router-link', 'router-view'],
        methods: {},
        localVue
    });
    wrapper.setData({business: mockBusiness, user: mockAdmin});
});

afterEach(() => {
    wrapper.destroy();
});


describe('Business tests', () => {
    beforeEach(() => {
        const checkSessionMethod = jest.spyOn(Business.methods, 'checkUserSession');
        checkSessionMethod.mockImplementation(() => {
            wrapper.vm.user = mockAdmin;
            wrapper.vm.business = mockBusiness;
            wrapper.vm.adminList = mockBusiness.administrators;
            wrapper.vm.images = [];
        });

        const getUserMethod = jest.spyOn(Business.methods, 'getUserInfo');
        getUserMethod.mockResolvedValue(mockAdmin);

        const getBusinessMethod = jest.spyOn(Business.methods, 'getBusiness');
        getBusinessMethod.mockResolvedValue(mockBusiness);
    });

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

describe("Functionality tests", () => {
    test("Check session is called", () => {
        expect(api.checkSession).toBeCalled();
    });

    test("Check GET user info is called and sets data", () => {
        expect(api.getUserFromID).toBeCalled();
        expect(wrapper.vm.user).toBe(mockAdmin);
    });

    test("Check GET business is being called and sets data", () => {
        expect(api.getBusinessFromId).toBeCalled();
        expect(wrapper.vm.business).toBe(mockBusiness);
    });
});

describe("Wishlist functionality tests", () => {
   test("Business is removed from wishlist when button is pressed", async () => {
       wrapper.vm.inWishlist = true;
       let button = wrapper.find("#wishlist-button");
       expect(button).toBeTruthy();
       await button.trigger("click");
       expect(wrapper.vm.inWishlist).toBe(false);
   });

    test("Business is added to wishlist when button is pressed", async () => {
        wrapper.vm.inWishlist = false;

        let button = wrapper.find("#wishlist-button");
        expect(button).toBeTruthy();
        await button.trigger("click");
        expect(wrapper.vm.inWishlist).toBe(true);
    });


});
