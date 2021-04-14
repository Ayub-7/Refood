import {createLocalVue, shallowMount} from '@vue/test-utils';
import Users from '../Users';
import {store} from '../../store';
import Vuesax from 'vuesax';


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
        "id": 2,
        "administrators": [
            {
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
                "created": "2021-04-07 01:09:35",
                "role": "USER",
                "businessesAdministered": [
                    2
                ]
            }
        ],
        "name": "Business2",
        "primaryAdministratorId": 5,
        "description": "Test Business 2",
        "address": "23 Testing Avenue",
        "businessType": "Retail Trade",
        "created": "2021-04-07 01:09:35"
    }
]

const $route = {
    params: {
        id: 5
    }
};


const localVue = createLocalVue();
localVue.use(Vuesax);


beforeEach(() => {


    wrapper = shallowMount(Users, {
        localVue,
        propsData: {},
        mocks: {$route, store},
        stubs: ['router-link', 'router-view'],
        methods: {},
        
    });
    wrapper.setData({user: mockUser})
    wrapper.setData({userViewingBusinesses: ['test', 'test']})
    const getUserMethod = jest.spyOn(Users.methods, 'getUserInfo');
    getUserMethod.mockResolvedValue(mockUser);
    wrapper.setData({businesses: mockBusinesses})


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
        expect(wrapper.find('#container').exists()).toBe(true)
    });

    test('addUserToBusiness function is called', async () => {
        wrapper.vm.addUserToBusiness = jest.fn();
        const openModalMethod = jest.spyOn(Users.methods, 'openModal')
        openModalMethod.mockImplementation(() => {
            wrapper.vm.showModal = true;
        });


        expect(wrapper.find('#option-add-to-business').exists()).toBe(true);
        wrapper.find('#option-add-to-business').trigger('click');

        await wrapper.vm.$nextTick(); // lets the wrapper go through the v-for loop.

        expect(wrapper.find('#add-user').exists()).toBe(true);
        wrapper.find('#add-user').trigger('click');

        expect(wrapper.vm.addUserToBusiness).toBeCalled();
    })

});


describe('Business link tests with businesses', () =>  {
    beforeEach(() => {
        wrapper.setData({businesses: mockBusinesses});
    });

    test('Business names load successfully', () => {
        expect(wrapper.find('.card').exists()).toBe(true);
    });

    test('goToBusinessPage function gets called',  () => {
        wrapper.vm.goToBusinessPage = jest.fn();
        wrapper.find('.card').trigger('click');
        expect(wrapper.vm.goToBusinessPage).toBeCalled();

    })
})

describe('Business link tests without businesses', () =>  {
    test('Business names don\'t appear', () => {
        expect(wrapper.find('#card').exists()).toBe(false)
    });
})