import { shallowMount, createLocalVue } from '@vue/test-utils';
import Homepage from '../components/Homepage.vue';
import Vuesax from 'vuesax';

import api from '../Api';

let wrapper;
let localVue = createLocalVue();
localVue.use(Vuesax);

const mockUser = {
    "id": 5,
    "firstName": "Rayna",
    "middleName": "YEP",
    "lastName": "Dalgety",
    "nickname": "Universal",
    "bio": "zero tolerance task-force",
    "email": "rdalgety3@ocn.ne.jp",
    "dateOfBirth": "2006-03-30",
    "phoneNumber": "+7 684 622 5902",
    "homeAddress": "44 Ramsey Court",
    "created": "2021-04-05 00:11:04",
    "role": "USER",
    "businessesAdministered": [
        2
    ]
}

const mockListing =
    [
        {
            "id": 4,
            "inventoryItem": {
                "id": 2,
                "product": {
                    "id": "W04GP5EC0B1798680",
                    "business": {
                        "name": "Dabshots",
                        "id": 1,
                        "administrators": [],
                        "primaryAdministratorId": null,
                        "description": "Nullam varius. Nulla facilisi. Cras non velit nec nisi vulputate nonummy.",
                        "address": {
                            "streetNumber": "0",
                            "streetName": "Vernon Place",
                            "suburb": null,
                            "city": "Sarpang",
                            "region": null,
                            "country": "Bhutan",
                            "postcode": null
                        },
                        "businessType": "Charitable organisation",
                        "created": "2020-05-18 09:06:11"
                    },
                    "name": "Compound - Mocha",
                    "description": "vel ipsum praesent blandit lacinia erat vestibulum sed magna at nunc",
                    "manufacturer": "Nestle",
                    "recommendedRetailPrice": 88.93,
                    "created": "2021-01-11 07:54:46",
                    "images": [],
                    "primaryImagePath": null
                },
                "quantity": 7,
                "pricePerItem": 3.0,
                "totalPrice": 80.0,
                "manufactured": "2020-01-26",
                "sellBy": null,
                "bestBefore": "2021-08-27",
                "expires": "2021-08-27"
            },
            "quantity": 1,
            "price": 15.5,
            "moreInfo": "Contact us for more information.",
            "created": "2021-02-01 23:00:00",
            "closes": "2021-09-08 00:00:00",
            "likes": 1
        }
    ]

const mockBusiness =
    {
        "id": 1,
        "administrators": [
            22
        ],
        "name": "Dabshots",
        "primaryAdministratorId": 1,
        "description": "Nullam varius. Nulla facilisi. Cras non velit nec nisi vulputate nonummy.",
        "address": {
            "streetNumber": "0",
            "streetName": "Vernon Place",
            "city": "Sarpang",
            "region": null,
            "country": "Bhutan",
            "postcode": null
        },
        "businessType": "Charitable organisation",
        "created": "2020-05-18 21:06:11"
    };

let mockCard = {
    id: 1,
    user: {},
    title: "Beans - Green",
    description: "Integer ac leo.",
    created: "2021-06-01 18:32:38",
    displayPeriodEnd: "2021-06-15 18:32:38",
    keywords: "aliquam augue quam",
    section: "Wanted"
}

const wishlist = {
    business: {
        name: "Dabshots",
        businessType: "Retail trade",
        id: 1
    },
    id: 1,
    muted: false,
    userId: 1
}

jest.mock("../Api.js", () => jest.fn);
api.getUserFromID = jest.fn(() => {
  return Promise.resolve({data: mockUser, status: 200});
});

api.checkSession = jest.fn(() => {
  return Promise.resolve({status: 200}).catch({response: {message: "Bad request", status: 400}});
});


api.getBusinessFromId = jest.fn(() => {
  return Promise.resolve({data: mockBusiness, status: 200}).catch({response: {message: "Bad request", status: 400}});
})

api.getMessages = jest.fn(() => {
  return Promise.resolve({status: 200}).catch({response: {message: "Bad request", status: 400}});
})

api.getUserLikedListings = jest.fn(() => {
    return Promise.resolve({data: mockListing, status: 200}).catch({response: {message: "Bad request", status: 400}});
})

api.removeLikeFromListing = jest.fn(() => {
    return Promise.resolve({data: mockListing, status: 200}).catch({response: {message: "Bad request", status: 400}});
})

api.getUsersWishlistedBusinesses = jest.fn(() => {
    return Promise.resolve({data: wishlist, status: 200}).catch({response: {message: "Bad request", status: 400}});
});

api.removeBusinessFromWishlist = jest.fn(() => {
    return Promise.resolve({status: 200}).catch({response: {message: "Bad request", status: 400}});
});

const getUserName = jest.spyOn(Homepage.methods, 'getUserName');
getUserName.mockImplementation(() =>  {
    return 'Rayna';
});

const getBusinessName = jest.spyOn(Homepage.methods, 'getBusinessName');

const getLoggedInUserIdMethod = jest.spyOn(Homepage.methods, 'getLoggedInUserId');
getLoggedInUserIdMethod.mockResolvedValue(mockUser.id);

