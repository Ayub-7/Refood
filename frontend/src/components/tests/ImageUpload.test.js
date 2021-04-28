import { shallowMount } from '@vue/test-utils';
import ImageUpload from '../ImageUpload';


let wrapper;

beforeEach(() => {
    wrapper = shallowMount(ImageUpload, {
        propsData: {
            productId: 'test',
            businessId: 'test'
        },
        mocks: {},
        stubs: [],
        methods: {},
    });
});

afterEach(() => {
    wrapper.destroy();
});

describe('Test image upload button', () => {

    test('UploadImage is called when file changes', () => {
        wrapper.vm.uploadImage = jest.fn()
        const upload = wrapper.find('#fileUpload').trigger('change');
        expect(wrapper.vm.uploadImage).toBeCalled();
    });
});
