import { shallowMount } from '@vue/test-utils';
import Users from '../Users';


let wrapper;

// Mock User for testing
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

const mockBusinesses = [
    {
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
                "created": "2021-04-05 00:11:04",
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
        "created": "2021-04-05 00:11:04"
    }
]

const $route = {
    params: {
        id: 5
    }
};



beforeEach(() => {
    wrapper = shallowMount(Users, {
        propsData: {},
        mocks: {$route},
        stubs: ['router-link', 'router-view'],
        methods: {},
    });
    wrapper.setData({user: mockUser})
    //wrapper.setData({businesses: mockBusinesses})
});

afterEach(() => {
    wrapper.destroy();
});

describe('User profile page tests', () => {

    test('Check calculateDuration is called', () => {
        wrapper.vm.calculateDuration = jest.fn();

        expect(typeof(wrapper.vm.calculateDuration)).toBeCalled;
    });

    test('User info loads successfully', () => {
        expect(wrapper.find('#body-profile').exists()).toBe(true)
    });
});


describe('Business link tests with businesses', () =>  {
    beforeEach(() => {
        wrapper.setData({businesses: mockBusinesses});
    });

    test('Business names load successfully', () => {
        expect(wrapper.find('#businessNames').exists()).toBe(true)
    });

    test('goToBusinessPage function gets called', () => {
        wrapper.vm.goToBusinessPage = jest.fn();
        const businessButton = wrapper.find('#business')
        businessButton.trigger('click');
        expect(wrapper.vm.goToBusinessPage).toBeCalled();


    })
})


describe('Business link tests without businesses', () =>  {
    test('Business names don\'t appear', () => {
        expect(wrapper.find('#businessNames').exists()).toBe(false)
    });
})