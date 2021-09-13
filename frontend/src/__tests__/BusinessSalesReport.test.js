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
        "business": "Skinder",
        "boughtListing": {
            "id": 1,
            "buyer": 1,
            "sold": "2021-09-06 14:04:32",
            "listed": "2021-04-28 17:31:58",
            "likes": 0,
            "quantity": 35,
            "listingId": 7042,
            "price": 2131.8
        },
        "listing": null,
        "status": "Bought",
        "viewStatus": "Unread",
        "created": "2021-09-06 14:04:38"
    },
    {
        "id": 2,
        "user": 1,
        "business": "Skinder",
        "boughtListing": {
            "id": 2,
            "buyer": 1,
            "sold": "2021-09-06 14:04:38",
            "listed": "2021-03-24 09:57:11",
            "likes": 0,
            "quantity": 1,
            "listingId": 5797,
            "price": 20.4
        },
        "listing": null,
        "status": "Bought",
        "viewStatus": "Unread",
        "created": "2021-09-06 14:04:38"
    },
    {
        "id": 3,
        "user": 1,
        "business": "Skinder",
        "boughtListing": {
            "id": 3,
            "buyer": 1,
            "sold": "2021-09-13 14:04:42",
            "listed": "2021-05-08 22:29:19",
            "likes": 0,
            "quantity": 5,
            "listingId": 2976,
            "price": 146.6
        },
        "listing": null,
        "status": "Bought",
        "viewStatus": "Unread",
        "created": "2021-09-13 14:04:42"
    },
]
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
        api.getBusinessListingNotifications = jest.fn(() => {
            return Promise.resolve({status: 201, data: salesHistory});
        });
        wrapper.vm.calculateReport = jest.fn();

        wrapper.vm.getSalesHistory();
        await wrapper.vm.$nextTick();

        expect(wrapper.vm.salesHistory).toBe(salesHistory);
    });

    test('When calculateSummary, business metrics are calculated correctly', async () => {
        let correctSummaryResult = {"averageItemsPerSale": "13.67", "averagePricePerItem": "56.07", "averageSale": "766.27", "title": "Title", "totalItems": "41.00", "totalSaleValue": "2298.80", "totalSales": 3};
        let results = wrapper.vm.calculateSummary(salesHistory, "Title");
        expect(results).toStrictEqual(correctSummaryResult);
    });

    test('When view summary with week granularity selected, summary is displayed correctly', async () => {
       wrapper.vm.currentYearSalesHistory = salesHistory
       let startDate = moment(new Date(wrapper.vm.dateStart))
       let intervalDate = startDate.add(7, 'days');
       let correctSummaryGranularity = [{"title":"Sep 03","averageSale":"1076.10","averagePricePerItem":"59.78","averageItemsPerSale":"18.00","totalSaleValue":"2152.20","totalItems":"36.00","totalSales":2},
           {"title":"Sep 10","averageSale":"146.60","averagePricePerItem":"29.32","averageItemsPerSale":"5.00","totalSaleValue":"146.60","totalItems":"5.00","totalSales":1}]
        await wrapper.vm.$nextTick();
        wrapper.vm.granularity(intervalDate, 7, 'days')
        expect(wrapper.vm.reportGranularity).toStrictEqual(correctSummaryGranularity);
    });

    test('When view summary with month granularity selected, summary is displayed correctly', async () => {
        wrapper.vm.currentYearSalesHistory = salesHistory
        let startDate = moment(new Date(wrapper.vm.dateStart))
        let intervalDate = startDate.add(1, 'months');
        let correctSummaryGranularity = [{"title":"September","averageSale":"766.27","averagePricePerItem":"56.07","averageItemsPerSale":"13.67",
            "totalSaleValue":"2298.80","totalItems":"41.00","totalSales":3}]
        await wrapper.vm.$nextTick();
        wrapper.vm.granularity(intervalDate, 1, 'months')
        expect(wrapper.vm.reportGranularity).toStrictEqual(correctSummaryGranularity);
    });

    test('When view summary with year granularity selected, summary is displayed correctly', async () => {
        wrapper.vm.currentYearSalesHistory = salesHistory
        let startDate = moment(new Date(wrapper.vm.dateStart))
        let intervalDate = startDate.add(1, 'years');
        let correctSummaryGranularity = [{"title":"2021","averageSale":"766.27","averagePricePerItem":"56.07",
            "averageItemsPerSale":"13.67","totalSaleValue":"2298.80","totalItems":"41.00","totalSales":3}]
        await wrapper.vm.$nextTick();
        wrapper.vm.granularity(intervalDate, 1, 'years')
        expect(wrapper.vm.reportGranularity).toStrictEqual(correctSummaryGranularity);
    });
});