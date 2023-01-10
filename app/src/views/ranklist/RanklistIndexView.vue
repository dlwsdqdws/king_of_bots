<template>
    <ContentField>
        <div style = "padding-top: 2vh;">
            <div class="container text-center">
                <div class="row">
                    <div class="col">
                    
                    </div>
                    <div class="col-4" style = "background-color: rgba(248,231,28,0.74); height: 15vh;">
                        <div style = "font-size: 150%; font-size: 200%; clear: both;">Champion</div>
                        <div v-if="winners.length >= 1">
                            <img :src = "winners[0].photo" alt = "" class = "winner-user-photo">
                            &nbsp;
                            <span class = "winner-user-username" style = "font-size: 120%; font-size: 150%;">
                                {{ winners[0].username }}
                            </span>
                        </div>
                    </div>
                    <div class="col">
                    
                    </div>
                </div>
                <div class="row">
                    <div class="col" style = "background-color: rgba(155,155,155,0.42); height: 15vh;">
                        <div style = "font-size: 150%; font-size: 200%;">Second Place</div>
                        <div v-if="winners.length >= 2">
                            <img :src = "winners[1].photo" alt = "" class = "winner-user-photo">
                            &nbsp;
                            <span class = "winner-user-username" style = "font-size: 120%; font-size: 150%;">
                                {{ winners[1].username }}
                            </span>
                        </div>
                    </div>
                    <div class="col-5">
                    
                    </div>
                    <div class="col" style = "background-color: rgba(245,166,35,0.55); height: 15vh;">
                        <div style = "font-size: 150%; font-size: 200%;">Third Place</div>
                        <div v-if="winners.length >= 3">
                            <img :src = "winners[2].photo" alt = "" class = "winner-user-photo">
                            &nbsp;
                            <span class = "winner-user-username" style = "font-size: 120%; font-size: 150%;">
                                {{ winners[2].username }}
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div style = "padding-top: 10vh;">
            <table class="table table-striped table-hover" style = "text-align: center;">
                <thead>
                    <tr>
                        <th>Users</th>
                        <th>Ratings</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for = "user in users" :key = "user.id">
                        <td>
                            <img :src = "user.photo" alt = "" class = "record-user-photo">
                            &nbsp;
                            <span class = "record-user-username">
                                {{ user.username }}
                            </span>
                        </td>
                        <td>{{ user.rating }}</td>
                        
                    </tr>
                </tbody>
            </table>
            <nav aria-label="...">
                <ul class="pagination" style = "float: right; padding-right: 5vw;">
                    <li class="page-item" @click="click_page(-2)">
                        <a class="page-link" href="#">Previous</a>
                    </li>
                    <li :class="'page-item ' + page.is_pull_page" v-for = "page in pages" :key = "page.number" @click="click_page(page.number)">
                        <a class="page-link" href="#">
                            {{ page.number }}
                        </a>
                    </li>
                    <li class="page-item" @click="click_page(-1)">
                        <a class="page-link" href="#">Next</a>
                    </li>
                </ul>
            </nav>
        </div>
    </ContentField>
</template>

<script>
    import ContentField from "../../components/ContentField.vue";
    import { useStore } from "vuex";
    import { ref } from "vue";
    import $ from 'jquery';

    export default {
        components : {
            ContentField
        },

        setup(){
            const store = useStore();
            let users = ref([]);
            let current_page = 1;
            let tot_users = 0;
            let pages = ref([]);
            let winners = ref([]);

            const click_page = page => {
                if(page === -2){
                    page = current_page - 1;
                }
                else if(page === -1){
                    page = current_page + 1;
                }

                let max_pages = parseInt(Math.ceil(tot_users / 10));
                if(page >= 1 && page <= max_pages){
                    pull_page(page);
                }
            }

            const update_pages = () => {
                let max_pages = parseInt(Math.ceil(tot_users / 10));
                let new_pages = [];

                for(let i = current_page-2; i <= current_page+2; i++){
                    if(i >= 1 && i <= max_pages){
                        new_pages.push({
                            number : i,
                            is_pull_page : i === current_page? "active" : "",
                        });
                    }
                }

                pages.value = new_pages;
            }

            const pull_page = page => {
                current_page = page
                $.ajax({
                    url : "https://app777.acapp.acwing.com.cn/api/ranklist/getlist/",
                    data : {
                        page,
                    },
                    type : "get",
                    headers : {
                        Authorization : "Bearer " + store.state.user.token,
                    },
                    success(resp){
                        users.value = resp.users;
                        tot_users = resp.user_cnt;
                        update_pages();
                    },
                });
            }

            const pull_winners = () => {
                $.ajax({
                    url : "https://app777.acapp.acwing.com.cn/api/ranklist/getlist/",
                    data : {
                        page : 1,
                    },
                    type : "get",
                    headers : {
                        Authorization : "Bearer " + store.state.user.token,
                    },
                    success(resp){
                        winners.value = resp.users;
                    },
                });
            }

            pull_page(current_page);
            pull_winners();


            return {
                users,
                pages,
                click_page,
                winners,
            }
        }
    }
</script>

<style scoped>
img.record-user-photo{
    width: 4.5vh;
    border-radius: 50%;
}
img.winner-user-photo{
    width: 6vh;
    border-radius: 50%;
}
</style>