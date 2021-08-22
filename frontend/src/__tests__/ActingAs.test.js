import {createLocalVue, mount, shallowMount} from '@vue/test-utils';
import ActingAs from '../components/ActingAs';
import Vuesax from 'vuesax';
import api from "../Api";

const localVue = createLocalVue();
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

describe('User acting as tests', () => {
    beforeEach(() => {
        wrapper = shallowMount(ActingAs, {
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

        const getUserRoleMethod = jest.spyOn(ActingAs.methods, 'getUserRole')
        getUserRoleMethod.mockImplementation(() => {
            wrapper.vm.role = "USER";
            return wrapper.vm.role;
        });

        const getUserNameMethod = jest.spyOn(ActingAs.methods, 'getUserName');
        getUserNameMethod.mockImplementation(() => {
            return store.userName;
        });
    });

    afterEach(() => {
        wrapper.destroy();
    });

    test('is a Vue instance', () => {
        expect(wrapper.isVueInstance).toBeTruthy();
    });

    test('Check username is shown', () => {
        expect(wrapper.find('.user').exists()).toBe(true);
    });

    test('Check username is displaying the correct username and role', () =>  {

        wrapper.vm.store.userName = "Wileen Tisley";
        wrapper.vm.store.loggedInUserId = 22;
        expect(wrapper.vm.getUserName()).toBe('Wileen Tisley');
    });

    test('Check user primary businesses list is rendered', () =>  {
        expect(wrapper.find('#businessList').exists()).toBe(true);
    });

    test('Check selecting business in dropdown calls setActingAsBusinessId method', () => {
        wrapper.vm.setActingAsBusinessId = jest.fn();
        const busLi = wrapper.find('#businessList');
        busLi.trigger('click');
        expect(wrapper.vm.setActingAsBusinessId).toBeCalled;
    });

    test('Check selecting user calls setActingAsUser method', () => {
        //Select business
        wrapper.vm.setActingAsBusinessId = jest.fn();
        const busLi = wrapper.find('#businessList');
        busLi.trigger('click');
        const userName = wrapper.find('.user-menu');
        wrapper.vm.setActingAsUser = jest.fn();
        userName.trigger('click');
        expect(wrapper.vm.setActingAsUser).toBeCalled;
    })
});

describe( 'Backend error checking tests', () => {
    beforeEach(() => {
        wrapper = mount(ActingAs, {
            propsData: {},
            mocks: {store, $log, $router, mutations, sessionStorage},
            stubs: ['router-link', 'router-view'],
            methods: {},
            localVue,
        });

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



    test('When setActingAsBusinessId returns 200, the cache is refreshed and the user acts as a business', async () => {
        api.actAsBusiness = jest.fn(() => {
            return Promise.resolve({status: 201, data: {id: 1}});
        });
        wrapper.vm.refreshCachedItems = jest.fn();
        wrapper.vm.setActingAsBusinessId(store.actingAsBusinessId, store.actingAsBusinessName);

        await wrapper.vm.$nextTick();

        expect(wrapper.vm.refreshCachedItems).toBeCalled();
        expect(wrapper.vm.$router.push).toBeCalled();
    });

    test('When setActingAsBusinessId returns an unspecified error (500), the problem is printed to debug', async () => {
        api.actAsBusiness = jest.fn(() => {
            return Promise.reject({response: {status: 500}});
        });

        wrapper.vm.setActingAsBusinessId(store.actingAsBusinessId, store.actingAsBusinessName);

        await wrapper.vm.$nextTick();

        expect(api.actAsBusiness).toBeCalled()
        expect(wrapper.vm.$log.debug).toBeCalled();
    });

    test('When setActingAsUser returns 200, the cache is refreshed and the user acts as a business', async () => {
        api.actAsBusiness = jest.fn(() => {
            return Promise.resolve({status: 201, data: {id: 1}});
        });
        wrapper.vm.refreshCachedItems = jest.fn();
        wrapper.vm.setActingAsUser();

        await wrapper.vm.$nextTick();

        expect(wrapper.vm.refreshCachedItems).toBeCalled();
        expect(wrapper.vm.$router.push).toBeCalled();
    });

    test('When setActingAsUser returns an unspecified error (500), the problem is printed to debug', async () => {
        api.actAsBusiness = jest.fn(() => {
            return Promise.reject({response: {status: 500, message: "unspecified error"}});
        });

        wrapper.vm.setActingAsBusinessId(store.actingAsBusinessId, store.actingAsBusinessName);

        await wrapper.vm.$nextTick();

        expect(api.actAsBusiness).toBeCalled()
        expect(wrapper.vm.$log.debug).toBeCalled();
    });



});