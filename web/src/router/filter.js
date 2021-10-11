import router from '../router'
import NProgress from 'nprogress'; // progress bar
import 'nprogress/nprogress.css'; // progress bar style
import map from '../map'
import nav from './nav'
import udStoreUtil from "../utils/udStoreUtil";

NProgress.configure({showSpinner: false}); // NProgress Configuration

// 白名单
const whiteList = [nav[map.login].url, nav[map._404].url];
router.beforeEach(async (to, from, next) => {
    NProgress.start();
    if (whiteList.indexOf(to.path) > -1) {
        next();
    } else {
        if (to.name !== map.login) {
            if(udStoreUtil.getStore("sessionBo.token")){
                next();
            }else if(to.query&&to.query.token){
                console.log("这里是token请求的结果",to.query.token);
            }else{
                router.push({name:map.login})
            }
        }else{
            next();
        }
    }
});
router.afterEach((to,from) => {
    NProgress.done();
    udStoreUtil.setStore("nav.currentRoutePath",to.path);
    const cached = udStoreUtil.getStore("nav.cached");
    cached.push(to.name);

});
