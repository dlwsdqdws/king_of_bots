<template>
    <ContentField>
        <div class="row justify-content-md-center">
            <div class="col-3">
                <form @submit.prevent="login">
                    <div class="mb-3">
                        <label for="username" class="form-label">Username</label>
                        <input v-model = "username" type="text" class="form-control" id="username" placeholder="Please enter your username">
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">Password</label>
                        <input v-model = "password" type="password" class="form-control" id="password" placeholder="Please enter your password">
                    </div>
                    <div class = "error_msg">{{ error_msg }}</div>
                    <button type="submit" class="btn btn-info">Sign In</button>
                </form>
            </div>
        </div>
    </ContentField>
</template>

<script>
    import ContentField from "../../../components/ContentField.vue";
    import {useStore} from "vuex"
    import {ref} from 'vue'
    import router from "../../../router/index";

    export default {
        components : {
            ContentField
        },
        setup(){
            const store = useStore();
            let username = ref("");
            let password = ref("");
            let error_msg = ref("");

            const login = () => {
                error_msg.value = "";
                store.dispatch("login", {
                    username : username.value,
                    password : password.value,
                    success() { 
                        store.dispatch("getinfo", {
                            success(){
                                router.push({name : 'home'});
                            }
                        });
                    },
                    error(){
                        error_msg.value = "The username and/or password you specified are not correct.";
                    }
                })
            }

            return {
                username,
                password,
                error_msg,
                login,
            }
        }
    }
</script>

<style scoped>
button {
    width : 100%;
}
div.error_msg{
    color : red;
    font-size: 12px;
}
</style>