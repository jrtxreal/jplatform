<template>
    <div>

        <el-dialog
                   width="600px" title="添加用户" :visible.sync="addUserVisible1" :close-on-click-modal="false"
                   @close="cancel">
            <!-- 弹出层内容 -->
            <div v-if="addUserVisible1">
                <el-form ref="form" :model="form" label-width="100px">
                    <el-form-item label="部门">
                        <el-cascader
                                style="width: 100%;"
                                v-model="form.department"
                                :options="cascaderOptions"
                                :props="cascaderProps"
                                clearable
                        ></el-cascader>
                    </el-form-item>
<!--                    <el-form-item label="用户名">-->
<!--                        <el-input v-model="form.name"></el-input>-->
<!--                    </el-form-item>-->
                    <el-form-item label="姓名">
                        <el-input v-model="form.nickName"></el-input>
                    </el-form-item>
                    <el-form-item label="密码">
                        <el-input show-password v-model="form.pass"></el-input>
                    </el-form-item>
                    <el-form-item label="手机号">
                        <el-input v-model="form.mobile"></el-input>
                    </el-form-item>
                    <el-form-item label="邮箱">
                        <el-input v-model="form.email"></el-input>
                    </el-form-item>
                </el-form>
            </div>
            <!-- 弹出层底部按钮 -->
            <div slot="footer" class="">
                <el-button @click="cancel">取 消</el-button>
                <el-button @click="addSubmit" type="primary">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>
<script>
    import user from "../../../api/user";
    import util from "../../../utils/udTreeUtil"
    import udStoreUtil from "../../../utils/udStoreUtil";

    export default {

        name: 'user-add',
        props: {
            addUserVisibleF: {
                type: Boolean,
                default: false
            }
        },
        watch: {
            addUserVisibleF(val) {
                this.addUserVisible1 = val
            }
        },
        data() {
            return {
                addUserVisible1: this.addUserVisibleF,
                form: {
                    name: '',
                    pass: '',
                    mobile: '',
                    nickName: '',
                    email: '',
                    department: ''
                },
                cascaderOptions: [],
                cascaderProps: {
                    emitPath: false,
                    value: 'id',
                    label: 'name',
                    checkStrictly: true
                },
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
        mounted() {

        },
        created() {
            const vm = this
            vm.form = vm.addUserForm;
            vm.getAdminDeptOfUser()
        },
        methods: {
            cancel() {
                this.$emit('addCancel')
            },
            //用户添加事件
            addSubmit() {
                // this.$emit('addSubmit',this.form)
                const vm = this
                if (vm.form.department == '' || vm.form.department == null) {
                    vm.showMessageA('部门不能为空', 'warning')
                }  else if (vm.form.nickName == '') {
                    vm.showMessageA('姓名不能为空', 'warning')
                } else if (vm.form.pass == '') {
                    vm.showMessageA('用户密码不能为空', 'warning')
                } else if (vm.form.mobile == '') {
                    vm.showMessageA('电话号码不能为空', 'warning')
                } else if (vm.form.email == '') {
                    vm.showMessageA('邮箱不能为空', 'warning')
                } else {
                    vm.getAddUser()
                }

            },
            //用户添加接口
            getAddUser: function () {
                const vm = this
                user.addUser({
                    password: vm.form.pass,
                    detInfo: JSON.stringify({phone: vm.form.mobile, email: vm.form.email, nickName: vm.form.nickName}),
                    deptId: vm.form.department
                }).then(data => {
                    if (data.code == 0) {
                        vm.showMessageA("用户添加成功", 'success')
                        vm.$emit('addSubmit', vm.form)
                            vm.form.pass = '',
                            vm.form.mobile = '',
                            vm.form.nickName = '',
                            vm.form.email = '',
                            vm.form.department = ''
                    } else {
                    }
                    // vm.$emit('addSubmit',this.form)

                }).catch(err => {
                    vm.showMessageA(err, 'warning')

                })

            },
            getAdminDeptOfUser: function () {
                const vm = this
                user.AdminDeptOfUser({}).then(data => {
                    let ss = util.build2(data.result, 'id', 'pid', 'key', 'ord', 'children')
                    vm.cascaderOptions = ss.topLeaveList
                }).catch(err => {

                })

            },
            showMessageA: function (message, type) {
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
