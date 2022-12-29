<template>
    <ContentField>
        <table class="table table-striped table-hover" style = "text-align: center;">
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
                        <button  @click="open_record_content(record.record.id)" type="button" class="btn btn-secondary">View Steps</button>
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
    import router from "../../router/index";

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

            const stringTo2D = map => {
                let g = [];
                for(let i = 0, k = 0; i < 13; i++){
                    let line = [];
                    for(let j = 0; j < 14; j++, k++){
                        if(map[k] === '0') line.push(0);
                        else line.push(1);
                    }
                    g.push(line);
                }

                return g;
            }

            const open_record_content = recordId => {
                for(const record of records.value){
                    if(record.record.id === recordId){
                        store.commit("updateIsRecord", true);
                        // console.log(record);
                        store.commit("updateGame", {
                            map : stringTo2D(record.record.map),
                            a_id : record.record.aid,
                            a_sx : record.record.asx,
                            a_sy : record.record.asy,
                            b_id : record.record.bid,
                            b_sx : record.record.bsx,
                            b_sy : record.record.bsy,
                        });
                        store.commit("updateSteps", {
                            a_steps : record.record.asteps,
                            b_steps : record.record.bsteps,
                        });
                        store.commit("updateRecordLoser", record.record.loser);
                        router.push({
                            name : "record_content",
                            params : {
                                recordId,
                            }
                        });
                        break;
                    }
                }
            }

            return {
                records,
                open_record_content,
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