import request from './apiSetting';

// 请求部门树结构
export const getDeptTree = payload => {
  return request({
    url: 'u/web/sys/dept/queryDeptOfUser/v1',
    method: 'post',
    data: payload,
  });
};
// 请求角色管理的部门树结构
export const getDeptTreeOfM = payload => {
  return request({
    url: '/u/web/sys/dept/queryAdminDeptOfUser/v1',
    method: 'post',
    data: payload,
  });
};
// 请求角色列表
export const getRoleList = payload => {
  return request({
    url: '/u/web/sys/role/queryAll/v1',
    method: 'post',
    data: payload,
  });
};

// 删除角色
export const delRole = payload => {
  return request({
    url: '/u/web/sys/role/delete/v1',
    method: 'get',
    data: payload,
  });
};

// 获取角色详情
export const roleDetail = payload => {
  return request({
    url: '/u/web/sys/role/query/v1',
    method: 'get',
    params:payload
  });
};

// 获取角色详情
export const addRole = payload => {
  return request({
    url: '/u/web/sys/role/add/v1',
    method: 'post',
    data: payload,
  });
};

//保存编辑
export const editRole = payload => {
  return request({
    url: '/u/web/sys/role/edit/v1',
    method: 'post',
    data: payload,
  });
};
export const deleteRole = payload => {
  return request({
    url: '/u/web/sys/role/deleteRoleDept/v1',
    method: 'post',
    data: payload,
  });
};
//获取当前角色下用户
export const getAccForRole = payload => {
  return request({
    url: '/u/web/sys/role/queryUserRole/v1',
    method: 'get',
    params: payload,
  });
};
//获取用户
export const getAcc = payload => {
  return request({
    url: '/u/web/sys/user/queryRoleUser/v1',
    method: 'post',
    data: payload,
  });
};
//获取可用用户
export const saveAccForRole = payload => {
  return request({
    url: '/u/web/sys/role/assignUser/v1',
    method: 'post',
    data: payload,
  });
};
//获取角色下权限
export const getPermitsForRole = payload => {
  return request({
    url: '/u/web/sys/role/queryRolePermit/v1',
    method: 'get',
    params: payload,
  });
};
//获取角色可分配权限
export const getPermitsForRoleAble = payload => {
  return request({
    url: '/u/web/src/queryUserPermit/v1',
    method: 'get',
    params: payload,
  });
};
//获取角色可分配权限
export const savePermitsForRole = payload => {
  return request({
    url: '/u/web/sys/role/grantPrivilegesToRole/v1',
    method: 'post',
    data:payload
  });
};
