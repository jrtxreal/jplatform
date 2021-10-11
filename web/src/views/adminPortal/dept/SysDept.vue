<template>
    <div class="sys-dept">
        <div class="container" v-if="deptList.length">
            <div class="left">
                <div class="left-top">
                    <el-button-group v-if="sessionBo.sa" style="display: flex;width: 100%;">
                        <el-button icon="el-icon-plus" style="flex-grow: 1;" @click="addRootDept">
                            添加根部门
                        </el-button>
                        <el-button   icon="el-icon-refresh" @click="initDeptList">
                        </el-button>
                    </el-button-group>
                        <el-button v-else icon="el-icon-refresh"
                                   style="width: 100%;" @click="refresh=!refresh">
                            刷新
                        </el-button>
                </div>
                <div class="left-bottom">
                    <!-- 部门树 -->
                    <dep-tree
                            ref="deptTree"
                            :tree-data="deptList"
                            :tree-props="treeProps"
                            :current-node-key="currentNodeKey"
                            :default-expand-all="true"
                            :expand-on-click-node="false"
                            :accordion="false"
                            node-key="id"
                            @current-change="change"
                    ></dep-tree>
                </div>
            </div>
            <div class="right">
                <div class="right-inner">
                    <div class="top">
                        <div style="display: flex;align-items: center;">
                            <el-page-header :content="treeIdMap[currentNodeKey].name">
                                <div slot="title"></div>
                            </el-page-header>
                        </div>
                        <div class="top-inner">
                            <el-button-group class="dep-button-group">
                                <el-button @click="addSubDept" :type="btnTab==1?'primary':''">添加子部门</el-button>
                                <el-button @click="actionTab(2)" :type="btnTab==2?'primary':''">编辑</el-button>
                                <el-button @click="queryUser()" :type="btnTab==3?'primary':''">人员</el-button>
                                <el-button @click="queryRole()" :type="btnTab==4?'primary':''">角色</el-button>
                            </el-button-group>
                        </div>
                    </div>
                    <div class="main">
                        <!-- 添加子部门 -->
                        <div class="" v-if="btnTab===1">
                            <dep-add-son></dep-add-son>
                        </div>
                        <!-- 编辑 -->
                        <div class="" v-if="btnTab===2">
                            <dep-edit-son></dep-edit-son>
                        </div>
                        <!-- 人员 -->
                        <div class="main-content" v-if="btnTab===3">
                            <dep-personnel></dep-personnel>
                        </div>
                        <!-- 角色 -->
                        <div class="main-content" v-if="btnTab===4">
                            <dep-role></dep-role>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="container" v-if="!deptList.length">
            <div class="nodata">
                <el-button icon="el-icon-plus">没有任何部门，去添加部门</el-button>
            </div>
        </div>
        <!--组织机构弹窗-->
        <el-dialog
                title="提示"
                :visible.sync="dialogOpen"
                width="600px"
                :before-close="handleClose">
            <dep-add-son v-if="dialogOpen"></dep-add-son>
        </el-dialog>
    </div>
