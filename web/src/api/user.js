import request from './apiSetting';

export default {
    addUser(payload) {
        return request({
            url: '/u/web/sys/user/add/v1',
            method: 'post',
            data: payload,
        });
    },
    //管理
    AdminDeptOfUser(payload) {
        return request({
            url: '/u/web/sys/dept/queryAdminDeptOfUser/v1',
            method: 'post',
            data: payload,
        });
    },
 //用户左边部门列表（树结构）管理加所属
    queryDeptOfUser(payload) {
        return request({
            url: '/u/web/sys/dept/queryDeptOfUser/v1',
            method: 'post',
            data: payload,
        });
    },
    //部门列表点击内容
    queryUserOfDept(payload) {
        return request({
            url: '/u/web/sys/dept/queryUserOfDept/v1',
            method: 'post',
            data: payload,
        });
    },
    //部门列表点击内容删除
    delete(payload) {
        return request({
            url: '/u/web/sys/user/delete/v1?userId=' + payload,
            method: 'get',
        });
    },
    //列表内容基本信息编辑
    edit(payload) {
        return request({
            url: '/u/web/sys/user/edit/v1',
            method: 'post',
            data: payload,
        });
    },
    //列表内容基本信息密码修改
    passwordChange(payload) {
        return request({
            url: '/u/web/sys/user/resetPwd/v1',
            method: 'post',
            data: payload,
        });
    },
    //列表内容基本信息角色管理
    queryRoleOfUser(payload) {
        return request({
            url: '/u/web/sys/role/queryRoleOfUser/v1',
            method: 'post',
            data: payload,
        });
    },
//列表内容基本信息角色管理
    queryAssignRoleForCurrentUser(payload) {
        return request({
            url: '/u/web/sys/role/queryAssignRoleForCurrentUser/v1',
            method: 'post',
            data: payload,
        });
    },
    deleteRoleDept:function (payload) {
        return request({
        // /u/web/sys/role/deleteUserRole/v1
        // /u/web/sys/role/deleteRoleDept/v1
            url: '/u/web/sys/role/deleteUserRole/v1',
            method: 'post',
            data: payload,
        });
    },
    assignRoleToUser:function (payload) {
        return request({
            url: '/u/web/sys/role/assignRoleToUser/v1',
            method: 'post',
            data: payload,
        });
    },
    //组织管理查询
    queryAdminDeptOfUser1:function (payload) {
        return request({
            url: '/u/web/sys/dept/queryAdminDeptOfUser1/v1',
            method: 'post',
            data: payload,
        });
    },
    //权限管理资源
    queryOfUser:function (payload) {
        return request({
            url: '/u/web/src/queryOfUser/v1',
            method: 'post',
            data: payload,
        });
    },



}


