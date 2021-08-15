import {mount, createLocalVue } from '@vue/test-utils';
import BusinessSalesHistory from '../components/BusinessSalesHistory.vue';
import Vuesax from 'vuesax';
import api from "../Api";
import axios from "axios";

let wrapper;
let localVue = createLocalVue();
localVue.use(Vuesax);

const mockUser = {
    "id": 5,
    "homeAddress": "44 Ramsey Court",
}

jest.mock("../Api.js", () => jest.fn);
api.checkSession = jest.fn(() => {
    return Promise.resolve({data: mockUser, status: 200});
});

axios.get = jest.fn(() => {
    return Promise.resolve({data: [{
            currencies: [{symbol: "€"}],
        }]}
    );
});

beforeEach(() => {
    wrapper = mount(BusinessSalesHistory, {
       mocks: {},
       stubs: {},
       methods: {},
       localVue,
    });
})

afterEach(() => {
    wrapper.destroy();
});

describe('Method Tests', () => {
    test("Session is checked and currency is set to euros", async () => {
       await wrapper.vm.getSession();

       expect(wrapper.vm.currency).toBe("€");
    });
});
