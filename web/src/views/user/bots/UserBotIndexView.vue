<template>
    <div class="container">
        <div class="row">
            <div class="col-3">
                <div class="card" style = "margin-top: 20px;">
                    <div class="card-body">
                        <img :src = "$store.state.user.photo" alt = "" style = "width:100%">
                    </div>
                </div>
            </div>
            <div class="col-9">
                <div class="card" style = "margin-top: 20px;">
                    <div class="card-header">
                        <span style = "font-size: 150%;">
                            My Bots
                        </span>
                        <button type="button" class="btn btn-primary float-end">
                            Create A Bot
                        </button>
                    </div>
                    <div class = "card-body">
                        <table class="table table-hover">
                            <thead>
                                <tr>
                                    <th>Name</th>
                                    <th>Create Time</th>
                                    <th>Operations</th>
                                </tr>
                            </thead>
                            <tbody style = "font-size: 90%;">
                                <tr v-for = "bot in bots" :key = "bot.id">
                                    <td>{{ bot.title }}</td>
                                    <td>{{ bot.createtime }}</td>
                                    <td>
                                        <button type="button" class="btn btn-secondary" style = "margin-right: 10px;">Modify</button>
                                        <button type="button" class="btn btn-danger">Delete</button>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import $ from "jquery";
    import {useStore} from "vuex";
    import {ref} from "vue";

    export default {
        setup() {
            const store = useStore();
            let bots = ref([]);

            // test APIs
            // $.ajax({
            //     url : "http://127.0.0.1:3000/user/bot/add/",
            //     type : "post",
            //     data : {
            //         title : "mybottitle",
            //         description : "mybotdescription",
            //         content : "mybotcontent"
            //     },
            //     headers : {
            //         Authorization : "Bearer " + store.state.user.token,
            //     },
            //     success(resp){
            //         console.log(resp);
            //     },
            //     error(resp){
            //         console.log(resp);
            //     }
            // });

            // $.ajax({
            //     url : "http://127.0.0.1:3000/user/bot/remove/",
            //     type : "post",
            //     data : {
            //         bot_id : 4,
            //     },
            //     headers : {
            //         Authorization : "Bearer " + store.state.user.token,
            //     },
            //     success(resp){
            //         console.log(resp);
            //     },
            //     error(resp){
            //         console.log(resp);
            //     }
            // });

            // $.ajax({
            //     url : "http://127.0.0.1:3000/user/bot/update/",
            //     type : "post",
            //     data : {
            //         bot_id : 3,
            //         title : "666",
            //         description : "666",
            //         content : "666"
            //     },
            //     headers : {
            //         Authorization : "Bearer " + store.state.user.token,
            //     },
            //     success(resp){
            //         console.log(resp);
            //     },
            //     error(resp){
            //         console.log(resp);
            //     }
            // });

            // $.ajax({
            //     url : "http://127.0.0.1:3000/user/bot/getlist/",
            //     type : "get",
            //     headers : {
            //         Authorization : "Bearer " + store.state.user.token,
            //     },
            //     success(resp){
            //         console.log(resp);
            //     },
            //     error(resp){
            //         console.log(resp);
            //     }
            // });

            const refresh_bots = () => {
                $.ajax({
                    url : "http://127.0.0.1:3000/user/bot/getlist/",
                    type : "get",
                    headers : {
                        Authorization : "Bearer " + store.state.user.token,
                    },
                    success(resp){
                        bots.value = resp;
                    }
                });
            }

            refresh_bots();

            return {
                bots
            }
        }
    }
</script>

<style scoped>

</style>