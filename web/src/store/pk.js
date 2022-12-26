
export default {
    state: {
        status : "matching", //matching or playing
        socket : "",
        opponent_username : "",
        opponent_photo : "",
    },
    getters : {
    },
    mutations: {
        updateSocket(state, socket){
            state.socket = socket;
        },

        updateOpponent(state, opponent){
            state.ooponent_username = opponent.username;
            state.opponent_photo = opponent.photo;
        },

        updateStatus(state, status){
            state.status = status;
        },


    },
    modules: {
    }
}