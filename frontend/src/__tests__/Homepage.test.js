import { shallowMount, createLocalVue } from '@vue/test-utils';
import Homepage from '../components/Homepage.vue';
import Vuesax from 'vuesax';
import HomepageMessages from "../components/HomePageMessages.vue";

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

jest.mock("../Api.js", () => jest.fn);
api.getUserFromID = jest.fn(() => {
  return Promise.resolve({data: mockUser, status: 200});
});

api.getUserCards = jest.fn(() => {
    return Promise.resolve({data: mockCard, status: 200}).catch({response: {message: "Bad request", status: 400}});
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


const getUserName = jest.spyOn(Homepage.methods, 'getUserName');
getUserName.mockImplementation(() =>  {
    return 'Rayna';
});

const getBussinessName = jest.spyOn(Homepage.methods, 'getBusinessName');

const getLoggedInUserIdMethod = jest.spyOn(Homepage.methods, 'getLoggedInUserId');
getLoggedInUserIdMethod.mockResolvedValue(mockUser.id);

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
                actingAsBusinessId: null
            }
        }
    });
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
});


describe('Homepage business tests', () => {
    beforeEach(() => {
        wrapper.vm.actingAsBusinessId = 1;
        getBussinessName.mockImplementation(() =>  {
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
       expect(wrapper.vm.user).toEqual(undefined);
       expect(wrapper.vm.businesses).toEqual([]);
       expect(wrapper.vm.userLoggedIn).toBeFalsy();
   });
});
