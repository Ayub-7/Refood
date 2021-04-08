import { shallowMount, createLocalVue } from '@vue/test-utils';
import Vuex from 'vuex';
import Register from '../components/Register';


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

describe('Registeration error checking', () => {
   test("Handles empty params", () => {
       const registerBtn = wrapper.find('.register-button');
       registerBtn.trigger('click');
       expect(wrapper.vm.errors.length).toBe(6);
   })
});