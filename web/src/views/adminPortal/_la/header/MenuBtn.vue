<template>
    <div class="menu-btn" @click="toggleLeft">
        <svg class="icon" style="width: 1.5em;height: 1.5em;vertical-align: middle;fill: currentColor;overflow: hidden;" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2017"><path d="M490.666667 298.666667a42.666667 42.666667 0 0 1 42.666666-42.666667H896a42.666667 42.666667 0 1 1 0 85.333333h-362.666667a42.666667 42.666667 0 0 1-42.666666-42.666666zM85.333333 554.666667a42.666667 42.666667 0 0 1 42.666667-42.666667h768a42.666667 42.666667 0 1 1 0 85.333333H128a42.666667 42.666667 0 0 1-42.666667-42.666666z m0 256a42.666667 42.666667 0 0 1 42.666667-42.666667h768a42.666667 42.666667 0 1 1 0 85.333333H128a42.666667 42.666667 0 0 1-42.666667-42.666666zM213.333333 256a42.666667 42.666667 0 0 0 0 85.333333h85.333334a42.666667 42.666667 0 0 0 0-85.333333H213.333333zM85.333333 298.666667a128 128 0 0 1 128-128h85.333334a128 128 0 0 1 0 256H213.333333a128 128 0 0 1-128-128z" fill="#75C82B" p-id="2018"></path></svg>
    </div>
</template>

<script>
    import udStoreUtil from "../../../../utils/udStoreUtil";
    export default {
        name: "MenuBtn",
        methods:{
            toggleLeft(){
                const storeKey = "la.left.open";
                udStoreUtil.setStore(storeKey,!udStoreUtil.getStore(storeKey));
            }
        }
    }
</script>

<style scoped>

</style>
