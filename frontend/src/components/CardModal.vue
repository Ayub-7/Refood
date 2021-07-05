<template>
  <vs-popup :title="selectedCard.title" :active.sync="showing">
    <div><b>Description:</b> {{selectedCard.description}}</div>
    <div><b>Creator name:</b> {{selectedCard.user.firstName}} {{selectedCard.user.lastName}}</div>
    <div><b>Creation date:</b> {{selectedCard.created}}</div>
    <div><b>Expiry date:</b> {{selectedCard.displayPeriodEnd}}</div>
    <div><b>Location:</b> {{selectedCard.user.homeAddress.suburb}} {{selectedCard.user.homeAddress.city}} {{selectedCard.user.homeAddress.country}}</div>
    <div><b>Keywords:</b> {{selectedCard.keywords}}</div>
    <div><b>Section:</b> {{selectedCard.section}}</div>
  </vs-popup>
</template>

<script>
export default {
  name: "CardModal",
  props: ['selectedCard'],
  data: function() {
    return {
      showing: false,
    }
  },
  methods:
      {
        /**
         * Opens modal by setting showing to true (linked to :active-sync), also resets form data before opening
         */
        openModal: function() {
          this.showing = true;
        },
      }
}
</script>

<style scoped>
</style>