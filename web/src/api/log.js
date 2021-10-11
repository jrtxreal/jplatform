import request from './apiSetting';

export const getLogInfoByName = prma =>{
      return request({
            url:"/u/web/log/query/v1",
            data:prma,
            method:'POST'
      });
};
