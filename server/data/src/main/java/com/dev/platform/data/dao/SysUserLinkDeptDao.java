package com.dev.platform.data.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import com.dev.platform.data.do_.SysRoleLinkDeptDo;
import com.dev.platform.data.dto.SysDeptDto;

public interface SysUserLinkDeptDao extends JpaRepository<SysRoleLinkDeptDo,Long> {

    // 查询用户管理的部门
    @Query(value = "SELECT DISTINCT sd.id AS id,sd.path,sd.NAME \n" +
            "FROM sys_role_link_dept srld,\n" +
            "sys_dept sd,sys_user_link_role sulr \n" +
            "WHERE srld.dept_id = sd.id \n" +
            "AND sulr.role_id = srld.role_id \n" +
            "AND sulr.user_id = :userId"
            ,nativeQuery = true)
    List<SysDeptDto> queryUserLinkDept(long userId);

    /**
     * 查找用户管理的部门信息
     * @param deptIds '/18/|/17/|/22/|/23/'
     * @return
     */
    @Query(value = "select sd.* from sys_dept sd \n" +
            "where sd.path REGEXP :deptIds"
            ,nativeQuery = true)
    List<SysDeptDto> queryDeptByDeptIds(String deptIds);

    /**
     *
     *@Description: 查询部门全路径
     *@Param null:
     *@Return:
     *@Author: Mr.zsf
     *@Date: 2021/6/18 11:55
     *@Version: 1.0
     */
    @Query(value = "SELECT distinct sd2.id,sd2.path,sd2.name FROM  sys_dept sd\n" +
            "LEFT JOIN sys_dept sd2 ON LOCATE(sd2.path , sd.path) > 0\n" +
            "WHERE sd.id REGEXP :deptIds"
            ,nativeQuery = true)
    List<SysDeptDto> queryAllParentDept(String deptIds);


}
