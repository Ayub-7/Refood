import {mount, createLocalVue } from '@vue/test-utils';
import CardNotifications from '../components/CardNotifications.vue';
import Vuesax from 'vuesax';
import api from "../Api";
import { store } from "../store"; 




let wrapper;
let localVue = createLocalVue();
localVue.use(Vuesax);
let $route = {
    params: {
        id: 1,
    }
}

let notificationData =[{"id":1105,"userId":1,"cardId":10001,"title":"Plastic Wrap","displayPeriodEnd":1626762673875,"status":"Expired"},{"id":1106,"userId":1,"cardId":10002,"title":"Plastic Wrap","displayPeriodEnd":1626762679077,"status":"Deleted"}]

let $log = {
    debug: jest.fn(),
}



beforeEach(() => {
    wrapper = mount(CardNotifications, {
        propsData: {
},
        mocks: {$route, $log, store},
        stubs: {},
        methods: {},
        localVue,
    })

    wrapper.vm.notifications = notificationData;

});

afterEach(() => {
    wrapper.destroy();
});

describe('Card modal functionality', () => {
    test('is a Vue instance', () => {
        expect(wrapper.isVueInstance).toBeTruthy();
    });


    test('If notifications exist list is shown', async () => {
        let button = wrapper.find('#dropdownButton');

        button.trigger('click')

        await wrapper.vm.$nextTick();

        expect(wrapper.find("#cardList").exists()).toBeTruthy();
    })

    test('If no notifications exist list is not shown', async () => {
        wrapper.vm.notifications = [];


        let button = wrapper.find('#dropdownButton');
        button.trigger('click')

        await wrapper.vm.$nextTick();
        expect(wrapper.find('#noCards').exists()).toBeTruthy();
    })
});
