import {createLocalVue, mount} from "@vue/test-utils";
import BusinessSalesGraph from "../components/BusinessSalesGraph";

let wrapper;
let localVue = createLocalVue();

let $log = {
  error: jest.fn(),
}

let mockData = [
  {
    sold: "2021-01-01 12:00:00",
    price: 24.56,
  },
  {
    sold: "2021-02-01 12:00:00",
    price: 5,
  },
  {
    sold: "2021-02-02 12:00:00",
    price: 11,
  }
];

beforeEach(() => {
  wrapper = mount(BusinessSalesGraph, {
    mocks: {$log},
    stubs: ["apexchart"],
    methods: {},
    localVue,
  });
});

describe("Method tests", () => {
  test("Month Labels are generated for a single year", () => {
    expect(wrapper.vm.generateMonthLabels(["2021"]).length).toBe(12);
  });

  test("Month Labels are generated for multiple years", () => {
    let years = ["2019", "2020", "2021"];
    expect(wrapper.vm.generateMonthLabels(years).length).toBe(12 * years.length);
  });

  test("Sold Listings are properly categorised and summed", () => {
    let data = wrapper.vm.totalMonthlyRevenue(mockData);
    expect(data["2021"]).toBeTruthy();
    expect(data["2021"].length).toBe(12);
    expect(data["2021"][1]).toBe(16);
  });

  test("Sold Listings are properly categorised and summed across different years", () => {
    let olderListing = {
      sold: "2020-01-12 12:00:00",
      price: 11.11,
    }
    mockData.push(olderListing);

    let data = wrapper.vm.totalMonthlyRevenue(mockData);
    expect(data["2020"]).toBeTruthy();
    expect(Object.keys(data).length).toBe(2);
  });

});
