import { shallowMount, createLocalVue } from '@vue/test-utils';
import ModifyInventory from '../components/ModifyInventory';
import Vuesax from 'vuesax';
import api from "../Api";


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
const mockProduct = [
    {
        "id": "WATT-420-BEANS",
        "name": "Watties Baked Beans - 420g can",
        "description": "Baked Beans as they should be.",
        "manufacturer": "Heinz Wattie's Limited",
        "recommendedRetailPrice": 2.2,
        "created": "2021-07-16T02:47:09.240Z",
        "images": [
            {
                "id": 1234,
                "filename": "/media/images/23987192387509-123908794328.png",
                "thumbnailFilename": "/media/images/23987192387509-123908794328_thumbnail.png"
            }
        ]
    }
]
let mockModifiedItem = {
    "productId": "W04GP5EC0B1798680",
    "quantity": 3,
    "pricePerItem": 5.5,
    "totalPrice": 10,
    "manufactured": "2021-01-27",
    "sellBy": "2022-01-27",
    "bestBefore": "2022-01-27",
    "expires": "2022-01-27"
}

api.getBusinessProducts = jest.fn(() => {
    return Promise.resolve({data: mockProduct, status: 200});
})

api.modifyInventory = jest.fn(() => {
    return Promise.resolve({data: mockModifiedItem, status: 200});
})

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

let $vs = {
    notify: jest.fn()
}

beforeEach(() => {
    wrapper = shallowMount(ModifyInventory, {
        mocks: {$vs, $route, $log},
        stubs: {},
        methods: {},
        propsData: {'item': mockInventory[0]},
        localVue,
    })
    wrapper.vm.item = {
        "id": "W04GP5EC0B1798680",
        "name": "Compound - Mocha",
        "description": "vel ipsum praesent blandit lacinia erat vestibulum sed magna at nunc",
        "manufacturer": "Nestle",
        "recommendedRetailPrice": 88.93,
        "created": "2021-01-11 20:54:46",
        "images": [],
        "primaryImagePath": null
    };

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
        wrapper.vm.invenForm.sellBy = '';
        wrapper.vm.invenForm.listExpiry = '';
        wrapper.vm.invenForm.bestBefore = '';
        wrapper.vm.invenForm.manufactureDate = '';
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

    test('Successful check', () => {
        wrapper.vm.invenForm.prodId = 'W04GP5EC0B1798680';
        wrapper.vm.invenForm.pricePerItem = 5.5;
        wrapper.vm.invenForm.manufactureDate = "2021-01-27";
        wrapper.vm.invenForm.bestBefore = "2022-01-27";
        wrapper.vm.invenForm.listExpiry = "2022-01-27";
        wrapper.vm.invenForm.sellBy = "2022-01-27";
        wrapper.vm.invenForm.quantity = 3;
        wrapper.vm.checkForm();
        expect(wrapper.vm.errors.length).toEqual(0);
    });
});

describe("Get business products", () => {
    test("Success", async () => {
        expect(wrapper.vm.products.length).toEqual(0);
        await wrapper.vm.getBusinessProducts();
        expect(wrapper.vm.products.length).toEqual(1);
        expect(wrapper.vm.products).toEqual(mockProduct);

    })
    test("Failure", async () => {
        api.getBusinessProducts = jest.fn(() => {
            return Promise.reject();
        })
        expect(wrapper.vm.products.length).toEqual(0);
        await wrapper.vm.getBusinessProducts();
        expect(wrapper.vm.products.length).toEqual(0);
    })
});

describe("Modify inventory tests", () => {
    test("Successful modify", async () => {
        wrapper.vm.item.id = 1;
        wrapper.vm.invenForm.prodId = 'W04GP5EC0B1798680';
        wrapper.vm.invenForm.pricePerItem = 5.5;
        wrapper.vm.invenForm.totalPrice = 12;
        wrapper.vm.invenForm.manufactureDate = "2021-01-27";
        wrapper.vm.invenForm.bestBefore = "2022-01-27";
        wrapper.vm.invenForm.listExpiry = "2022-01-27";
        wrapper.vm.invenForm.sellBy = "2022-01-27";
        wrapper.vm.invenForm.quantity = 3;
        wrapper.vm.addNewInv = true;
        wrapper.vm.modifyInv = true;
        await wrapper.vm.updateInventory();
        expect(wrapper.vm.addNewInv).toBeFalsy();
        expect(wrapper.vm.modifyInv).toBeFalsy();
        expect(wrapper.vm.$vs.notify).toBeCalled();
    });

    test("Unsuccessful with bad response", async () => {
        api.modifyInventory = jest.fn(() => {
            return Promise.reject({response: {message: "Bad request", status: 400}});
        })
        wrapper.vm.addNewInv = true;
        wrapper.vm.modifyInv = true;
        await wrapper.vm.updateInventory();
        expect(wrapper.vm.addNewInv).toBeTruthy();
        expect(wrapper.vm.modifyInv).toBeTruthy();
        expect(wrapper.vm.$vs.notify).toBeCalled();
    });

    test("Unsuccessful with forbidden response", async () => {
        api.modifyInventory = jest.fn(() => {
            return Promise.reject({response: {message: "Forbidden", status: 403}});
        })
        wrapper.vm.addNewInv = true;
        wrapper.vm.modifyInv = true;
        await wrapper.vm.updateInventory();
        expect(wrapper.vm.addNewInv).toBeTruthy();
        expect(wrapper.vm.modifyInv).toBeTruthy();
        expect(wrapper.vm.$vs.notify).toBeCalled();
    });
})

describe("Autofill", () => {
    test("Autofill", () => {
        expect(wrapper.vm.invenForm.prodId).toEqual('')
        expect(wrapper.vm.invenForm.manufactureDate).toEqual('')
        expect(wrapper.vm.invenForm.sellBy).toEqual('')
        expect(wrapper.vm.invenForm.bestBefore).toEqual('')
        expect(wrapper.vm.invenForm.listExpiry).toEqual('')
        expect(wrapper.vm.invenForm.quantity).toEqual(0)
        expect(wrapper.vm.invenForm.pricePerItem).toEqual(0)
        wrapper.vm.setCurrentItem(mockModifiedItem);
        expect(wrapper.vm.invenForm.prodId).toEqual('W04GP5EC0B1798680')
        expect(wrapper.vm.invenForm.manufactureDate).toEqual('2021-01-27')
        expect(wrapper.vm.invenForm.sellBy).toEqual('2022-01-27')
        expect(wrapper.vm.invenForm.bestBefore).toEqual('2022-01-27')
        expect(wrapper.vm.invenForm.listExpiry).toEqual('2022-01-27')
        expect(wrapper.vm.invenForm.quantity).toEqual(3)
        expect(wrapper.vm.invenForm.pricePerItem).toEqual(5.5)
    })
})