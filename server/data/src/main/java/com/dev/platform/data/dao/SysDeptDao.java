package com.dev.platform.data.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import com.dev.platform.data.do_.SysDeptDo;
import com.dev.platform.data.dto.SysDeptDto;
import com.dev.platform.data.dto.SysDeptDto2;


public interface SysDeptDao extends JpaRepository<SysDeptDo, Long> {
    @Query(value = "SELECT * FROM sys_dept WHERE name like :#{'%' + #name+ '%'}",
            countQuery = "SELECT count(*) FROM sys_dept WHERE name like :#{'%' + #name+ '%'}",
            nativeQuery = true)
    Page<SysDeptDo> queryDeptByName(String name, Pageable pageable);

    // '<删除部门>
    @Modifying
    @Query("delete from SysDeptDo sd  WHERE sd.id = :deptId")
    void deleteById(long deptId);

    @Query(value = "SELECT a.* FROM sys_dept a WHERE a.pid=:deptId",
            nativeQuery = true)
    List<SysDeptDto> querySysDeptByPid(long deptId);

    @Query(value = "SELECT * FROM sys_dept  WHERE id=:deptId",
            nativeQuery = true)
    SysDeptDto querySysDeptById(long deptId);

    List<SysDeptDo> findSysDeptDoByPid(long deptId);

    @Query(value = "SELECT sd2.id,sd2.path,sd2.name FROM  sys_dept sd\n" +
            "LEFT JOIN sys_dept sd2 ON LOCATE(sd2.path , sd.path) > 0\n" +
            "WHERE sd.id = :deptId UNION ALL \n" +
            "SELECT sd.id,sd.path,sd.name FROM sys_dept sd  WHERE sd.path LIKE CONCAT('%','/',:deptId,'/','%')",
            nativeQuery = true)
    List<SysDeptDto> queryParentChildByDeptId(long deptId);

    @Query(value = "SELECT  * from sys_dept sd  where sd.id in (19,18,22)",
            nativeQuery = true)
    List<SysDeptDo> queryDept();

    @Query(value = "select sd.* from sys_dept sd,sys_user su where sd.id=su.dept_id " +
            " AND (CASE WHEN :#{#condition} is null OR sd.name like :#{'%' + #condition + '%'} THEN 1 ELSE 0 END) = 1", nativeQuery = true)
    List<SysDeptDto> queryOrgList(String condition);


    //查询用户所属顶级部门（）
    @Query(value = "select sd.* from sys_dept sd \n" +
            "where sd.pid=0 and EXISTS (select '' from sys_dept t  left join sys_user su on t.id=su.dept_id where su.id=:userId and  LOCATE(concat('/',sd.id,'/'),t.path)) ", nativeQuery = true)
    SysDeptDo queryTopDeptByUserId(long userId);

    // <查询用户的管理部门>
    @Query(value = "SELECT sd.*,1 as admin_tag\n" +
            "FROM sys_role_link_dept srld\n" +
            "left join sys_user_link_role sulr on sulr.role_id = srld.role_id\n" +
            "left join sys_dept sd on sd.id = srld.dept_id\n" +
            "WHERE sulr.user_id = :userId and sd.id = srld.dept_id", nativeQuery = true)
    List<SysDeptDto2> queryAdminDeptOfUser(long userId);

    // <查询关联部门>
    // 包括上级部门和下级部门如图表示   （A-B）-C-（D,E）,当deptIds中包含c，则至少返回A,B,C,D,E，如果整体结果中包含重复项则去重
    @Query(value = "SELECT DISTINCT sd2.*,2 as admin_tag FROM sys_dept sd \n" +
            "left join sys_dept sd2 on  locate(sd.`path` ,sd2.`path`) > 0\n" +
            "where sd.id in :deptIds and sd2.id not in :deptIds\n" +
            "UNION \n" +
            "SELECT DISTINCT sd2.*,3 as admin_tag FROM sys_dept sd\n" +
            "left join sys_dept sd2 on  locate(sd2.`path` ,sd.`path`) > 0\n" +
            "where sd.id in :deptIds", nativeQuery = true)
    List<SysDeptDto2> queryRelatedDept(List<Long> deptIds);

    // 查询所有部门(志远)
    @Query(value = "select sd.*,1 as admin_tag from sys_dept sd", nativeQuery = true)
    List<SysDeptDto2> queryAllDept();

    // 查询所有部门(郡辉)
    @Query(value = "select sd.id AS id,sd.path,sd.NAME from sys_dept sd", nativeQuery = true)
    List<SysDeptDto> queryAllDept1();

    // 查询某部门的所有下级部门
    @Query(value = "select sd2.* from sys_dept sd\n" +
            "left join sys_dept sd2 on locate(sd.`path` ,sd2.`path`) > 0\n" +
            "where sd.id = :deptId and sd2.id != sd.id", nativeQuery = true)
    List<SysDeptDo> queySubDept(Long deptId);
}
