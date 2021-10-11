<!--  -->
<template>
    <div class="sys-role">
        <div class="role-left">
            <div class="left-top">
                <el-button style="width: 100%;" icon="el-icon-refresh" @click="getDeptTreeList">刷新部门</el-button>
            </div>
            <dep-tree ref="deptTree"
                      :tree-data="mnRoleList"
                      :tree-props="defaultProps"
                      :expand-on-click-node="false"
                      :default-expand-all="true"
                      :current-node-key="deptId"
                      :accordion="false"
                      node-key="id"
                      @current-change="handleNodeClick">
            </dep-tree>
            <!-- <div class="left-buttom">
              <el-button class="role-btn" @click="pro">维护组织</el-button>
            </div> -->
        </div>
        <div class="role-right">
            <role-detail
                    v-if="roleId"
                    :roleId="roleId"
                    :deptId="deptId"
                    :rolePath="currentRolePath"
                    :pathName="currentRolePathName"
                    :depatName="currentDeptName"
                    v-on:setId="setDeptId"
            ></role-detail>
            <role-list v-else v-on:setId="setRoleId" :deptId="deptId"></role-list>
        </div>
    </div>
</template>

<script>
    import RoleEmpty from './empty';
    import RoleList from './list/index';
    import RoleDetail from './detail/index';
    import map from '../../../map'
    import depTree from '../dept/tree';
    // jk
    import {getDeptTree, getRoleList} from '../../../api/roleManage';
    import udTree from '../../../utils/udTreeUtil'


    export default {
        name: map.sys_role_query,
        components: {
            'dep-tree': depTree,
            'role-empty': RoleEmpty,
            'role-list': RoleList,
            'role-detail': RoleDetail,
        },
        data() {
            return {
                // 树结构
                defaultProps: {
                    children: "children",
                    label: "name",
                    id: 'id',
                },
                list: [],
                isDetail: false,
                isEmpty: false,
                comName: '',
                deptId: 0,
                roleId: 0,
                mnRoleList: [],
                currentRolePath: '/',
                currentRolePathName: "/",
                currentDeptName: ''
            };
        },
        // 监听属性 类似于data概念
        computed: {},
        // 监控data中的数据变化
        watch: {},

        methods: {
            // 点击树节点, 清除roleId 返回角色列表页
            handleNodeClick(data) {
                const vm = this;
                vm.deptId = data.id;
                this.roleId = null;
                //vm.setDeptId(data.id)
            },
            // 设置所选部门ID 用于请求角色列表
            setDeptId(id) {
                this.deptId = id.deptId;
                this.roleId = null;
            },
            // 设置所选角色ID 用于跳转详情页
            setRoleId(id) {
                this.roleId = id.id;
                this.currentRolePath = id.path;
                this.currentRolePathName = id.fullPath;
                this.currentDeptName = id.deptName;
            },
            pro() {
                console.log('维护组织');
            },

            //zmn 获取部门列表
            getDeptTreeList: function () {
                const vm = this;
                const selected = vm.deptId;
                getDeptTree({}).then((data => {
                    if (data.code == 0) {
                        let obj = udTree.build2(data.result, 'id', 'pid', null, 'ord', 'children');
                        vm.mnRoleList = obj.topLeaveList;
                        vm.treeIdMap = obj.idMap;
                        if (!selected || !vm.treeIdMap[selected]) {
                            vm.deptId = obj.topLeaveList[0].id;
                            vm.$nextTick().then(() => {
                                vm.$refs.deptTree.$children[0].setCurrentKey(vm.deptId);
                                this.roleId = null;
                            });

                        } else {
                            vm.$nextTick().then(() => {
                                vm.$refs.deptTree.$children[0].setCurrentKey(selected);
                                this.roleId = null;
                            });

                        }

                    }
                })).catch((error => {
                    vm.$message.error('获取部门列表失败，请联系管理员\n' + error);
                }))
            }
        },

        //  创建之前
        beforeCreate() {
        },
        // 创建完成（可以访问当前this实例）
        created() {
            //this.getDepartList();
            const vm = this;
            vm.getDeptTreeList();
        },
        mounted() {
            // this.setCheckedKeys(1);
        },
        //  更新之前
        beforeUpdate() {
        },
        //  更新之后
        updated() {
        },
        //  销毁之前
        beforeDestroy() {
        },
        //  销毁完成
        destroyed() {
        },
        //  如果页面有keep-alive缓存功能，这个函数会触发
        activated() {
        },
    };
</script>
<style lang="scss">
    //@import url(); 引入公共css类
    .sys-role {
        $basicMargin: 10px;
        display: flex;
        height: calc(100% - #{$basicMargin*2});
        width: calc(100% - #{$basicMargin*2});
        margin-top: $basicMargin;
        margin-left: $basicMargin;

        .role-left {
            font-family: Helvetica Neue, Helvetica, PingFang SC, Hiragino Sans GB, Microsoft YaHei, Arial, sans-serif;

            .el-tree-node__label {
                font-size: 16px !important;
            }

            box-sizing: border-box;
            border-right: 2px dashed gray;
            width: 230px;
            margin-right: .5rem;
            overflow: scroll;
            scrollbar-width: thin;

            .left-top {
                padding-bottom: .5rem;
            }
        }

        .role-right {
            flex-grow: 1;
            scrollbar-width: thin;
            overflow: scroll;

            .el-table__body-wrapper {
                scrollbar-width: thin;
            }
        }
    }
</style>
