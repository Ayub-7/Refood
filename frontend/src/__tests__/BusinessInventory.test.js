import { shallowMount, createLocalVue } from '@vue/test-utils';
import BusinessInventory from '../components/BusinessInventory';
import Vuesax from 'vuesax';


const mockInventory = [
    {
        "id": 2,
        "product": {
            "id": "W04GP5EC0B1798680",
            "name": "Compound - Mocha",
            "description": "vel ipsum praesent blandit lacinia erat vestibulum sed magna at nunc",
            "manufacturer": "Nestle",
            "recommendedRetailPrice": 88.93,
            "created": "2021-01-11 20:54:46",
            "images": [],
            "primaryImagePath": null
        },
        "quantity": 7,
        "pricePerItem": 3,
        "totalPrice": 80,
        "manufactured": "2020-01-27",
        "sellBy": null,
        "bestBefore": "2021-08-27",
        "expires": "2021-08-27",
        "productName": "Compound - Mocha",
        "productId": "W04GP5EC0B1798680"
    }
]

const $route = {
    params: {
        id: 1
    }
};

const localVue = createLocalVue();
localVue.use(Vuesax);

let wrapper;

let $route = {
    params: {
        id: 1,
    }
}

beforeEach(() => {
   wrapper = shallowMount(BusinessInventory, {
       mocks: {$route},
       stubs: {},
       methods: {},
       localVue,
   })

   const getBusinessInventory = jest.spyOn(BusinessInventory.methods, "getBusinessInventory");

   getBusinessInventory.mockResolvedValue(mockInventory);

   wrapper.vm.inventory = mockInventory;



});

afterEach(() => {
    wrapper.destroy();
});

describe('Component', () => {
    test('is a Vue instance', () => {
        expect(wrapper.isVueInstance).toBeTruthy();
    });
});

describe('Inventory Table', () => {
    test('Loads appropriate data', async () => {
        //TODO get this test working, or implement manual tests
        await wrapper.vm.$nextTick();
        console.log(wrapper.html())
    });
});