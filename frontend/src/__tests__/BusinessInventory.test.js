import { shallowMount, createLocalVue } from '@vue/test-utils';
import BusinessInventory from '../components/BusinessInventory';
import Vuesax from 'vuesax';

const localVue = createLocalVue();
localVue.use(Vuesax);

let wrapper;

beforeEach(() => {
   wrapper = shallowMount(BusinessInventory, {
       mocks: {},
       stubs: {},
       methods: {},
       localVue,
   })
});

afterEach(() => {
    wrapper.destroy();
});

describe('Component', () => {
    test('is a Vue instance', () => {
        expect(wrapper.isVueInstance).toBeTruthy();
    });
});