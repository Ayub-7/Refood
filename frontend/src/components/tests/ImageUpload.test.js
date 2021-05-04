import { createLocalVue, shallowMount } from '@vue/test-utils';
import ImageUpload from '../ImageUpload';
import Vuesax from "vuesax";

const mockProducts = [
    {
        "id": "07-4957066",
        "name": "Spoon",
        "description": "Soup, Plastic",
        "recommendedRetailPrice": 14.69,
        "created": "2021-05-03 09:56:02",
        "images": [],
        "primaryImagePath": null
    },
    {
        "id": "55-9986232",
        "name": "Lamb Leg",
        "description": "Bone - In Nz",
        "recommendedRetailPrice": 43.66,
        "created": "2021-05-03 09:56:02",
        "images": [],
        "primaryImagePath": null
    },
    {
        "id": "W04GP5EC0B1798680",
        "name": "Compound - Mocha",
        "description": "vel ipsum praesent blandit lacinia erat vestibulum sed magna at nunc",
        "recommendedRetailPrice": 88.93,
        "created": "2021-01-11 20:54:46",
        "images": [],
        "primaryImagePath": null
    },
    {
        "id": "WAUVT64B54N722288",
        "name": "Pastry - Cheese Baked Scones",
        "description": "amet erat nulla tempus vivamus",
        "recommendedRetailPrice": 19.88,
        "created": "2021-03-05 14:36:54",
        "images": [],
        "primaryImagePath": null
    }
]

let wrapper;

const localVue = createLocalVue();
localVue.use(Vuesax);

beforeEach(() => {
    wrapper = shallowMount(ImageUpload, {
        localVue,
        propsData: {
            businessId: 'test',
            products: mockProducts
        },
        mocks: {},
        stubs: [],
        methods: {},
    });
    wrapper.setData({showModal: true})
});

afterEach(() => {
    wrapper.destroy();
});

describe('Test image upload button', () => {
    test('uploadImage is called when new file uploaded', async () => {
        wrapper.vm.uploadImage = jest.fn();
        
        expect(wrapper.find('#fileUpload').exists()).toBe(true);
        wrapper.find('#fileUpload').trigger('change');

        expect(wrapper.vm.uploadImage).toBeCalled();
    })
});
