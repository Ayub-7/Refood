import {createLocalVue, mount, shallowMount} from '@vue/test-utils';
import BusinessSalesReport from '../components/BusinessSalesReport';
import Vuesax from 'vuesax';
import api from "../Api";

const localVue = createLocalVue();
const moment = require('moment');
let wrapper;
localVue.use(Vuesax);

let store = {
    loggedInUserId: 22,
    role: "USER",
    userName: "Wileen Tisley",
    userPrimaryBusinesses: [],
    actingAsBusinessId: 1,
    actingAsBusinessName: "Dabshots"
}

let $router;
let mutations;
let sessionStorage;

let $log = {
    debug: jest.fn()
}

let salesHistory = [
    {
        "id": 1,
        "buyer": 3,
        "product": {
            "id": "W04GP5EC0B1798680",
            "business": {
                "name": "Dabshots",
                "id": 1,
                "administrators": [],
                "primaryAdministratorId": null,
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
            "name": "Compound - Mocha",
            "description": "vel ipsum praesent blandit lacinia erat vestibulum sed magna at nunc",
            "manufacturer": "Nestle",
            "recommendedRetailPrice": 88.93,
            "created": "2021-01-11 07:54:46",
            "images": [],
            "primaryImagePath": null
        },
        "sold": "2021-09-16 13:05:28",
        "listed": "2021-02-03 11:00:00",
        "likes": 0,
        "quantity": 2,
        "listingId": 5,
        "price": 5.99
    },
    {
        "id": 2,
        "buyer": 3,
        "product": {
            "id": "W04GP5EC0B1798680",
            "business": {
                "name": "Dabshots",
                "id": 1,
                "administrators": [],
                "primaryAdministratorId": null,
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
            "name": "Compound - Mocha",
            "description": "vel ipsum praesent blandit lacinia erat vestibulum sed magna at nunc",
            "manufacturer": "Nestle",
            "recommendedRetailPrice": 88.93,
            "created": "2021-01-11 07:54:46",
            "images": [],
            "primaryImagePath": null
        },
        "sold": "2021-09-16 13:05:37",
        "listed": "2021-02-02 11:00:00",
        "likes": 0,
        "quantity": 1,
        "listingId": 4,
        "price": 15.5
    }
];

describe('User acting as tests', () => {
    beforeEach(() => {
        wrapper = shallowMount(BusinessSalesReport, {
            propsData: {},
            mocks: {store, $log, $router, mutations, sessionStorage},
            stubs: ['router-link', 'router-view'],
            methods: {},
            localVue,
        });

        mutations = {
            setActingAsBusiness: jest.fn(),
            setActingAsUser: jest.fn()
        };

        sessionStorage = {
            getItem: jest.fn(),
            removeItem: jest.fn()
        };

        $router = {
            push: jest.fn()
        }

    });

    afterEach(() => {
        wrapper.destroy();
    });


    test('When getBusinessListingNotifications returns true, salesHistory is set', async () => {
        api.getBusinessSales = jest.fn(() => {
            return Promise.resolve({status: 201, data: salesHistory});
        });
        wrapper.vm.calculateReport = jest.fn();

        wrapper.vm.getSalesHistory();
        await wrapper.vm.$nextTick();

        expect(wrapper.vm.salesHistory).toBe(salesHistory);
    });

    test('When calculateSummary, business metrics are calculated correctly', async () => {
        let results = wrapper.vm.calculateSummary(salesHistory, "Title");
        expect(results).toStrictEqual(
            {"averageItemsPerSale": "1.50", "averagePricePerItem": "7.16", "averageSale": "10.75", "title": "Title", "totalItems": "3.00", "totalSaleValue": "21.49", "totalSales": 2}
        );
    });

    test('When view summary with week granularity selected, summary is displayed correctly', async () => {
        wrapper.vm.currentYearSalesHistory = salesHistory
        let startDate = moment(new Date(wrapper.vm.dateStart))
        let intervalDate = startDate.add(7, 'days');
        await wrapper.vm.$nextTick();
        wrapper.vm.granularity(intervalDate, 7, 'days')
        expect(wrapper.vm.reportGranularity).toStrictEqual(
            [{"averageItemsPerSale": "1.50", "averagePricePerItem": "7.16", "averageSale": "10.75", "title": "Sep 24", "totalItems": "3.00", "totalSaleValue": "21.49", "totalSales": 2}]
        );
    });

    test('When view summary with month granularity selected, summary is displayed correctly', async () => {
        wrapper.vm.currentYearSalesHistory = salesHistory
        let startDate = moment(new Date(wrapper.vm.dateStart))
        let intervalDate = startDate.add(1, 'months');
        await wrapper.vm.$nextTick();
        wrapper.vm.granularity(intervalDate, 1, 'months')
        expect(wrapper.vm.reportGranularity).toStrictEqual(
            [{"averageItemsPerSale": "1.50", "averagePricePerItem": "7.16", "averageSale": "10.75", "title": "September", "totalItems": "3.00", "totalSaleValue": "21.49", "totalSales": 2}]
        );
    });

    test('When view summary with year granularity selected, summary is displayed correctly', async () => {
        wrapper.vm.currentYearSalesHistory = salesHistory
        let startDate = moment(new Date(wrapper.vm.dateStart))
        let intervalDate = startDate.add(1, 'years');
        await wrapper.vm.$nextTick();
        wrapper.vm.granularity(intervalDate, 1, 'years')
        expect(wrapper.vm.reportGranularity).toStrictEqual([{"averageItemsPerSale": "1.50", "averagePricePerItem": "7.16", "averageSale": "10.75", "title": "2021", "totalItems": "3.00", "totalSaleValue": "21.49", "totalSales": 2}]
        );
    });
});
