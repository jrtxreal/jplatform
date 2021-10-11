import request from './apiSetting';

// export const login = payload => {
//   return request({
//     url: '/g/user/login',
//     method: 'post',
//     data: payload,
//   });
// };
//   //验证码获取
//   export const loginCode = payload => {
//     return request({
//       url: '/g/web/sys/getKaptcha/v1?state='+payload,
//       method: 'get',
//     });
//   };
//   //登录
//   export const loginIn = payload => {
//     return request({
//       url: '/g/web/sys/login/v1',
//       method: 'post',
//       data: payload,
//     });
//   };
export default {
  login(payload){
    return request({
    url: '/g/user/login',
    method: 'post',
    data: payload,
  });
},
  loginCode(payload){
    return request({
      url: '/g/web/sys/getKaptcha/v1?state='+payload,
      method: 'get',
    });
  },
  loginIn(payload){
    return request({
      url: '/g/web/sys/login/v1',
      method: 'post',
      data: payload,
    });
  },
  msgCode(payload){
    return request({
      url: '/g/web/sys/getMsgCode/v1?phone='+payload,
      method: 'get',
    });
  },
  logout(payload){
    return request({
      url: '/g/web/sys/logout/v1',
      method: 'get',
      params: payload,
    });
  }
}


