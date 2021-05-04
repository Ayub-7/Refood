import { shallowMount, createLocalVue } from '@vue/test-utils';
import ModifyCatalogue from '../components/ModifyCatalog';
import Vuesax from 'vuesax';
import {store} from "../store";

//import {CurrencyInput} from "../components/CurrencyInput";
//import api from "../Api";

let wrapper;

const localVue = createLocalVue();
localVue.use(Vuesax);


// let $vs = {
//     notify: jest.fn()
// }

store.loggedInUserId = 5;

//Mock user
const mockUser = {
    "id": 5,
    "firstName": "Rayna",
    "middleName": "YEP",
    "lastName": "Dalgety",
    "nickname": "Universal",
    "bio": "zero tolerance task-force",
    "email": "rdalgety3@ocn.ne.jp",
    "dateOfBirth": "1999-02-28",
    "phoneNumber": "+7 684 622 5902",
    "homeAddress": "44 Ramsey Court",
}

let $log = {
    debug: jest.fn(),
    error: jest.fn()
}

const getUserMethod = jest.spyOn(ModifyCatalogue.methods, 'getUserInfo');
beforeEach(() => {
    wrapper = shallowMount(ModifyCatalogue, {
        propsData: {},
        mocks: {store, $log},
        stubs: ['router-link', 'router-view'],
        methods: {},
        // components: CurrencyInput,
        localVue,
    });

    getUserMethod.mockImplementation(() =>{
        wrapper.vm.user = mockUser;
        wrapper.vm.currencyCode = "NZD";
        wrapper.vm.currencySymbol = "$"
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

//TESTS TO CHECK LOGIN ERROR HANDLING
describe('Modify Catalogue form error checking', () => {


    beforeEach(() => {
        // const createItemMethod = jest.spyOn(AddToCatalogue.methods, 'createItem');
        // createItemMethod.mockResolvedValue(mockUsers);
        //wrapper.vm.checkForm = jest.fn();
    });

    test('Handles empty modification', () => {
        const addBtn = wrapper.find('.add-button');
        addBtn.trigger('click');
        expect(wrapper.vm.errors.length).toBe(2);
    });

    test('Handles no product name modification', () => {
        wrapper.vm.productName = "";
        wrapper.vm.productId = "BB";
        wrapper.vm.description = "Good product";
        wrapper.vm.rrp = 22;

        const addBtn = wrapper.find('.add-button')
        addBtn.trigger('click');
        expect(wrapper.vm.errors.length).toBe(1);
    });

    test('Handles no product id modification', () => {
        wrapper.vm. productName = "Big Tyre";
        wrapper.vm.productId = "";
        wrapper.vm.description = "Good product";
        wrapper.vm.rrp = 22;

        const addBtn = wrapper.find('.add-button')
        addBtn.trigger('click');
        expect(wrapper.vm.errors.length).toBe(1);
    });

    test('Handles no rrp modification', () => {
        wrapper.vm. productName = "Big Tyre";
        wrapper.vm.productId = "BB";
        wrapper.vm.description = "Good product";
        wrapper.vm.rrp = null;

        const addBtn = wrapper.find('.add-button')
        addBtn.trigger('click');
        expect(wrapper.vm.errors.length).toBe(0);
    });

    test('Handles negative rrp modification', () => {
        wrapper.vm. productName = "Big Tyre";
        wrapper.vm.productId = "BB";
        wrapper.vm.description = "Good product";
        wrapper.vm.rrp = -2;

        const addBtn = wrapper.find('.add-button')
        addBtn.trigger('click');
        expect(wrapper.vm.errors.length).toBe(1);
    });

});

