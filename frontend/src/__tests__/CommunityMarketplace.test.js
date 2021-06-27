import { mount, createLocalVue } from '@vue/test-utils';
import CommunityMarketplace from '../components/CommunityMarketplace.vue';
import Vuesax from 'vuesax';

let wrapper;
let localVue = createLocalVue();
localVue.use(Vuesax);

beforeEach(() => {
    wrapper = mount(CommunityMarketplace, {
        propsData: {},
        mocks: {},
        stubs: ['router-link', 'router-view'],
        methods: {},
        // data () {
        //     return {
        //         displaytype: true,
        //     }
        // },
        localVue,
    });

    const getSession = jest.spyOn(CommunityMarketplace.methods, 'getSession');
    getSession.mockResolvedValue(() => {
        wrapper.vm.userSession.id = 2;
    });

});

afterEach(() => {
    wrapper.destroy();
});


describe('Component', () => {
    test('is a Vue instance', () => {
        expect(wrapper.isVueInstance).toBeTruthy();
    });
});


describe('CommunityMarketplace toggle tests', () => {
    test('Grid view is shown at first', () => {
        const gridContainer = wrapper.find("#grid-container")
        expect(gridContainer.exists()).toBe(true);
    });
});

describe('CommunityMarketplace toggle tests', () => {
    beforeEach(() => {

        wrapper.vm.displaytype = false;
    });

    test('Table view is shown when slider toggled', () => {
        const tableContainer = wrapper.find("#tableContainer")
        expect(tableContainer.exists()).toBe(true);
    });
});