import {mount, createLocalVue } from '@vue/test-utils';
import HomepageMessages from '../components/HomePageMessages.vue';
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

let $vs = {
    notify: jest.fn()
}

let oneMessage = [{
    "id": 3,
    "sender": {
        "id": 8186,
        "firstName": "Tricia",
        "middleName": "Xenos",
        "lastName": "Vergo",
        "nickname": "fault-tolerant",
        "bio": "In eleifend quam a odio.",
        "email": "xvergo4l@vkontakte.ru",
        "dateOfBirth": "1977-11-24",
        "phoneNumber": "+31 637 443 9610",
        "homeAddress": {
            "streetNumber": null,
            "streetName": null,
            "suburb": null,
            "city": null,
            "region": null,
            "country": "Poland",
            "postcode": null
        },
        "created": "2020-03-05 04:37:27",
        "role": "USER",
        "businessesAdministered": null
    },
    "card": {
        "id": 6134,
        "user": 8186,
        "title": "7up Diet, 355 Ml",
        "description": "Ut at dolor quis odio consequat varius.",
        "created": "2021-07-13 01:01:38",
        "displayPeriodEnd": "2021-08-03 01:01:38",
        "keywords": "sapien iaculis",
        "section": "ForSale"
    },
    "receiver": {
        "id": 83,
        "firstName": "Allegra",
        "middleName": "Jaquith",
        "lastName": "King",
        "nickname": "Visionary",
        "bio": "In eleifend quam a odio. In hac habitasse platea dictumst.",
        "email": "jking1q@printfriendly.com",
        "dateOfBirth": "1983-07-29",
        "phoneNumber": "+380 600 119 0770",
        "homeAddress": {
            "streetNumber": "33005",
            "streetName": "Cascade",
            "suburb": null,
            "city": "Xingou",
            "region": null,
            "country": "China",
            "postcode": null
        },
        "created": "2020-05-30 04:08:45",
        "role": "USER",
        "businessesAdministered": null
    },
    "description": "asdsaddas",
    "sent": "2021-07-23 13:09:52"
}]


api.getMessages = jest.fn().mockResolvedValue({data: oneMessage});

api.deleteMessage = jest.fn(() => {
    return Promise.resolve({data: oneMessage[0].id, status: 200});
});

beforeEach(() => {
    wrapper = mount(HomepageMessages, {
        mocks: {$route, $log, $vs},
        stubs: {},
        methods: {},
        localVue,
    })
    wrapper.vm.messages = oneMessage;
});

afterEach(() => {
    wrapper.destroy();
});

describe('Homepage Messages functionality', () => {
    test('is a Vue instance', () => {
        expect(wrapper.isVueInstance).toBeTruthy();
    });

    test('Message is shown', () => {
        expect(wrapper.find("#message-notification-card")).toBeTruthy()
    });

    test('Open modal correctly updates showing value', () => {
        //Setup
        wrapper.vm.showing = false;
        expect(wrapper.vm.detailedView).toBe(false);

        //Execution
        wrapper.vm.openDetailedModal(oneMessage[0])

        //Result
        expect(wrapper.vm.detailedView).toBe(true);
    })

    // test('Delete message, actually deletes it', async () => {
    //     await wrapper.vm.deleteMessage(3);
    //     expect(wrapper.vm.$vs.notify()).toBeCalled();
    // });
});


// describe('Detailed message UI', () => {
//
//     test('Message button opens message box', () => {
//         //Setup
//         //wrapper.vm.showing = true;
//         wrapper.vm.currentMessage = oneMessage[0];
//         wrapper.vm.detailedView = true;
//         wrapper.vm.showing = true;
//
//         let button2 = wrapper.find(".card-modal-message-button")
//
//         //Execution
//         button2.trigger("click")
//
//         //Result
//         expect(wrapper.vm.messaging).toBeTruthy();
//         expect(wrapper.find("#card-modal-message")).toBeTruthy();
//     });
//
//
// });