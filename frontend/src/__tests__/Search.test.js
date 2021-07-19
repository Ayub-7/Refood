import { createLocalVue, shallowMount } from '@vue/test-utils';
import Search from '../components/Search.vue';
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

const mockBusinessesFromSearch = [
    {
        "name": "Dabshots",
        "id": 1,
        "administrators": [
            {
                "id": 1,
                "firstName": "Wilma",
                "middleName": "Janet",
                "lastName": "Sails",
                "nickname": "Open-architected",
                "bio": "Profit-focused scalable moratorium",
                "email": "jsails0@go.com",
                "dateOfBirth": "1989-02-28",
                "phoneNumber": "+57 242 190 0153",
                "homeAddress": {
                    "streetNumber": "44",
                    "streetName": "Menomonie Way",
                    "suburb": null,
                    "city": "Zhashkiv",
                    "region": null,
                    "country": "Ukraine",
                    "postcode": null
                },
                "created": "2020-08-06 23:35:52",
                "role": "USER",
                "businessesAdministered": null
            }
        ],
        "primaryAdministratorId": 1,
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
    }
]

let $vs = {
    loading: jest.fn(),
}

const localVue = createLocalVue();
localVue.use(Vuesax);

beforeEach(() => {
    wrapper = shallowMount(Search, {
        localVue,
        propsData: {},
        mocks: {$vs},
        stubs: ['router-link', 'router-view'],
        methods: {},
        data () {
            return {
                users: mockUsersFromSearch
            }
        }
    });
    expect(wrapper).toBeTruthy();
    api.searchUsersQuery = jest.fn(() => {
        return Promise.resolve({data: mockUsersFromSearch, status: 200}).finally();
    });

    api.searchBusinessesQuery = jest.fn(() => {
        return Promise.resolve({data: mockBusinessesFromSearch, status: 200}).finally();
    });
    $vs.loading.close = jest.fn()
});

afterEach(() => {
    wrapper.destroy();
});

describe('Search page tests', () => {
    test('Table exists when valid data is in response', () => {
        expect(wrapper.find('#userTable').exists()).toBe(true);        
    })

    test('Mobile mode not on when window is normal size', () => {
        global.innerWidth = 1300;
        global.dispatchEvent(new Event('resize'));
        expect(wrapper.vm.mobileMode).toBe(false)
    })
    

    test('Mobile mode active when window size is reduced', () => {
        global.innerWidth = 1299;
        global.dispatchEvent(new Event('resize'));

        expect(wrapper.vm.mobileMode).toBe(true)

    })
});

describe("Test searching without query", () => {
   test("Successful search - No query", async () => {
      wrapper.vm.searchbarUser = "";
      await wrapper.vm.searchUsers();
      expect(wrapper.vm.$vs.loading).not.toBeCalled();
   });


    test("Successful search - No query", async () => {
        wrapper.vm.searchbarBusiness = "";
        await wrapper.vm.searchBusiness();
        expect(wrapper.vm.$vs.loading).not.toBeCalled();
    });

});


describe("Test searching with query", () => {
    test("Successful user search - with query", async () => {
        wrapper.vm.$vs.loading.close = jest.fn();
        wrapper.vm.searchbarUser = "Something";
        await wrapper.vm.searchUsers();
        expect(wrapper.vm.$vs.loading).toBeCalled();
    });
    test("Successful business search - with query", async () => {
        wrapper.vm.$vs.loading.close = jest.fn();
        wrapper.vm.searchbarBusiness = "Something";
        await wrapper.vm.searchBusiness();
        expect(wrapper.vm.$vs.loading).toBeCalled();
    });
});
