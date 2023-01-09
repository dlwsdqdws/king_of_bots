
export default {
    state: {
        router_name : "menu",   //pages : menu, pk, record, recond_content, ranklist, user_bot
    },
    getters : {
    },
    mutations: {
        updateRouterName(state, router_name){
            state.router_name = router_name;
        },
    },
    modules: {
    }
}