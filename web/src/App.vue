<template>
  <div>
    <div>bot name: {{ botname }}</div>
    <div>bot rate: {{ botrate }}</div>
  </div>
  <router-view/>
</template>

<script>
  import $ from 'jquery';
  import {ref} from 'vue';



  export default {
    name : "App",
    setup : () => {
      let botname = ref("");
      let botrate = ref("");

      $.ajax({
        url : "http://127.0.0.1:3000/pk/getbotinfo/",
        type : "get",
        success : resp => {
          botname.value = resp.name;
          botrate.value = resp.rate;
        }
      })

      return {botname, botrate};
    }
  }
</script>

<style>
  body{
    background-image: url("./assets/background.png");
    background-size: cover;
  }
</style>
