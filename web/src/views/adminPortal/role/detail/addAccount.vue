<!-- 添加角色下的用户 -->
<template>
    <div class="role-account-warp">
        <!-- <div class="oprate">
          <el-button type="primary" @click="onSubmit">保存</el-button>
        </div> -->
        <div style="text-align: right;padding-bottom: .5rem;">
            <el-button type="primary" @click="onSubmit">保存</el-button>
            <el-button icon="el-icon-refresh" @click="refreshInfo"></el-button>
        </div>
        <div class="select-account">
            <el-divider content-position="left">
                <div style="color: gray;">当前角色下的用户</div>
            </el-divider>
            <div class="accountList">
                <div v-if="selectaccList.length==0"
                     style="text-align: center;font-size: 14px;color:#909399;padding-bottom: 1.5rem;">暂无数据
                </div>
                <el-tag
                        v-for="item in selectaccList"
                        :key="item.id"
                        :closable="item.canDelete"
                        @close="handleClose(item)"
                        class="accitem"
                >
                    {{ item.nickName + "(" + item.username +")" +"@"+item.fullPath}}
                </el-tag>
                <!--无权修改的用户-->
                <el-tag
                        v-for="item in selectaccList2"
                        :key="item.id"
                        class="accitem"
                >
                    {{ item.nickName + "(" + item.username +")" +"@"+item.fullPath}}
                </el-tag>
            </div>
        </div>
        <div style="padding-top: .5rem;">
            <div class="serchForm">
                    <div class="item">
                        <el-input  placeholder="请输入用户名或姓名" v-model="form.accountName"></el-input>
                    </div>
                    <div class="item">
                        <el-cascader
                                placeholder="请选择部门"
                                v-model="form.depart"
                                :options="options"
                                :props="cascaderPropsmn"
                                :show-all-levels="false"
                                @change="handleChange"
                        ></el-cascader>
                    </div>
                    <div class="item">
                        <el-button @click="onSearch()">查询</el-button>
                    </div>
            </div>
            <div class="search">
                <el-table
                        ref="multipleTable"
                        :data="tableData"
                        tooltip-effect="dark"
                        style="width: 100%"
                        @selection-change="handleSelectionChange"
                        border
                        stripe
                >
                    <el-table-column type="selection" width="55"></el-table-column>
                    <el-table-column
                            prop="username"
                            label="用户名"
                            width="120"
                    ></el-table-column>
                    <el-table-column
                            prop="nickName"
                            label="姓名"
                            width="120"
                    ></el-table-column>
                    <el-table-column
                            prop="fullPath"
                            label="部门"
                            show-overflow-tooltip
                    ></el-table-column>
                </el-table>
                <div style="padding-top: .5rem;">
                    <el-pagination
                            background
                            layout="prev, pager, next"
                            :total="userTotal"
                            :page-size="10"
                            @size-change="handleSizeChange"
                            @current-change="handleCurrentChange"
                            :current-page.sync="current"
                    ></el-pagination>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import {
        getAcc,
        getAccForRole,
        getDeptTreeOfM,
        saveAccForRole,
    } from '../../../../api/roleManage';
    import udTree from '../../../../utils/udTreeUtil';
    import lodash from "lodash";

    export default {
        components: {},
        data() {
            return {
                tableData: [],
                isRefresh: false,
                cascaderPropsmn: {
                    value: 'id',
                    label: 'name',
                    children: 'children',
                    multiple: false,
                    emitPath: false,
                    checkStrictly: true,
                },
                selectaccList: [],
                selectaccList2: [],
                tableCheckCache: [],
                multipleSelection: [], // id
                form: {
                    accountName: '',
                    depart: 0,
                },
                current: 1,
                userTotal: 10,
                options: [],
                isSave: false
            };
        },
        props: {
            roleId: {
                type: Number,
                default: 0,
            },
        },
        // 监听属性 类似于data概念
        computed: {
            //
        },
        // 监控data中的数据变化
        watch: {
            multipleSelection: {
                handler(newVal) {
                    this.multipleSelection = newVal;
                    this.lasterSelect();
                },
                // immediate: true,

                deep: true,
            },
        },
        methods: {
            // 获得当前角色的用户
            getList() {
            },
            tableCheck() {
                const vm = this
                const rm = {};
                const ar = vm.selectaccList;
                ar.forEach(item => {
                    rm[item.id] = true;
                });
                vm.tableData.forEach(item => {
                    if (rm[item.id]) {
                        vm.$refs.multipleTable.toggleRowSelection(item,true);
                    }else{
                        vm.$refs.multipleTable.toggleRowSelection(item,false);
                    }
                });
            },

            // 关闭标签
            handleClose(e) {
                const vm = this
                for (let i = 0; i < vm.selectaccList.length; i++) {
                    const item = vm.selectaccList[i];
                    if (item.id === e.id) {
                        vm.selectaccList.splice(i, 1);
                        vm.tableCheck();
                        return;
                    }
                }
            },
            // 查询
            onSearch() {
                const vm = this;
                var prma2 = {
                    size: 10,
                    page: 1,
                    username: vm.form.accountName,
                    deptId: vm.form.depart,
                };
                vm.getAccount(prma2);
            },

            // 重置
            resetForm(formName) {
                const vm = this;
                this.$refs[formName].resetFields();
                var prma2 = {
                    size: 10,
                    page: 1,
                    username: '',
                    deptId: '',
                };
                vm.getAccount(prma2);
            },
            //zmn
            // removal: function () {
            //   //去重
            //   const vm = this;
            //   let array = vm.selectaccList;
            //   if (array.length > 0) {
            //     for (var i = 0; i < array.length; i++) {
            //       for (var j = i + 1; j < array.length; ) {
            //         if (array[i].id == array[j].id) {
            //           array.splice(j, 1); //去除重复的对象；
            //         } else {
            //           j++;
            //         }
            //       }
            //     }
            //   } else {
            //   }
            // },
            // 保存
            onSubmit() {
                const vm = this;
                vm.isSave = true;
                let array = vm.selectaccList;
                var ids = [];
                for (let index = 0; index < array.length; index++) {
                    const element = array[index];
                    ids.push(element.id);
                }
                console.log(ids.join(","));
                var prma = {
                    userIds: ids.join(','),
                    roleId: vm.roleId,
                };
                vm.saveInfoForUser(prma);
            },
            //zmn保存用户信息
            saveInfoForUser: function (prma) {
                const vm = this;
                saveAccForRole(prma)
                    .then((data) => {
                        //console.log(data);
                        vm.isSave = false;
                        if (data.code == 0) {
                            vm.$message({
                                message: '保存成功',
                                type: 'success',
                            });
                        }
                    })
                    .catch((error) => {
                        vm.isSave = false;
                        vm.$message.error('保存失败，请联系管理员\n' + error);
                    });
            },
            // 用户选择
            handleSelectionChange(val) {
                const vm = this;
                // 根据id作为key
                const fn = (ar) => {
                    let m = {};
                    ar.forEach(i => {
                        m[i.id] = i;
                    })
                    return m;
                }
                if (val.length > vm.tableCheckCache.length) {
                    const m1 = fn(vm.tableCheckCache);
                    for (let i = 0; i < val.length; i++) {
                        const item = val[i];
                        if (!m1[item.id]) {
                            let has = false;
                            for(let j=0;j<vm.selectaccList.length;j++){
                                const item2 = vm.selectaccList[j];
                                if(item2.id===item.id){
                                    has = true;
                                    break;
                                }
                            }
                            if(!has){
                                vm.selectaccList.splice(vm.selectaccList.length,1,item);
                            }
                            break;
                        }
                    }
                    vm.tableCheckCache = val;
                } else if (val.length < vm.tableCheckCache.length) {
                    const m2 = fn(val);
                    for (let i = 0; i < vm.tableCheckCache.length; i++) {
                        const item = vm.tableCheckCache[i];
                        if (!m2[item.id]) {
                            for (let j = 0; j < vm.selectaccList.length; j++) {
                                const item2 = vm.selectaccList[j];
                                 if(item2.id===item.id){
                                     vm.selectaccList.splice(j,1);
                                     break;
                                 }
                            }
                            break;
                        }
                    }
                    vm.tableCheckCache = val;
                }
            },
            // 页码
            async handleCurrentChange(index) {
                //console.log(index);
                const vm = this;
                var prma2 = {
                    size: 10,
                    page: index,
                    username: '',
                    deptId: '',
                };
                await vm.getAccount(prma2);
                vm.tableCheck();
            },
            handleSizeChange() {
            },
            //zmn获取用户
            getAccount: function (prma) {
                const vm = this;
                return new Promise(resolve => {
                    getAcc(prma).then((data) => {
                        vm.tableData = data.result.content;
                        vm.userTotal = data.result.totalElements;
                        vm.tableCheckCache = [];
                        resolve();
                        if (vm.isRefresh) {
                            vm.isRefresh = false;
                        }
                    }, e => {
                    });
                })
            },
            //zmn获取当前角色下用户
            getAccForRole: function (prma) {
                const vm = this;
                return new Promise(resolve => {
                    getAccForRole(prma)
                        .then((data) => {
                            vm.selectaccList = data.result.filter(item => {
                                return item.canDelete
                            });
                            vm.selectaccList2 = data.result.filter(item => {
                                return !item.canDelete
                            });
                            resolve();
                        }, e => {
                        });
                })
            },
            //获取所管理的部门
            getDeptTreeList: function () {
                const vm = this;
                getDeptTreeOfM()
                    .then((data) => {
                        //console.log(data);
                        if (data.code == 0) {
                            let roleList = udTree.build2(
                                data.result,
                                'id',
                                'pid',
                                null,
                                'ord',
                                'children',
                            );
                            //console.log(roleList);
                            if (roleList) {
                                vm.options = roleList.topLeaveList;
                            }
                        }
                    })
                    .catch((error) => {
                        vm.$message.error('获取部门列表失败，请联系管理员\n' + error);
                    });
            },
            handleChange(value) {
                const vm = this;
                if (value) {
                    //vm.selectDepatList = value;
                } else {
                    //vm.selectDepatList = [];
                }
            },
            async refreshInfo() {
                const vm = this;
                var prma2 = {
                    size: 10,
                    page: 1,
                    username: '',
                    deptId: '',
                };
                await vm.getAccount(prma2);
                var prma = {
                    roleId: vm.roleId,
                };
                await vm.getAccForRole(prma);
            },
        },
        async mounted() {
            const vm = this;
            var prma = {
                roleId: vm.roleId,
            };
            await vm.getAccForRole(prma);
            var prma2 = {
                size: 10,
                page: 1,
                username: '',
                deptId: '',
            };
            await vm.getAccount(prma2);
            vm.tableCheck();
            vm.getDeptTreeList();
            //this.lasterSelect();
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
    .role-account-warp {
        .serchForm {
            display: flex;
            .item{
                padding-bottom: .5rem;
                padding-right: .5rem;
            }
        }

        .oprate {
            height: auto;
            display: flex;
            justify-content: flex-end;
            padding: 0 0 15px;
        }

        .select-account {
            min-width: 800px;
            border: 1px solid #dddddd;
            border-top: none;
            display: flex;
            flex-direction: column;

            .el-divider--horizontal {
                margin-top: 0px;
            }

            .accountList {
                .accitem {
                    margin-left: 10px;
                    margin-right: 10px;
                    margin-bottom: 20px;
                }
            }
        }

        .el-form {
            .el-form-item {
                margin-right: 30px;

                .el-input {
                    width: 150px !important;
                }

                .el-cascader {
                    width: 150px !important;
                }
            }
        }
    }
</style>
