<template>
    <div class="main">
        <div class="tab">
            <el-tabs v-model="activeTab" type="card" @tab-remove="removeTab" @tab-click="clickTab">
                <el-tab-pane
                        :key="item.name"
                        v-for="item in navTabs"
                        :label="item.title"
                        :name="item.name"
                        :closable="item.closeable"
                >
                </el-tab-pane>
            </el-tabs>
        </div>
        <div class="content" style="scrollbar-width:thin;">
            <keep-alive :include="cached">
                <router-view></router-view>
            </keep-alive>
        </div>
    </div>
</template>

<script>
    import udStoreUtil from "../../../../utils/udStoreUtil";

    export default {
        name: "MainPanel",
        data() {
            return {}
        },
        computed: {
            cached: {
                get() {
                    return udStoreUtil.getStore("nav.cached")
                },
                set(val) {
                    udStoreUtil.setStore("nav.cached", val)
                }
            },
            activeTab: {
                get() {
                    return udStoreUtil.getStore("nav.activeTab")
                },
                set(val) {
                    udStoreUtil.setStore("nav.activeTab", val)
                }
            },
            navTabs: {
                get() {
                    return udStoreUtil.getStore("nav.navTabs")
                },
                set(val) {
                    udStoreUtil.getStore("nav.navTabs", val)
                }
            }
        },
        watch: {
            activeTab(n, o) {
                const vm = this;
                vm.$router.push({name: n});
            }
        },
        methods: {
            removeTab(tabName) {
                const vm = this;
                let tabs = vm.navTabs;
                if (vm.activeTab === tabName) {
                    tabs.forEach((tab, index) => {
                        if (tab.name === tabName) {
                            let nextTab = tabs[index + 1] || tabs[index - 1];
                            if (nextTab) {
                                vm.activeTab = nextTab.name;
                            }
                        }
                    });
                }
                let otherTabs = tabs.filter(tab => {
                    return tab.name !== tabName
                });
                udStoreUtil.setStore("nav.navTabs", otherTabs);
                let cached = vm.cached;
                cached = cached.filter(item=>{return item != tabName})
                vm.cached = cached;
            },
            clickTab(tab) {
                const vm = this;
                vm.navTabs.forEach(item => {
                    if (item.name === tab.name) {
                        vm.activeTab = item.name;
                    }
                })
            }
        }
    }
</script>

<style scoped>

</style>
