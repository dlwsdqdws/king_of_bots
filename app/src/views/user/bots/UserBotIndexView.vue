<template>
    <ContentField>
        <div class="game-table">
            <div>
                <span style="font-size: 150%;">
                    My Bots
                </span>
                <button type="button" style="float: right;" @click="show_add_modal_handler(true)">
                    Create A Bot
                </button>

                <!-- Modal -->
                <div class="game-modal" id="add-bot-button" tabindex="-1" v-if="show_add_modal">
                    <div>
                        <h5 style="margin : 2px;">Create A Bot</h5>
                    </div>
                    <div>
                        <div>
                            <label for="add-bot-title">Title</label>
                            <input style="width: 85%;" v-model="botadd.title" type="text" id="add-bot-title"
                                placeholder="Please enter bot title">
                        </div>
                        <div>
                            <label for="add-bot-desc">Description</label>
                            <textarea style="width: 85%; margin-top: 10px;" v-model="botadd.description"
                                id="add-bot-desc" rows="3" placeholder="Please enter bot description"></textarea>
                        </div>
                        <div>
                            <label for="add-bot-code">Code</label>

                            <div>
                                <select v-model="states.lang">
                                    <option v-for="lang of langs" :key="lang" :value="lang">{{ lang }}</option>
                                </select>

                                <select v-model="states.theme">
                                    <option v-for="theme of themes" :key="theme" :value="theme">{{ theme }}
                                    </option>
                                </select>
                            </div>

                            <VAceEditor v-model:value="botadd.content" @init="editorInit" :lang="states.lang"
                                :theme="states.theme" :options="{
                                    //https://juejin.cn/post/7133470577775345671
                                    useWorker: true,
                                    enableBasicAutocompletion: true,
                                    enableSnippets: true,
                                    enableLiveAutocompletion: true,
                                    fontSize: 14,
                                    showPrintMargin: false,
                                    printMargin: true,
                                    enableMultiselect: true,
                                    animatedScroll: true,
                                    showFoldWidgets: true,
                                    fadeFoldWidgets: true,
                                }" style="height: 200px" />
                        </div>
                    </div>
                    <div>
                        <div class="error-msg">{{ botadd.error_msg }}</div>
                        <button type="button" @click="show_add_modal_handler(false)">Close</button>
                        <button type="button" @click="add_bot">Create</button>
                    </div>
                </div>


                <table>
                    <thead>
                        <tr>
                            <th>Name</th>
                            <th>Create Time</th>
                            <th>Operations</th>
                        </tr>
                    </thead>
                    <tbody style="font-size: 90%;">
                        <tr v-for="bot in bots" :key="bot.id">
                            <td>{{ bot.title }}</td>
                            <td>{{ bot.createtime }}</td>
                            <td>
                                <button type="button" style="margin-right: 10px;" @click = "show_update_modal_handler(bot.id, true)">Modify</button>
                                <button type="button" @click="remove_bot(bot)">Delete</button>

                                <!-- Modal -->
                                <div class="game-modal" :id="'update-bot-modal-' + bot.id" tabindex="-1" v-if = "bot.show_update_modal">
                                    <div>
                                        <h5 style="margin : 2px;">Modify The Bot</h5>
                                    </div>
                                    <div>
                                        <div>
                                            <label for="add-bot-title">Title</label>
                                            <input style="width: 85%;" v-model="bot.title" type="text"
                                                id="add-bot-title" placeholder="Please enter bot title">
                                        </div>
                                        <div>
                                            <label for="add-bot-desc">Description</label>
                                            <textarea style="width: 85%; margin-top: 10px;" v-model="bot.description"
                                                id="add-bot-desc" rows="3"
                                                placeholder="Please enter bot description"></textarea>
                                        </div>
                                        <div>
                                            <label for="add-bot-code">Code</label>

                                            <div>
                                                <select v-model="states.lang">
                                                    <option v-for="lang of langs" :key="lang" :value="lang">{{
                                                        lang
                                                    }}</option>
                                                </select>

                                                <select v-model="states.theme">
                                                    <option v-for="theme of themes" :key="theme" :value="theme">
                                                        {{
                                                            theme
                                                        }}</option>
                                                </select>
                                            </div>

                                            <VAceEditor v-model:value="bot.content" @init="editorInit"
                                                :lang="states.lang" :theme="states.theme" :options="{
                                                    //https://juejin.cn/post/7133470577775345671
                                                    useWorker: true,
                                                    enableBasicAutocompletion: true,
                                                    enableSnippets: true,
                                                    enableLiveAutocompletion: true,
                                                    fontSize: 14,
                                                    showPrintMargin: false,
                                                    printMargin: true,
                                                    enableMultiselect: true,
                                                    animatedScroll: true,
                                                    showFoldWidgets: true,
                                                    fadeFoldWidgets: true,
                                                }" style="height: 200px" />
                                        </div>
                                    </div>
                                    <div>
                                        <div class="error-msg">{{ bot.error_msg }}</div>
                                        <button type="button" @click = "show_update_modal_handler(bot.id, false)">Close</button>
                                        <button type="button" @click="update_bot(bot)">Save</button>
                                    </div>
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </ContentField>
</template>

