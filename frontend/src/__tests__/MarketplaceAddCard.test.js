import {mount, createLocalVue } from '@vue/test-utils';
import MarketplaceAddCard from '../components/MarketplaceAddCard.vue';
import Vuesax from 'vuesax';

let wrapper;
let localVue = createLocalVue();
localVue.use(Vuesax);
let $route = {
    params: {
        id: 1,
    }
}

let $log = {
    debug: jest.fn(),
}

beforeEach(() => {
    wrapper = mount(MarketplaceAddCard, {
        mocks: {$route, $log},
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

    test('No section error', () => {
        wrapper.vm.section = null;
        wrapper.vm.title = "Weee";
        wrapper.vm.checkForm();
        expect(wrapper.vm.errors.includes('no-section')).toBeTruthy();
    });

    test('No title error', () => {
        wrapper.vm.section = "Wanted";
        wrapper.vm.title = "";
        wrapper.vm.checkForm();
        expect(wrapper.vm.errors.includes('no-title')).toBeTruthy();
    });

    test('To long title error', () => {
        wrapper.vm.section = "Wanted";
        wrapper.vm.title = "The quick brown fox jumped over the lazy orange dog";
        wrapper.vm.checkForm();
        expect(wrapper.vm.errors.includes('long-title')).toBeTruthy();
    });

    test('Valid card', () => {
       wrapper.vm.section = "Wanted";
       wrapper.vm.title = "Volkswagen Golf GTi mk5";
       expect(wrapper.vm.checkForm()).toBeTruthy();
    });

    test('Open modal', () => {
        wrapper.vm.openModal();
        expect(wrapper.vm.showing).toBeTruthy();
    });



    test('Close modal after successful submission', () => {
        wrapper.vm.section = "Wanted";
        wrapper.vm.title = "Volkswagen Golf GTi mk5";
        wrapper.vm.id = 7;
        expect(wrapper.vm.checkForm()).toBeTruthy();
        wrapper.vm.addToMarketplace();
        expect(wrapper.vm.showing).toBeFalsy();
    });
});