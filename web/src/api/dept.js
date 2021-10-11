import request from "./apiSetting";

export default {
    //获取当前用户
    queryAdminDeptOfUser(payload) {
        return request({
            url: '/u/web/sys/dept/queryAdminDeptOfUser/v1',
            method: 'post',
            data: payload,
        });
    },
    editDept(payload) {
        return request({
            url: '/u/web/sys/dept/edit/v1',
            method: 'post',
            data: payload,
        });
    },
    addDept(payload) {
        return request({
            url: '/u/web/sys/dept/add/v1',
            method: 'post',
            data: payload,
        });
    },
    deleteDept(payload) {
        return request({
            url: '/u/web/sys/dept/delete/v1',
            method: 'get',
            params: payload,
        });
    },
    queryUserOfDept(payload) {
        return request({
            url: '/u/web/sys/dept/queryUserOfDept/v1',
            method: 'post',
            data: payload,
        });
    },
    queryRoleOfDept(payload) {
        return request({
            url: '/u/web/sys/dept/queryRoleOfDept/v1',
            method: 'post',
            data: payload,
        });
    },
}