<script>
import $ from "jquery";
import { useStore } from "vuex";
import { ref, reactive, watch } from "vue";
import { VAceEditor } from 'vue3-ace-editor';
import ace from 'ace-builds';
import "ace-builds/webpack-resolver";
import ContentField from "@/components/ContentField.vue";


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
    components: {
        ContentField,
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

        let show_add_modal = ref(false);

        const states = reactive({
            lang: 'c_cpp',
            theme: 'monokai',
        });

        watch(
            () => states.lang,
            async lang => {
                states.content = (
                    await {
                        c_cpp: '',
                        java: '',
                        python: '',
                    }[lang]
                ).default;
            },
            { immediate: true },
        );

        let botadd = reactive({
            title: "",
            description: "",
            content: "",
            error_msg: ""
        });


        const refresh_bots = () => {
            $.ajax({
                url: "https://www.dlwsdqdws.com/api/user/bot/getlist/",
                type: "get",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    for(const bot of resp){
                        bot.show_update_modal = false;
                    }
                    bots.value = resp;
                }
            });
        }

        refresh_bots();

        const add_bot = () => {
            botadd.error_msg = "";
            $.ajax({
                url: "https://www.dlwsdqdws.com/api/user/bot/add/",
                type: "post",
                data: {
                    title: botadd.title,
                    description: botadd.description,
                    content: botadd.content
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    if (resp.error_msg === "success") {
                        botadd.title = "";
                        botadd.description = "";
                        botadd.content = "";
                        show_add_modal.value = false;
                        refresh_bots();
                    } else {
                        botadd.error_msg = resp.error_msg;
                    }
                }
            });
        }

        const remove_bot = (bot) => {
            $.ajax({
                url: "https://www.dlwsdqdws.com/api/user/bot/remove/",
                type: "post",
                data: {
                    bot_id: bot.id,
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    if (resp.error_msg === "success") {
                        refresh_bots();
                    }
                },
            });
        }

        const update_bot = (bot) => {
            botadd.error_msg = "";
            $.ajax({
                url: "https://www.dlwsdqdws.com/api/user/bot/update/",
                type: "post",
                data: {
                    bot_id: bot.id,
                    title: bot.title,
                    description: bot.description,
                    content: bot.content
                },
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    if (resp.error_msg === "success") {
                        
                        refresh_bots();
                    } else {
                        bot.error_msg = resp.error_msg;
                    }
                }
            });
        }

        const show_add_modal_handler = is_show => {
            show_add_modal.value = is_show;
        }

        const show_update_modal_handler = (bot_id, is_show) => {
            const new_bots = [];
            for(const bot of bots.value){
                if(bot.id === bot_id){
                    bot.show_update_modal = is_show;
                }
                new_bots.push(bot);
            }
            bots.value = new_bots;
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
            show_add_modal,
            show_add_modal_handler,
            show_update_modal_handler,
        }
    }
}
</script>

<style scoped>
div.error-msg {
    color: red;
}

select {
    margin-right: 15px;
    margin-bottom: 10px;
}

div.game-table {
    display: flex;
    width: 100%;
    height: calc(100% - 5vh);
    justify-content: center;
    padding-top: 5vh;
}

div.game-table table {
    background-color: rgba(255, 255, 255, 0.5);
    border-radius: 5px;
}


td {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    width: 12vw;
    max-width: 12vw;
    text-align: center;
}

th {
    text-align: center;
}

.game-modal {
    background-color: white;
    padding: 10px;
    border-radius: 5px;
    position: absolute;
    height: 50vh;
    width: 40vw;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    margin: auto;
    text-align: left;
}
</style>