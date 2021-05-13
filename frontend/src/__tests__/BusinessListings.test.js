import { shallowMount, createLocalVue } from '@vue/test-utils';
import BusinessListings from "../components/BusinessListings";
import Vuesax from 'vuesax';

const localVue = createLocalVue();
localVue.use(Vuesax);

let wrapper;

beforeEach(() => {
    wrapper = shallowMount(BusinessListings, {
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

describe("UI tests", () => {
   test("Table/grid view switch changes successfully", async () => {
        expect(wrapper.vm.tableView).toBeFalsy();
        wrapper.vm.tableView = true;
        await wrapper.vm.$nextTick();
        expect(wrapper.find("#table-view")).toBeTruthy()
   });
});