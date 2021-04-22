import { shallowMount, createLocalVue } from '@vue/test-utils';
import Business from '../components/BusinessRegister';
import Vuesax from 'vuesax';
let wrapper;

const localVue = createLocalVue();
localVue.use(Vuesax);

let $vs = {
    notify: jest.fn()
}

let store = {
    userDateOfBirth: '1989-02-28'
}

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
    "businessesAdministered": []
}



beforeEach(() => {
    wrapper = shallowMount(Business, {
        propsData: {},
        mocks: {$vs, store},
        stubs: ['router-link', 'router-view'],
        methods: {},
        localVue,
    });

});

afterEach(() => {
    wrapper.destroy();
});

//TESTS TO CHECK LOGIN ERROR HANDLING
describe('Business Register error checking', () => {


    beforeEach(() => {
        wrapper.vm.user = mockUser;
        wrapper.vm.checkAge = jest.fn();
    });

    test('Handles empty Register', () => {
        const registerBtn = wrapper.find('.register-button')
        registerBtn.trigger('click');
        console.log(wrapper.vm.errors);
        expect(wrapper.vm.errors.length).toBe(3);
    });

    test('Handles only name', () => {
        wrapper.vm.businessName = 'bestBusiness';
        const registerBtn = wrapper.find('.register-button')
        registerBtn.trigger('click');
        expect(wrapper.vm.errors.length).toBe(2);
    })
    test('Handles only a type', () => {
        wrapper.vm.businessType = 'Charitable Organisation';
        const registerBtn = wrapper.find('.register-button')
        registerBtn.trigger('click');
        expect(wrapper.vm.errors.length).toBe(2);
    })

    test('Handles old enough user', () => {
        const registerBtn = wrapper.find('.register-button')
        registerBtn.trigger('click');
        expect(wrapper.vm.errors.length).toBe(3);
    })
});

describe('Business Register user age checking', () => {
    beforeEach(() => {
        const checkAgeMethod = jest.spyOn(Business.methods, 'checkAge');
        checkAgeMethod.mockResolvedValue(false);
    });

    test('Handles to young user', () => {
        const registerBtn = wrapper.find('.register-button')
        registerBtn.trigger('click');
        expect(wrapper.vm.errors.length).toBe(4);
    })
});

