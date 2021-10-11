<template>
        <div class="la">
            <div v-show="leftOpen" class="left">
                <left-wraper></left-wraper>
            </div>
            <div class="right">
                <header-wraper></header-wraper>
                <main-panel></main-panel>
            </div>
        </div>
</template>

<script>
    import HeaderWraper from "./header/HeaderWrapper";
    import LeftWraper from "./left/leftWrapper";
    import udStoreUtil from "../../../utils/udStoreUtil";
    import MainPanel from "./main/MainPanel";
    export default {
        name: "LayoutWrapper",
        components: {MainPanel, LeftWraper, HeaderWraper},
        computed:{
            leftOpen(){
                return udStoreUtil.getStore('la.left.open');
            }
        },
        methods:{

        }
    }
</script>

<style lang="scss">
@import "../../../styles/adminPortal/la";
</style>
