<template>
    <div class="header">
        <div :style="{display:'flex','flex-grow':leftOpen?1:0}">
            <div class="menu-wrapper">
                <menu-btn></menu-btn>
            </div>
        </div>
        <logo-cmpt v-if="!leftOpen"></logo-cmpt>
        <el-dropdown trigger="click" @command="handleCommand">
            <div class="user-portal-wrapper">
                <user-portal></user-portal>
            </div>
            <el-dropdown-menu slot="dropdown">
                <div style="padding: 1rem;background-color: #ffffff;color: gray;text-align: center;">{{basicUserInfo.nickName}}</div>
                <div style="padding: 1rem;background-color: #ffffff;color: gray;">{{basicUserInfo.username}}</div>
                <el-dropdown-item command = "logout" style="text-align: center;">
                    前台页面
                </el-dropdown-item>
                <el-dropdown-item command = "logout" style="text-align: center;">
                    <i class="el-icon el-icon-right" ></i>退出
                </el-dropdown-item>
            </el-dropdown-menu>
        </el-dropdown>
    </div>
</template>

<script>
    import MenuBtn from "./MenuBtn";
    import LogoCmpt from "../left/LogoCmpt";
    import udStoreUtil from "../../../../utils/udStoreUtil";
    import UserPortal from "./UserPortal";
    import map from "../../../../map";
    import login from "../../../../api/login";
    import udFrameWorkUtil from "../../../../utils/udFrameWorkUtil";

    export default {
        name: "HeaderWrapper",
        components: {UserPortal, LogoCmpt, MenuBtn},
        computed: {
            leftOpen() {
                return udStoreUtil.getStore('la.left.open');
            },
            basicUserInfo(){
                return udStoreUtil.getStore('sessionBo.basicUserInfo');
            }
        },
        methods:{
            handleCommand(key){
                this[key]();
            },
            logout(){
                const vm = this;
                login.logout({token:udStoreUtil.getStore("sessionBo.token")}).then(d=>{
                    this.$router.push({name:map.login})
                },e=>{
                    udFrameWorkUtil.handleEr(vm,e);
                })
            }
        }
    }
</script>

<style scoped>

</style>
