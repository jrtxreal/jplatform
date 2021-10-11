<template>
    <div class="sys-user">
        <div class="container">
            <div class="left">
                <div class="left-top">
                    <el-button icon="el-icon-refresh" style="width: 100%;" @click="getGroupList">刷新部门</el-button>
                </div>
                <dep-tree
                        ref="deptTree"
                        :tree-data="deptListU"
                        :tree-props="treePropsU"
                        :current-node-key="currentNodeKeyU"
                        :default-expand-all="true"
                        :accordion="false"
                        :expand-on-click-node="false"
                        node-key="id"
                        @current-change="change"
                ></dep-tree>
            </div>
            <div class="right">
                <!-- 列表页 -->
                <div v-if="currentType=='list'">
                    <!-- 头部搜索 -->
                    <div class="margin-bottom">
                        <!--  -->
                        <el-row class="flex align-center" type="flex" justify="space-between">
                            <el-col :span="12">
                                <div style="display: flex;flex-direction: row" @keyup.enter="searchClick">
                                    <el-input
                                            placeholder="请输入用户名"
                                            style="margin-right: 20px;max-width: 300px;"

                                            clearable
                                            @clear="searchClick"
                                            v-model="inputSearch">
                                        <i slot="suffix" class="el-input__icon el-icon-search"></i>
                                    </el-input>
                                    <el-button @click="searchClick" type="primary">搜索</el-button>
                                </div>
                            </el-col>
                            <el-col :span="6" style="text-align:right;">
                                <el-button v-if="checkPermit(map.sys_user_add)"
                                           @click="addUser"  type="primary">添加</el-button>
                            </el-col>
                        </el-row>
                    </div>
                    <!-- table列表 -->
                    <user-table
                            ref="addContent"
                            :tableDataId="tableData"
                            :searchContent="inputContent"
                            @selectType="selectType"></user-table>
                </div>
                <!-- 详情页 -->
                <div v-if="currentType=='view'">
                    <user-view @selectType="selectType" :rowLi="rowLi"></user-view>
                </div>
            </div>
        </div>
        <!-- 添加用户弹窗 -->
        <user-add
                v-if="addUserVisible"
                :addUserVisibleF="addUserVisible"
                @addCancel="addCancel"
                @addSubmit="addSubmit"
        ></user-add>
    </div>
</template>
<script>
import depTree from '../dept/tree';
import userTable from './list';
import userView from './view';
import userAdd from './add';
import map from '../../../map'
import udFrameWorkUtil from "../../../utils/udFrameWorkUtil";
import user from "../../../api/user";
import util from "../../../utils/udTreeUtil";
import udStoreUtil from "../../../utils/udStoreUtil";

export default {
    name:map.sys_user_query,
    components:{depTree,userTable,userView,userAdd},
    data(){
        return{
            map,
            currentNodeKeyU: 0,
            current: [],
            deptListU: [],
            treePropsU: {children: "children", label: "name"},
            treeIdMap: {},
            selectDepartmentName:'',
            currentType:'list',
            inputSearch:'',
            inputContent:'',
            tableData:1,
            rowLi:{},
            addUserVisible:false,
        }
    },
    computed:{
        addUserForm:{
            get(){
                return udStoreUtil.getStore("user.goAdd.form")
            },
            set(val){
                udStoreUtil.setStore("user.goAdd.form",val)
            }
        }
    },
    created() {
        const vm =this
       vm.getGroupList()

    },
    methods: {
        checkPermit(permit){
          return udFrameWorkUtil.checkPermit(permit,'all');
        },
        //左侧部门树返回选中节点
        selectNode(data){
            this.selectDepartmentName = data
        },
        selectType(data){
            const vm =this
            vm.currentType = data.view
            vm.rowLi =data.row
            // vm.getGroupList()
            // vm.$nextTick(()=>{
            //     vm.getGroupList()
            // })

        },
        change(data) {
            const vm = this;
            vm.currentNodeKeyU = data.id;
            vm.currentType="list"
            vm.tableData = vm.currentNodeKeyU
        },
        //获取部门列表数据
        getGroupList: function () {
            const vm = this
            user.queryDeptOfUser({}).then(data => {
                let list =   util.build2(data.result, 'id', 'pid', 'key', 'ord', 'children')
                if(vm.deptListU.length >0){
                 vm.deptListU=list.topLeaveList
                vm.$nextTick().then(() => {
                    vm.$refs.deptTree.$children[0].setCurrentKey( vm.currentNodeKeyU);
                    vm.tableData = vm.currentNodeKeyU
                    vm.currentType="list"
                });
                }else {
                    vm.deptListU=list.topLeaveList
                    vm.$nextTick().then(() => {
                        vm.$refs.deptTree.$children[0].setCurrentKey(vm.deptListU[0].id);
                        vm.tableData=vm.deptListU[0].id
                        vm.currentType="list"
                    });

                }
            }).catch(err => {
                vm.showMessageAuth(err, 'warning')
            })
        },
        //添加用户
        addUser(){
            const vm = this;
            const department = vm.tableData;
            vm.addUserForm = {
                name: '',
                pass: '',
                mobile: '',
                nickName: '',
                email: '',
                department: department
            };
            this.addUserVisible = true
        },
        searchClick(){
            const vm =this
            vm.inputContent = vm.inputSearch

         },
        //取消添加
        addCancel(){
            this.addUserVisible = false
        },
        //
        addSubmit(data){
            const vm =this
            vm.addUserVisible = false
            this.$refs.addContent.getContent();
        }

    }
}
</script>
<style lang="scss" >
@import '../../../styles/base.scss';
.sys-user{
    $basicMargin:10px;
    display: flex;
    height: 100%;
    width: 100%;
    scrollbar-width:thin;
    .container {
        scrollbar-width:thin;
        box-sizing: border-box;
        height: calc(100% - #{$basicMargin*2});
        width: calc(100% - #{$basicMargin*2});
        margin-left: $basicMargin;
        margin-top: $basicMargin;
        display: flex;
        .left{
            padding: 0;
            box-sizing: border-box;
            border-right: 2px dashed gray!important;
            /* height: 100%; */
            display: block;
            position: relative;
            scrollbar-width:thin;
            overflow: scroll;
            .left-top{
                padding-bottom: .5rem;
            }
        }
        .right{
            padding: 10px 5px 0 10px;
            overflow:scroll;
            scrollbar-width:thin;
            .el-table__body-wrapper{
                scrollbar-width:thin;
            }
        }
    }
}

</style>
