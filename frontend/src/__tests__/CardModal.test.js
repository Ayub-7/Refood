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

let $vs = {
    notify: jest.fn()
}

let $log = {
    debug: jest.fn(),
    error: jest.fn(),
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
        mocks: {$route, $log, $vs},
        stubs: {},
        methods: {},
        localVue,
    });

    wrapper.vm.title = "Valid Title";
    wrapper.vm.section = "ForSale";

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

    test('Open modal correctly updates showing value', async () => {
        //Setup
        wrapper.vm.showing = false;
        expect(wrapper.vm.showing).toBe(false);

        //Execution
        await wrapper.vm.openModal()

        //Result
        expect(wrapper.vm.showing).toBe(true);
    })

    test('Open modal calls resetState', async () => {


        //Setup
        wrapper.vm.resetState = jest.fn();
        wrapper.vm.showing = false;
        expect(wrapper.vm.showing).toBe(false);

        //Execution
        await wrapper.vm.openModal()

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

    test('User is not the owner - successfully shows message button', async () => {
        api.checkSession = jest.fn(() => {
           return Promise.resolve({status: 200, data: {id: 2}});
        });

        await wrapper.vm.getUserId();

        expect(wrapper.find(".card-modal-message-button")).toBeTruthy();
    });

    test('User is card owner - successfully shows edit button', async () => {
        api.checkSession = jest.fn(() => {
            return Promise.resolve({status: 200, data: {id: 1}});
        });

        await wrapper.vm.getUserId();

        expect(wrapper.find(".card-modal-edit-button")).toBeTruthy();
    });

    test('Prefills are set', async () => {
        wrapper.vm.setPrefills();

        //cardDetails
        expect(wrapper.vm.title).toBe(cardDetails.title);
        expect(wrapper.vm.description).toBe(cardDetails.description);
        expect(wrapper.vm.section).toBe(cardDetails.section);
    });
});

describe('Card editing', () => {
   test('Edited card title is too long', async () => {
      wrapper.vm.title = "OverFiftyCharactersLong".repeat(4);
      await wrapper.vm.$nextTick();

      expect(wrapper.vm.editErrors.title.error).toBeTruthy();
      expect(wrapper.vm.editErrors.title.message).toBe("Card title is too long");
   });

    test('Edited card title is too short', async () => {
        wrapper.vm.title = ""; // No longer valid.
        await wrapper.vm.$nextTick();
        expect(wrapper.vm.editErrors.title.error).toBeTruthy();
        expect(wrapper.vm.editErrors.title.message).toBe("A valid card title is required");
    });

    test('Edited card title is invalid', async () => {
        wrapper.vm.title = "  ";
        await wrapper.vm.$nextTick();
        expect(wrapper.vm.editErrors.title.error).toBeTruthy();
        expect(wrapper.vm.editErrors.title.message).toBe("A valid card title is required");
    });


    test('Edited card title is of valid length', async () => {
        wrapper.vm.title = "Valid";
        await wrapper.vm.$nextTick();
        expect(wrapper.vm.editErrors.title.error).toBeFalsy();
    });

    test('Edited card has missing section', async () => {
        wrapper.vm.section = "";
        await wrapper.vm.$nextTick();

        expect(wrapper.vm.editErrors.section.error).toBeTruthy();
    });

    test('Edited card has valid section', async () => {
        wrapper.vm.section = "Wanted";
        await wrapper.vm.$nextTick();

        expect(wrapper.vm.editErrors.section.error).toBeFalsy();
    });

    test('Card edit is invalid - bad title', async () => {
        wrapper.vm.title = "OverFiftyCharactersLong".repeat(4);
        await wrapper.vm.$nextTick();

        expect(wrapper.vm.validateCardEdit()).toBeFalsy();
    });

    test('Card edit is invalid - bad section', async () => {
        wrapper.vm.section = ""
        await wrapper.vm.$nextTick();

        expect(wrapper.vm.validateCardEdit()).toBeFalsy();
    });

    test('Card edit is valid', async () => {
        wrapper.vm.title = "New Edited Title";
        wrapper.vm.section = "Exchange";
        await wrapper.vm.$nextTick();

        expect(wrapper.vm.validateCardEdit()).toBeTruthy();
    });

    test('Card edit success saves, then user is notified', async () => {

        wrapper.vm.title = "New Edited Title";
        wrapper.vm.section = "Exchange";

        //wrapper.vm.validateCardEdit();
        wrapper.vm.saveCardEdit();

        expect(wrapper.vm.validateCardEdit()).toBeTruthy();
        expect(wrapper.vm.$vs.notify).toBeCalled();
    });

    test('Card edit success fails, then user is notified', async () => {
        wrapper.vm.validateCardEdit = jest.fn(() => {
            return false;
        });

        wrapper.vm.title = "New Edited Title";
        wrapper.vm.section = "Exchange";

        //wrapper.vm.validateCardEdit();
        wrapper.vm.saveCardEdit();

        expect(wrapper.vm.validateCardEdit()).toBeFalsy();
        expect(wrapper.vm.$vs.notify).toBeCalled();

    });

});



describe('Messaging', () => {
    test('is a Vue instance', () => {
        expect(wrapper.isVueInstance).toBeTruthy();
    });

    test('Message box shows with input', () => {
        //Setup
        wrapper.vm.showing = true;
        wrapper.vm.messaging = true;

        let button = wrapper.find(".card-modal-message-button")
        button.trigger("click")

        //Result
        expect(wrapper.vm.messaging).toBeTruthy();
        expect(wrapper.find("#card-modal-message")).toBeTruthy();
        expect(wrapper.find("#message-input")).toBeTruthy();
        expect(wrapper.find("#card-modal-message-send")).toBeTruthy();
    });


    test('Message is checked after send', () => {
        api.postMessage = jest.fn(() => {
            return Promise.resolve({status: 201, data: {id: 1}});
        });

        wrapper.vm.checkMessage = jest.fn(() => {
            return true;
        });

        wrapper.vm.message = "message desc";
        wrapper.vm.selectedCard.user.id = 1;
        wrapper.vm.selectedCard.id = 1;

        wrapper.vm.sendMessage(wrapper.vm.selectedCard, wrapper.vm.message);

        expect(wrapper.vm.checkMessage).toBeCalled();
    });

    test('User is card owner - successfully shows delete button', async () => {
        api.checkSession = jest.fn(() => {
            return Promise.resolve({status: 200, data: {id: 1}});
        });

        await wrapper.vm.getUserId();

        expect(wrapper.find(".card-modal-delete-button")).toBeTruthy();

    });

    test('After sent success, Message clears and user is notified', () => {
        api.postMessage = jest.fn(() => {
            return Promise.resolve({status: 201, data: {messageId: 1}});
        });

        wrapper.vm.sendPostMessage(7, 12, "message");

        expect(wrapper.vm.message).toBe("");
        expect(wrapper.vm.$vs.notify).toBeCalled();
    });


    test('On null recipient, User is notified and message is not cleared', () => {
        wrapper.vm.message = "message";
        let recipient = null;

        wrapper.vm.checkMessage(wrapper.vm.message, recipient);

        expect(wrapper.vm.message).toBe("message");
        expect(wrapper.vm.$vs.notify).toBeCalled();
    });

    test('On blank message, User is notified', () => {
        wrapper.vm.message = "";
        let recipient = 7;

        wrapper.vm.checkMessage(wrapper.vm.message, recipient);

        expect(wrapper.vm.$vs.notify).toBeCalled();
    });
});