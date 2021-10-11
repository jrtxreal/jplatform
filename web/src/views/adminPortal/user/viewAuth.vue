<template>
    <div>
        <el-row class="align-center" type="flex" justify="space-between">
            <el-col :span="12">
                所在部门：
                <el-select v-model="selectDep" @change="changeDep" placeholder="请选择" clearable>
                    <el-option
                            v-for="item in selectDepList"
                            :key="item.path"
                            :label="item.fullPath"
                            :value="item.path">
                    </el-option>
                </el-select>
            </el-col>
            <el-col :span="6" class="text-right">
                <el-button icon="el-icon-refresh" @click="refreshAuth" :loading="isRefresh"
                ></el-button>
            </el-col>
        </el-row>
        <!-- 权限管理 -->
        <div class="padding-lr" v-if="permissList.length==0" style="text-align: center;font-size: 14px;color:#909399 ">
            暂无数据
        </div>
        <div class="border-left border-right border-bottom" v-for="(item,index) in permissList" :key="index">

            <el-divider content-position="left">{{item.srcGrpName}}</el-divider>
            <div class="padding-lr">
                <div class="padding-lr">
                    <el-tag
                            v-for="item in item.data"
                            :key="item.id"
                            class="margin-lr-sm margin-bottom"
                    >
                        {{ item.name }}
                    </el-tag>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
    import user from "../../../api/user";
    import util from "../../../utils/udTreeUtil";

    export default {
        name: 'view-org',
        props: {
            rowContent: {
                type: Object,
                default: function () {
                    return []
                }
            }
        },
        data() {
            return {
                selectDep: '',
                isRefresh: false,//刷新
                authData: [],//总数据
                selectDepList: [{
                    value: 1,
                    name: 'A部门',
                }, {
                    value: 4,
                    name: 'B部门',
                }, {
                    value: 3,
                    name: 'C部门',
                }],
                permissList: [
                    {
                        name: '系统管理-用户管理',
                        btns: ['权限A', '权限B', '权限C'],
                    },
                    {
                        name: '系统管理-角色管理',
                        btns: ['权限E', '权限F', '权限G'],
                    },
                ],
                permissListTemp: [],
            }
        },
        mounted() {

        },
        created() {
            const vm = this
            vm.getGroupManageOfUser()
            vm.getAuth(3)
        },
        methods: {
            // 关闭标签
            handleClose(item) {
                this.selectaccList.splice(this.selectaccList.indexOf(item), 1);
            },
            //权限所属部门列表
            getGroupManageOfUser: function () {
                const vm = this
                user.queryAdminDeptOfUser1({userId: vm.rowContent.id}).then(data => {
                    if (data.code == 0) {
                        vm.selectDepList = data.result
                    }
                }).catch(err => {
                    vm.$message({
                        type: 'warning',
                        message: err
                    });
                })
            },
            //获取权限资源数据管理
            getAuth: function (index, e) {
                const vm = this
                user.queryOfUser({userId: vm.rowContent.id}).then(data => {
                    if (data.code == 0) {
                        vm.authData = data.result
                        vm.permissList = vm.authDataReset(data.result)
                        if (index == 1) {
                            vm.changedata(e)
                        }
                    vm.isRefresh=false
                        // vm.permissListTemp=vm.authDataReset(data.result)
                    }
                }).catch(err => {
                    vm.isRefresh=false
                    vm.$message({
                        type: 'warning',
                        message: err
                    });
                })
            },
            //数据分组
            authDataReset: function (data) {
                const map = {},
                    dest = [];
                for (let i = 0; i < data.length; i++) {
                    const ai = data[i];
                    if (!map[ai.srcGrpName]) { //依赖分组字段可自行更改！
                        dest.push({
                            srcGrpName: ai.srcGrpName, //依赖分组字段可自行更改！
                            data: [ai]
                        });
                        map[ai.srcGrpName] = ai; //依赖分组字段可自行更改！
                    } else {
                        for (let j = 0; j < dest.length; j++) {
                            const dj = dest[j];
                            if (dj.srcGrpName == ai.srcGrpName) { //依赖分组字段可自行更改！
                                dj.data.push(ai);
                                break;
                            }
                        }
                    }
                }

                return dest

            },
            //
            changeDep(e) {
                const vm = this
                vm.permissList = vm.permissListTemp
                vm.getAuth(1, e)
                // vm.permissList.forEach((item,index)=>{
                //     item.data.forEach((item1,index1)=>{
                //         if(item1.deptPath.indexOf(e)==-1){
                //             vm.permissList[index].data.splice(index1,1);
                //         }
                //     })
                // })

            },
            changedata: function (e) {
                const vm = this
                vm.permissList.forEach((item, index) => {
                    item.data.forEach((item1, index1) => {
                        if (item1.deptPath.indexOf(e) == -1) {
                            vm.permissList[index].data.splice(index1, 1);
                        }
                    })
                })
            },
            onSubmit() {

            },
            //刷新
            refreshAuth: function () {
                const vm =this
                vm.isRefresh=true
                vm.getGroupManageOfUser()
                vm.getAuth()

            },
            showMessageAuth: function (message, type) {
                this.$message({
                    message: message,
                    type: type
                });
            },
        }
    }
</script>
<style lang="scss" scoped>
    @import '../../../styles/base.scss';

</style>