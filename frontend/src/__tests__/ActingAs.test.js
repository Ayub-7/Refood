import {createLocalVue, shallowMount} from '@vue/test-utils';
import ActingAs from '../components/ActingAs';
import Vuesax from 'vuesax';
//import {mutations} from "@/store";
//import {store} from "@/store";
//import Users from "@/components/Users";
//import {store} from "@/store";

const localVue = createLocalVue();
let wrapper;
localVue.use(Vuesax);


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

// let store = {loggedInUserId: 22,
//     role: "USER",
//     userName: "Wileen Tisley",
//     userPrimaryBusinesses: [],
//     actingAsBusinessId: null,
//     actingAsBusinessName: null
// }
//console.log(localVue);
beforeEach(() => {
    //console.log(mockBusinesses);
    wrapper = shallowMount(ActingAs, {
        localVue,
        propsData: {},
        mocks: {},
        stubs: ['router-link', 'router-view'],
        methods: {},
        data() {
            return {
                loggedInUserId: 22,
                userName: "Wileen Tisley",
                role: "USER",
                userPrimaryBusinesses: mockBusinesses,
                actingAsBusinessId: null,
                actingAsBusinessName: null
            }
        }
    });
    const getUserNameMethod = jest.spyOn(ActingAs.methods, 'getUserName')
    getUserNameMethod.mockImplementation(() => {
        wrapper.vm.userName = "Wileen Tisley";
        return wrapper.vm.userName;
    });
    const getUserRoleMethod = jest.spyOn(ActingAs.methods, 'getUserRole')
    getUserRoleMethod.mockImplementation(() => {
        wrapper.vm.role = "USER";
        return wrapper.vm.role;
    });
    //wrapper.vm.store.userPrimaryBusinesses = mockBusinesses;
    //wrapper.vm.userName = "Wileen Tisley";
    //wrapper.setData({role: "USER"})
    //wrapper.vm.role =  "USER";
    //wrapper.setData({userPrimaryBusinesses: mockBusinesses})
    //wrapper.vm.userPrimaryBusinesses =  mockBusinesses;
    //wrapper.setData({actingAsBusinessName: null})
    //wrapper.vm.actingAsBusinessName =  null;
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
        // wrapper.vm.userName = "Wileen Tisley";
        // wrapper.vm.role =  "USER";
        expect(wrapper.find('.user').text()).toBe('Logged in as USER Wileen Tisley');
    });

    test('Check user primary businesses dropdown is rendered', () =>  {
        expect(wrapper.find('.dropdown').exists()).toBe(true);
    });

    // TODO: get jest test working for changing from user to business
    // test('Check selecting business in dropdown updates actingAsBusinessId, actingAsBusinessName ', () => {
    //     //Select business
    //     wrapper.vm.userPrimaryBusinesses =  mockBusinesses;
    //     const businessOne = wrapper.vm.userPrimaryBusinesses[0];
    //     wrapper.vm.buss = businessOne.name;
    //     wrapper.find('.dropdown').trigger('click');
    //     const setActingAsBusiness = jest.spyOn(ActingAs.methods, 'setActingAsBusinessId')
    //     setActingAsBusiness.mockImplementation(() => {
    //         wrapper.vm.actingAsBusinessId = mockBusinesses[0].id;
    //         wrapper.vm.actingAsBusinessName = mockBusinesses[0].name;
    //     });
    //     expect(wrapper.vm.actingAsBusinessId).toBe(businessOne.id);
    //     expect(wrapper.vm.actingAsBusinessName).toBe(businessOne.name);
    // });

    // TODO: get jest test working for changing from user to business
    // test('Check selecting business in dropdown displays business info, updates user text', () => {
    //     //Select business
    //     wrapper.vm.userPrimaryBusinesses =  mockBusinesses;
    //     wrapper.vm.userName = "Wileen Tisley";
    //     const businessOne = wrapper.vm.userPrimaryBusinesses[0];
    //     wrapper.vm.buss = businessOne.name;
    //     wrapper.find('.dropdown').trigger('change');
    //     expect(wrapper.find('.business').exists()).toBe(true);
    //     expect(wrapper.find('.business').text()).toBe('Logged in as BUSINESS: ' + businessOne.name);
    //     expect(wrapper.find('.user').text()).toBe('Act as User: ' + wrapper.vm.userName);
    // });

    test('Check selecting business in dropdown calls setActingAsBusinessId method', () => {
        //Select business
        wrapper.vm.userPrimaryBusinesses =  mockBusinesses;
        const businessOne = wrapper.vm.userPrimaryBusinesses[0];
        wrapper.vm.setActingAsBusinessId = jest.fn();
        wrapper.vm.buss = businessOne.name;
        wrapper.find('.dropdown').trigger('change');
        expect(wrapper.vm.setActingAsBusinessId).toBeCalled;
    });

    // TODO: get jest test working for changing from user to business and back to business
    // test('Check selecting username in userinfo, makes user act as user, hides business info', () => {
    //     //Select business
    //     wrapper.vm.userPrimaryBusinesses =  mockBusinesses;
    //     wrapper.vm.userName = "Wileen Tisley";
    //     wrapper.vm.role = "USER";
    //     const businessOne = wrapper.vm.userPrimaryBusinesses[0];
    //     wrapper.vm.buss = businessOne.name;
    //     wrapper.find('.dropdown').trigger('change');
    //     wrapper.find('.user').trigger('click');
    //     expect(wrapper.find('.dropdown').exists()).toBe(false);
    //     expect(wrapper.find('.user').text()).toBe('Logged in as ' + wrapper.vm.role +  ' ' + wrapper.vm.userName);
    //     expect(wrapper.find('.business').exists()).toBe(false);
    // });

    test('Check selecting user calls setActingAsUser method', () => {
        //Select business
        wrapper.vm.userPrimaryBusinesses =  mockBusinesses;
        const businessOne = wrapper.vm.userPrimaryBusinesses[0];
        wrapper.vm.buss = businessOne.name;
        wrapper.vm.setActingAsUser = jest.fn();
        wrapper.find('.dropdown').trigger('change');
        wrapper.find('.user').trigger('click');
        expect(wrapper.vm.setActingAsUser).toBeCalled;
    });
});
