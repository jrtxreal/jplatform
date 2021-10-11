import axios from 'axios';
import router from '../router';


import map from '../map';
import nav from '../router/nav'
import excType from '../def/resultCode';
import excMsg from '../def/msg';
import udStoreUtil from "../utils/udStoreUtil";
import setting from "../setting";


axios.defaults.timeout = setting.timeout;
axios.defaults.baseURL = setting.serverPath;
axios.defaults.crossDomain = true;
axios.defaults.withCredentials = true;
axios.defaults.headers.common['Content-Type'] = 'application/json;charset=UTF-8';
// 请求拦截
axios.interceptors.request.use(
  config => {
    config.headers['X-Requested-With'] = 'XMLHttpRequest';
    config.headers['Access-Control-Allow-Origin'] = '*';
    config.headers.common['Authorization'] = udStoreUtil.getStore("sessionBo.token");
    return config;
  },
  err => {
    return Promise.reject(err);
  },
);
// 响应拦截
axios.interceptors.response.use(
  res => {
    const data = res.data;
    if (data && data.code == 0) {
      //请求成功;
      return data;
    } else {
      //请求失败;
      const err = new Error();
      err.response = res;
      err.request = res.request;
      if (!data) {
        err.code = excType.no_data; //无数据返回；
      } else if (!data.code) {
        err.code = excType.no_code; //无状态返回;
      } else {
        err.code = data.code;
        err.message = data.msg;
        err.msg = data.msg;
      }
      switch (err.code) {
        case excType.no_data: {
          //未收到返回数据
          console.error(err.request.responseURL);
          console.error('未获取到数据');
          break;
        }
        case excType.no_code: {
          //返回格式错误
          console.error(err.request.responseURL);
          console.log('返回格式错误');
          break;
        }
        case excType.AUC: {
          //认证过期，需重新登录；
          udStoreUtil.setStore('user.token', null);
          if (router.currentRoute.path != nav[map.login].url) {
            let isOk = window.confirm(excMsg.AUC);
            if (isOk) {
              router.push({name:map.login});
            }
          }
          break;
        }
        case excType.AUZ: {
          //权限不足;
          console.error(err.request.responseURL);
          console.log(excMsg.AUZ);
          break;
        }
        case excType.BIZ: {
          //业务逻辑错误
          break;
        }
        case excType.DEV: {
          //提供给开发人员的错误信息
          console.error(err.request.responseURL);
          console.log(err.message);
          break;
        }
        case excType.SYS: {
          //提供给开发人员的错误信息
          console.error(err.request.responseURL);
          console.log(excMsg.SYS);
          break;
        }
        default: {
          break;
        }
      }
      return Promise.reject(err);
    }
  },
  err => {
    if (err.response) {
      //判断服务器是否有响应;
      const status = err.response.status;
      switch (status) {
        case 404: {
          console.error(err.request.responseURL);
          console.log('服务器报404');
          break;
        }
        case 500: {
          console.error(err.request.responseURL);
          console.log('服务器报500');
          break;
        }
      }
    } else {
      console.log('无法与服务器建立连接');
    }
  },
);

export default axios;
