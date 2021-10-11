package com.dev.platform.data.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import com.dev.platform.data.do_.SysRoleDo;
import com.dev.platform.data.dto.SysRoleDto;

public interface SysRoleDao extends JpaRepository<SysRoleDo, Long> {


    //'3.<查询角色>
    // '查询部门下的角色 > 管理关系
    @Query(value = "SELECT  sr.id,sr.name,sr.def,sr.disabled,sd.id as dept_id,sd.name as dept_name,sd.path, \n" +
            "(select GROUP_CONCAT(a.name  order by LENGTH(a.path) asc SEPARATOR '/') from sys_dept a where  LOCATE(CONCAT('/',a.id,'/'),sd.path) >0) as full_path \n" +
            "FROM sys_role_link_dept srld \n" +
            "LEFT JOIN sys_role sr on srld.role_id=sr.id\n" +
            "LEFT JOIN sys_dept sd ON srld.dept_id = sd.id " +
            " WHERE sd.path like CONCAT((select t.path from sys_dept t where t.id=:deptId ),'%') "+
            " AND (CASE WHEN :#{#condition} is null OR sr.name like :#{'%' + #condition + '%'} THEN 1 ELSE 0 END) = 1",
            countQuery = "SELECT count(1) \n" +
                    "FROM sys_role_link_dept srld \n" +
                    "LEFT JOIN sys_role sr on srld.role_id=sr.id\n" +
                    " LEFT JOIN sys_dept sd ON sr.dept_id = sd.id " +
                    " WHERE sd.path like CONCAT((select t.path from sys_dept t where t.id=:deptId ),'%')"+
                    " AND (CASE WHEN :#{#condition} is null OR sr.name like :#{'%' + #condition + '%'} THEN 1 ELSE 0 END) = 1",
            nativeQuery = true)
    Page<SysRoleDto> querySysRoleByDeptId(Pageable pageRequest, long deptId,String condition);

    @Query(value = "SELECT sr.* FROM sys_role sr WHERE sr.id =:roleId", nativeQuery = true)
    SysRoleDto queryRoleById(long roleId);

    // 查询部门以及子部门下的角色
    @Query(value = "SELECT DISTINCT sr.* FROM sys_role sr " +
            "LEFT JOIN sys_dept sd on sr.dept_id=sd.id " +
            "WHERE sd.path  LIKE CONCAT('%',:deptId,'%')",
            nativeQuery = true)
    List<SysRoleDo> querySysRoleByDeptId(long deptId);

    // 查询所有角色
    @Query(value = "SELECT sr.id AS role_id,sr.`name`,sd.path,sd.id AS dept_id FROM sys_role sr\n" +
            "LEFT JOIN sys_role_link_dept srld ON sr.id = srld.role_id\n" +
            "LEFT JOIN sys_dept sd ON srld.dept_id = sd.id",nativeQuery = true)
    List<SysRoleDto> queryAllRole();
}
