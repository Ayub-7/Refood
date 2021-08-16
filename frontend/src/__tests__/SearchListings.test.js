import { createLocalVue, shallowMount } from '@vue/test-utils';
import SearchListings from '../components/SearchListings.vue';
import Vuesax from 'vuesax';
import api from "../Api";

let wrapper;

//Mock response data from search request
const mockUsersFromSearch = [ 
    {
        "id": 28,
        "firstName": "Raquela",
        "middleName": "YEP",
        "lastName": "Haylands",
        "nickname": "Future-proofed",
        "bio": "24/7 workforce",
        "email": "rhaylands5@shutterfly.com",
        "dateOfBirth": "2004-10-06",
        "phoneNumber": "+86 944 435 8212",
        "homeAddress": {
            "streetNumber": "32",
            "streetName": "Little Fleur Trail",
            "city": "Christchurch",
            "region": "Canterbury",
            "country": "New Zealand",
            "postcode": "8080"
        },
        "created": "2021-05-01 03:51:26",
        "role": "USER",
        "businessesAdministered": [],
        "country": "New Zealand",
        "city": "Christchurch"
    }
]

const testListings = [
    {
        "id": 2140,
        "inventoryItem": {
            "id": 6449,
            "product": {
                "id": "19UYA42733A359764",
                "business": {
                    "name": "Gobi Desserts",
                    "id": 135,
                    "administrators": [],
                    "primaryAdministratorId": null,
                    "description": "Sed accumsan felis.",
                    "address": {
                        "streetNumber": "9859",
                        "streetName": "Fuller",
                        "suburb": null,
                        "city": "Kalchevaya",
                        "region": null,
                        "country": "Ukraine",
                        "postcode": null
                    },
                    "businessType": "Non-profit organisation",
                    "created": "2021-02-12 03:44:28"
                },
                "name": "Beef - Tenderlion, Center Cut",
                "description": "Morbi non lectus.",
                "manufacturer": "Donec PC",
                "recommendedRetailPrice": 43.0,
                "created": "2021-05-25 09:54:30",
                "images": [],
                "primaryImagePath": null
            },
            "quantity": 82,
            "pricePerItem": 43.85,
            "totalPrice": 3595.7,
            "manufactured": "2021-03-23",
            "sellBy": "2020-11-11",
            "bestBefore": "2020-10-18",
            "expires": "2022-03-10"
        },
        "quantity": 24,
        "price": 1052.4,
        "moreInfo": "Duis aliquam convallis nunc.",
        "created": "2021-05-13 07:52:55",
        "closes": "2022-03-10 12:50:42",
        "likes": 0
    },
    {
        "id": 6339,
        "inventoryItem": {
            "id": 6449,
            "product": {
                "id": "19UYA42733A359764",
                "business": {
                    "name": "Gobi Desserts",
                    "id": 135,
                    "administrators": [],
                    "primaryAdministratorId": null,
                    "description": "Sed accumsan felis.",
                    "address": {
                        "streetNumber": "9859",
                        "streetName": "Fuller",
                        "suburb": null,
                        "city": "Kalchevaya",
                        "region": null,
                        "country": "Ukraine",
                        "postcode": null
                    },
                    "businessType": "Non-profit organisation",
                    "created": "2021-02-12 03:44:28"
                },
                "name": "Beef - Tenderlion, Center Cut",
                "description": "Morbi non lectus.",
                "manufacturer": "Donec PC",
                "recommendedRetailPrice": 43.0,
                "created": "2021-05-25 09:54:30",
                "images": [],
                "primaryImagePath": null
            },
            "quantity": 82,
            "pricePerItem": 43.85,
            "totalPrice": 3595.7,
            "manufactured": "2021-03-23",
            "sellBy": "2020-11-11",
            "bestBefore": "2020-10-18",
            "expires": "2022-03-10"
        },
        "quantity": 6,
        "price": 263.1,
        "moreInfo": "Vivamus in felis eu sapien cursus vestibulum.",
        "created": "2021-03-05 16:41:17",
        "closes": "2022-03-10 18:05:58",
        "likes": 0
    }
]

let $vs = {
    loading: jest.fn(),
}

let $log = {
    debug: jest.fn(),
    error: jest.fn()
}

const localVue = createLocalVue();
localVue.use(Vuesax);

beforeEach(() => {
    wrapper = shallowMount(SearchListings, {
        localVue,
        propsData: {},
        mocks: {$vs, $log},
        stubs: ['router-link', 'router-view'],
        methods: {},
        data () {
            return {
                users: mockUsersFromSearch
            }
        }
    });
    expect(wrapper).toBeTruthy();

    api.searchListingsByLocation = jest.fn(() => {
        return Promise.resolve({data: testListings, status: 200}).finally();
    });
});

afterEach(() => {
    wrapper.destroy();
});

describe('Search page tests', () => {
    test('is a Vue instance', () => {
        expect(wrapper.isVueInstance).toBeTruthy();
    });

    /*
    test('Table exists when valid data is in response', () => {
        wrapper.vm.searchbarListingAddress = "Ukraine AND Kalchevaya"
        wrapper.vm.searchListingsByAddress();

        wrapper.vm.listings = testListings;

        expect(wrapper.vm.listings).toBe(testListings);
    })

    test('Error is presented when ', () => {
        api.searchListingsByLocation = jest.fn(() => {
            return Promise.reject({response: {status: 500}});
        });

        wrapper.vm.searchListingsByAddress();

        expect(wrapper.vm.$vs.notify).toBeCalled();
        //expect(wrapper.vm.error).toBe("true");
    })
     */
});
