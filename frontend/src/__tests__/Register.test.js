import { shallowMount, createLocalVue } from '@vue/test-utils';
import Vuex from 'vuex';
import Vuesax from 'vuesax';
import Register from '../components/Register';
import api from '../Api'

let wrapper;
let store;
let actions;
let mutations;
let state;
const localVue = createLocalVue();
localVue.use(Vuex);
localVue.use(Vuesax);


beforeEach(() => {
    actions = {
        someAction: jest.fn()
    };
    mutations = {
        someMutation: jest.fn()
    };
    state = {
        key: {}
    };
    store = new Vuex.Store({
        actions,
        mutations,
        state,
    });
    wrapper = shallowMount(Register, {
        propsData: {},
        mocks: {},
        stubs: {},
        methods: {},
        store,
        localVue,
    });
});

afterEach(() => {
    wrapper.destroy();
});

//TESTS TO CHECK LOGIN ERROR HANDLING
describe('Register error checking', () => {
    test('Handles empty register', () => {
        wrapper.vm.firstname = '';
        wrapper.vm.lastname = '';
        wrapper.vm.middlename = '';
        wrapper.vm.nickname = '';
        wrapper.vm.bio = '';
        wrapper.vm.email = '';
        wrapper.vm.password = '';
        wrapper.vm.confirm_password = '';
        wrapper.vm.dateofbirth = '';
        wrapper.vm.phonenumber = '';
        wrapper.vm.country = '';
        const registerBtn = wrapper.find('.register-button')
        registerBtn.trigger('click');
        expect(wrapper.vm.errors.length).toBe(7);
    });

    test('Handles only email', () => {
        wrapper.vm.email = 'test@email.com';
        wrapper.vm.firstname = '';
        wrapper.vm.lastname = '';
        wrapper.vm.middlename = '';
        wrapper.vm.nickname = '';
        wrapper.vm.bio = '';
        wrapper.vm.password = '';
        wrapper.vm.confirm_password = '';
        wrapper.vm.dateofbirth = '';
        wrapper.vm.phonenumber = '';
        wrapper.vm.country = '';
        const registerBtn = wrapper.find('.register-button')
        registerBtn.trigger('click');
        expect(wrapper.vm.errors.length).toBe(6);
    })
    test('Handles only password', () => {
        wrapper.vm.password = 'Potato123!';
        wrapper.vm.confirm_password = 'Potato123!';
        wrapper.vm.email = '';
        wrapper.vm.firstname = '';
        wrapper.vm.lastname = '';
        wrapper.vm.middlename = '';
        wrapper.vm.nickname = '';
        wrapper.vm.bio = '';
        wrapper.vm.dateofbirth = '';
        wrapper.vm.phonenumber = '';
        wrapper.vm.country = '';
        const registerBtn = wrapper.find('.register-button')
        registerBtn.trigger('click');
        expect(wrapper.vm.errors.length).toBe(5);
    })

    test('Handles bad email', () => {
        wrapper.vm.password = 'Potato123!';
        wrapper.vm.confirm_password = 'Potato123!';
        wrapper.vm.firstname = 'bob';
        wrapper.vm.lastname = 'steve';
        wrapper.vm.dateofbirth = '15/09/145';
        wrapper.vm.country = 'New Zealand';
        wrapper.vm.email = 'thisisnotaemail.com'
        wrapper.vm.middlename = '';
        wrapper.vm.nickname = '';
        wrapper.vm.bio = '';
        wrapper.vm.phonenumber = '027254871';
        const registerBtn = wrapper.find('.register-button')
        registerBtn.trigger('click');
        expect(wrapper.vm.errors).toStrictEqual(['thisisnotaemail.com'])
    })

});

