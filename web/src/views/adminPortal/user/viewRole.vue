<template>
    <div>
        <el-row class="align-center" type="flex" justify="space-between">
            <el-col :span="12">
            </el-col>
            <el-col :span="6" class="text-right">
                <el-button icon="el-icon-refresh" @click="refreshRole"
                ></el-button>
                <el-button type="primary" @click="onSubmit">保存</el-button>
            </el-col>
        </el-row>
        <!-- 已有角色 -->
        <div class="border-left border-right border-bottom">
            <el-divider content-position="left">已有角色</el-divider>
            <div class="padding-lr">
                <div v-if="!selectaccList.length&&!selectaccList2.length" style="text-align: center;font-size: 14px;color:#909399 ">暂无数据</div>
                <el-tag
                        v-for="item in selectaccList"
                        :key="item.id"
                        :closable="true"
                        @close="handleClose(item)"
                        class="margin-lr-sm margin-bottom"
                >
                    {{item.name }}@{{ item.fullPath}}
                </el-tag>
                <el-tag
                        v-for="item in selectaccList2"
                        :key="item.id"
                        :closable="false"
                        class="margin-lr-sm margin-bottom"
                >
                    {{item.name }}@{{ item.fullPath}}
                </el-tag>
            </div>
        </div>
        <!-- 所有角色 -->
        <div class="border-left border-right border-bottom">
            <el-divider content-position="left">所有角色</el-divider>
            <div class="padding-lr">
                <el-table
                        row-key="roleId"
                        ref="multipleTableRole"
                        :data="tableData"
                        tooltip-effect="dark"
                        style="width: 100%;min-height: 300px"
                        @selection-change="handleSelectionChange"
                        border
                >
                    <el-table-column type="selection" width="55"></el-table-column>
                    <el-table-column
                            prop="name"
                            label="角色名"
                            width="120"
                    ></el-table-column>
                    <el-table-column
                            prop="fullPath"
                            label="部门"
                            show-overflow-tooltip
                    ></el-table-column>
                </el-table>
                <!--                <div class="border margin-tb-sm padding-lr-sm">-->
                <!--                    <el-pagination-->
                <!--                        background-->
                <!--                        @size-change="handleSizeChange"-->
                <!--                        @current-change="handleCurrentChange"-->
                <!--                        :current-page="currentPage"-->
                <!--                        :page-sizes="[100, 200, 300, 400]"-->
                <!--                        :page-size="100"-->
                <!--                        layout="total, sizes, prev, pager, next, jumper"-->
                <!--                        :total="400">-->
                <!--                    </el-pagination>-->
                <!--                </div>-->
            </div>
        </div>
    </div>
</template>
<script>
    import user from "../../../api/user";
    import util from "../../../utils/udTreeUtil";
    import lodash from 'lodash';
    export default {
        name: 'view-role',
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
                selectaccList: [], // 可删除部分
                selectaccList2:[], // 不可删除部分
                tableData: [],
                currentPage: 1,
                roleIds: "",
                isRefresh: false,
            }
        },
        async mounted() {
            const vm = this
            await vm.getRoleOfUser();
            await vm.getAllRoleOfUser();
            vm.tableCheck();

        },
        methods: {
            // 关闭标签
            handleClose(e) {
                const vm = this
                for (let i = 0; i < vm.selectaccList.length; i++) {
                    const item = vm.selectaccList[i];
                    if (item.roleId === e.roleId) {
                        vm.selectaccList.splice(i, 1);
                        return;
                    }
                }
                vm.tableCheck();
            },
            //获取当前角色
            getRoleOfUser: function () {
                const vm = this
                return new Promise(resolve => {
                    user.queryRoleOfUser({userId: vm.rowContent.id}).then(data => {
                        if (data.code == 0) {
                            vm.selectaccList = data.result.filter(item=>{return item.canDelete});
                            vm.selectaccList2 = data.result.filter(item=>{return !item.canDelete});
                            resolve();
                        }
                    }).catch(err => {
                        vm.$message({
                            type: 'warning',
                            message: err
                        });
                    })
                });
            },
            //获取所有角色
            getAllRoleOfUser: function () {
                const vm = this
                return new Promise(resolve => {
                    user.queryAssignRoleForCurrentUser({}).then(data => {
                        if (data.code === 0) {
                            vm.tableData = data.result
                            resolve();
                        }
                    }).catch(err => {
                        vm.$message({
                            type: 'warning',
                            message: err
                        });
                    })
                })
            },
            // 分页
            // handleSizeChange(val) {
            //     console.log(`每页 ${val} 条`);
            // },
            // handleCurrentChange(val) {
            //     console.log(`当前页: ${val}`);
            // },

            tableCheck: function () {
                const vm = this
                const rm = {};
                const ar = vm.selectaccList;
                ar.forEach(item => {
                    rm[item.roleId] = true;
                });
                console.log("江山笑",vm.tableData);
                console.log("江山笑",rm);
                vm.tableData.forEach(item => {
                    if (rm[item.roleId]) {
                        vm.$refs.multipleTableRole.toggleRowSelection(item,true);
                    }else{
                        vm.$refs.multipleTableRole.toggleRowSelection(item,false);
                    }
                });
            },
            async refreshRole() {
                const vm = this
                await vm.getAllRoleOfUser();
                vm.getRoleOfUser();
            },
            onSubmit() {
                const vm = this
                vm.getAssignRoleToUser()

            },
            getAssignRoleToUser: function () {
                const vm = this
                vm.roleIds = vm.selectaccList.map(item => {
                    return item.roleId
                }).join(",");
                user.assignRoleToUser({userId: vm.rowContent.id, roleIds: vm.roleIds}).then(data => {
                    if (data.code == 0) {
                        vm.$message({
                            type: 'success',
                            message: '保存成功!'
                        });
                        vm.getRoleOfUser()
                    }
                }).catch(err => {
                    vm.$message({
                        type: 'warning',
                        message: err
                    });
                })
            },
            handleSelectionChange(val) {
                const vm = this;
                vm.selectaccList = lodash.cloneDeep(val);
            },
        }
    }
</script>
<style lang="scss" scoped>
    @import '../../../styles/base.scss';

</style>
