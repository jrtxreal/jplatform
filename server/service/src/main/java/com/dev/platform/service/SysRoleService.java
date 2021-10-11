package com.dev.platform.service;

import com.dev.platform.data.dto.SysRoleDto;
import com.dev.platform.service.vo.SysRoleVo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface SysRoleService extends BasicService {


    /**
     * 添加角色
     * 验证sysRoleVo,所属部门为当前操作用户的所在部门
     *
     * @param sysRoleVo
     * @param deptIds
     */
    void addRole(SysRoleVo sysRoleVo, String deptIds) throws Exception;


    /**
     * 删除角色
     * '删除此角色管理的部门
     * '角色下有人员那么角色下的人员变为普通用户
     * '超级管理员角色不能删除
     *
     * @param roleId
     */
    void deleteRole(long roleId);



    /**
     * 修改角色
     *
     * @param sysRoleVo
     * @param deptIds
     */
    void editRole(SysRoleVo sysRoleVo, String deptIds) throws Exception;


    /**
     * 查询某部门下角色
     *
     * @param pageRequest
     * @param deptId
     * @return
     */
    Page<SysRoleDto> queryRoleByDeptId(PageRequest pageRequest, long deptId,String username);
    SysRoleDto queryRoleById(long id);

    /**
     * 给角色分配组织
     *
     * @param roleId
     * @param deptIds
     */
    void assignOrgToRole(long roleId, String deptIds);


    /**
     * 删除角色部门关系
     *
     * @param roleId
     */
    void deleteRoleLinkDept(long roleId);

    /**
     * 禁用角色
     *
     * @param roleId
     */
    void disableRole(long roleId);
}
