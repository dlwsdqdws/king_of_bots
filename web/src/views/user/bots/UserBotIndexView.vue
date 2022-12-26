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

                                        <div>
                                            <select v-model="states.lang">
                                                <option v-for="lang of langs" :key="lang" :value="lang">{{ lang }}</option>
                                            </select>

                                            <select v-model="states.theme">
                                                <option v-for="theme of themes" :key="theme" :value="theme">{{ theme }}</option> 
                                            </select>
                                        </div>

                                        <VAceEditor
                                            v-model:value="botadd.content"
                                            @init="editorInit"
                                            :lang="states.lang"
                                            :theme="states.theme"
                                            :options="{
                                                //https://juejin.cn/post/7133470577775345671
                                                useWorker: true,
                                                enableBasicAutocompletion: true,
                                                enableSnippets: true,
                                                enableLiveAutocompletion: true,
                                                fontSize : 14,
                                                showPrintMargin: false,
                                                printMargin: true,
                                                enableMultiselect: true,
                                                animatedScroll: true,
                                                showFoldWidgets: true,
                                                fadeFoldWidgets: true,
                                            }"
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

                                                        <div>
                                                            <select v-model="states.lang">
                                                                <option v-for="lang of langs" :key="lang" :value="lang">{{ lang }}</option>
                                                            </select>

                                                            <select v-model="states.theme">
                                                                <option v-for="theme of themes" :key="theme" :value="theme">{{ theme }}</option> 
                                                            </select>
                                                        </div>

                                                        <VAceEditor
                                                            v-model:value="bot.content"
                                                            @init="editorInit"
                                                            :lang="states.lang"
                                                            :theme="states.theme"
                                                            :options="{
                                                                //https://juejin.cn/post/7133470577775345671
                                                                useWorker: true,
                                                                enableBasicAutocompletion: true,
                                                                enableSnippets: true,
                                                                enableLiveAutocompletion: true,
                                                                fontSize : 14,
                                                                showPrintMargin: false,
                                                                printMargin: true,
                                                                enableMultiselect: true,
                                                                animatedScroll: true,
                                                                showFoldWidgets: true,
                                                                fadeFoldWidgets: true,
                                                            }"
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
    import {ref, reactive, watch} from "vue";
    import {Modal} from "bootstrap/dist/js/bootstrap";
    import { VAceEditor } from 'vue3-ace-editor';
    import ace from 'ace-builds';
    import "ace-builds/webpack-resolver";
    

    import modeC_cppUrl from 'ace-builds/src-noconflict/mode-c_cpp?url';
    import modeJavaUrl from 'ace-builds/src-noconflict/mode-java?url';
    import modePythonUrl from 'ace-builds/src-noconflict/mode-python?url';

    import themeGithubUrl from 'ace-builds/src-noconflict/theme-github?url';
    import themeChromeUrl from 'ace-builds/src-noconflict/theme-chrome?url';
    import themeMonokaiUrl from 'ace-builds/src-noconflict/theme-monokai?url';

    import workerBaseUrl from 'ace-builds/src-noconflict/worker-base?url';

    import snippetsJavaUrl from 'ace-builds/src-noconflict/snippets/java?url';
    import snippetsPythonUrl from 'ace-builds/src-noconflict/snippets/python?url';

    import 'ace-builds/src-noconflict/ext-language_tools';

    export default {
        components : {
            VAceEditor,
        },
        setup() {
            ace.config.set(
                "basePath", 
                "https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/")


            ace.config.setModuleUrl('ace/mode/c_cpp', modeC_cppUrl);
            ace.config.setModuleUrl('ace/mode/java', modeJavaUrl);
            ace.config.setModuleUrl('ace/mode/python', modePythonUrl);
            ace.config.setModuleUrl('ace/theme/github', themeGithubUrl);
            ace.config.setModuleUrl('ace/theme/chrome', themeChromeUrl);
            ace.config.setModuleUrl('ace/theme/monokai', themeMonokaiUrl);
            ace.config.setModuleUrl('ace/mode/base', workerBaseUrl);
            ace.config.setModuleUrl('ace/snippets/javascript', snippetsJavaUrl);
            ace.config.setModuleUrl('ace/snippets/json', snippetsPythonUrl);
            ace.require("ace/ext/language_tools");

            const store = useStore();
            let bots = ref([]);

            const langs = ['c_cpp', 'java', 'python'];
            const themes = ['github', 'chrome', 'monokai'];

            const states = reactive({
                lang: 'c_cpp',
                theme: 'monokai',
            });

            watch(
                () => states.lang,
                async lang => {
                    states.content = (
                    await {
                        c_cpp : '',
                        java :  '',
                        python : '',
                    }[lang]
                    ).default;
                },
                { immediate: true },
            );

            let botadd = reactive({
                title : "",
                description : "",
                content : "",
                error_msg : ""
            });


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
                update_bot,
                langs,
                themes,
                states,
            }
        }
    }
</script>

<style scoped>
div.error-msg{
    color : red;
}

select {
  margin-right: 15px;
  margin-bottom: 10px;
}
</style>