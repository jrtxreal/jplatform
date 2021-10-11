import request from './apiSetting';

export default {
    //获取资源分组数据
    srcGroup(payload) {
        return request({
            url: '/u/web/srcGrp/queryAll/v1',
            method: 'post',
            data: payload,
        });
    },
    //获取资源分组内容数据
    srcGroupContent(payload) {
        return request({
            url: '/u/web/src/queryOfGrp/v1',
            method: 'post',
            data: payload,
        });
    },
    //资源分组内容数据添加
    srcGroupContentAdd(payload) {
        return request({
            url: '/u/web/src/addToGrp/v1',
            method: 'post',
            data: payload,
        });
    },
//资源分组内容数据删除
    srcGroupContentDel(payload) {
        return request({
            url: '/u/web/src/delete/v1',
            method: 'post',
            data: payload,
        });
    },
    //资源分组内容数据编辑保存
    srcGroupContentEdt(payload) {
        return request({
            url: '/u/web/src/edit/v1',
            method: 'post',
            data: payload,
        });
    },

//资源分组内容数据移动
    srcGroupContentMove(payload) {
        return request({
            url: '/u/web/src/srcMoveInBatch/v1',
            method: 'post',
            data: payload,
        });
    },
    //资源分组添加
    srcGroupAdd(payload) {
        return request({
            url: '/u/web/srcGrp/add/v1',
            method: 'post',
            data: payload,
        });
    },
    //资源分组别写
    srcGroupEdit(payload) {
        return request({
            url: '/u/web/srcGrp/edit/v1',
            method: 'post',
            data: payload,
        });
    },

//资源分组删除
srcGroupDelete(payload) {
    return request({
        url: '/u/web/srcGrp/delete/v1',
        method: 'post',
        data: payload,
    });
},
}

