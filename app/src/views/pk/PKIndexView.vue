<template>
    <ContentField>
        <PlayGround v-if = "$store.state.pk.status === 'playing'" /> 
        <MatchGrond v-if = "$store.state.pk.status === 'matching'" />
        <ResultBoard v-if = "$store.state.pk.loser !== 'none'" />
        <div class = "user-color1" v-if = "$store.state.pk.status === 'playing' && parseInt($store.state.user.id) === parseInt($store.state.pk.a_id)">left-bottom</div>
        <div class = "user-color2" v-if = "$store.state.pk.status === 'playing' && parseInt($store.state.user.id) === parseInt($store.state.pk.b_id)">right-top</div>
    </ContentField>
</template>

<script>
    import ContentField from "../../components/ContentField.vue";
    import PlayGround from "../../components/PlayGround.vue";
    import MatchGrond from "../../components/MatchGround.vue";
    import ResultBoard from "../../components/ResultBoard.vue";
    import { onMounted, onUnmounted } from "vue";
    import { useStore } from "vuex";

    export default {
        components : {
            ContentField,
            PlayGround,
            MatchGrond,
            ResultBoard,
        },

        setup(){
            const store = useStore();
            const socketUrl = `wss://www.dlwsdqdws.com/websocket/${store.state.user.token}/`;

            store.commit("updateIsRecord", false);

            let socket = null;

            store.commit("updateOpponent", {
                username : "My Opponent",
                photo : "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
            });

            onMounted(() => {
                socket = new WebSocket(socketUrl);

                socket.onopen = ()=> {
                    store.commit("updateSocket", socket);
                }

                socket.onmessage = msg => {
                    const data = JSON.parse(msg.data);
                    
                    if(data.event === "start_matching"){
                        // mathch successfully
                        store.commit("updateOpponent", {
                            username : data.opponent_username,
                            photo : data.opponent_photo,
                        });
                        setTimeout(() => {
                            store.commit("updateStatus", "playing");
                        }, 200);
                        store.commit("updateGame", data.game);
                    }else if(data.event === "move"){
                        const game = store.state.pk.gameObject;
                        const [snake0, snake1] = game.snakes;
                        snake0.set_direction(data.a_direction);
                        snake1.set_direction(data.b_direction);
                    }else if(data.event === "result"){
                        const game = store.state.pk.gameObject;
                        const [snake0, snake1] = game.snakes;
                        if(data.loser === "all" || data.loser === "A"){
                            snake0.status = "die";
                        }
                        if(data.loser === "all" || data.loser === "B"){
                            snake1.status = "die";
                        }
                        store.commit("updateLoser", data.loser);
                    }
                }

                socket.onclose = () => {
                }
            });

            onUnmounted(() => {
                store.commit("updateStatus", "matching");
                store.commit("updateLoser", "none");
                socket.close();
            });
        }
    }
</script>

<style scoped>
div.user-color1 {
    text-align: center;
    color : #4876EC;
    font-size: 30px;
    font-weight: 600;
    position: absolute;
    bottom: 5vh;
    width: 100%;
    text-align: center;
}
div.user-color2 {
    text-align: center;
    color : #F94848;
    font-size: 30px;
    font-weight: 600;
    position: absolute;
    bottom: 5vh;
    width: 100%;
    text-align: center;
}
</style>