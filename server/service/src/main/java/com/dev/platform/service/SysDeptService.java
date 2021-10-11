package com.dev.platform.service;

import java.util.List;

import com.dev.platform.data.dto.SysDeptDto;
import com.dev.platform.data.dto.SysDeptDto2;
import com.dev.platform.data.dto.SysOrgUserDto;
import com.dev.platform.service.vo.SysDeptVo;

public interface SysDeptService extends BasicService {


    /**
     * 添加组织
     *
     * @param sysDeptVo
     */
    void addSysDept(SysDeptVo sysDeptVo) throws Exception;

    void editSysDept(SysDeptVo sysOrgVo) throws Exception;

    /**
     * 查询组织列表
     *
     * @param deptId
     * @param spreadTag
     * @return
     */
    List<SysDeptDto> querySysDept(long deptId, Boolean spreadTag);
    SysDeptDto querySysDeptById(long deptId);

    //<查询用户管理部门>
    List<SysDeptDto2> queryAdminOrgOfUser() throws Exception;

    //<根据userId,查询用户管理部门>
    List<SysDeptDto2> queryAdminOrgOfUser1(long userId) throws Exception;


    //<查询用户的归属部门和管理部门，其中所属部门管理标志为0，所属部门管理标志为1>
    List<SysDeptDto2> queryDeptOfUser() throws Exception;
    /**
     * 变更组织路径
     * 'A.currentDeptId的pid变更,B.修改currentDept父节点的节点状态,C.修改currentDept及下级节点path
     * 'D.修改targetDeptId的节点状态
     * 'A为叶子节点的情况
     * '目标节点的情况：a. targetDept为currentDept或curentDept的子节点X, b.targetDeptId==0,C.普通
     *
     * @param currentDeptId
     * @param targetDeptId
     */
    void changeSysOrgPath(long currentDeptId, long targetDeptId) throws Exception;
    /**
     * 删除组织
     *
     * @param deptId
     */
    void deleteSysDept(long deptId);

    /**
     *
     *@Description: 部门列表
     *@Param:
     *@Return: java.util.List<com.dev.platform.paperless.data.dto.SysDeptDto>
     *@Author: Mr.zsf
     *@Date: 2021/6/22 14:54
     *@Version: 1.0
     */
    List<SysDeptDto> queryOrgList(String condition);


    // 查询部门下的人以及子部门
    List<SysOrgUserDto> queryDeptAndUserList(String deptId);
}
