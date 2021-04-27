import { shallowMount, createLocalVue } from '@vue/test-utils';
import Vuex from 'vuex';
import Login from '../components/Login';
import Vuesax from 'vuesax';

let wrapper;
let store;
let actions;
let mutations;
let state;
const localVue = createLocalVue();
localVue.use(Vuex);
localVue.use(Vuesax);

const $router = {
    push: jest.fn()
}

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
    wrapper = shallowMount(Login, {
        propsData: {},
        mocks: {$router},
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
describe('Login error checking', () => {
    test('Handles empty login', () => {
        const loginBtn = wrapper.find('.loginButton')
        loginBtn.trigger('click');
        expect(wrapper.vm.errors.email).toBeTruthy();
        expect(wrapper.vm.errors.password).toBeTruthy();
        expect(wrapper.vm.errors.hasErrors).toBe(true);
    });
    
    test('Handles only email', () => {
        wrapper.vm.email = 'test@email.com';
        const loginBtn = wrapper.find('.loginButton')
        loginBtn.trigger('click');
        expect(wrapper.vm.errors.email).toBeFalsy();
        expect(wrapper.vm.errors.password).toBeTruthy();
        expect(wrapper.vm.errors.hasErrors).toBe(true);
    })
    test('Handles only password', () => {
        wrapper.vm.password = 'test';
        const loginBtn = wrapper.find('.loginButton')
        loginBtn.trigger('click');
        expect(wrapper.vm.errors.email).toBeTruthy();
        expect(wrapper.vm.errors.password).toBeFalsy();
        expect(wrapper.vm.errors.hasErrors).toBe(true);
    })

    test('Handles bad email', () => {
        wrapper.vm.password = 'test';
        wrapper.vm.email = 'thisisnotaemail.com'
        const loginBtn = wrapper.find('.loginButton')
        loginBtn.trigger('click');
        expect(wrapper.vm.errors.email).toBeTruthy();
        expect(wrapper.vm.errors.password).toBeFalsy();
        expect(wrapper.vm.errors.hasErrors).toBe(true);
    })
  
});