</template>
<script>
    import depTree from './tree';
    import depAddSon from './add';
    import depEditSon from './edit';
    import depPersonnel from './personnel';
    import depRole from './role';
    import map from '../../../map'
    import deptAPI from "../../../api/dept"
    import udMsgUtil from "../../../utils/udMsgUtil";
    import udTreeUtil from "../../../utils/udTreeUtil";
    import udStoreUtil from "../../../utils/udStoreUtil";
    import lodash from "lodash"
    import udFrameWorkUtil from "../../../utils/udFrameWorkUtil";

    export default {
        name: map.sys_dept_query,
        components: {depTree, depAddSon, depEditSon, depPersonnel, depRole},
        data() {
            return {
                currentNodeKey: 0,
                current: [],
                deptList: [],
                treeProps: {children: "children", label: "name"},
                treeIdMap: {},
            }
        },
        computed: {
            btnTab: {
                get() {
                    return udStoreUtil.getStore("dept.btnTab");
                },
                set(val) {
                    udStoreUtil.setStore("dept.btnTab", val);
                },
            },
            deptId: {
                get() {
                    return udStoreUtil.getStore("dept.goUserList.deptId");
                },
                set(val) {
                    udStoreUtil.setStore("dept.goUserList.deptId", val);
                },
            },
            deptId2: {
                get() {
                    return udStoreUtil.getStore("dept.goRoleList.deptId");
                },
                set(val) {
                    udStoreUtil.setStore("dept.goRoleList.deptId", val);
                },
            },
            dialogOpen: {
                get() {
                    return udStoreUtil.getStore("dept.goAdd.dialogOpen");
                },
                set(val) {
                    udStoreUtil.setStore("dept.goAdd.dialogOpen", val);
                },
            },
            isRoot: {
                get() {
                    return udStoreUtil.getStore("dept.goAdd.isRoot");
                },
                set(val) {
                    udStoreUtil.setStore("dept.goAdd.isRoot", val);
                },
            },
            isRootCache: {
                get() {
                    return udStoreUtil.getStore("dept.goAdd.isRootCache");
                },
                set(val) {
                    udStoreUtil.setStore("dept.goAdd.isRootCache", val);
                },
            },
            deptVo: {
                get() {
                    return udStoreUtil.getStore("dept.goEdit.deptVo");
                },
                set(val) {
                    udStoreUtil.setStore("dept.goEdit.deptVo", val);
                },
            },
            parentDeptVo: {
                get() {
                    return udStoreUtil.getStore("dept.goAdd.parentDeptVo");
                },
                set(val) {
                    udStoreUtil.setStore("dept.goAdd.parentDeptVo", val);
                },
            },
            parentDeptVoCache: {
                get() {
                    return udStoreUtil.getStore("dept.goAdd.parentDeptVoCache");
                },
                set(val) {
                    udStoreUtil.setStore("dept.goAdd.parentDeptVoCache", val);
                },
            },
            refresh: {
                get() {
                    return udStoreUtil.getStore("dept.refresh");
                },
                set() {
                     udStoreUtil.setStore("dept.refresh",!udStoreUtil.getStore("dept.refresh"));
                }
            },
            sessionBo: {
                get() {
                    return udStoreUtil.getStore("sessionBo");
                }
            }
        },
        watch: {
            refresh(n, o) {
                const vm = this;
                vm.initDeptList();
            }
        },
        created() {
            const vm = this;
            vm.initDeptList();
        },
        methods: {
            handleClose(done) {
                const vm = this;
                vm.parentDeptVo = vm.parentDeptVoCache;
                vm.isRoot = vm.isRootCache;
                done();
            },
            addRootDept() {
                const vm = this;
                // 先把当前的deptVo状态信息以及是否为添加根节点信息保存下拉
                vm.parentDeptVoCache = vm.parentDeptVo;
                vm.isRootCache = vm.isRoot;
                // 然后再对信息做改变
                vm.isRoot = true;
                let ord = 1;
                if (vm.deptList.length) {
                    ord = vm.deptList.length + 1;
                }
                const parentDeptVo = {};
                parentDeptVo.pid = 0;
                parentDeptVo.childOrd = ord;
                parentDeptVo.fullPath = "/";
                vm.parentDeptVo = parentDeptVo;
                vm.dialogOpen = true;
            },
            addSubDept() {
                // 添加子部门
                const vm = this;
                vm.isRoot = false;
                const copy = lodash.cloneDeep(vm.treeIdMap[vm.currentNodeKey]);
                let ord = 1;
                if (copy.children && copy.children.length) {
                    ord = copy.children.length + 1;
                }
                delete copy.children;
                copy.childOrd = ord;
                vm.parentDeptVo = copy;
                vm.btnTab = 1;
            },
            editDept(deptVo) {
                const vm = this;
                const copy = lodash.cloneDeep(deptVo);
                delete copy.children
                vm.deptVo = copy;
                vm.btnTab = 2;
            },
            queryUser() {
                const vm = this;
                vm.deptId = vm.currentNodeKey;
                vm.btnTab = 3;
            },
            queryRole() {
                const vm = this;
                vm.deptId2 = vm.currentNodeKey;
                vm.btnTab = 4;
            },
            initDeptList() {
                const vm = this;
                const selected = vm.currentNodeKey;
                deptAPI.queryAdminDeptOfUser({}).then(d => {
                    if (d.result && d.result.length) {
                        const obj = udTreeUtil.build2(d.result, "id", "pid", null, "ord", "children");
                        vm.deptList = obj.topLeaveList;
                        vm.treeIdMap = obj.idMap;
                        if (!selected || !vm.treeIdMap[selected]) {
                            vm.currentNodeKey = vm.deptList[0].id;
                            const vo = vm.treeIdMap[vm.currentNodeKey];
                            vm.editDept(vo);
                        } else {
                            const vo = vm.treeIdMap[selected];
                            vm.editDept(vo);
                            vm.$nextTick().then(() => {
                                vm.$refs.deptTree.$children[0].setCurrentKey(selected);
                            });

                        }


                    }
                }, e => {
                    udFrameWorkUtil.handleEr(vm, e);
                })
            },
            change(data) {
                const vm = this;
                vm.currentNodeKey = data.id;
                vm.editDept(data);
            },
            //操作按钮
            actionTab(index) {
                this.btnTab = index
            },

        }
    }
</script>
<style lang="scss">
    @import '../../../styles/base.scss';

    .sys-dept {
        .el-page-header__left{
            display: none;
        }
        .el-cascader, .el-textarea, .el-input-number, .el-select {
            width: calc(100% - 1px)
        }

        height: calc(100% - 20px);
        width: calc(100% - 20px);
        margin-top: 10px;
        margin-left: 10px;

        .el-dialog__wrapper {
            scrollbar-width: thin;
        }

        .container {
            display: flex;
            height: 100%;
            width: 100%;
            align-items: stretch;

            .nodata {
                height: 100%;
                width: 100%;
                display: flex;
                justify-content: center;
                align-items: center;
            }

            .left {
                width: 250px;
                border-right: 2px dashed darkgray;
                margin-right: .75rem;

                .left-top {
                    padding-right: .5rem;
                    padding-left: .75rem;
                    display: flex;
                }

                .left-bottom {
                    width: 100%;
                    overflow-y: scroll;
                    scrollbar-width: thin;
                    display: flex;
                    border-right: none;
                    align-items: stretch;

                    .el-tree {
                        padding: .3rem 0 .75rem .75rem;
                        width: 100%;
                        background-color: transparent;
                    }
                }
            }

            .right {
                flex-grow: 1;
                //border: 2px dashed #eeeeee;
                scrollbar-width: thin;
                overflow-y: scroll;

                .right-inner {
                    padding: 1rem;

                    .top {
                        margin-bottom: 1rem;
                        display: flex;

                        .top-inner {
                            justify-content: flex-end;
                            flex-grow: 1;
                            .dep-button-group {
                                height: 100%;
                                display: flex;
                                align-items: center; /*定义body的元素垂直居中*/
                                justify-content: flex-end; /*定义body的里的元素水平靠右*/
                            }
                        }
                    }

                    .main {

                        display: flex;

                        > div {
                            padding: 2rem 0 1rem 0;
                        }

                        .main-content {
                            padding-top: 0;
                        }
                    }
                }
            }
        }
    }
</style>
