import { mount, createLocalVue } from '@vue/test-utils';
import Homepage from '../components/Homepage.vue';
import Vuesax from 'vuesax';

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

beforeEach(() => {
    wrapper = mount(Homepage, {
        propsData: {},
        mocks: {$router},
        stubs: ['router-link', 'router-view'],
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

    const checkSessionMethod = jest.spyOn(Homepage.methods, 'checkUserSession');
    checkSessionMethod.mockImplementation(() => {
        wrapper.vm.user = mockUser;
        wrapper.vm.currencyCode = "NZD";
        wrapper.vm.currencySymbol = "$"
    });


    const getUserMethod = jest.spyOn(Homepage.methods, 'getUserDetails');
    getUserMethod.mockResolvedValue(mockUser);
    expect(wrapper).toBeTruthy();
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

