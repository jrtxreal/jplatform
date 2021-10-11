package com.dev.platform.data.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import com.dev.platform.data.do_.SysRoleLinkDeptDo;
import com.dev.platform.data.dto.SysDeptDto;
import com.dev.platform.data.dto.SysRoleDto;

public interface SysRoleLinkDeptDao extends JpaRepository<SysRoleLinkDeptDo, Long> {
    //'1.<给角色分配组织>
//    void assignOrgToRole(long roleId, List<Long> deptIds);

    // '2.<根据角色id删除角色部门关系>
    @Modifying
    @Query("delete from SysRoleLinkDeptDo where roleId=:roleId")
    void deleteSysRoleLinkDept(long roleId);

    // '2.<根据部门ID删除角色部门关系>
    @Modifying
    @Query("delete from SysRoleLinkDeptDo where deptId=:deptId")
    void deleteSysRoleLinkDeptByDeptId(long deptId);

    // '3.<查询当前部门下的角色 > 管理关系>
    @Query(value = "SELECT DISTINCT sr.* \n" +
            "FROM sys_role sr \n" +
            " LEFT JOIN sys_dept sd ON sr.dept_id = sd.id " +
            "WHERE sd.path like :#{'%' + #deptId+ '%'}",
            countQuery = "SELECT count(distinct sr.id) \n" +
                    "FROM sys_role sr \n" +
                    " LEFT JOIN sys_dept sd ON sr.dept_id = sd.id " +
                    "WHERE sd.path like :#{'%' + #deptId+ '%'}",
            nativeQuery = true)
    Page<SysRoleDto> queryRoleOfDept(Pageable pageable, long deptId);

    // '4.<查询当前角色的管理部门，带当前用户可关闭标志>
    @Query(value = "SELECT * FROM sys_role_link_dept WHERE role_id=:roleId",nativeQuery = true)
    List<SysDeptDto> queryDeptForRole(long roleId);

    @Modifying
    @Query(value = "delete from sys_role_link_dept where dept_id=:deptId and role_id=:roleId",nativeQuery = true)
    void deleteRoleDept(long deptId, long roleId);
}
