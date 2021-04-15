import {createLocalVue, shallowMount} from '@vue/test-utils';
import ActingAs from '../components/ActingAs';
import {store} from '../store';
//import Users from "@/components/Users";
//import Users from "../components/Users";



const localVue = createLocalVue();
let wrapper;
jest.mock('../store', () => ({ store: jest.fn()}));

//jest.mock('../store.js', () => jest.fn);


//Mock User
// const mockUser = {
//     "id": 5,
//     "firstName": "Rayna",
//     "middleName": "YEP",
//     "lastName": "Dalgety",
//     "nickname": "Universal",
//     "bio": "zero tolerance task-force",
//     "email": "rdalgety3@ocn.ne.jp",
//     "dateOfBirth": "2006-03-30",
//     "phoneNumber": "+7 684 622 5902",
//     "homeAddress": "44 Ramsey Court",
//     "created": "2021-04-05 00:11:04",
//     "role": "USER",
//     "businessesAdministered": [
//         2
//     ]
// }

// Mock Business
const mockBusinesses = [
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
    },
    {
        "id": 2,
        "administrators": [
            22
        ],
        "name": "Layo",
        "primaryAdministratorId": 2,
        "description": "Nam ultrices, libero non mattis pulvinar, nulla pede ullamcorper augue, a suscipit nulla elit ac nulla. Sed vel enim sit amet nunc viverra dapibus. Nulla suscipit ligula in lacus.",
        "address": {
            "streetNumber": "95403",
            "streetName": "Hanover Park",
            "city": "El Guapinol",
            "region": null,
            "country": "Honduras",
            "postcode": null
        },
        "businessType": "Accommodation and Food Services",
        "created": "2020-08-25 22:22:19"
    },
    {
        "id": 3,
        "administrators": [
            22
        ],
        "name": "Skinder",
        "primaryAdministratorId": 3,
        "description": "Morbi sem mauris, laoreet ut, rhoncus aliquet, pulvinar sed, nisl. Nunc rhoncus dui vel sem.",
        "address": {
            "streetNumber": "6794",
            "streetName": "Jackson Way",
            "city": "Xialaba",
            "region": null,
            "country": "China",
            "postcode": null
        },
        "businessType": "Retail Trade",
        "created": "2020-09-11 20:50:50"
    }
]

beforeEach(() => {
    wrapper = shallowMount(ActingAs, {
        localVue,
        propsData: {},
        mocks: {},
        stubs: ['router-link', 'router-view'],
        methods: {},
    });
    const setUserLoggedIn = jest.spyOn(mutations.methods, 'setUserLoggedIn');
    setUserLoggedIn.mockResolvedValue(22, "USER");
    store.setUserLoggedIn(22, "USER");
    const setUserName = jest.spyOn(mutations.methods, 'setUserName');
    setUserName.mockResolvedValue("Wileen Tilsley");
    const setUserPrimaryBusinesses = jest.spyOn(mutations.methods, 'setUserPrimaryBusinesses');
    setUserPrimaryBusinesses.mockResolvedValue(mockBusinesses);


});

afterEach(() => {
    wrapper.destroy();
});


describe('User acting as tests', () => {
    test('is a Vue instance', () => {
        expect(wrapper.isVueInstance).toBeTruthy();
    });

    test('Check username is shown', () => {
        expect(wrapper.find('.user').exists()).toBe(true);
    });

    test('Check username is displaying the correct username and role', () =>  {
        expect(wrapper.find('.user').toEqual('Logged in as ' + wrapper.store.role +  ' ' + wrapper.store.userName));
    });

    test('Check user primary businesses dropdown is rendered', () =>  {
        expect(wrapper.find('.dropdown').exists()).toBe(true);
    });

    test('Check selecting business in dropdown updates actingAsBusinessId, actingAsBusinessName ', () => {
        //Select business
        const businessOne = wrapper.store.userPrimaryBusinesses[0]
        wrapper.vm.buss = businessOne.name;
        wrapper.find('.dropdown').trigger('click');
        expect(wrapper.store.actingAsBusinessId).toBe(businessOne.id);
        expect(wrapper.store.actingAsBusinessName).toBe(businessOne.name);
    });

    test('Check selecting business in dropdown displays business info, updates user text', () => {
        //Select business
        const businessOne = wrapper.store.userPrimaryBusinesses[0]
        wrapper.vm.buss = businessOne.name;
        wrapper.find('.dropdown').trigger('click');
        expect(wrapper.find('.business').exists()).toBe(true);
        expect(wrapper.find('.business').toEqual('Logged in as BUSINESS: ' + businessOne.name));
        expect(wrapper.find('.user').toEqual('Act as User: ' + wrapper.store.userName));
    });

    test('Check selecting business in dropdown calls setActingAsBusinessId method', () => {
        //Select business
        const businessOne = wrapper.store.userPrimaryBusinesses[0]
        wrapper.vm.setActingAsBusinessId = jest.fn();
        wrapper.vm.buss = businessOne.name;
        wrapper.find('.dropdown').trigger('click');
        expect(wrapper.vm.setActingAsBusinessId).toBeCalled;
    });

    test('Check selecting username in userinfo, makes user act as user, hides business info', () => {
        //Select business
        const businessOne = wrapper.store.userPrimaryBusinesses[0]
        wrapper.vm.buss = businessOne.name;
        wrapper.find('.dropdown').trigger('click');
        wrapper.find('.user').trigger('click');
        expect(wrapper.find('.dropdown').exists()).toBe(false);
        expect(wrapper.find('.user').toEqual('Logged in as ' + wrapper.store.role +  ' ' + wrapper.store.userName));
        expect(wrapper.find('.business').exists()).toBe(false);
    });

    test('Check selecting user calls setActingAsUser method', () => {
        //Select business
        const businessOne = wrapper.store.userPrimaryBusinesses[0]
        wrapper.vm.buss = businessOne.name;
        wrapper.vm.setActingAsUser = jest.fn();
        wrapper.find('.dropdown').trigger('click');
        wrapper.find('.user').trigger('click');
        expect(wrapper.vm.setActingAsUser).toBeCalled;
    });
});
