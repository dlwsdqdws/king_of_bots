<template>
  <NavBar />
  <router-view> </router-view>
</template>

<script>
  import "bootstrap/dist/css/bootstrap.min.css";
  import "bootstrap/dist/js/bootstrap";

  import NavBar from './components/NavBar.vue';

  import $ from "jquery";

  export default {
    components: {
      NavBar
    },

    setup(){
      $.ajax({
        url: "http://127.0.0.1:3000/user/account/token/",
        type : "post",
        data : {
          username : 'test',
          password : 'test',
        },
        success(resp){
          console.log(resp);
        },
        error(resp){
          console.log(resp);
        }
      }),

      $.ajax({
        url : "http://127.0.0.1:3000/user/account/info/",
        type : "get",
        headers : {
          Authorization : "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzMDliOTVjNWQxZGU0NWE0OWZhOGZkNjE0NmYxZjQxNCIsInN1YiI6IjIiLCJpc3MiOiJzZyIsImlhdCI6MTY3MTgxNTA1NSwiZXhwIjoxNjczMDI0NjU1fQ.o46hvaowUISGmXEuf4QpZE5E13zkH0H_AM0G0gpCrJg"
        },
        success(resp){
          console.log(resp);
        },
        error(resp){
          console.log(resp);
        }
      })
    }
  }
</script>

<style>
  body{
    background-image: url("./assets/images/background.png");
    background-size: cover;
  }
</style>
