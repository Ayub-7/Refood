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

let $router = {
    push: jest.fn(),
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
}];

let listingNotifications = [
    {
        buyer: 1,
        id: 1,
        likes: 5,
        listed: "2021-08-10 12:00:00",
        listingId: 1,
        price: 20.5,
        quantity: 10,
        sold: "2021-08-17 12:00:00",
        product: {
            name: "Garlic",
            business: {
                name: "Dabshots",
                address: {
                    streetNumber: "88",
                    streetName: "Ilam Road",
                    suburb: "Ilam",
                    city: "Christchurch",
                    region: "Canterbury",
                    country: "New Zealand"
                },
            }
        }
    },
    {
        id: 2,
        status: "Liked",
        created: "2021-08-17 10:03:43",
        listing: {
            inventoryItem: {
                product: {
                    name: "Pastry",
                    business: {
                      id: 1
                    },
                }
            }
        }
    }
];

api.getMessages = jest.fn().mockResolvedValue({data: oneMessage});

api.deleteMessage = jest.fn(() => {
    return Promise.resolve({data: oneMessage[0].id, status: 200});
});

api.getListingNotifications = jest.fn( () => {
   return Promise.resolve({status: 200, data: listingNotifications});
});

beforeEach(() => {
    wrapper = mount(HomepageMessages, {
        mocks: {$route, $log, $vs, $router},
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
});

describe('Message validation', () => {
    test('When message is valid, check passes', () => {
        wrapper.vm.message = 'Simple correct message';

        let res = wrapper.vm.checkMessage();

        expect(res).toBeTruthy();
        expect(wrapper.vm.errors).toStrictEqual([]);
    });

    test('When message is blank, check fails', () => {
        wrapper.vm.message = '';

        let res = wrapper.vm.checkMessage();

        expect(res).toBeFalsy();
        expect(wrapper.vm.errors.includes('bad-content')).toBeTruthy();
    });

    test('When message is over character limit, check fails', () => {
        wrapper.vm.message = ' Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris condimentum metus mauris, ut vehicula lacus sollicitudin vel. Etiam consectetur maximus vulputate. Etiam non laoreet velit, sed consequat lectus. Ut rhoncus suscipit urna sed maximus. Vestibulum volutpat iaculis lorem ac faucibus. Quisque ultrices nisi et augue consectetur, maximus fringilla neque aliquam. Cras et felis vitae justo iaculis egestas eu eu nisl. Integer pellentesque arcu eget erat finibus dapibus. Cras eleifend ante eget suscipit vestibulum. Sed nunc nisi, hendrerit id sodales nec, varius fermentum turpis. Suspendisse dictum mollis est, sit amet dignissim elit vehicula nec. Nullam eget dui ac augue laoreet ultrices. Cras laoreet rhoncus odio. ';

        let res = wrapper.vm.checkMessage();

        expect(res).toBeFalsy();
        expect(wrapper.vm.errors.includes('bad-content')).toBeTruthy();
    });
});

describe('Listing notification methods tests', () => {
   test('City and country address string is returned', () => {
     let address = {city: "Christchurch", country: "New Zealand"};

     expect(wrapper.vm.createAddressString(address)).toBe("Christchurch, New Zealand");
   });

    test('Full address string is returned', () => {
        let address = {
            streetNumber: "88",
            streetName: "Ilam Road",
            suburb: "Ilam",
            city: "Christchurch",
            region: "Canterbury",
            country: "New Zealand"
        };

        expect(wrapper.vm.createAddressString(address)).toBe("88 Ilam Road, Ilam, Christchurch, Canterbury, New Zealand");
    });
});

describe('Listing notification functionality tests', () => {
    test("Bought listings are shown", () => {
        expect(wrapper.find(".bought-listing-notification-card")).toBeTruthy();
    });

    test("Liked listings notifications are shown", () => {
        expect(wrapper.find(".liked-listing-notification")).toBeTruthy();
    });

    test("Liked listing view button redirects to listing page on click", async () => {
        await wrapper.vm.$nextTick();
        let button = wrapper.find("#view-listing-button");
        expect(button).toBeTruthy();

        await button.trigger('click');
        expect(wrapper.vm.$router.push).toBeCalled();
    });
});
