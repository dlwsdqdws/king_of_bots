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
                        <button type="button" class="btn btn-primary float-end" data-bs-toggle="modal" data-bs-target="#add-bot-button">
                            Create A Bot
                        </button>
                        <!-- Modal -->
                        <div class="modal fade" id="add-bot-button" tabindex="-1">
                            <div class="modal-dialog modal-xl">
                                <div class="modal-content">
                                <div class="modal-header">
                                    <h1 class="modal-title fs-5">Create A Bot</h1>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>
                                <div class="modal-body">
                                    <div class="mb-3">
                                        <label for="add-bot-title" class="form-label">Title</label>
                                        <input v-model="botadd.title" type="text" class="form-control" id="add-bot-title" placeholder="Please enter bot title">
                                    </div>
                                    <div class="mb-3">
                                        <label for="add-bot-desc" class="form-label">Description</label>
                                        <textarea v-model = "botadd.description" class="form-control" id="add-bot-desc" rows="3" placeholder="Please enter bot description"></textarea>
                                    </div>
                                    <div class="mb-3">
                                        <label for="add-bot-code" class="form-label">Code</label>
                                        <VAceEditor
                                            v-model:value="botadd.content"
                                            @init="editorInit"
                                            lang="c_cpp"
                                            theme="textmate"
                                            style="height: 300px" />
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <div class = "error-msg">{{ botadd.error_msg }}</div>
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                    <button type="button" class="btn btn-primary" @click="add_bot">Create</button>
                                </div>
                                </div>
                            </div>
                        </div>
                        

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
                                        <button type="button" class="btn btn-secondary" style = "margin-right: 10px;" data-bs-toggle="modal" :data-bs-target="'#update-bot-modal-' + bot.id">Modify</button>

                                        <!-- Modal -->
                                        <div class="modal fade" :id="'update-bot-modal-' + bot.id" tabindex="-1">
                                            <div class="modal-dialog modal-xl">
                                                <div class="modal-content">
                                                <div class="modal-header">
                                                    <h1 class="modal-title fs-5" >Modify The Bot</h1>
                                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                                </div>
                                                <div class="modal-body">
                                                    <div class="mb-3">
                                                        <label for="add-bot-title" class="form-label">Title</label>
                                                        <input v-model="bot.title" type="text" class="form-control" id="add-bot-title" placeholder="Please enter bot title">
                                                    </div>
                                                    <div class="mb-3">
                                                        <label for="add-bot-desc" class="form-label">Description</label>
                                                        <textarea v-model = "bot.description" class="form-control" id="add-bot-desc" rows="3" placeholder="Please enter bot description"></textarea>
                                                    </div>
                                                    <div class="mb-3">
                                                        <label for="add-bot-code" class="form-label">Code</label>
                                                        <!-- <select v-model="states.lang">
                                                            <option v-for="lang of langs" :value="lang">{{ lang }}</option>
                                                        </select> -->
                                                        <VAceEditor
                                                            v-model:value="bot.content"
                                                            @init="editorInit"
                                                            lang="c_cpp"
                                                            theme="textmate"
                                                            style="height: 300px" />
                                                    </div>
                                                </div>
                                                <div class="modal-footer">
                                                    <div class = "error-msg">{{ bot.error_msg }}</div>
                                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                                    <button type="button" class="btn btn-primary" @click="update_bot(bot)">Save</button>
                                                </div>
                                                </div>
                                            </div>
                                        </div>

                                        <button type="button" class="btn btn-danger" @click="remove_bot(bot)">Delete</button>
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
    import {ref, reactive} from "vue";
    import {Modal} from "bootstrap/dist/js/bootstrap";
    import { VAceEditor } from 'vue3-ace-editor';
    import ace from 'ace-builds';


    export default {
        components : {
            VAceEditor,
        },
        setup() {
            ace.config.set(
                "basePath", 
                "https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/")


            const store = useStore();
            let bots = ref([]);

            // const langs = ['json', 'javascript', 'html', 'yaml'];

            let botadd = reactive({
                title : "",
                description : "",
                content : "",
                error_msg : ""
            });

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
                // url : "http://127.0.0.1:3000/user/bot/remove/",
                // type : "post",
                // data : {
                //     bot_id : 4,
                // },
                // headers : {
                //     Authorization : "Bearer " + store.state.user.token,
                // },
                // success(resp){
                //     console.log(resp);
                // },
                // error(resp){
                //     console.log(resp);
                // }
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
                // headers : {
                //     Authorization : "Bearer " + store.state.user.token,
                // },
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

            const add_bot = () => {
                botadd.error_msg = "";
                $.ajax({
                    url : "http://127.0.0.1:3000/user/bot/add/",
                    type : "post",
                    data : {
                        title : botadd.title,
                        description : botadd.description,
                        content : botadd.content
                    },
                    headers : {
                        Authorization : "Bearer " + store.state.user.token,
                    },
                    success(resp){
                        if(resp.error_msg === "success"){
                            botadd.title = "";
                            botadd.description = "";
                            botadd.content = "";
                            Modal.getInstance("#add-bot-button").hide();
                            refresh_bots();
                        }else{
                            botadd.error_msg = resp.error_msg;
                        }
                    }
                });
            }

            const remove_bot = (bot) => {
                $.ajax({
                    url : "http://127.0.0.1:3000/user/bot/remove/",
                    type : "post",
                    data : {
                        bot_id : bot.id,
                    },
                    headers : {
                        Authorization : "Bearer " + store.state.user.token,
                    },
                    success(resp){
                        if(resp.error_msg === "success"){
                            refresh_bots();
                        }
                    },
                });
            }

            const update_bot = (bot) => {
                botadd.error_msg = "";
                $.ajax({
                    url : "http://127.0.0.1:3000/user/bot/update/",
                    type : "post",
                    data : {
                        bot_id : bot.id,
                        title : bot.title,
                        description : bot.description,
                        content : bot.content
                    },
                    headers : {
                        Authorization : "Bearer " + store.state.user.token,
                    },
                    success(resp){
                        if(resp.error_msg === "success"){
                            Modal.getInstance('#update-bot-modal-' + bot.id).hide();
                            refresh_bots();
                        }else{
                            bot.error_msg = resp.error_msg;
                        }
                    }
                });
            }

            return {
                bots,
                botadd,
                add_bot,
                remove_bot,
                update_bot
            }
        }
    }
</script>

<style scoped>
div.error-msg{
    color : red;
}
</style>