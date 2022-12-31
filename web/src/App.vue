<template>
  <NavBar />
  <router-view> </router-view>
</template>

<script>
  import "bootstrap/dist/css/bootstrap.min.css";
  import "bootstrap/dist/js/bootstrap";

  import NavBar from './components/NavBar.vue';

  // import $ from "jquery";

  export default {
    components: {
      NavBar
    },

    setup(){
    }
  }
</script>

<style>
  body{
    background-image: url("./assets/images/background.png");
    background-size: cover;
  }
</style>
