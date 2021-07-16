import { mount, createLocalVue } from '@vue/test-utils';
import CommunityMarketplace from '../components/CommunityMarketplace.vue';
import Vuesax from 'vuesax';
import api from "../Api";

let wrapper;
let localVue = createLocalVue();
localVue.use(Vuesax);


// will get jest specific errors if there is no full user object
let cards = [
    {
        "id": 1,
        "user":{"id":7848,"firstName":"Alyson","middleName":"Lexis","lastName":"Sealy","nickname":"analyzing","bio":"Duis mattis egestas metus. Aenean fermentum.","email":"lsealymz@tumblr.com","dateOfBirth":"1989-11-28","phoneNumber":"+93 680 161 9001","homeAddress":{"streetNumber":null,"streetName":null,"suburb":null,"city":null,"region":null,"country":"Philippines","postcode":null},"created":"2020-04-23 12:22:09","role":"USER","businessesAdministered":[]},
        "title": "Beans - Green",
        "description": "Integer ac leo.",
        "created": "2021-06-01 06:32:38",
        "displayPeriodEnd": 1623738758000,
        "keywords": "aliquam augue quam",
        "section": "Wanted"
    },
    {
        "id": 5,
        "user":{
            "id":6151,
            "firstName":"Ashley",
            "middleName":"Lorraine",
            "lastName":"Losemann",
            "nickname":"leverage",
            "bio":"Nunc nisl. Duis bibendum, felis sed interdum venenatis, turpis enim blandit mi, in porttitor pede justo eu massa.",
            "email":"llosemann3m@businessweek.com",
            "dateOfBirth":"1984-10-23",
            "phoneNumber":"+63 225 255 8996",
            "homeAddress":{
                "streetNumber":"599",
                "streetName":"Quincy",
                "suburb":null,
                "city":"Khotiv",
                "region":null,
                "country":"Ukraine",
                "postcode":null},
            "created":"2019-03-30 07:39:01",
            "role":"USER",
            "businessesAdministered":[]},
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