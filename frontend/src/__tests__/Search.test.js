import { createLocalVue, shallowMount } from '@vue/test-utils';
import Search from '../components/Search.vue';
import Vuesax from 'vuesax';

let wrapper;

//Mock response data from search request
const mockUsersFromSearch = [ 
    {
        "id": 28,
        "firstName": "Raquela",
        "middleName": "YEP",
        "lastName": "Haylands",
        "nickname": "Future-proofed",
        "bio": "24/7 workforce",
        "email": "rhaylands5@shutterfly.com",
        "dateOfBirth": "2004-10-06",
        "phoneNumber": "+86 944 435 8212",
        "homeAddress": {
            "streetNumber": "32",
            "streetName": "Little Fleur Trail",
            "city": "Christchurch",
            "region": "Canterbury",
            "country": "New Zealand",
            "postcode": "8080"
        },
        "created": "2021-05-01 03:51:26",
        "role": "USER",
        "businessesAdministered": [],
        "country": "New Zealand",
        "city": "Christchurch"
    }
]

const localVue = createLocalVue();
localVue.use(Vuesax);

beforeEach(() => {
    wrapper = shallowMount(Search, {
        localVue,
        propsData: {},
        mocks: {},
        stubs: ['router-link', 'router-view'],
        methods: {},
        data () {
            return {
                users: mockUsersFromSearch
            }
        }
    });
    expect(wrapper).toBeTruthy();
});

afterEach(() => {
    wrapper.destroy();
});

describe('Search page tests', () => {
    test('Table exists when valid data is in response', () => {
        expect(wrapper.find('#userTable').exists()).toBe(true);        
    })

    test('Mobile mode not on when window is normal size', () => {
        global.innerWidth = 1300;
        global.dispatchEvent(new Event('resize'));
        expect(wrapper.vm.mobileMode).toBe(false)
    })
    

    test('Mobile mode active when window size is reduced', () => {
        global.innerWidth = 1299;
        global.dispatchEvent(new Event('resize'));

        expect(wrapper.vm.mobileMode).toBe(true)

    })
});

