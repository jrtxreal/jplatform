<template>
    <!-- 列表 -->
    <div class="sys-user-list">
        <div class="user-table-list">
            <el-table
                    :data="tableData"
                    stripe
                    :header-cell-style="{background:'#eef1f6',color:'#606266'}"
                    :border="true"
                    style="width: 100%">
                <el-table-column
                        prop="username"
                        label="用户名"
                        width="150">
                </el-table-column>
                <el-table-column
                        prop="nickName"
                        label="姓名"
                        width="150">
                </el-table-column>
                <el-table-column
                        prop="fullPath"
                        label="部门">
                </el-table-column>
                <el-table-column
                        prop="disabled"
                        label="禁用"
                        width="50">
                    <template slot-scope="scope">
                        <span>{{scope.row.disabled? '是':'否'}}</span>
                    </template>
                </el-table-column>
                <el-table-column
                        label="操作"
                        width="100">
                    <template slot-scope="scope">
                        <el-button v-if="udFrameWorkUtil.checkPermit(map.sys_user_edit,scope.row.path)"
                                   @click="userInfo(scope.row)" type="text" size="small">详情</el-button>
                        <el-button v-if="udFrameWorkUtil.checkPermit(map.sys_user_delete,scope.row.path)"
                                   type="text" size="small" @click="delUser(scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <!--  -->
        <div class="footer">
            <el-pagination
                    background
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="currentPage"
                    :page-sizes="[10, 20, 30]"
                    :page-size="pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total">
            </el-pagination>
        </div>
    </div>
</template>
<script>
    import user from "../../../api/user";
    import view from "./view";
    import udFrameWorkUtil from "../../../utils/udFrameWorkUtil";
    import map from "../../../map"
    export default {
        name: 'user-table',
        props: {
            tableDataId: {
                type: Number,
                default: function () {
                    return []
                }
            },
            searchContent: {
                type: String,
            }
        },
        data() {
            return {
                map,
                udFrameWorkUtil,
                tableData: [],
                pageSize: 10,
                total: 200,
                currentPage: 1,
            }
        },
        mounted() {

        },
        watch: {
            tableDataId: {
                handler: function (value, old) {
                    if (value !== 1) {
                        this.getGroupContentTable()
                    }
                },
                immediate: true, //关键
                deep: true,
            },
            searchContent: {
                handler: function (value, old) {
                    this.getGroupContentTable()
                },
                immediate: false, //关键
                deep: true,
            }
            // console.log("111122222",vm.searchContent,vm.tableDataId)

        },
        created() {
            const vm = this

        },
        methods: {
            //添加用户 数据重新请求
            getContent() {
                this.getGroupContentTable()
            },
            //详情
            userInfo(row) {
                let rowLi = {row: row, view: 'view'}
                this.$emit('selectType', rowLi)
            },
            //获取部门内容数据
            getGroupContentTable: function () {
                const vm = this
                user.queryUserOfDept({
                    page: vm.currentPage,
                    size: vm.pageSize,
                    deptId: vm.tableDataId,
                    condition: vm.searchContent
                }).then(data => {
                    vm.tableData = data.result.content
                    // console.log(vm.tableData)
                    vm.total = data.result.totalElements
                }).catch(err => {
                    console.log(err)
                })
            },
            //用户删除
            getUserDelete: function (e) {
                const vm = this
                user.delete(e.id).then(data => {
                    if (data.code == 0) {
                        this.$message({
                            type: 'success',
                            message: '删除成功!'
                        });
                        vm.getGroupContentTable()
                    }
                }).catch(err => {
                    this.$message({
                        type: 'warning',
                        message: '删除失败!'
                    });
                })
            },
            //删除
            delUser(row) {
                const vm = this
                this.$confirm('此操作将永久删除该用户, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    vm.getUserDelete(row)
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            handleSizeChange(val) {
                const vm = this
                vm.pageSize = val
                vm.getGroupContentTable()
            },
            handleCurrentChange(val) {
                const vm = this
                vm.currentPage = val
                vm.getGroupContentTable()
            }
        }
    }
</script>
<style lang="scss" scoped>
    @import '../../../styles/base.scss';
    .sys-user-list{
        .user-table-list {

            .flex {
                display: flex;
            }

            .align-center {
                align-items: center; /*定义body的元素垂直居中*/
            }
        }
        .footer{
            margin-top: .5rem;
        }
    }
</style>
