import request from './apiSetting';

export const getSettingInfo = prma =>{
      return request({
            url:"/u/web/sys/setting/query/v1",
            data:prma,
            method:'POST'
      });
};
export const saveSettingInfo = prma =>{
      return request({
            url:"/u/web/sys/setting/edit/v1",
            data:prma,
            method:'POST'
      });
};
