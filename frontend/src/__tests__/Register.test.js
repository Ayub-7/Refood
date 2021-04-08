import { shallowMount, createLocalVue } from '@vue/test-utils';
import Vuex from 'vuex';
import Register from '../components/Register';
import api from '../Api'
import {verify} from "password-hash";

let wrapper;
let store;
let actions;
let mutations;
let state;
const localVue = createLocalVue();
localVue.use(Vuex);

// Mocking registered users
const mockUsers = {
    "id": 1,
    "users" : [
        {
            "firstName": "Helene",
            "lastName": "Newton",
            "middleName": "Angeline",
            "nickname": "non",
            "bio": "cillum nisi ullamco labore",
            "email": "angelinenewton@digitalus.com",
            "dateOfBirth": "1995-08-21",
            "phoneNumber": "(868) 442-3726",
            "homeAddress": "Albemarle Terrace",
            "password": "proident",
            "registerDate": "3/14/2021",
            "id": 1
        },
        {
            "firstName": "Sheri",
            "lastName": "Christian",
            "middleName": "Griffith",
            "nickname": "velit",
            "bio": "nulla quis elit ullamco",
            "email": "griffithchristian@digitalus.com",
            "dateOfBirth": "1995-08-21",
            "phoneNumber": "(983) 576-3532",
            "homeAddress": "Village Road",
            "password": "id",
            "registerDate": "3/14/2021",
            "id": 2
        },
        {
            "firstName": "Irwin",
            "lastName": "Lara",
            "middleName": "Fuentes",
            "nickname": "adipisicing",
            "bio": "irure reprehenderit laboris aute",
            "email": "fuenteslara@digitalus.com",
            "dateOfBirth": "1995-08-21",
            "phoneNumber": "(917) 580-3281",
            "homeAddress": "Fiske Place",
            "password": "culpa",
            "registerDate": "3/14/2021",
            "id": 3
        },
        {
            "firstName": "Eloise",
            "lastName": "Hewitt",
            "middleName": "Stella",
            "nickname": "exercitation",
            "bio": "irure ex laborum ipsum",
            "email": "stellahewitt@digitalus.com",
            "dateOfBirth": "1995-08-21",
            "phoneNumber": "(833) 599-2927",
            "homeAddress": "Java Street",
            "password": "ea",
            "registerDate": "3/14/2021",
            "id": 4
        }
    ]
}

// Mocking $route
const $route = {
    params: {
        id: 1
    }
};

beforeEach(() => {
    actions = {
        someAction: jest.fn()
    };
    mutations = {
        someMutation: jest.fn()
    };
    state = {
        key: {}
    };
    store = new Vuex.Store({
        actions,
        mutations,
        state,
    });
    wrapper = shallowMount(Register, {
        propsData: {},
        mocks: {$route},
        stubs: ['router-link', 'router-view'],
        methods: {},
        store,
        localVue,
    });
});

afterEach(() => {
    wrapper.destroy();
});

