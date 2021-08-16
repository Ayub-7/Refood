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

const mockSale =
{
    "id": 1,
    "user": {
        "id": 1,
        "firstName": "Wilma",
        "middleName": "Janet",
        "lastName": "Sails",
        "nickname": "Open-architected",
        "bio": "Profit-focused scalable moratorium",
        "email": "jsails0@go.com",
        "dateOfBirth": "1989-02-28",
        "phoneNumber": "+57 242 190 0153",
        "homeAddress": {
            "streetNumber": "44",
            "streetName": "Menomonie Way",
            "suburb": null,
            "city": "Zhashkiv",
            "region": null,
            "country": "Ukraine",
            "postcode": null
        },
        "created": "2020-08-06 23:35:52",
        "role": "USER",
        "businessesAdministered": [
            {
                "name": "Dabshots",
                "id": 1,
                "administrators": [
                    1
                ],
                "primaryAdministratorId": 1,
                "description": "Nullam varius. Nulla facilisi. Cras non velit nec nisi vulputate nonummy.",
                "address": {
                    "streetNumber": "0",
                    "streetName": "Vernon Place",
                    "suburb": null,
                    "city": "Sarpang",
                    "region": null,
                    "country": "Bhutan",
                    "postcode": null
                },
                "businessType": "Charitable organisation",
                "created": "2020-05-18 09:06:11"
            },
            {
                "name": "Layo",
                "id": 2,
                "administrators": [
                    {
                        "id": 2,
                        "firstName": "Karrie",
                        "middleName": "Salvatore",
                        "lastName": "Loyley",
                        "nickname": "local area network",
                        "bio": "Quality-focused next generation synergy",
                        "email": "sloyley1@wordpress.com",
                        "dateOfBirth": "1995-11-06",
                        "phoneNumber": "+48 864 927 4819",
                        "homeAddress": {
                            "streetNumber": "8120",
                            "streetName": "GroverJunction",
                            "suburb": null,
                            "city": "Cimarelang",
                            "region": null,
                            "country": "Indonesia",
                            "postcode": null
                        },
                        "created": "2020-05-01 01:25:12",
                        "role": "USER",
                        "businessesAdministered": [
                            "Layo"
                        ]
                    },
                    {
                        "id": 9,
                        "firstName": "Ruthe",
                        "middleName": "Ogdan",
                        "lastName": "Ruit",
                        "nickname": "Open-architected",
                        "bio": "Configurable coherent capacity",
                        "email": "oruit8@reddit.com",
                        "dateOfBirth": "1995-05-10",
                        "phoneNumber": "+62 283 517 0351",
                        "homeAddress": {
                            "streetNumber": "9885",
                            "streetName": "Oak Parkway",
                            "suburb": null,
                            "city": "Ambato",
                            "region": null,
                            "country": "Ecuador",
                            "postcode": null
                        },
                        "created": "2020-09-24 18:01:34",
                        "role": "USER",
                        "businessesAdministered": [
                            {
                                "name": "Grain n Simple",
                                "id": 5,
                                "administrators": [
                                    {
                                        "id": 5,
                                        "firstName": "Shermy",
                                        "middleName": "Pearle",
                                        "lastName": "Layborn",
                                        "nickname": "artificial intelligence",
                                        "bio": "Intuitive client-server standardization",
                                        "email": "playborn4@amazon.com",
                                        "dateOfBirth": "2000-12-30",
                                        "phoneNumber": "+62 527 277 7359",
                                        "homeAddress": {
                                            "streetNumber": "5",
                                            "streetName": "Hagan Avenue",
                                            "suburb": null,
                                            "city": "Chengdong",
                                            "region": null,
                                            "country": "China",
                                            "postcode": null
                                        },
                                        "created": "2020-10-17 00:47:11",
                                        "role": "USER",
                                        "businessesAdministered": [
                                            "Grain n Simple"
                                        ]
                                    },
                                    9,
                                    {
                                        "id": 8,
                                        "firstName": "Elysee",
                                        "middleName": "Maurene",
                                        "lastName": "Took",
                                        "nickname": "benchmark",
                                        "bio": "Team-oriented interactive installation",
                                        "email": "mtook7@chron.com",
                                        "dateOfBirth": "1985-11-09",
                                        "phoneNumber": "+234 186 824 2303",
                                        "homeAddress": {
                                            "streetNumber": "5",
                                            "streetName": "Utah Terrace",
                                            "suburb": null,
                                            "city": "Huangfang",
                                            "region": null,
                                            "country": "China",
                                            "postcode": null
                                        },
                                        "created": "2020-10-31 22:39:42",
                                        "role": "USER",
                                        "businessesAdministered": [
                                            "Grain n Simple"
                                        ]
                                    }
                                ],
                                "primaryAdministratorId": 5,
                                "description": "Praesent blandit. Nam nulla.",
                                "address": {
                                    "streetNumber": "302",
                                    "streetName": "Forest Run Place",
                                    "suburb": null,
                                    "city": "Putinci",
                                    "region": null,
                                    "country": "Serbia",
                                    "postcode": null
                                },
                                "businessType": "Retail Trade",
                                "created": "2020-05-22 20:21:22"
                            },
                            "Layo"
                        ]
                    },
                    1
                ],
                "primaryAdministratorId": 2,
                "description": "Nam ultrices, libero non mattis pulvinar, nulla pede ullamcorper augue, a suscipit nulla elit ac nulla. Sed vel enim sit amet nunc viverra dapibus. Nulla suscipit ligula in lacus.",
                "address": {
                    "streetNumber": "95403",
                    "streetName": "Hanover Park",
                    "suburb": null,
                    "city": "El Guapinol",
                    "region": null,
                    "country": "Honduras",
                    "postcode": null
                },
                "businessType": "Accommodation and Food Services",
                "created": "2020-08-25 10:22:19"
            },
            {
                "name": "Skinder",
                "id": 3,
                "administrators": [
                    {
                        "id": 3,
                        "firstName": "Felice",
                        "middleName": "Tabbitha",
                        "lastName": "Jaeggi",
                        "nickname": "intranet",
                        "bio": "Managed foreground budgetary management",
                        "email": "tjaeggi2@independent.co.uk",
                        "dateOfBirth": "1976-12-06",
                        "phoneNumber": "+1 659 270 1003",
                        "homeAddress": {
                            "streetNumber": "5305",
                            "streetName": "Melrose Drive",
                            "suburb": null,
                            "city": "Sidon",
                            "region": null,
                            "country": "Lebanon",
                            "postcode": null
                        },
                        "created": "2020-12-24 17:40:59",
                        "role": "USER",
                        "businessesAdministered": [
                            "Skinder"
                        ]
                    },
                    {
                        "id": 7,
                        "firstName": "Papageno",
                        "middleName": "Batholomew",
                        "lastName": "Dolton",
                        "nickname": "Persevering",
                        "bio": "Advanced bi-directional flexibility",
                        "email": "bdolton6@liveinternet.ru",
                        "dateOfBirth": "2000-07-12",
                        "phoneNumber": "+380 428 944 6622",
                        "homeAddress": {
                            "streetNumber": "01801",
                            "streetName": "Grasskamp Lane",
                            "suburb": null,
                            "city": "Malhou",
                            "region": "Santarém",
                            "country": "Portugal",
                            "postcode": "2380-506"
                        },
                        "created": "2020-07-21 03:54:32",
                        "role": "USER",
                        "businessesAdministered": [
                            "Skinder"
                        ]
                    },
                    1
                ],
                "primaryAdministratorId": 3,
                "description": "Morbi sem mauris, laoreet ut, rhoncus aliquet, pulvinar sed, nisl. Nunc rhoncus dui vel sem.",
                "address": {
                    "streetNumber": "6794",
                    "streetName": "Jackson Way",
                    "suburb": null,
                    "city": "Xialaba",
                    "region": null,
                    "country": "China",
                    "postcode": null
                },
                "businessType": "Retail Trade",
                "created": "2020-09-11 08:50:50"
            }
        ]
    },
    "listing": {
        "id": 13,
        "inventoryItem": {
            "id": 304,
            "product": {
                "id": "1FTKR1AD5BP367098",
                "business": {
                    "name": "Bubbletube",
                    "id": 183,
                    "administrators": [],
                    "primaryAdministratorId": null,
                    "description": null,
                    "address": {
                        "streetNumber": "2275",
                        "streetName": "Butterfield",
                        "suburb": null,
                        "city": "Iwata",
                        "region": null,
                        "country": "Japan",
                        "postcode": "925-0145"
                    },
                    "businessType": "Accommodation and Food Services",
                    "created": "2020-04-03 11:11:46"
                },
                "name": "Wine - Chateauneuf Du Pape",
                "description": "Suspendisse potenti.",
                "manufacturer": "Amet PC",
                "recommendedRetailPrice": 18.95,
                "created": "2021-05-07 21:57:30",
                "images": [],
                "primaryImagePath": null
            },
            "quantity": 73,
            "pricePerItem": 20.33,
            "totalPrice": 1484.09,
            "manufactured": "2021-05-21",
            "sellBy": "2020-12-13",
            "bestBefore": "2020-12-27",
            "expires": "2021-08-01"
        },
        "quantity": 29,
        "price": 589.6,
        "moreInfo": "Maecenas tincidunt lacus at velit.",
        "created": "2021-04-13 07:59:18",
        "closes": "2021-08-01 17:20:11",
        "likes": 0
    },
    "status": "Bought",
    "created": "2021-08-16 05:41:52"
}

let $route = {
    params: {
        businessId: 1,
        listingId: 1,
    }
}

jest.mock("../Api.js", () => jest.fn);
api.checkSession = jest.fn(() => {
    return Promise.resolve({data: mockUser, status: 200});
});

api.getSales = jest.fn(() => {
    return Promise.resolve({data: mockUser, status: 200}).catch({response: {message: "Bad request", status: 400}});
});

axios.get = jest.fn(() => {
    return Promise.resolve({data: [{
            currencies: [{symbol: "€"}],
        }]}
    );
});

beforeEach(() => {
    wrapper = mount(BusinessSalesHistory, {
       mocks: {$route},
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
