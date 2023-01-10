<template>
  <!-- <div class="window"> -->
    <div class="game-body">
      <MenuView v-if = "$store.state.router.router_name === 'menu'" />
      <PKIndexView v-else-if = "$store.state.router.router_name === 'pk'" />
      <RecordIndexView v-else-if = "$store.state.router.router_name === 'record'" />
      <RecordContentView v-else-if = "$store.state.router.router_name === 'record_content'" />
      <RanklistIndexView v-else-if = "$store.state.router.router_name === 'ranklist'" />
      <UserBotIndexView v-else-if = "$store.state.router.router_name === 'user_bot'" />
    </div>
  <!-- </div> -->
</template>

<script>
  import { useStore } from "vuex";
  import MenuView from "./views/MenuView.vue";
  import PKIndexView from "./views/pk/PKIndexView.vue";
  import RecordIndexView from "./views/record/RecordIndexView.vue";
  import RecordContentView from "./views/record/RecordContentView.vue";
  import RanklistIndexView from "./views/ranklist/RanklistIndexView.vue";
  import UserBotIndexView from "./views/user/bots/UserBotIndexView.vue";

  export default {
    components: {
      MenuView,
      PKIndexView,
      RecordIndexView,
      RecordContentView,
      RanklistIndexView,
      UserBotIndexView
    },

    setup(){
      const store = useStore();

      const jwt_token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJjZGU2OWFlZDU5ZWM0MGU3OWEzZDNiMTNmODQ4YzcxNSIsInN1YiI6IjIiLCJpc3MiOiJzZyIsImlhdCI6MTY3MzI0MjI3OCwiZXhwIjoxNjc0NDUxODc4fQ.6YHQ_dNrEjTcSPwONFOf4MNJQh_HGLiXAc6ot-gNeCo";
            if(jwt_token) {
                store.commit("updateToken", jwt_token);
                store.dispatch("getinfo", {
                    success() {
                        store.commit("updatePullingInfo", false);
                    },
                    error() {
                        store.commit("updatePullingInfo", false);
                    }
                })
            }
            else{
                store.commit("updatePullingInfo", false);
        }

    }
  }
</script>

<style scoped>
  body{
    margin: 0;
  }

  div.game-body{
    background-image: url("https://video.acwing.com/image/default/BF8E0DE6C8C541E0A3B64A8BA2404848-6-2.png?auth_key=1673883051-997b0e6290-0-71887bb0b36961592692784c5e270fbb");
    /* background-color: red; */
    background-size: cover;
    width : 100%;
    height: 100%;
  }

  div.window {
    height : 100vh;
    width : 100vw;
  }
</style>
