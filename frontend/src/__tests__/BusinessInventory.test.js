import { mount, createLocalVue } from '@vue/test-utils';
import BusinessInventory from '../components/BusinessInventory';
import Vuesax from 'vuesax';

const localVue = createLocalVue();
localVue.use(Vuesax);

let wrapper;

let $route = {
    params: {
        id: 1,
    }
}

beforeEach(() => {
   wrapper = mount(BusinessInventory, {
       mocks: {$route},
       stubs: {},
       methods: {},
       localVue,
   })

   //TODO: Setup this properly since this is temp fix so it can be merged
   const getBusinessProducts = jest.spyOn(BusinessInventory.methods, "getProducts");

   getBusinessProducts.mockResolvedValue([]);

});

afterEach(() => {
    wrapper.destroy();
});

describe('Component', () => {
    test('is a Vue instance', () => {
        expect(wrapper.isVueInstance).toBeTruthy();
    });
});

describe('New sale listing modal tests', () => {
   beforeEach(async () => {
       wrapper.vm.newListingPopup = true;
       await wrapper.vm.$nextTick();
       expect(wrapper.find(".new-listing-modal")).toBeTruthy();
   });

   test("No changes to input fields - reject form", () => {
       expect(wrapper.vm.validateNewListing()).toBeFalsy();
       expect(wrapper.vm.newListingErrors.price.error).toBe(false);
       expect(wrapper.vm.newListingErrors.quantity.error).toBe(true);
       expect(wrapper.vm.newListingErrors.closes.error).toBe(true);
   });

    test("Invalid form with negative price", () => {
        wrapper.vm.price = -1.50;

        expect(wrapper.vm.validateNewListing()).toBeFalsy();
        expect(wrapper.vm.newListingErrors.price.error).toBe(true);
        expect(wrapper.vm.newListingErrors.quantity.error).toBe(true);
        expect(wrapper.vm.newListingErrors.closes.error).toBe(true);
    });

    test("Invalid form with bad quantity", () => {
        wrapper.vm.quantity = -1;
        expect(wrapper.vm.validateNewListing()).toBeFalsy();
        expect(wrapper.vm.newListingErrors.quantity.error).toBe(true);

        wrapper.vm.quantity = 0;
        expect(wrapper.vm.validateNewListing()).toBeFalsy();
        expect(wrapper.vm.newListingErrors.quantity.error).toBe(true);

        wrapper.vm.quantity = null;
        expect(wrapper.vm.validateNewListing()).toBeFalsy();
        expect(wrapper.vm.newListingErrors.quantity.error).toBe(true);
    });

    test("Invalid form with bad date", () => {
        wrapper.vm.closes = "";
        expect(wrapper.vm.validateNewListing()).toBeFalsy();
        expect(wrapper.vm.newListingErrors.closes.error).toBe(true);

        wrapper.vm.closes = "2020-05-31T14:00";
        expect(wrapper.vm.validateNewListing()).toBeFalsy();
        expect(wrapper.vm.newListingErrors.quantity.error).toBe(true);
    });

    test("Successfully creates a new form", () => {
        wrapper.vm.listingQuantity = 5;
        wrapper.vm.price = 5.5;
        wrapper.vm.closes = "2022-01-01T15:00";
        expect(wrapper.vm.validateNewListing()).toBeTruthy();
        expect(wrapper.vm.newListingErrors.quantity.error).toBe(false);
        expect(wrapper.vm.newListingErrors.closes.error).toBe(false);
        expect(wrapper.vm.newListingErrors.price.error).toBe(false);
    });

    test("New Listing closes on button click", async () => {
        let closeButton = wrapper.find("#cancel-button");
        expect(closeButton).toBeTruthy();
        await closeButton.trigger("click");

        expect(wrapper.vm.newListingPopup).toBe(false);
        expect(wrapper.vm.newListingErrors.quantity.error).toBe(false);
        expect(wrapper.vm.newListingErrors.closes.error).toBe(false);
        expect(wrapper.vm.newListingErrors.price.error).toBe(false);
    });
});