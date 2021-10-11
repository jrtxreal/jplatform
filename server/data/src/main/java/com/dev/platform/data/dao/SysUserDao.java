package com.dev.platform.data.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import com.dev.platform.data.do_.SysUserDo;
import com.dev.platform.data.dto.SysDeptDto;
import com.dev.platform.data.dto.SysUserDto;
import com.dev.platform.data.dto.SysUserDto3;
import com.dev.platform.data.dto.SysUserDto4;

public interface SysUserDao extends JpaRepository<SysUserDo,Long> {

    @Query(value = "SELECT\n" +
            "su.id,su.username,su.disabled,su.dept_id,\n" +
            "su.det_info,JSON_VALUE (su.det_info, '$.nickName') AS nick_name,\n" +
            "sd.`name` AS dept_name,sd.path from sys_user su\n" +
            "LEFT JOIN sys_dept sd on su.dept_id = sd.id WHERE 1=1 And sd.path LIKE CONCAT('%',:deptPath,'%') \n" +
            "AND su.id not in (:exclusions)\n"+
            "AND (CASE WHEN :#{#username} is null or su.username LIKE :#{'%' + #username + '%'} or \n" +
            "JSON_VALUE(su.det_info , '$.nickName') LIKE :#{'%' + #username + '%'} THEN 1 ELSE 0 END) = 1 ORDER BY su.dept_id ASC",
            countQuery = "SELECT count(*) from sys_user su\n" +
                    "LEFT JOIN sys_dept sd on su.dept_id = sd.id WHERE 1=1 and sd.path LIKE CONCAT('%',:deptPath,'%') \n" +
                     "AND su.id not in (:exclusions)\n"+
                    "AND (CASE WHEN  :#{#username} is null or su.username LIKE :#{'%' + #username + '%'} or \n" +
                    "JSON_VALUE(su.det_info , '$.nickName') LIKE :#{'%' + #username + '%'} THEN 1 ELSE 0 END) =1",
            nativeQuery = true)
    Page<SysUserDto> queryUserByDeptIdByPage(List<Long> exclusions, String deptPath, String username, Pageable pageable);

    @Query(value = "SELECT su.id,su.username,su.disabled,su.dept_id,JSON_VALUE(su.det_info,'$.nickName') as nick_name, sd.`name` as dept_name,sd.path," +
            "(select GROUP_CONCAT(a.name  order by LENGTH(a.path) asc SEPARATOR '/') from sys_dept a where  LOCATE(CONCAT('/',a.id,'/'),sd.path) >0) as full_path \n" +
            " FROM sys_user su \n" +
            "LEFT JOIN sys_dept sd ON su.dept_id = sd.id\n" +
            "WHERE 1=1 AND sd.path LIKE concat('%',:path,'%') \n" +
            "AND su.id not in (:exclusions)\n"+
            "AND (CASE WHEN :#{#username} is null\n" +
            "or su.username LIKE :#{'%' + #username + '%'}\n" +
            "or JSON_VALUE(su.det_info,'$.nickName') LIKE :#{'%' + #username + '%'}\n" +
            " THEN 1 ELSE 0 END) = 1 \n"+
            "ORDER BY sd.pid ASC,sd.ord ASC",
            countQuery = "SELECT count(*) FROM sys_user su \n" +
                    "LEFT JOIN sys_dept sd ON su.dept_id = sd.id\n" +
                    "WHERE 1=1 AND sd.path LIKE concat('%',:path,'%')\n"+
                    "AND su.id not in (:exclusions)\n"+
                    "AND (CASE WHEN :#{#username} is null\n" +
                    " or su.username LIKE :#{'%' + #username + '%'}\n" +
                    " or JSON_VALUE(su.det_info,'$.nickName') LIKE :#{'%' + #username + '%'}\n" +
                    " THEN 1 ELSE 0 END) = 1 \n",
                    nativeQuery = true)
    Page<SysUserDto> queryUserByPage(List<Long> exclusions,Pageable pageable,String path,String username);

    /**
     * 查询当前部门以及子部门下的的用户
     * @param deptId
     * @return
     */
    @Query(value = "select su.* from sys_user su\n" +
            "left join sys_dept sd on su.dept_id=sd.id " +
            " WHERE sd.path like CONCAT((select t.path from sys_dept t where t.id=:deptId ),'%')",
            nativeQuery = true)
    List<SysUserDo> queryUserOfDept(long deptId);

    List<SysUserDo> querySysUserDoByDeptId(long deptId);

    /**
     *
     *@Description: 查询用户详情
     *@Param userId:
     *@Return: com.dev.platform.paperless.data.dto.SysUserDto
     *@Author: Mr.zsf
     *@Date: 2021/6/15 17:28
     *@Version: 1.0
     */
    @Query(value = "SELECT su.id,su.username,su.disabled,su.dept_id,sd.`name` as dept_name,sd.path FROM sys_user su \n" +
            "LEFT JOIN sys_dept sd ON su.dept_id = sd.id\n" +
            "WHERE su.id=:userId",nativeQuery = true)
    SysUserDto queryUserByUserId(long userId);


    @Query(value = "SELECT su.*,sd.`name` AS dept_name,sd.path from sys_user su\n" +
            "LEFT JOIN sys_dept sd on su.dept_id = sd.id WHERE 1=1 And sd.path LIKE CONCAT('%',:deptPath,'%') \n" +
            "AND (CASE WHEN :#{#condition} is null OR JSON_VALUE(su.det_info,'$.nickName') like :#{'%' + #condition + '%'} THEN 1 ELSE 0 END) = 1 ORDER BY su.dept_id ASC",
            nativeQuery = true)
    List<SysUserDto3> queryUserByDeptId(String deptPath, String condition);


    @Query(value = "select sd.* from sys_dept sd\n" +
            "left join sys_user su on su.dept_id= sd.id\n" +
            "where sd.name like concat('%',:condition ,'%')\n" +
            "or JSON_VALUE(su.det_info,'$.nickName') like :#{'%' + #condition + '%'}",nativeQuery = true)
    List<SysDeptDto> searchDept(String condition);

    @Query(value = "select su.*,sd.name as dept_name from sys_user su left join sys_dept sd on su.dept_id= sd.id\n" +
            "where JSON_VALUE(su.det_info,'$.nickName') like :#{'%' + #condition + '%'}",nativeQuery = true)
    List<SysUserDto> searchUser(String condition);

    @Query(value = "select su.* from sys_user su \n" +
            "where JSON_VALUE(su.det_info,'$.phone') like :#{'%' + #phone + '%'}",nativeQuery = true)
    List<SysUserDo> queryUserByPhone(String phone);

    // 通过id
    @Query(value = "select su.*,JSON_VALUE(su.det_info,'$.phone') as phone from sys_user su where su.id in (:userIds)",nativeQuery = true)
    List<SysUserDto4> queryUserByUserIds(List<Long> userIds);
}
