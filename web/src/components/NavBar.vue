<template>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
  <div class="container">
    <router-link class="navbar-brand" :to = "{name : 'home'}">Kong Of Bots</router-link>

    <div class="collapse navbar-collapse" id="navbarText">
      <ul class="navbar-nav me-auto mb-2 mb-lg-0">
        <li class="nav-item">
          <router-link class="nav-link" active-class="active" :to = "{name : 'pk_index'}">Battles</router-link>
        </li>
        <li class="nav-item">
          <router-link class="nav-link" active-class="active" :to = "{name : 'record_index'}">Matches</router-link>
        </li>
        <li class="nav-item">
          <router-link class="nav-link" active-class="active" :to = "{name : 'ranklist_index'}">Battles</router-link>
        </li>
      </ul>

      <ul class="navbar-nav" v-if = "$store.state.user.is_login">
        <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
            {{ $store.state.user.username }}
          </a>
          <ul class="dropdown-menu">
            <router-link class="dropdown-item" :to = "{name : 'user_bot_index'}">Battles</router-link>
            <li><hr class = "dropdown-divider"></li>
            <li><a class="dropdown-item" href="#" @click="logout">Sign Out</a></li>
          </ul>
        </li>
      </ul>

      <ul class="navbar-nav" v-else>
        <li class="nav-item dropdown">
          <router-link class="nav-link" :to = "{name : 'user_account_login'}" role="button">
           Sign In
          </router-link>
        </li>
        <li class="nav-item dropdown">
          <router-link class="nav-link" :to = "{name : 'user_account_register'}" role="button">
           Sign Up
          </router-link>
        </li>
      </ul>

    </div>
  </div>
</nav>
</template>

<script>
import {useStore} from "vuex";

export default {
  setup() {
    const store = useStore();

    const logout = () => {
      store.dispatch("logout");
    }

    return {
      logout
    }
  }
}

</script>

<style  scoped>

</style>