<template>
    <div>
        <el-row class="align-center" type="flex" justify="space-between">
            <el-col :span="12">
            </el-col>
            <el-col :span="6" class="text-right">
                <el-button icon="el-icon-refresh" @click="refreshOrg" :loading="isRefresh"
                ></el-button>
            </el-col>
        </el-row>
        <!-- 管理部门 -->
        <div class="border-left border-right border-bottom">
            <el-divider content-position="left">管理部门</el-divider>
            <div class="padding-lr">
                <div v-if="selectdepList.length==0" style="text-align: center;font-size: 14px;color:#909399 ">暂无数据</div>
                <el-tag
                v-for="item in selectdepList"
                :key="item.id"
                @close="handleClose(item)"
                class="margin-lr-sm margin-bottom"
                >
                {{ item.fullPath }}
                </el-tag>
            </div>
        </div>
    </div>
</template>
<script>
    import user from "../../../api/user";
    import util from "../../../utils/udTreeUtil";
export default {
    name:'view-org',
    props:{
        rowContent: {
            type: Object,
            default: function () {
                return [
                ]
            }
        }
    },
    data(){
        return{
            isRefresh:false,
            selectdepList: [],
        }
    },
    mounted(){

    },
    created() {
        const vm =this
        vm.getGroupManageOfUser()
    },
    methods:{
        // 关闭标签
        handleClose(item) {
            this.selectaccList.splice(this.selectaccList.indexOf(item), 1);
        },
        getGroupManageOfUser:function(){
            const vm =this
            user.queryAdminDeptOfUser1({userId:vm.rowContent.id}).then(data=>{
                if(data.code==0){
                    vm.selectdepList =data.result
                    vm.isRefresh=false
                }
            }).catch(err=>{
                vm.isRefresh=false
                vm.$message({
                    type: 'warning',
                    message: err
                });
            })
        },
        //刷新
        refreshOrg:function(){
            const vm =this
            vm.isRefresh=true
            vm.getGroupManageOfUser()
        },
        onSubmit(){
        }
    }
}
</script>
<style lang="scss" scoped>
@import '../../../styles/base.scss';

</style>