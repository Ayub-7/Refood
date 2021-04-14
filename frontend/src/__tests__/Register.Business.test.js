import { shallowMount, createLocalVue } from '@vue/test-utils';
import Vuex from 'vuex';
import Business from '../components/BusinessRegister';
import api from '../Api'

let wrapper;
let store;
let actions;
let mutations;
let state;
const localVue = createLocalVue();
localVue.use(Vuex);

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
    wrapper = shallowMount(Business, {
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
describe('Business Register error checking', () => {
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
});

