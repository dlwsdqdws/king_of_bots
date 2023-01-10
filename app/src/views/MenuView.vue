<template>
    <div class = "menu-field">
        <div class = "menu">
            <div class = "menu-item" @click="click_pk_handler">PK</div>
            <div class = "menu-item" @click="click_record_handler">Records</div>
            <div class = "menu-item" @click="click_ranklist_handler">Ranklist</div>
            <div class = "menu-item" @click="click_user_bot_handler">My Bots</div>
        </div>
    </div>
</template>

<script>
import { useStore } from 'vuex';

export default {
    setup() {
        const store = useStore();

        const click_pk_handler = () => {
            store.commit("updateRouterName", "pk");
        }

        const click_record_handler = () => {
            store.commit("updateRouterName", "record");
        }

        const click_user_bot_handler = () => {
            store.commit("updateRouterName", "user_bot");
        }

        const click_ranklist_handler = () => {
            store.commit("updateRouterName", "ranklist");
        }

        return {
            click_pk_handler,
            click_record_handler,
            click_user_bot_handler,
            click_ranklist_handler,
        }
    }
}
</script>

<style scoped>
div.menu-field {
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
}

div.menu {
    width : 25vw;
    height: 30vh;
    background-color: rgba(0, 0, 0, 0.2);
}

div.menu-item {
    width : 100%;
    height: 7.5vh;
    font-size: 28px;
    font-style: italic;
    font-weight: 700;
    line-height: 7.5vh;
    color: white;
    text-align: center;
    cursor: pointer;
    user-select: none;
}

div.menu-item:hover {
    transform: scale(1.2);
    transition: 200ms;
}

</style>