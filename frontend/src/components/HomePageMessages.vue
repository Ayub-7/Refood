<template>
  <div id="body">
      <div v-for="message in messages" :key="message.id">
        <vs-card id="message-notification-card">
            <div id="message-notification-container">
                <div id="message-text">New message from {{users[message.sender.id || message.sender].firstName}} {{users[message.sender.id || message.sender].lastName}} about {{message.card.title}}</div>
                <vs-button class="message-button">Expand</vs-button>
                <vs-button class="message-button">Delete</vs-button>
            </div>
            
        </vs-card>
      </div>
  </div>
</template>

<script>

import api from "../Api";
import { store } from '../store';

export default {
    data: function() {
        return {
          messages: [],
          users: {}
        
        }
    },

    mounted() {
      this.getMessages();

    },

    methods: {
      getMessages: function() {
        api.getMessages(store.loggedInUserId)
        .then((response) => {
          this.messages = response.data;
        

          for (let message of this.messages) {
            console.log(message.sender, message)
            this.users[message.sender.id] = message.sender;
          }

          console.log(this.users);
        }).catch(() => {
          this.$vs.notify({title:`Could not get messages`, text: "There was an error getting messages", color:'danger'});

        })
      }
      
    }

[{"id":34,"sender":{"id":3,"firstName":"Felice","middleName":"Tabbitha","lastName":"Jaeggi","nickname":"intranet","bio":"Managed foreground budgetary management","email":"tjaeggi2@independent.co.uk","dateOfBirth":"1976-12-06","phoneNumber":"+1 659 270 1003","homeAddress":{"streetNumber":"5305","streetName":"Melrose Drive","suburb":null,"city":"Sidon","region":null,"country":"Lebanon","postcode":null},"created":"2020-12-24 17:40:59","role":"USER","businessesAdministered":[{"name":"Skinder","id":3,"administrators":[3,{"id":7,"firstName":"Papageno","middleName":"Batholomew","lastName":"Dolton","nickname":"Persevering","bio":"Advanced bi-directional flexibility","email":"bdolton6@liveinternet.ru","dateOfBirth":"2000-07-12","phoneNumber":"+380 428 944 6622","homeAddress":{"streetNumber":"01801","streetName":"Grasskamp Lane","suburb":null,"city":"Malhou","region":"Santarém","country":"Portugal","postcode":"2380-506"},"created":"2020-07-21 03:54:32","role":"USER","businessesAdministered":["Skinder"]},{"id":1,"firstName":"Wilma","middleName":"Janet","lastName":"Sails","nickname":"Open-architected","bio":"Profit-focused scalable moratorium","email":"jsails0@go.com","dateOfBirth":"1989-02-28","phoneNumber":"+57 242 190 0153","homeAddress":{"streetNumber":"44","streetName":"Menomonie Way","suburb":null,"city":"Zhashkiv","region":null,"country":"Ukraine","postcode":null},"created":"2020-08-06 23:35:52","role":"USER","businessesAdministered":[{"name":"Dabshots","id":1,"administrators":[1],"primaryAdministratorId":1,"description":"Nullam varius. Nulla facilisi. Cras non velit nec nisi vulputate nonummy.","address":{"streetNumber":"0","streetName":"Vernon Place","suburb":null,"city":"Sarpang","region":null,"country":"Bhutan","postcode":null},"businessType":"Charitable organisation","created":"2020-05-18 09:06:11"},{"name":"Layo","id":2,"administrators":[{"id":2,"firstName":"Karrie","middleName":"Salvatore","lastName":"Loyley","nickname":"local area network","bio":"Quality-focused next generation synergy","email":"sloyley1@wordpress.com","dateOfBirth":"1995-11-06","phoneNumber":"+48 864 927 4819","homeAddress":{"streetNumber":"8120","streetName":"GroverJunction","suburb":null,"city":"Cimarelang","region":null,"country":"Indonesia","postcode":null},"created":"2020-05-01 01:25:12","role":"USER","businessesAdministered":["Layo"]},{"id":9,"firstName":"Ruthe","middleName":"Ogdan","lastName":"Ruit","nickname":"Open-architected","bio":"Configurable coherent capacity","email":"oruit8@reddit.com","dateOfBirth":"1995-05-10","phoneNumber":"+62 283 517 0351","homeAddress":{"streetNumber":"9885","streetName":"Oak Parkway","suburb":null,"city":"Ambato","region":null,"country":"Ecuador","postcode":null},"created":"2020-09-24 18:01:34","role":"USER","businessesAdministered":[{"name":"Grain n Simple","id":5,"administrators":[{"id":5,"firstName":"Shermy","middleName":"Pearle","lastName":"Layborn","nickname":"artificial intelligence","bio":"Intuitive client-server standardization","email":"playborn4@amazon.com","dateOfBirth":"2000-12-30","phoneNumber":"+62 527 277 7359","homeAddress":{"streetNumber":"5","streetName":"Hagan Avenue","suburb":null,"city":"Chengdong","region":null,"country":"China","postcode":null},"created":"2020-10-17 00:47:11","role":"USER","businessesAdministered":["Grain n Simple"]},9,{"id":8,"firstName":"Elysee","middleName":"Maurene","lastName":"Took","nickname":"benchmark","bio":"Team-oriented interactive installation","email":"mtook7@chron.com","dateOfBirth":"1985-11-09","phoneNumber":"+234 186 824 2303","homeAddress":{"streetNumber":"5","streetName":"Utah Terrace","suburb":null,"city":"Huangfang","region":null,"country":"China","postcode":null},"created":"2020-10-31 22:39:42","role":"USER","businessesAdministered":["Grain n Simple"]}],"primaryAdministratorId":5,"description":"Praesent blandit. Nam nulla.","address":{"streetNumber":"302","streetName":"Forest Run Place","suburb":null,"city":"Putinci","region":null,"country":"Serbia","postcode":null},"businessType":"Retail Trade","created":"2020-05-22 20:21:22"},"Layo"]},1],"primaryAdministratorId":2,"description":"Nam ultrices, libero non mattis pulvinar, nulla pede ullamcorper augue, a suscipit nulla elit ac nulla. Sed vel enim sit amet nunc viverra dapibus. Nulla suscipit ligula in lacus.","address":{"streetNumber":"95403","streetName":"Hanover Park","suburb":null,"city":"El Guapinol","region":null,"country":"Honduras","postcode":null},"businessType":"Accommodation and Food Services","created":"2020-08-25 10:22:19"},"Skinder"]}],"primaryAdministratorId":3,"description":"Morbi sem mauris, laoreet ut, rhoncus aliquet, pulvinar sed, nisl. Nunc rhoncus dui vel sem.","address":{"streetNumber":"6794","streetName":"Jackson Way","suburb":null,"city":"Xialaba","region":null,"country":"China","postcode":null},"businessType":"Retail Trade","created":"2020-09-11 08:50:50"}]},"card":{"id":2,"user":{"id":35,"firstName":"Hammad","middleName":"Carlye","lastName":"Hymans","nickname":"multimedia","bio":"Proin interdum mauris non ligula pellentesque ultrices.","email":"chymanse@behance.net","dateOfBirth":"1986-11-14","phoneNumber":"+375 584 698 9967","homeAddress":{"streetNumber":"23819","streetName":"Northview","suburb":null,"city":"Buta Ranquil","region":null,"country":"Argentina","postcode":"8353"},"created":"2021-02-09 20:02:51","role":"USER","businessesAdministered":[{"name":"Eayo","id":100,"administrators":[35],"primaryAdministratorId":35,"description":null,"address":{"streetNumber":"84946","streetName":"Spaight","suburb":null,"city":"Inírida","region":null,"country":"Colombia","postcode":"940028"},"businessType":"Non-profit organisation","created":"2019-02-09 09:13:27"}]},"title":"Soup - Campbells - Chicken Noodle","description":"Sed vel enim sit amet nunc viverra dapibus.","created":"2021-07-02 00:58:25","displayPeriodEnd":"2021-07-21 00:58:25","keywords":"nulla","section":"Exchange"},"receiver":1,"description":null,"sent":null},{"id":35,"sender":{"id":4,"firstName":"Holt","middleName":"Vincent","lastName":"Skelhorne","nickname":"impactful","bio":"Extended intermediate budgetary management","email":"vskelhorne3@washington.edu","dateOfBirth":"2005-05-08","phoneNumber":"+86 537 663 8760","homeAddress":{"streetNumber":"39","streetName":"Longview Hill","suburb":null,"city":"Drochia","region":null,"country":"Moldova","postcode":"MD-5233"},"created":"2020-06-22 06:17:39","role":"USER","businessesAdministered":[{"name":"Skiptube","id":4,"administrators":[4,{"id":6,"firstName":"Keelby","middleName":"Hersch","lastName":"Ganiclef","nickname":"leading edge","bio":"Future-proofed client-server complexity","email":"hganiclef5@reference.com","dateOfBirth":"1983-07-17","phoneNumber":"+7 599 583 7488","homeAddress":{"streetNumber":"5776","streetName":"Nancy Point","suburb":null,"city":"Bijeli","region":null,"country":"Indonesia","postcode":null},"created":"2021-03-20 02:05:43","role":"USER","businessesAdministered":["Skiptube"]}],"primaryAdministratorId":4,"description":"Phasellus in felis.","address":{"streetNumber":"2557","streetName":"Starling Pass","suburb":null,"city":"Skinnskatteberg","region":"Västmanland","country":"Sweden","postcode":"739 30"},"businessType":"Retail Trade","created":"2020-10-17 21:30:14"}]},"card":{"id":2,"user":35,"title":"Soup - Campbells - Chicken Noodle","description":"Sed vel enim sit amet nunc viverra dapibus.","created":"2021-07-02 00:58:25","displayPeriodEnd":"2021-07-21 00:58:25","keywords":"nulla","section":"Exchange"},"receiver":1,"description":null,"sent":null},{"id":37,"sender":{"id":10,"firstName":"Ruthe","middleName":"Iosep","lastName":"Yoseloff","nickname":"systematic","bio":"Multi-tiered static toolset","email":"iyoseloff9@elegantthemes.com","dateOfBirth":"2003-05-11","phoneNumber":"+420 882 542 6613","homeAddress":{"streetNumber":"976","streetName":"Heffernan Pass","suburb":null,"city":"Kan’onjichō","region":null,"country":"Japan","postcode":"769-1613"},"created":"2020-07-17 00:25:47","role":"USER","businessesAdministered":[]},"card":{"id":2,"user":35,"title":"Soup - Campbells - Chicken Noodle","description":"Sed vel enim sit amet nunc viverra dapibus.","created":"2021-07-02 00:58:25","displayPeriodEnd":"2021-07-21 00:58:25","keywords":"nulla","section":"Exchange"},"receiver":1,"description":null,"sent":null},{"id":38,"sender":{"id":15,"firstName":"Chiarra","middleName":"Dyanne","lastName":"Phelip","nickname":"context-sensitive","bio":"Synergized scalable attitude","email":"dphelipe@godaddy.com","dateOfBirth":"2002-01-04","phoneNumber":"+93 374 108 2586","homeAddress":{"streetNumber":"0","streetName":"Bashford Road","suburb":null,"city":"Piracicaba","region":null,"country":"Brazil","postcode":"13400-000"},"created":"2020-04-25 18:23:01","role":"USER","businessesAdministered":[]},"card":{"id":7,"user":{"id":6564,"firstName":"Gunner","middleName":"Opal","lastName":"Widmore","nickname":"productivity","bio":"Donec dapibus. Duis at velit eu est congue elementum.","email":"owidmoref3@yolasite.com","dateOfBirth":"1984-03-10","phoneNumber":"+255 313 924 3887","homeAddress":{"streetNumber":"04","streetName":"1st","suburb":null,"city":"Aguitu","region":null,"country":"China","postcode":null},"created":"2019-12-11 09:51:25","role":"USER","businessesAdministered":[]},"title":"Lid Tray - 16in Dome","description":"Aenean lectus.","created":"2021-07-14 00:58:25","displayPeriodEnd":"2021-08-14 00:58:25","keywords":"suspendisse potenti","section":"Exchange"},"receiver":1,"description":null,"sent":null},{"id":39,"sender":{"id":25,"firstName":"Irvin","middleName":"Ariel","lastName":"O'Lunney","nickname":"demand-driven","bio":"Nullam porttitor lacus at turpis.","email":"aolunney4@netlog.com","dateOfBirth":"1980-06-05","phoneNumber":"+593 683 840 9124","homeAddress":{"streetNumber":"96","streetName":"Laurel","suburb":null,"city":"Syrostan","region":null,"country":"Russia","postcode":"456382"},"created":"2019-04-29 17:44:22","role":"USER","businessesAdministered":[]},"card":{"id":7,"user":6564,"title":"Lid Tray - 16in Dome","description":"Aenean lectus.","created":"2021-07-14 00:58:25","displayPeriodEnd":"2021-08-14 00:58:25","keywords":"suspendisse potenti","section":"Exchange"},"receiver":1,"description":null,"sent":null},{"id":40,"sender":7,"card":{"id":7,"user":6564,"title":"Lid Tray - 16in Dome","description":"Aenean lectus.","created":"2021-07-14 00:58:25","displayPeriodEnd":"2021-08-14 00:58:25","keywords":"suspendisse potenti","section":"Exchange"},"receiver":1,"description":null,"sent":null}]}
</script>

<style>

#message-notification-container {
    display: flex;
    justify-content: space-between;
    margin-right: 10px;
}

#message-text {
    width: 80%;
    font-size: 14px;
    height: 30px;
    line-height: 30px;
}


#message-notification-card {
    width: 95%;
    margin: auto;
    margin-top: 5px;
}



</style>