api.getUserCards = jest.fn(() => {
    return Promise.resolve({data: [mockCard], status: 200}).catch({response: {message: "Bad request", status: 400}});
});

const $router = {
    push: jest.fn()
};

const $log = {
    error: jest.fn(),
};

let $vs = {
    loading: jest.fn(),
    notify: jest.fn()
};

beforeEach(() => {
    wrapper = shallowMount(Homepage, {
        propsData: {},
        mocks: {$router, $log, $vs},
        stubs: ['router-link', 'router-view', 'CardModal'],
        methods: {},
        localVue,
        data () {
            return {
                userId: mockUser.id,
                business: mockBusiness,
                actingAsBusinessId: null,

            }
        }
    });
    wrapper.vm.$vs.loading.close = jest.fn();
});

afterEach(() => {
    wrapper.destroy();
});

describe('Homepage user tests', () => {

    test('User\'s first name is shown', () => {
        const nameTitle = wrapper.find("#name")
        expect(nameTitle.text().includes('Rayna')).toBe(true);
    });

    test('Go to profile gets called when clicked', () => {
        const profileButton = wrapper.find("#user-profile-btn");
        wrapper.vm.goToProfile = jest.fn();
        profileButton.trigger('click')
        expect(wrapper.vm.goToProfile).toBeCalled();
    });

    test('Go to marketplace is present', () => {
        const profileButton = wrapper.find("#marketplace-btn");
        expect(profileButton).toBeTruthy();
    });

    test('Card modal successfully opens', async () => {
        expect(wrapper.vm.showMarketModal).toBeFalsy();
        await wrapper.vm.openMarketModal();
        expect(wrapper.vm.showMarketModal).toBeTruthy();
        expect(wrapper.find('#market-card-modal')).toBeTruthy();
    });

    test('User\'s cards are retrieved and set', async () => {
        expect(wrapper.vm.cards).toStrictEqual([]);
        await wrapper.vm.getUserCards();
        await wrapper.vm.$nextTick();
        expect(wrapper.vm.cards).toEqual([mockCard]);
    });
});


describe('Homepage business tests', () => {
    beforeEach(() => {
        wrapper.vm.actingAsBusinessId = 1;
        getBusinessName.mockImplementation(() =>  {
            return 'Dabshots';
        });
        const getLoggedInUserIdMethod = jest.spyOn(Homepage.methods, 'getBusinessId');
        getLoggedInUserIdMethod.mockResolvedValue(wrapper.vm.actingAsBusinessId);
    });

    test('is a Vue instance', () => {
        expect(wrapper.isVueInstance).toBeTruthy();
    });

    test('Business\'s name is shown', () => {
        const busPageTitle = wrapper.find("#business-name")
        expect(busPageTitle.text().includes('Dabshots')).toBe(true);
    });

    test('Go to business profile gets called when clicked', () => {
        const profileButton = wrapper.find("#bus-profile-btn");
        wrapper.vm.goToProfile = jest.fn();
        profileButton.trigger('click')
        expect(wrapper.vm.goToProfile).toBeCalled();
    });

    test('Go to business catalogue gets called when clicked', () => {
        const profileButton = wrapper.find("#bus-catalogue-btn");
        wrapper.vm.goToProductCatalogue = jest.fn();
        profileButton.trigger('click')
        expect(wrapper.vm.goToProductCatalogue).toBeCalled();
    });

});

describe("Tests for functionality", ()=> {
   test("Get user ID test successfully", async () => {
       await wrapper.vm.getUserDetails(5);
       expect(wrapper.vm.user).toEqual(mockUser);
       expect(wrapper.vm.businesses).toEqual([2]);
       expect(wrapper.vm.userLoggedIn).toBeTruthy();
   });

   test("Get user ID while logged out", async () => {
       api.getUserFromID = jest.fn(() => {
           return Promise.reject({response: {message: "You must be logged in!", status: 401}});
       });
       await wrapper.vm.getUserDetails(5);
       expect(wrapper.vm.user).toEqual(null);
       //expect(wrapper.vm.user).toEqual(undefined); //breaks jest, have uncommented for now
       expect(wrapper.vm.businesses).toEqual([]);
       expect(wrapper.vm.userLoggedIn).toBeFalsy();
   });
});

describe("Tests for watchlist functionality", ()=> {
    test("Unlike listing successfully", async () => {
        expect(wrapper.vm.likes).toBe(1);
        await wrapper.vm.unlike(4);
        expect(wrapper.vm.likes).toBe(0);
    });
});

describe("Tests for business watchlist functionality", ()=> {
    test("Clicking star button calls the remove business from watchlist function", async () => {
        wrapper.vm.watchlist = false;
        const star = wrapper.find("#like-icon");
        star.trigger('click');
        expect(wrapper.vm.$vs.notify).toBeCalled();
    });

    test('Clicking on the watchlist tab displays watchlist', () => {
        const tab = wrapper.find("#watchlist-tab");
        tab.trigger('click');
        expect(wrapper.vm.watchlist).toBeTruthy();
    });

    test('After completing the remove business from wishlist method, get wishlist is called', async () => {
        await wrapper.vm.removeFromWishlist(1);
        expect(api.getUsersWishlistedBusinesses).toBeCalled();
    });
});
