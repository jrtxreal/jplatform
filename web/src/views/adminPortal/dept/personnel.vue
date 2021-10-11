<template>
    <div class="dept-user">
        <div style="padding-bottom: .5rem;">
            <el-input
                    clearable
                    @clear="query"
                    v-model="condition"
                    placeholder="请输入‘用户名’ 或 ‘显示名称’">
                <el-button  slot="prepend" icon="el-icon-refresh"  @click="initTableData"></el-button>
                <el-button  slot="append" icon="el-icon-search"  @click="query"></el-button>
            </el-input>
        </div>
        <el-table
                :data="tableData"
                stripe
                :header-cell-style="{background:'#eef1f6',color:'#606266'}"
                :border="true"
                style="width: 100%">
            <el-table-column
                    prop="username"
                    label="用户名"
                    width="200">
            </el-table-column>
            <el-table-column
                    label="显示名称"
                    width="200">
                <template slot-scope="scope">
                    {{JSON.parse(scope.row.detInfo||"{}").nickName}}
                </template>
            </el-table-column>
            <el-table-column
                    label="部门">
                <template slot-scope="scope">
                   {{scope.row.fullPath.replace(/\//g,"--")}}
                </template>
            </el-table-column>
        </el-table>
        <div class="block">
            <el-pagination
                    background
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="pageVo.page"
                    :page-sizes="[10, 20, 30]"
                    :page-size="pageVo.size"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="pageVo.total">
            </el-pagination>
        </div>
    </div>
</template>
<script>
    import deptAPI from "../../../api/dept"
    import udStoreUtil from "../../../utils/udStoreUtil";
    import udFrameWorkUtil from "../../../utils/udFrameWorkUtil";

    export default {
        name: 'dep-personnel',
        data() {
            return {
                condition: "",
                conditionCache: "",
                tableData: [],
                pageVo: {
                    page: 1,
                    size: 10,
                    total: 0,
                },
            }
        },
        computed: {
            deptId: {
                get() {
                    return udStoreUtil.getStore("dept.goUserList.deptId");
                }
            },
        },
        created() {
            const vm = this;
            vm.initTableData();
        },
        methods: {
            query() {
                const vm = this;
                vm.conditionCache = vm.condition;
                vm.initTableData();
            },
            initTableData() {
                const vm = this;
                const params = {};
                params.page = vm.pageVo.page;
                params.size = vm.pageVo.size;
                params.deptId = vm.deptId;
                params.condition = vm.conditionCache;
                deptAPI.queryUserOfDept(params).then(d => {
                    vm.tableData = d.result.content;
                    vm.pageVo.page = d.result.number + 1;
                    vm.pageVo.size = d.result.size;
                    vm.pageVo.total = d.result.totalElements;
                }, e => {
                    udFrameWorkUtil.handleEr(vm,e);
                })
            },
            handleSizeChange(val) {
                const vm = this;
                vm.pageVo.size = val;
                vm.initTableData();
            },
            handleCurrentChange(val) {
                const vm = this;
                vm.pageVo.page = val;
                vm.initTableData();
            }
        },
    }
</script>
<style lang="less">
    .dept-user {
        .top {
            .top-input {
                width: 15rem;
            }
        }
        .block{
            margin-top: .5rem;
        }
    }
</style>
