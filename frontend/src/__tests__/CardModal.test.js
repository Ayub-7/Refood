import {mount, createLocalVue } from '@vue/test-utils';
import CardModal from '../components/CardModal.vue';
import Vuesax from 'vuesax';
import api from "../Api";

let wrapper;
let localVue = createLocalVue();
localVue.use(Vuesax);
let $route = {
    params: {
        id: 1,
    }
}

let $log = {
    debug: jest.fn(),
}

let cardDetails = {
    id: 1,
    user: {
    id: 6077,
    firstName: "Goldy",
    middleName: "Henri",
        lastName: "Chadburn",
        nickname: "ability",
        bio: "Mauris sit amet eros.",
        email: "hchadburn1k@zdnet.com",
        dateOfBirth: "1987-06-10",
        phoneNumber: "+86 627 626 5485",
        homeAddress: {
            streetNumber: "0",
            streetName: "Briar Crest",
            suburb: null,
            city: "Qinhong",
            region: null,
            country: "China",
            postcode: null
        },
        created: "2019-01-27 00:39:30",
        role: "USER",
        businessesAdministered: []
    },
    title: "Beans - Green",
    description: "Integer ac leo.",
    created: "2021-06-01 18:32:38",
    displayPeriodEnd: "2021-06-15 18:32:38",
    keywords: "aliquam augue quam",
    section: "Wanted"
}

beforeEach(() => {
    wrapper = mount(CardModal, {
        propsData: {
            selectedCard: cardDetails
        },
        mocks: {$route, $log},
        stubs: {},
        methods: {},
        localVue,
    })
});

afterEach(() => {
    wrapper.destroy();
});

describe('Card modal functionality', () => {
    test('is a Vue instance', () => {
        expect(wrapper.isVueInstance).toBeTruthy();
    });

    test('Reset state correctly resets values', () => {
        //Setup
        wrapper.vm.message = "Hello"
        wrapper.vm.messaging = true;
        
        expect(wrapper.vm.message).toBe("Hello")
        expect(wrapper.vm.messaging).toBe(true);

        //Execution
        wrapper.vm.resetState()

        //Result
        expect(wrapper.vm.message).toBe("")
        expect(wrapper.vm.messaging).toBe(false);
    });

    test('Open modal correctly updates showing value', () => {
        //Setup
        wrapper.vm.showing = false;
        expect(wrapper.vm.showing).toBe(false);
        
        //Execution
        wrapper.vm.openModal()

        //Result
        expect(wrapper.vm.showing).toBe(true);
    })

    test('Open modal calls resetState', () => {


        //Setup
        wrapper.vm.resetState = jest.fn();
        wrapper.vm.showing = false;
        expect(wrapper.vm.showing).toBe(false);

        //Execution
        wrapper.vm.openModal();

        expect(wrapper.vm.resetState).toBeCalled()
    })
});


describe('Card modal UI', () => {
    test('is a Vue instance', () => {
        expect(wrapper.isVueInstance).toBeTruthy();
    });

    test('Message button opens message box', () => {
        //Setup
        wrapper.vm.showing = true;
        let button = wrapper.find(".card-modal-message-button")

        //Execution
        button.trigger("click")

        //Result
        expect(wrapper.vm.messaging).toBeTruthy();
        expect(wrapper.find("#card-modal-message")).toBeTruthy();
    });

    test('User is not the owner - successfully shows message button', () => {
        api.checkSession = jest.fn(() => {
           return Promise.resolve({status: 200, data: {id: 2}});
        });

        wrapper.vm.getUserId();

        expect(wrapper.find(".card-modal-message-button")).toBeTruthy();
    });

    test('User is card owner - successfully shows edit button', () => {
        api.checkSession = jest.fn(() => {
            return Promise.resolve({status: 200, data: {id: 1}});
        });

        wrapper.vm.getUserId();

        expect(wrapper.find(".card-modal-edit-button")).toBeTruthy();

    });

    test('User is card owner - successfully shows delete button', () => {
        api.checkSession = jest.fn(() => {
            return Promise.resolve({status: 200, data: {id: 1}});
        });

        wrapper.vm.getUserId();

        expect(wrapper.find(".card-modal-delete-button")).toBeTruthy();

    });

});
