<template>
    <ContentField>
        <table class="table table-striped table-hover">
            <thead>
                <tr>
                    <th>PlayerA</th>
                    <th>PlayerB</th>
                    <th>Winner</th>
                    <th>Match Time(PST)</th>
                    <th>Operations</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for = "record in records" :key = "record.record.id">
                    <td>
                        <img :src = "record.a_photo" alt = "" class = "record-user-photo">
                        &nbsp;
                        <span class = "record-user-username">
                            {{ record.a_username }}
                        </span>
                    </td>
                    <td>
                        <img :src = "record.b_photo" alt = "" class = "record-user-photo">
                        &nbsp;
                        <span class = "record-user-username">
                            {{ record.b_username }}
                        </span>
                    </td>
                    <td>{{ record.result }}</td>
                    <td>
                        {{ record.record.createtime }}
                    </td>
                    <td>
                        <button type="button" class="btn btn-secondary">View Steps</button>
                    </td>
                </tr>
            </tbody>
        </table>
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
            let records = ref([]);
            let current_page = 1;
            let tot_records = 0;


            const pull_page = page => {
                current_page = page
                $.ajax({
                    url : "http://127.0.0.1:3000/record/getlist/",
                    data : {
                        page,
                    },
                    type : "get",
                    headers : {
                        Authorization : "Bearer " + store.state.user.token,
                    },
                    success(resp){
                        records.value = resp.records;
                        tot_records = resp.records_cnt;
                    },
                    error(resp){
                        console.log(resp);
                    }
                });
            }

            pull_page(current_page);

            console.log(tot_records);

            return {
                records,
            }
        }
    }
</script>

<style scoped>
img.record-user-photo{
    width: 4.5vh;
    border-radius: 50%;
}
</style>