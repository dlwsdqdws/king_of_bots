<template>
    <ContentField>
        <div class="row justify-content-md-center">
            <div class="col-3">
                <form @submit.prevent="register">
                    <div class="mb-3">
                        <label for="username" class="form-label">Username</label>
                        <input v-model = "username" type="text" class="form-control" id="username" placeholder="Please enter your username">
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">Password</label>
                        <input v-model = "password" type="password" class="form-control" id="password" placeholder="Please enter your password">
                    </div>
                    <div class="mb-3">
                        <label for="confirmedPassword" class="form-label">Confirm Password</label>
                        <input v-model="confirmedPassword" type="password" class="form-control" id="confirmedPassword" placeholder="Please enter your password again">
                    </div>
                    <div class = "error_msg">{{ error_msg }}</div>
                    <button type="submit" class="btn btn-info">Sign Up</button>
                </form>
            </div>
        </div>
    </ContentField>
</template>

<script>
    import ContentField from '../../../components/ContentField.vue'
    import { ref } from 'vue'
    import router from '../../../router/index'
    import $ from 'jquery'


    export default {
        components : {
            ContentField
        },
        setup() {
            let username = ref("");
            let password = ref('');
            let confirmedPassword = ref('');

            let error_msg = ref("");

            const register = () => {
                $.ajax({
                    url : "https://www.dlwsdqdws.com/api/user/account/register/",
                    type : "post",
                    data : {
                        username: username.value,
                        password: password.value,
                        confirmedPassword: confirmedPassword.value,
                    },
                    success(resp){
                        if(resp.error_msg === "success"){
                            router.push({name : "user_account_login"});
                        }else{
                            error_msg.value = resp.error_msg;
                        }
                    },
                    // error(resp){
                    //     error_msg.value = resp.error_msg;
                    // }
                });
            }

            return {
                username,
                password,
                confirmedPassword,
                error_msg,
                register,
            }
        }
    }
</script>

<style scoped>
button {
    width: 100%;
}
div.error_msg{
    color : red;
    font-size: 12px;
}
</style>