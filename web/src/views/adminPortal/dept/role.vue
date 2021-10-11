<template>
    <div class="sys-dept-role">
        <div style="padding-bottom: .5rem;">
            <el-button icon="el-icon-refresh" @click="initTableData">刷新</el-button>
        </div>
        <el-table
        :data="tableData"
        stripe
        :header-cell-style="{background:'#eef1f6',color:'#606266'}"
        :border="true"
        style="width: 100%">
            <el-table-column
                prop="name"
                label="角色名"
                width="200">
            </el-table-column>
            <el-table-column
                prop="fullPath"
                label="部门">
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
    import deptAPI from "../../../api/dept";
    import udStoreUtil from "../../../utils/udStoreUtil";
    import udFrameWorkUtil from "../../../utils/udFrameWorkUtil";

    export default {
        name:'dep-personnel',
        data() {
            return {
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
                    return udStoreUtil.getStore("dept.goRoleList.deptId");
                }
            },
        },
        created() {
            const vm = this;
            vm.initTableData();

        },
        methods: {
            initTableData() {
                const vm = this;
                const params = {};
                params.page = vm.pageVo.page - 1;
                params.size = vm.pageVo.size;
                params.deptId = vm.deptId;
                deptAPI.queryRoleOfDept(params).then(d => {
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
<style lang="scss">
.sys-dept-role{
    .el-table__body-wrapper{
        scrollbar-width:thin;
    }
}
</style>
