<template>
    <div class="matchground-field">
        <div class = "matchground">
            <div class = "matchground-head">
                <div>
                    <div class="user-photo">
                        <img :src = "$store.state.user.photo" alt = "" >
                    </div>
                    <div class="user-username">
                        {{ $store.state.user.username }}
                    </div>
                </div>
                    <div class = "user-select-bot">
                        <select v-model = "select_bot" class="form-select" aria-label="Default select example">
                            <option value = "-1" selected>Manual</option>
                            <option v-for = "bot in bots" :key = "bot.id" :value="bot.id">
                                {{ bot.title }}
                            </option>
                        </select>
                    </div>
                <div>
                    <div class="user-photo">
                        <img :src = "$store.state.pk.opponent_photo" alt = "" >
                    </div>
                    <div class="user-username">
                        {{ $store.state.pk.ooponent_username }}
                    </div>
                </div>
            </div>
                    <div class = "match-button">
                        <button @click = "click_match_btn" type="button">
                            {{ match_btn_info }}
                        </button>
                    </div>
        </div>
    </div>
</template>

<script>
import { ref } from 'vue'
import { useStore } from 'vuex';
import $ from "jquery";

export default {
    setup() {
        const store = useStore();

        let match_btn_info = ref("Start Matching");
        let bots = ref([]);
        let select_bot = ref("-1");

        const click_match_btn = () => {
            if(match_btn_info.value === "Start Matching"){
                match_btn_info.value = "Stop Matching";
                store.state.pk.socket.send(JSON.stringify({
                    event : "start_matching",
                    bot_id : select_bot.value,
                }));
            } else{
                match_btn_info.value = "Start Matching";
                store.state.pk.socket.send(JSON.stringify({
                    event : "stop_matching"
                }));
            }
        }

        const refresh_bots = () => {
                $.ajax({
                    url : "https://www.dlwsdqdws.com/api/user/bot/getlist/",
                    type : "get",
                    headers : {
                        Authorization : "Bearer " + store.state.user.token,
                    },
                    success(resp){
                        bots.value = resp;
                    }
                });
            }


        // get bots list
        refresh_bots();

        return {
            match_btn_info,
            click_match_btn,
            bots,
            select_bot
        }
    }
}
</script>

<style scoped>
div.matchground-field{
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
}

div.matchground {
    width : 65%;
    height : 65%;
    background-color: rgba(50, 50, 50, 0.5);
    display: flex;
    flex-direction: column;
    justify-content: space-around;
}

div.matchground-head {
    display: flex;
    justify-content: space-evenly;
}
div.user-photo {
    text-align : center;
}
div.user-photo > img {
    border-radius: 50%;
    width : 10vh;
}
div.user-username {
    text-align : center;
    font-size : 20px;
    font-weight: 600;
    color : white;
    padding-top: 2vh;
}
div.user-select-bot {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 15vw;
    text-align: center;
}
div.user-select-bot>select{
    width: 12vw;
    height: 5vh;
    margin: 0 auto;
    font-size: 20px;
    border-radius: 5px;
}

.match-button {
    text-align: center;
}

.match-button > button {
    font-size: 20px;
    color : white;
    border-radius: 5px;
    background-color: rgba(49, 108, 244);
    padding : 10px 20px;
    border : none;
    cursor: pointer;
}
</style>