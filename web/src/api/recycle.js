import request from './apiSetting';

// 组织机构列表
export const getOrgList = payload => {
    return request({
        url: '/u/web/sys/dept/queryOrgAndUserList/v1',
        method: 'get',
        params: payload,
    });
};

// 搜索部门、成员、团队
export const searchName = payload => {
    return request({
        url: '/u/web/group/searchUser/v1',
        method: 'get',
        params: payload,
    });
};

// 二级回收站列表
export const recycleFileList = payload => {
    return request({
        url: '/u/web/doc/recycle/secondRecoveryList/v1',
        method: 'post',
        data: payload,
    });
};

// 彻底删除
export const realCleanBin = payload => {
    return request({
        url: '/u/web/doc/recycle/realCleanBin/v1',
        method: 'get',
        params: payload,
    });
};

// 文件还原
export const recoverFile = payload => {
    return request({
        url: '/u/web/doc/recycle/recoverFile/v1',
        method: 'get',
        params: payload,
    });
};

