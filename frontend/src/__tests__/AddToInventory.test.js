import { shallowMount, createLocalVue } from '@vue/test-utils';
import AddToInventory from '../components/AddToInventory';
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


const localVue = createLocalVue();
localVue.use(Vuesax);

let wrapper;

let $route = {
    params: {
        id: 1,
    }
}

let $log = {
    debug: jest.fn(),
}

beforeEach(() => {
    wrapper = shallowMount(AddToInventory, {
        mocks: {$route, $log},
        stubs: {},
        methods: {},
        localVue,
    })
    wrapper.vm.products = [{
        "id": "W04GP5EC0B1798680",
        "name": "Compound - Mocha",
        "description": "vel ipsum praesent blandit lacinia erat vestibulum sed magna at nunc",
        "manufacturer": "Nestle",
        "recommendedRetailPrice": 88.93,
        "created": "2021-01-11 20:54:46",
        "images": [],
        "primaryImagePath": null
    }];
});

afterEach(() => {
    wrapper.destroy();
});

describe('Component', () => {
    test('is a Vue instance', () => {
        expect(wrapper.isVueInstance).toBeTruthy();
    });

    test('Invalid ID error', () => {
        wrapper.vm.invenForm.prodId = '#@!%##@$%';
        wrapper.vm.checkForm();
        expect(wrapper.vm.errors.includes('invalid-chars')).toBeTruthy();
    });

    test('Invalid PricePerItem error', () => {
        wrapper.vm.invenForm.pricePerItem = 'B';
        wrapper.vm.checkForm();
        expect(wrapper.vm.errors.includes('pricePerItem')).toBeTruthy();
    });

    test('No date error', () => {
        wrapper.vm.checkForm();
        expect(wrapper.vm.errors.includes('no-dates')).toBeTruthy();
        expect(wrapper.vm.errors.includes('past-expiry')).toBeTruthy();
    });

    test('Bad price per item error', () => {
        wrapper.vm.invenForm.pricePerItem = -50;
        wrapper.vm.checkForm();
        expect(wrapper.vm.invenForm.pricePerItem).toBeTruthy();
    });

    test('Past best before date error', () => {
        wrapper.vm.invenForm.bestBefore = '01-01-2000';
        wrapper.vm.checkForm();
        expect(wrapper.vm.errors.includes('past-date')).toBeTruthy();
        expect(wrapper.vm.errors.includes('past-best')).toBeTruthy();
    });

    test('Past list expiry date error', () => {
        wrapper.vm.invenForm.listExpiry = '01-01-2000';
        wrapper.vm.checkForm();
        expect(wrapper.vm.errors.includes('past-date')).toBeTruthy();
        expect(wrapper.vm.errors.includes('past-expiry')).toBeTruthy();
    });

    test('Future manufacture date error', () => {
        wrapper.vm.invenForm.manufactureDate = '01-01-2200';
        wrapper.vm.checkForm();
        expect(wrapper.vm.errors.includes('future-date')).toBeTruthy();
    });

    test('Past sell by date error', () => {
        wrapper.vm.invenForm.sellBy = '01-01-2000';
        wrapper.vm.checkForm();
        expect(wrapper.vm.errors.includes('past-date')).toBeTruthy();
        expect(wrapper.vm.errors.includes('past-sell')).toBeTruthy();
    });

    test('Autofill', () => {
        wrapper.vm.invenForm.prodId = "W04GP5EC0B1798680";
        wrapper.vm.autofill();
        expect(wrapper.vm.invenForm.pricePerItem).toEqual(88.93);
    })
});