import { shallowMount, createLocalVue } from '@vue/test-utils';
import Business from '../components/BusinessRegister';
import Vuesax from 'vuesax';
//import ActingAs from "@/components/ActingAs";
let wrapper;

const localVue = createLocalVue();
localVue.use(Vuesax);

let $vs = {
    notify: jest.fn()
}

let store = {
    userDateOfBirth: '1989-02-28'
}
const checkAgeMethod = jest.spyOn(Business.methods, 'checkAge');
beforeEach(() => {
    wrapper = shallowMount(Business, {
        propsData: {},
        mocks: {$vs, store},
        stubs: {},
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
        checkAgeMethod.mockImplementation(() => {
            return true;
        });
    });

    test('Handles empty Register', () => {
        const registerBtn = wrapper.find('.register-button')
        registerBtn.trigger('click');
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
        checkAgeMethod.mockImplementation(() => {
            return false;
        });
    });

    test('Handles to young user', () => {
        const registerBtn = wrapper.find('.register-button')
        registerBtn.trigger('click');
        expect(wrapper.vm.errors.length).toBe(4);
    })
});

