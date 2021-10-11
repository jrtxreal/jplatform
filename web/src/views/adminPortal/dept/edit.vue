<template>
    <el-form ref="form" :model="form" label-width="100px" label-position="left">
        <el-form-item label="部门名称">
            <el-input v-model="form.name" @input="handleNameInput"></el-input>
        </el-form-item>
        <el-form-item label="上级部门">
            <!-- <el-input v-model="form.name" :disabled="true">
                <el-button slot="append" class="btn-select-department" type="primary">变更</el-button>
            </el-input> -->
            <el-cascader
                    v-model="form.pid"
                    :disabled="optionsEmpty"
                    :options="cascaderOptions"
                    :props="cascaderProps"
            ></el-cascader>
        </el-form-item>
        <el-form-item label="部门路径">
            <el-input type="textarea" disabled v-model="form.fullPath"></el-input>
        </el-form-item>
        <el-form-item label="排序号">
            <el-input-number v-model="form.ord" :min="0" label=""></el-input-number>
        </el-form-item>
        <el-form-item label="部门类型">
            <div>{{form.deptType}}</div>
        </el-form-item>
        <el-form-item label="创建时间">
            <div>{{form.createTime}}</div>
        </el-form-item>
        <el-form-item label="最后修改">
            <div>{{form.lastUpdate}}</div>
        </el-form-item>
        <el-form-item label="最后修改人">
            <div>{{form.updateBy}}</div>
        </el-form-item>
        <el-form-item>
            <el-button style="float:right;margin-left: .5rem;" type="danger" @click="deleteDept">删除</el-button>
            <el-button style="float: right;" type="primary" @click="save">保存</el-button>
        </el-form-item>
    </el-form>
</template>
<script>
    import udStoreUtil from "../../../utils/udStoreUtil";
    import deptAPI from "../../../api/dept";
    import udTreeUtil from "../../../utils/udTreeUtil";
    import udMsgUtil from "../../../utils/udMsgUtil";
    import udFrameWorkUtil from "../../../utils/udFrameWorkUtil";

    export default {
        name: 'dep-edit-son',
        props: {},
        data() {
            return {
                form: {},
                optionsEmpty: true,
                cascaderOptions: [],
                treeIdMap: {},
                cascaderProps: {
                    value: 'id',
                    label: 'name',
                    checkStrictly: true
                }
            }
        },
        computed: {
            deptVo: {
                get() {
                    return udStoreUtil.getStore("dept.goEdit.deptVo");
                }
            },
            refresh: {
                get() {
                    return udStoreUtil.getStore("dept.refresh");
                },
                set(val){
                    udStoreUtil.setStore("dept.refresh",val);
                }
            },
        },
        watch: {
            deptVo(n, o) {
                const vm = this;
                vm.form = n;
                vm.initCascaderOptions();
            },
        },
        created() {
            const vm = this;
            vm.form = vm.deptVo;
            vm.initCascaderOptions();
        },
        mounted() {

        },
        methods: {
            handleNameInput() {
                const vm = this;
                let namePath = vm.form.fullPath;
                namePath = namePath.substr(0, namePath.lastIndexOf("/", namePath.length - 2));
                namePath = namePath + "/" + vm.form.name + "/";
                vm.form.fullPath = namePath;
            },
            initCascaderOptions() {
                const vm = this;
                deptAPI.queryAdminDeptOfUser({}).then(d => {
                    const result = d.result.filter(item => {
                        return !(item.path.indexOf(vm.form.path) > -1)
                    })
                    if (result && result.length) {
                        const obj = udTreeUtil.build2(result, "id", "pid", null, "ord", "children");
                        vm.cascaderOptions = obj.topLeaveList;
                        vm.optionsEmpty = false;
                        vm.treeIdMap = obj.idMap;
                    } else {
                        vm.optionsEmpty = true;
                        vm.cascaderOptions = [];
                    }

                }, e => {
                    udFrameWorkUtil.handleEr(vm,e);
                })
            },
            //保存
            save() {
                const vm = this;
                let r = vm.form.pid;
                if(r && r instanceof Array){
                    vm.form.pid = r[r.length -1];
                }
                deptAPI.editDept(vm.form).then(d => {
                    udMsgUtil.ok(vm, "修改成功");
                    vm.refresh = !vm.refresh;
                }, e => {
                    udFrameWorkUtil.handleEr(vm,e);
                })
            },
            //删除
            deleteDept(){
                const vm = this;
                vm.$confirm('此操作将永久删除该部门, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    deptAPI.deleteDept({deptId:vm.form.id}).then(d=>{
                        udMsgUtil.ok(vm, "删除成功");
                        vm.refresh = !vm.refresh;
                    },e=>{
                        udFrameWorkUtil.handleEr(vm,e);
                    })
                }).catch(() => {});
            }
        }
    }
</script>
<style lang="scss" scoped>
    @import '../../../styles/base.scss';

    .btn-select-department {
        background-color: #1867c0 !important;
        color: #fff !important;
    }
</style>
