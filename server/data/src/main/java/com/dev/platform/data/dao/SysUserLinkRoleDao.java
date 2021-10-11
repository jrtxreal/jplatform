package com.dev.platform.data.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import com.dev.platform.data.do_.SysUserLinkRoleDo;
import com.dev.platform.data.dto.SysRoleDto;
import com.dev.platform.data.dto.SysUserDto;

public interface SysUserLinkRoleDao extends JpaRepository<SysUserLinkRoleDo,Long> {

    // <根据用户id删除用户和角色间的关系>

//    @Modifying
//    @Query(value="delete sulr from SysUserLinkRoleDo sulr  WHERE sulr.userId = :userId",nativeQuery = true)
//    void deleteSysUserLinkRoleByUserId(long userId);


    // <根据角色id删除用户和角色间的关系>
    @Modifying
    @Query(value="delete from SysUserLinkRoleDo sulr  WHERE sulr.roleId = :roleId",nativeQuery = true)
    void deleteSysUserLinkRoleByRoleId(long roleId);

    // <根据角色id查询该角色下的用户>
    @Query(value = "SELECT su.id,su.username,JSON_VALUE(su.det_info,'$.nickName') as nick_name, su.dept_id,sd.path from sys_user_link_role sulr\n" +
            "LEFT JOIN sys_user su ON sulr.user_id = su.id\n" +
            "LEFT JOIN sys_dept sd ON su.dept_id = sd.id\n" +
            "WHERE sulr.role_id  = :roleId",nativeQuery = true)
    List<SysUserDto> queryUserOfRole(long roleId);

    // <根据用户id查询该用户已拥有的角色>
    @Query(value = "SELECT sd.id as dept_id,sulr.user_id,sr.id as role_id,sr.`name` ,sd.path\n" +
            "            FROM sys_user_link_role sulr \n" +
            "            LEFT JOIN sys_role sr on sulr.role_id = sr.id\n" +
            "            LEFT JOIN sys_role_link_dept srld on srld.role_id = sr.id\n" +
            "            LEFT JOIN sys_dept sd ON srld.dept_id = sd.id\n" +
            "            WHERE sulr.user_id = :userId AND sr.disabled = 0",nativeQuery = true)
    List<SysRoleDto> queryRoleOfUser(long userId);

    // <为当前用户查询可分配角色>
    @Query(value = "SELECT sulr.user_id,sr.id as role_id,sr.`name` ,sd.path,sd.id as dept_id \n" +
            "FROM sys_role_link_dept srld\n" +
            "LEFT JOIN sys_user_link_role sulr on srld.role_id = sulr.role_id\n" +
            "LEFT JOIN sys_role sr on srld.role_id = sr.id\n" +
            "LEFT JOIN sys_dept sd ON srld.dept_id = sd.id\n" +
            "WHERE sulr.user_id = :userId AND sr.disabled = 0",nativeQuery = true)
    List<SysRoleDto> queryRoleForCurrentUser(long userId);
}
