<template>
    <div>
        <h1>测试页面</h1>
     <el-select v-model="selected">
         <el-option value="1">aaa</el-option>
         <el-option value="2">bbb</el-option>
     </el-select>
        <el-button size="large" @click="testLogin">万能的一个按钮</el-button>
    </div>
</template>

<script>
    import map from "../map";
    import {login} from "../api/login"
    import key from "../def/key";
    import udCryptUtil from "../utils/udCryptUtil";
    import udTreeUtil from "../utils/udTreeUtil";
    export default {
        name: map.index,
        data(){
            return{
                selected:1,
            }
        },
        activated(){
            //this.$router.go(0)
        },
        deactivated(){
            console.log('deactivated页面关闭时触发');
        },
        methods:{
            testLogin(){
             window.open("/pages/video.html")
            }
        }
    }
</script>

<style scoped>

</style>
