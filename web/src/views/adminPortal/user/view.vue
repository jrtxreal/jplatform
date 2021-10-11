<template>
    <div style="height:100%">
        <el-container>
            <header style="padding: 20px;">
                <el-page-header @back="backIndex" :content="rowLi.fullPath+'_'+rowLi.username+' ('+form.nickName+')'">
                </el-page-header>
            </header>
            <el-main>
                <el-tabs v-model="tabIndex" type="border-card">
                    <el-tab-pane label="基本信息" name="0">
                        <div v-if="tabIndex==='0'"  style="width:50%">
                            <el-form ref="form" :model="form" label-width="90px">
                                <el-form-item label="手机号">
                                    <el-input disabled v-model="form.mobile"></el-input>
                                </el-form-item>
                                <el-form-item label="姓名">
                                    <el-input v-model="form.nickName"></el-input>
                                </el-form-item>
                                <el-form-item label="邮箱">
                                    <el-input v-model="form.email"></el-input>
                                </el-form-item>
                                <el-form-item label="部门">
                                    <el-cascader
                                            style="width: 100%;"
                                            v-model="form.deptId"
                                            :options="cascaderOptions"
                                            :props="cascaderProps"
                                            clearable
                                    ></el-cascader>
                                </el-form-item>
                                <el-form-item label="禁用" style="align-items: center">
                                    <el-switch
                                            v-model="form.disabled">
                                    </el-switch>
                                </el-form-item>
                                <el-form-item>
                                    <el-button style="float: right;" type="primary" @click="onSubmit(1)">保存</el-button>
                                </el-form-item>
                            </el-form>
                        </div>
                    </el-tab-pane>
                    <el-tab-pane  label="密码" name="1">
                        <div v-if="tabIndex==='1'" style="width:50%">
                            <el-form ref="form" :model="form" label-width="90px">
                                <el-form-item label="新密码">
                                    <el-input v-model="form.newUserPwd"></el-input>
                                </el-form-item>
                                <el-form-item label="管理员密码">
                                    <el-input v-model="form.adminPwd"></el-input>
                                </el-form-item>
                                <el-form-item>
                                    <el-button style="float: right;" type="primary" @click="onSubmit(2)">保存</el-button>
                                </el-form-item>
                            </el-form>
                        </div>
                    </el-tab-pane>
                    <el-tab-pane label="角色" name="2">
                        <view-role v-if="tabIndex==='2'" :rowContent="rowLi"></view-role>
                    </el-tab-pane>
                    <el-tab-pane label="组织" name="3">
                        <view-org v-if="tabIndex==='3'" :rowContent="rowLi"></view-org>
                    </el-tab-pane>
                    <el-tab-pane  label="权限" name="4">
                        <view-auth v-if="tabIndex==='4'" :rowContent="rowLi"></view-auth>
                    </el-tab-pane>
                </el-tabs>
            </el-main>
            <el-footer>
            </el-footer>
        </el-container>
    </div>
</template>
<script>
    import viewRole from './viewRole'
    import viewOrg from './viewOrg'
    import viewAuth from './viewAuth'
    import user from "../../../api/user";
    import util from "../../../utils/udTreeUtil";

    export default {
        name: 'user-view',
        components: {viewRole, viewOrg, viewAuth},
        props: {
            rowLi: {
                type: Object,
                default: function () {
                    return []
                }
            }

        },
        data() {
            return {
                tabIndex:'0',
                cascaderOptions:[],
                form: {
                    mobile: '',
                    email: '',
                    disabled: '',
                    newUserPwd: '',
                    adminPwd: '',
                    nickName:'',
                    deptId:0,
                    fillPath:''
                },
                cascaderProps: {
                    emitPath: false,
                    value: 'id',
                    label: 'name',
                    checkStrictly: true
                },
            }
        },
        mounted() {

        },
        created() {
            const vm = this
            const detInfo = JSON.parse(vm.rowLi.detInfo)
            vm.form.mobile = detInfo.phone
            vm.form.email = detInfo.email
            vm.form.nickName =detInfo.nickName
            vm.form.disabled = vm.rowLi.disabled
            vm.form.deptId = vm.rowLi.deptId;
            vm.getAdminDeptOfUser()


        },
        methods: {
            //
            backIndex() {
                let rowLi = {view: 'list'}
                this.$emit('selectType', rowLi)

            },
            getAdminDeptOfUser: function () {
                const vm = this
                user.AdminDeptOfUser({}).then(data => {
                    let ss = util.build2(data.result, 'id', 'pid', 'key', 'ord', 'children')
                    vm.cascaderOptions = ss.topLeaveList
                }).catch(err => {

                })

            },
            getEdit: function () {
                const vm = this
                const detInfo = JSON.parse(vm.rowLi.detInfo)
                vm.rowLi.disabled = vm.form.disabled
                vm.rowLi.deptId = vm.form.deptId;
                detInfo.phone = vm.form.mobile
                detInfo.email = vm.form.email
                detInfo.nickName = vm.form.nickName
                const info = JSON.stringify(detInfo)
                user.edit(
                    {
                        id: vm.rowLi.id,
                        username: vm.rowLi.username,
                        password: '',
                        disabled: vm.rowLi.disabled,
                        detInfo: info,
                        deptId: vm.rowLi.deptId
                    }).then(data => {
                    if (data.code == 0) {
                        this.$message({
                            type: 'success',
                            message: '编辑成功!'
                        });
                    }

                }).catch(err => {
                    this.$message({
                        type: 'warning',
                        message: err
                    });
                })
            },
            //密码修改接口请求
            getPasswordChange: function () {
                const vm = this
                user.passwordChange({
                    newUserPwd: vm.form.newUserPwd,
                    adminPwd: vm.form.adminPwd,
                    userId: vm.rowLi.id

                }).then(data => {
                    if (data.code == 0) {
                        vm.form.newUserPwd = '',
                        vm.form.adminPwd = ''
                        this.$message({
                            type: 'success',
                            message: '密码修改成功!'
                        });
                    }
                }).catch(err => {
                    this.$message({
                        type: 'warning',
                        message: err
                    });

                })
            },
            //保存提交 e：1代表用户信息修改提交  2代表密码修改提交
            onSubmit(e) {
                const vm = this
                if (e == 1) {
                    // console.log('保存')
                    vm.getEdit()
                } else if (e == 2) {
                    vm.getPasswordChange()
                }

            }
        }
    }
</script>
<style lang="scss" scoped>
    .el-container {
        height: 100%;
    }

    .goback:hover {
        color: #13c648;
    }
</style>
