import Vue from 'vue';
import VueRouter from 'vue-router';
Vue.use(VueRouter);
// 处理vue-router重复路由到当前路径下会报错的问题
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
    return originalPush.call(this, location).catch(err => err)
}

import route_cmn from "./routeSeg/route_cmn";
import setting from "../setting";
import route_la from "./routeSeg/route_la";

const routes = [
    ...route_cmn,
    ...route_la,
];

const router = new VueRouter({
    scrollBehavior: () => ({y: 0}),
    mode: 'history',
    base: setting.basePath,
    routes: routes,
});

export default router;
