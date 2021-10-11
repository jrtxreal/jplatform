import Vue from 'vue';
import ElementUI from 'element-ui';
import locale from 'element-ui/lib/locale/lang/zh-CN'; // lang i18n
import App from './App.vue';
import store from './store';
import router from './router';
import './router/filter.js';  // 权限控制器
import '@/styles/elui/index.css';
import 'normalize.css/normalize.css';
import '@/styles/index.scss'; // global css
Vue.use(ElementUI, { locale, zIndex: 300 });
Vue.config.productionTip = false;
new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app');
