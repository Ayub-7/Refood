import {shallowMount, createLocalVue} from '@vue/test-utils';
import ReImage from "../components/ReImage";
import Vuesax from 'vuesax';

let wrapper;
const localVue = createLocalVue();
localVue.use(Vuesax);

const mockImagePath = "/media/images/23987192387509-123908794328.png";

beforeEach(() => {
    wrapper = shallowMount(ReImage, {
        propsData: {},
        mocks: {},
        stubs: {},
        methods: {},
        localVue,
    });

    jest.resetModules();
});

afterEach(() => {
    jest.resetModules();
});

describe("ReImage tests", () => {
    test("Default product item image url is retrieved - dev", () => {
        process.env.NODE_ENV = "development";
        expect(wrapper.vm.getImgUrl(null)).toEqual('../../public/ProductShoot.jpg')
    });

    test("Default product item image url is retrieved - staging", () => {
        process.env.NODE_ENV = "staging";
        expect(wrapper.vm.getImgUrl(null)).toEqual('../../public/ProductShoot.jpg')
    });

    test("Default product item image url is retrieved - production", () => {
        process.env.NODE_ENV = "production";
        expect(wrapper.vm.getImgUrl(null)).toEqual('../../public/ProductShoot.jpg')
    });
});
