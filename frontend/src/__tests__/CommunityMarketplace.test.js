import { mount, createLocalVue } from '@vue/test-utils';
import CommunityMarketplace from '../components/CommunityMarketplace.vue';
import Vuesax from 'vuesax';
import api from "../Api";

let wrapper;
let localVue = createLocalVue();
localVue.use(Vuesax);

let cards = [
    {
        "id": 1,
        "user": {
            "id": 6077,
        },
        "title": "Beans - Green",
        "description": "Integer ac leo.",
        "created": "2021-06-01 06:32:38",
        "displayPeriodEnd": 1623738758000,
        "keywords": "aliquam augue quam",
        "section": "Wanted"
    },
    {
        "id": 5,
        "user": {
            "id": 5803,
        },
        "title": "Glycerine",
        "description": "Lorem ipsum dolor sit amet, consectetuer adipiscing elit.",
        "created": "2021-06-06 06:32:38",
        "displayPeriodEnd": 1624170758000,
        "keywords": "semper sapien a",
        "section": "Wanted"
    },
];

jest.mock("../Api.js", () => jest.fn);
api.getCardsBySection = jest.fn(() => {
    return Promise.resolve({data: cards, status: 200});
});

api.checkSession = jest.fn(() => {
    return Promise.resolve({status: 200});
});

let $vs = {
    loading: jest.fn(),
}

beforeEach(() => {
    wrapper = mount(CommunityMarketplace, {
        propsData: {},
        mocks: {$vs},
        stubs: ['router-link', 'router-view'],
        methods: {},
        localVue,
    });

    wrapper.vm.$vs.loading.close = jest.fn();
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
    beforeEach(() => {
        wrapper.vm.displaytype = false;
    });

    test('Grid view is shown at first', () => {
        const gridContainer = wrapper.find("#grid-container")
        expect(gridContainer.exists()).toBe(true);
    });

    test('Table view is shown when slider toggled', async () => {
        const displayTypeButton = wrapper.find("#display-type-button");
        displayTypeButton.trigger("click");
        await wrapper.vm.$nextTick();

        const tableContainer = wrapper.find("#tableContainer");
        expect(tableContainer.exists()).toBe(true);
    });
});

describe('Method tests', () => {
   test('Cards is successfully set.', async () => {
       await wrapper.vm.getSectionCards('forSale');
       expect(wrapper.vm.cards).toStrictEqual(cards);
   });
});