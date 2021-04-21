import { mount, shallowMount } from '@vue/test-utils';
import Homepage from '../components/Homepage.vue';

let wrapper;

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

// Mocking $store
// const store = {
//     loggedInUserId: 5,
//     actingAsBusinessId: 1,
// };

const getLoggedInUserIdMethod = jest.spyOn(Homepage.methods, 'getLoggedInUserId');
getLoggedInUserIdMethod.mockResolvedValue(mockUser.id);

beforeEach(() => {
    wrapper = shallowMount(Homepage, {
        propsData: {},
        mocks: {},
        stubs: ['router-link', 'router-view'],
        methods: {},
        data () {
            return {
                userFirstName: mockUser.firstName,
                userId: mockUser.id,
                business: mockBusiness,
                actingAsBusinessId: null
            }
        }
    });

    const getUserMethod = jest.spyOn(Homepage.methods, 'getUserDetails');
    getUserMethod.mockResolvedValue(mockUser);



    expect(wrapper).toBeTruthy();
});

afterEach(() => {
    wrapper.destroy();
});

describe('Homepage user tests', () => {
    beforeEach(() => {

    });

    test('User\'s first name is shown', () => {
        const nameTitle = wrapper.find("#pageTitle")

        expect(nameTitle.text().includes(wrapper.vm.userFirstName)).toBe(true);
    });


    test('Go to profile gets called when clicked', () => {
        const profileButton = wrapper.find("#userProfile");
        wrapper.vm.goToProfile = jest.fn();

        profileButton.trigger('click')

        expect(wrapper.vm.goToProfile).toBeCalled();
    })
});

describe('Homepage business tests', () => {
    beforeEach(() => {
        wrapper.vm.actingAsBusinessId = 1;

        const getLoggedInUserIdMethod = jest.spyOn(Homepage.methods, 'getBusinessId');
        getLoggedInUserIdMethod.mockResolvedValue(wrapper.vm.actingAsBusinessId);
    });

    test('Business\'s name is shown', () => {
        const busPageTitle = wrapper.find("#busPageTitle")
        expect(busPageTitle.text().includes(wrapper.vm.business.name)).toBe(true);
    });


    test('Go to business profile gets called when clicked', () => {
        const profileButton = wrapper.find("#busProfile");
        wrapper.vm.goToProfile = jest.fn();

        profileButton.trigger('click')

        expect(wrapper.vm.goToProfile).toBeCalled();
    });

    test('Go to business catalogue gets called when clicked', () => {
        const profileButton = wrapper.find("#busCatalogue");
        wrapper.vm.goToProductCatalogue = jest.fn();

        profileButton.trigger('click')

        expect(wrapper.vm.goToProductCatalogue).toBeCalled();
    });

});

