import request from './apiSetting';

export const getDictSections = prma =>{
      return request({
            url:"/u/web/sys/dicGrp/query/v1",
            data:prma,
            method:'POST'
      });
};
export const addDictSection = prma =>{
      return request({
            url:"/u/web/sys/dicGrp/add/v1",
            data:prma,
            method:'POST'
      })
};
export const deleteDictSection = prma =>{
      return request({
            url:"/u/web/sys/dicGrp/delete/v1",
            data:prma,
            method:'POST'
      })
};
export const editDictSection = prma =>{
      return request({
            url:"/u/web/sys/dicGrp/edit/v1",
            data:prma,
            method:'POST'
      })
};
export const addDict = prma =>{
      return request({
            url:"/u/web/sys/dic/add/v1",
            data:prma,
            method:'POST'
      })
};
export const deleteDict = prma =>{
      return request({
            url:"/u/web/sys/dic/delete/v1",
            data:prma,
            method:'POST'
      })
};
export const editDict = prma =>{
      return request({
            url:"/u/web/sys/dic/edit/v1",
            data:prma,
            method:'POST'
      })
};
export const getDict = prma =>{
      return request({
            url:"/u/web/sys/dic/query/v1",
            data:prma,
            method:'POST'
      })
};
