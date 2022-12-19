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

      <ul class="navbar-nav">
        <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
            Sign In/Sign Up
          </a>
          <ul class="dropdown-menu">
            <router-link class="dropdown-item" :to = "{name : 'user_bot_index'}">Battles</router-link>
            <li><hr class = "dropdown-divider"></li>
            <li><a class="dropdown-item" href="#">Sign Out</a></li>
          </ul>
        </li>
      </ul>
    </div>
  </div>
</nav>
</template>

<script>

</script>

<style  scoped>

</style>