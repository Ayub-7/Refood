import { shallowMount, createLocalVue } from '@vue/test-utils';
import ProductCatalogue from "../components/ProductCatalogue";
import Vuesax from 'vuesax';
import {store} from "../store";

let wrapper;

const localVue = createLocalVue();
localVue.use(Vuesax);

store.loggedInUserId = 7;

//Mock user
const mockUser = {
    "id": 7,
    "firstName": "Papageno",
    "middleName": "Batholomew",
    "lastName": "Dolton",
    "nickname": "Persevering",
    "bio": "Advanced bi-directional flexibility",
    "email": "bdolton6@liveinternet.ru",
    "dateOfBirth": "2000-07-12",
    "phoneNumber": "+380 428 944 6622",
    "homeAddress": "44 Ramsey Court",
}
const mockBusiness =
    {
        "id": 7,
        "administrators": [
            22
        ],
        "name": "Skinder",
        "primaryAdministratorId": 1,
        "description": "Nullam varius. Nulla facilisi. Cras non velit nec nisi vulputate nonummy.",
        "address": {
            "streetNumber": "0",
            "streetName": "Vernon Place",
            "city": "Sarpang",
            "region": null,
            "country": "Bhutan",
            "postcode": null
        },
        "businessType": "Charitable organisation",
        "created": "2020-05-18 21:06:11"
    };

let $log = {
    debug: jest.fn(),
    error: jest.fn()
}

const getUserMethod = jest.spyOn(ProductCatalogue.methods, 'getUserInfo');
beforeEach(() => {
    wrapper = shallowMount(ProductCatalogue, {
        propsData: {},
        mocks: {store, $log},
        stubs: ['router-link', 'router-view'],
        methods: {},
        // components: CurrencyInput,
        localVue,
        data () {
            return {
                userId: mockUser.id,
                business: mockBusiness,
                actingAsBusinessId: null
            }
        }

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

describe('ProductCatalogue business tests', () => {
    test('Business\'s name is shown in the title', () => {
        const busPageTitle = wrapper.find("#pagetitle")
        expect(busPageTitle.text().includes('Products')).toBe(true);
    });
});