//TESTS TO CHECK LOGIN ERROR HANDLING
describe('Register error checking', () => {
    const getRegisterMethod = jest.spyOn(Register.methods, 'createUserInfo');
    getRegisterMethod.mockResolvedValue(mockUsers);
    test('Handles empty register', () => {
        wrapper.vm.firstname = '';
        wrapper.vm.lastname = '';
        wrapper.vm.middlename = '';
        wrapper.vm.nickname = '';
        wrapper.vm.bio = '';
        wrapper.vm.email = '';
        wrapper.vm.password = '';
        wrapper.vm.confirm_password = '';
        wrapper.vm.dateofbirth = '';
        wrapper.vm.phonenumber = '';
        wrapper.vm.homeaddress = '';
        const registerBtn = wrapper.find('.register-button')
        registerBtn.trigger('click');
        expect(wrapper.vm.errors.length).toBe(7);
    });

    test('Handles only email', () => {
        wrapper.vm.email = 'test@email.com';
        wrapper.vm.firstname = '';
        wrapper.vm.lastname = '';
        wrapper.vm.middlename = '';
        wrapper.vm.nickname = '';
        wrapper.vm.bio = '';
        wrapper.vm.password = '';
        wrapper.vm.confirm_password = '';
        wrapper.vm.dateofbirth = '';
        wrapper.vm.phonenumber = '';
        wrapper.vm.homeaddress = '';
        const registerBtn = wrapper.find('.register-button')
        registerBtn.trigger('click');
        expect(wrapper.vm.errors.length).toBe(6);
    })
    test('Handles only password', () => {
        wrapper.vm.password = 'Potato123!';
        wrapper.vm.confirm_password = 'Potato123!';
        wrapper.vm.email = 'test@email.com';
        wrapper.vm.firstname = '';
        wrapper.vm.lastname = '';
        wrapper.vm.middlename = '';
        wrapper.vm.nickname = '';
        wrapper.vm.bio = '';
        wrapper.vm.dateofbirth = '';
        wrapper.vm.phonenumber = '';
        wrapper.vm.homeaddress = '';
        const registerBtn = wrapper.find('.register-button')
        registerBtn.trigger('click');
        expect(wrapper.vm.errors.length).toBe(5);
    })

    test('Handles bad email', () => {
        wrapper.vm.password = 'Potato123!';
        wrapper.vm.confirm_password = 'Potato123!';
        wrapper.vm.firstname = 'bob';
        wrapper.vm.lastname = 'steve';
        wrapper.vm.dateofbirth = '15/09/145';
        wrapper.vm.homeaddress = '24 fake street';
        wrapper.vm.email = 'thisisnotaemail.com'
        wrapper.vm.middlename = '';
        wrapper.vm.nickname = '';
        wrapper.vm.bio = '';
        wrapper.vm.phonenumber = '027254871';
        const registerBtn = wrapper.find('.register-button')
        registerBtn.trigger('click');
        expect(wrapper.vm.errors.length).toBe(1);
    })

// test("Checks that it won't register an email that is already registered", () => {
//        wrapper.vm.firstname = 'Omar';
//        wrapper.vm.lastname = 'Sheta';
//        wrapper.vm.middlename = 'Hany Mohamed Ahmed';
//        wrapper.vm.nickname = 'Bababooey';
//        wrapper.vm.bio = "Rammstein rocks bro!";
//        wrapper.vm.email = "angelinenewton@digitalus.com";
//        wrapper.vm.password = "Potato1!";
//        wrapper.vm.confirm_password = "Potato1!";
//        wrapper.vm.dateofbirth = new Date().setUTCFullYear(1998, 4, 1);
//        wrapper.vm.phonenumber = 6969696969;
//        wrapper.vm.homeaddress = "420 Borat Street, Almaty, Great Nation of Kazakhstan";
//        const registerBtn = wrapper.find('.register-button');
//        registerBtn.trigger('click');
//        expect(wrapper.vm.errors.length).toBe(1);
//    });

   test("Checks that the password hashes and is not stored in plain text", () => {
       wrapper.vm.firstname = 'Omar';
       wrapper.vm.lastname = 'Sheta';
       wrapper.vm.middlename = 'Hany Mohamed Ahmed';
       wrapper.vm.nickname = 'Bababooey';
       wrapper.vm.bio = "Rammstein rocks bro!";
       wrapper.vm.email = "angelinenewton@digitalus.com";
       wrapper.vm.password = "Potato1!";
       wrapper.vm.confirm_password = "Potato1!";
       wrapper.vm.dateofbirth = new Date().setUTCFullYear(1998, 4, 1);
       wrapper.vm.phonenumber = 6969696969;
       wrapper.vm.homeaddress = "420 Borat Street, Almaty, Great Nation of Kazakhstan";
       const registerBtn = wrapper.find('.register-button');
       registerBtn.trigger('click');
       console.log(wrapper);
       console.log("Potato1!");
       console.log(wrapper.vm.password);
       expect(verify("Potato1!", wrapper.vm.createUserInfo().hashedPassword)).toBeTruthy();
   });

   // test("Checks that a successful registration takes the user to their profile", () => {
   //     wrapper.vm.firstName = 'Omar';
   //     wrapper.vm.lastName = 'Sheta';
   //     wrapper.vm.middlename = 'Hany Mohamed Ahmed';
   //     wrapper.vm.bio = "Rammstein rocks bro!";
   //     wrapper.vm.email = "wtilsley0@rakuten.co.jp";
   //     wrapper.vm.password = "Potato1!";
   //     wrapper.vm.confirmPassword = "Potato1!";
   //     wrapper.vm.dateofbirth = new Date().setUTCFullYear(1998, 4, 1);
   //     wrapper.vm.phonenum = 6969696969;
   //     wrapper.vm.address = "420 Borat Street, Almaty, Great Nation of Kazakhstan";
   // });
   //
   // test("Checks that the data of the user does not vanish if backend turns off", () => {
   //     wrapper.vm.firstName = 'Omar';
   //     wrapper.vm.lastName = 'Sheta';
   //     wrapper.vm.middlename = 'Hany Mohamed Ahmed';
   //     wrapper.vm.bio = "Rammstein rocks bro!";
   //     wrapper.vm.email = "wtilsley0@rakuten.co.jp";
   //     wrapper.vm.password = "Potato1!";
   //     wrapper.vm.confirmPassword = "Potato1!";
   //     wrapper.vm.dateofbirth = new Date().setUTCFullYear(1998, 4, 1);
   //     wrapper.vm.phonenum = 6969696969;
   //     wrapper.vm.address = "420 Borat Street, Almaty, Great Nation of Kazakhstan";
   // });

});