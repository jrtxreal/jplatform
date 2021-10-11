<template>
    <div>
        <el-form ref="form" :model="form" label-width="100px" label-position="left">
            <el-form-item label="部门名称">
                <el-input v-model="form.name" @input="handleNameInput"></el-input>
            </el-form-item>
            <el-form-item label="上级部门" v-if="!isRoot">
                <el-cascader
                        v-model="form.pid"
                        :options="cascaderOptions"
                        :props="cascaderProps"
                ></el-cascader>
            </el-form-item>
            <el-form-item label="部门路径">
                <el-input type="textarea" :disabled="true" v-model="form.fullPath"></el-input>
            </el-form-item>
            <el-form-item label="排序号">
                <el-input-number v-model="form.ord" :min="0" label=""></el-input-number>
            </el-form-item>
            <el-form-item label="部门类型">
                <el-select v-model="form.deptType" placeholder="">
                    <el-option label="部门" value="部门"></el-option>
                    <el-option label="公司" value="公司"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button style="float: right;" type="primary" @click="save">保存</el-button>
            </el-form-item>
        </el-form>

        <!-- 选择部门 -->
        <el-dialog title="变更上级部门" :visible.sync="departmentVisible" @close="cancel">
            <!-- 弹出层内容 -->

            <!-- 弹出层底部按钮 -->
            <div slot="footer" class="">
                <el-button @click="cancel">取 消</el-button>
                <el-button @click="cancel" type="primary">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>
<script>
    import udStoreUtil from "../../../utils/udStoreUtil";
    import deptAPI from "../../../api/dept";
    import udTreeUtil from "../../../utils/udTreeUtil";
    import udMsgUtil from "../../../utils/udMsgUtil";
    import udFrameWorkUtil from "../../../utils/udFrameWorkUtil";

    // private long id;
    // @NotBlank(message = "请填写部门名称")
    // private String name;
    // private long pid;
    // private int ord;
    // private boolean leaf;
    // @NotBlank(message = "部门类型不能为空")
    // private String deptType;
    // private String path;
    export default {
        name: 'dep-add-son',
        props: {},
        data() {
            return {
                form: {
                    fullPath: "",
                    parentFullPath: "",
                    name: "",
                    pid: -1,
                    ord: 0,
                    leaf: true,
                    deptType: "",
                    path: ""
                },
                departmentVisible: false,
                cascaderOptions: [],
                cascaderProps: {
                    value: 'id',
                    label: 'name',
                    checkStrictly: true
                }
            }
        },
        computed: {
            isRoot: {
                get() {
                    return udStoreUtil.getStore("dept.goAdd.isRoot")
                }
            },
            dialogOpen:{
                get() {
                    return udStoreUtil.getStore("dept.goAdd.dialogOpen");
                },
                set(val) {
                    udStoreUtil.setStore("dept.goAdd.dialogOpen", val);
                },
            },
            parentDeptVo: {
                get() {
                    return udStoreUtil.getStore("dept.goAdd.parentDeptVo");
                },
                set(val) {
                    udStoreUtil.setStore("dept.goAdd.parentDeptVo", val);
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
        watch:{
            isRoot(n,o){
                console.log("ccccc","root发生了变化,变成了:",n);
            },
        },
        created() {
            const vm = this;
            vm.form.pid = vm.parentDeptVo.id;
            vm.form.parentFullPath = vm.parentDeptVo.fullPath;
            vm.form.fullPath = vm.form.parentFullPath;
            vm.form.ord = vm.parentDeptVo.childOrd;
            if (vm.isRoot === false) {
                vm.form.deptType = "部门";
            }else{
                vm.form.deptType = "公司";
            }
            vm.initCascaderOptions();
        },
        methods: {
            handleNameInput() {
                const vm = this;
                vm.form.fullPath = vm.form.parentFullPath + vm.form.name + "/";
            },
            initCascaderOptions() {
                const vm = this;
                deptAPI.queryAdminDeptOfUser({}).then(d => {
                    const result = d.result.filter(item => {
                        return 1 === 1
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
            selectDepartment() {
                this.departmentVisible = true
            },
            cancel() {
                this.departmentVisible = false
            },
            //排序
            handleSortChange(e) {

            },
            //保存
            save() {
                const vm = this;
                deptAPI.addDept(vm.form).then(d => {
                    udMsgUtil.ok(vm,"添加成功");
                    vm.refresh = !vm.refresh;
                    vm.dialogOpen = false;
                }, e => {
                    udFrameWorkUtil.handleEr(vm,e);
                })
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
