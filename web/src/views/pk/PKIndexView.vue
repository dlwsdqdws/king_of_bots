<template>
    <PlayGround v-if = "$store.state.pk.status === 'playing'" /> 
    <MatchGrond v-if = "$store.state.pk.status === 'matching'" />
</template>

<script>
    import PlayGround from "../../components/PlayGround.vue";
    import MatchGrond from "../../components/MatchGround.vue";
    import { onMounted, onUnmounted } from "vue";
    import { useStore } from "vuex";

    export default {
        components : {
            PlayGround,
            MatchGrond,
        },

        setup(){
            const store = useStore();
            const socketUrl = `ws://127.0.0.1:3000/websocket/${store.state.user.token}/`;

            let socket = null;

            store.commit("updateOpponent", {
                username : "My Opponent",
                photo : "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
            });

            onMounted(() => {
                socket = new WebSocket(socketUrl);

                socket.onopen = ()=> {
                    console.log("connected");
                }

                socket.onmessage = msg => {
                    const data = JSON.parse(msg.data);
                    console.log(data);
                }

                socket.onclose = () => {
                    console.log("disconnected");
                }
            });

            onUnmounted(() => {
                socket.close();
            });
        }
    }
</script>

<style scoped>

</style>