<!-- 添加角色下权限 -->
<template>
    <div class="premissWarp">
        <div class="oprate">
            <el-button type="primary" @click="onSubmit" :loading='isSave'>保存</el-button>
            <el-button
                    icon="el-icon-refresh"
                    @click="refreshInfo"
                    :loading="isRefresh"
            ></el-button>
        </div>
        <div class="select-depart" v-infinite-scroll>
            <el-divider content-position="left">
                <div style="color:gray;">当前角色已获取权限</div>
            </el-divider>
            <div class="accountList">
                <el-tag
                        v-for="item in selectedList"
                        :key="item.id"
                        class="accitem"
                        closable
                        @close="handleClose(item)"
                        v-infinite-scroll
                >
                    {{ item.name }}
                </el-tag>
            </div>
        </div>
        <div class="premission">
            <el-divider content-position="left">权限集合</el-divider>
            <div class="check-warp">
                <div
                        class="accountList"
                        v-for="perItem in permissList"
                        :key="perItem.name"
                >
                    <span>{{ perItem.name }}</span>
                    <el-checkbox-group
                            v-model="checkedCities"
                            @change="handleCheckedCitiesChange"
                    >
                        <el-checkbox
                                v-for="item in perItem.list"
                                :label="item.id"
                                :key="item.id"
                                v-model="item.id"
                                @change="(arg) => { checkChang(item,arg)}"
                        >
                            {{ item.name }}
                        </el-checkbox>
                    </el-checkbox-group>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import {
        getPermitsForRole,
        getPermitsForRoleAble,
        savePermitsForRole,
    } from '../../../../api/roleManage';

    export default {
        components: {},
        data() {
            return {
                selectedList: [],
                checkedCities: [],
                oldSelected: [],
                permissList: [],
                isRefresh: false,
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
        computed: {},
        // 监控data中的数据变化
        watch: {},
        methods: {
            //权限删除事件
            handleClose: function (item) {
                this.selectedList.splice(this.selectedList.indexOf(item), 1);
                this.checkedCities.splice(this.checkedCities.indexOf(item.id), 1);
            },
            //已经获取的权限列表
            getPermitsList: function (prma) {
                const vm = this;
                return new Promise(resolve => {
                    getPermitsForRole(prma)
                        .then((data) => {
                            vm.selectedList = data.result;
                            resolve();
                        })
                        .catch((error) => {
                            vm.$message.error('获取权限列表失败，请联系管理员\n' + error);
                        });
                })
            },
            removal: function () {
                //去重
                const vm = this;
                let array = vm.selectedList;
                if (array.length > 0) {
                    for (var i = 0; i < array.length; i++) {
                        for (var j = i + 1; j < array.length;) {
                            if (array[i].id == array[j].id) {
                                array.splice(j, 1); //去除重复的对象；
                            } else {
                                j++;
                            }
                        }
                    }
                } else {
                }
            },
            //集合checkBox channge事件
            handleCheckedCitiesChange(value) {
            },
            //单个checkBox选中事件
            checkChang: function (val,status) {
                const vm = this;
                if(status){
                    vm.selectedList.push(val);
                }else{
                    for(let i=0;i<vm.selectedList.length;i++){
                        const item = vm.selectedList[i];
                        if(item.id === val.id){
                            vm.selectedList.splice(i,1);
                            break;
                        }
                    }
                }

            },
            //保存
            onSubmit: function () {
                const vm = this;
                vm.isSave = true;
                var idsStr = vm.selectedList
                    .map(function (obj, index) {
                        return obj.id;
                    })
                    .join(',');
                var prma = {
                    srcIds: idsStr,
                    roleId: vm.roleId,
                };
                //console.log(prma);
                savePermitsForRole(prma)
                    .then((data) => {
                        //console.log("权限保存");
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
                        vm.$message.error('权限保存失败，请联系管理员\n' + error);
                    });
            },
            checkAble(){
                const vm = this;
                vm.checkedCities = vm.selectedList.map(item=>{return item.id});
            },
            //获取当前账号下可供选择的权限集合
            getAblePermits: function (prma) {
                const vm = this;
                return new Promise(resolve => {
                  getPermitsForRoleAble(prma)
                          .then((data) => {
                            vm.permissList = data.result;
                            resolve();
                          }).catch((error) => {
                            vm.isRefresh = false;
                            vm.$message.error('获取可用权限列表失败，请联系管理员\n' + error);
                   });
                })
            },
            async refreshInfo() {
                const vm = this;
                const prma = {
                    roleId: vm.roleId,
                };
                await vm.getPermitsList(prma);
                await vm.getAblePermits(prma);
                vm.checkAble();
            },
        },
        //  创建之前
        beforeCreate() {
        },
        // 创建完成（可以访问当前this实例）
        created() {
        },
        //  挂载之前
        beforeMount() {
        },
        // 挂载完成（可以访问DOM元素）
        async mounted() {
            const vm = this;
            var prma = {
                roleId: vm.roleId,
            };
            await vm.getPermitsList(prma);
            await vm.getAblePermits(prma);
            vm.checkAble()
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
<style lang="scss" scoped>
    .premissWarp {
        display: flex;
        flex-direction: column;
        padding: 0 20px 0 0;
    }

    .oprate {
        height: auto;
        display: flex;
        justify-content: flex-end;
    }

    .select-depart {
        position: relative;

        .el-divider--horizontal {
            min-height: 150px;
            display: block;
            margin: 24px 0;
            border: 1px solid #dddddd;
            background: #fff !important;
        }

        .accountList {
            position: absolute;
            top: 24px;
            width: 100%;
            height: 150px;
            padding: 15px 0px 0 4%;
            box-sizing: border-box;
            overflow: auto;

            .accitem {
                line-height: 30px;
                white-space: nowrap;
                cursor: pointer;
                margin: .5rem .5rem 0 0;
            }
        }
    }

    .check-warp {
        padding: 0px 10px;

        .accountList {
            margin-bottom: 20px;

            .el-checkbox-group {
                margin: 10px 10px 0;

                .el-checkbox {
                    margin-bottom: 10px;
                }
            }
        }
    }

    //@import url(); 引入公共css类
</style>
