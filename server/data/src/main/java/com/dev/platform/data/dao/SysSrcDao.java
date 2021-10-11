package com.dev.platform.data.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import com.dev.platform.data.do_.SysSrcDo;
import com.dev.platform.data.dto.SysSrcDto;
/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/5/31 17:45
 */
public interface SysSrcDao extends JpaRepository<SysSrcDo, Long> {
    // <查询某资源分组下的所有资源Id>
    @Query(value = "SELECT a.src_id FROM sys_src_link_src_grp a WHERE a.src_grp_id = :srcGrpId", nativeQuery = true)
    List<Long> querySrcIdsOfSrcGrp(long srcGrpId);

    // <查询所有资源带分组信息>
    @Query(value = "SELECT ss.*,ssg.id as src_grp_id,ssg.name as src_grp_name,ssg.ord\n" +
            "FROM sys_src ss\n" +
            "left join sys_src_link_src_grp sslsg on ss.id =sslsg.src_id \n" +
            "LEFT JOIN sys_src_grp ssg on ssg.id = sslsg.src_grp_id\n" +
            "where 1=1\n" +
            "order by ssg.ord ASC,sslsg.ord ASC", nativeQuery = true)
    List<SysSrcDto> queryAllSrc();

//    // <查询部门管理员角色资源，带分组信息>
//    @Query(value = "SELECT ss.*,ssg.id as src_grp_id,ssg.name as src_grp_name,ssg.ord\n" +
//            "FROM sys_src ss\n" +
//            "left join sys_src_link_src_grp sslsg on ss.id =sslsg.src_id \n" +
//            "LEFT JOIN sys_src_grp ssg on ssg.id = sslsg.src_grp_id\n" +
//            "LEFT JOIN sys_src_range_tpl ssrt on ssrt.src_grp_id = ssg.id\n" +
//            "WHERE ssrt.`type` = 'DA'\n" +
//            "order by ssg.ord ASC,sslsg.ord ASC", nativeQuery = true)
//    List<SysSrcDto> querySrcOfDeptAdmin();

    // <查询某个分组下的所有资源>
    @Query(value = "select ss.*,sslsg.ord from sys_src_link_src_grp sslsg \n" +
            "LEFT JOIN sys_src ss on sslsg.src_id = ss.id \n" +
            "LEFT JOIN sys_src_grp ssg on sslsg.src_grp_id =ssg.id \n" +
            "WHERE sslsg.src_grp_id = :srcGrpId\n" +
            "and (case when :srcName is null or ss.name like :#{'%' + #srcName +'%'} then 1 else 0 end) = 1\n"+
            "order by ssg.ord ASC,sslsg.ord ASC", nativeQuery = true)
    List<SysSrcDto> querySrcOfSrcGrp(long srcGrpId,String srcName);

    // <查询自定义角色资源，带分组信息>
    @Query(value = "SELECT ss.*,ssg.id as src_grp_id,ssg.name as src_grp_name,ssg.ord\n" +
            "from sys_permit_to_role sptr \n" +
            "LEFT JOIN sys_src ss on ss.id = sptr.src_id\n" +
            "LEFT JOIN sys_src_link_src_grp sslsg on sslsg.src_id = ss.id\n" +
            "LEFT JOIN sys_src_grp ssg on ssg.id = sslsg.src_grp_id \n" +
            "WHERE sptr.role_id = :roleId\n" +
            "order by ssg.ord ASC,sslsg.ord ASC", nativeQuery = true)
    List<SysSrcDto> querySrcOfUdRole(long roleId);

    // <查询用户拥有的资源,带分组信息>
    @Query(value = "SELECT ss.*,ssg.id as src_grp_id,ssg.name as src_grp_name,ssg.ord,sd.`path` as dept_path\n" +
            "            FROM sys_permit_to_role sptr\n" +
            "            LEFT JOIN sys_src ss on ss.id = sptr.src_id\n" +
            "            LEFT JOIN sys_src_link_src_grp sslsg on sslsg.src_id = ss.id\n" +
            "            LEFT JOIN sys_src_grp ssg on ssg.id = sslsg.src_grp_id\n" +
            "            LEFT JOIN sys_user_link_role sulr on sulr.role_id = sptr.role_id\n" +
            "            LEFT JOIN sys_role_link_dept srld on srld.role_id = sptr.role_id \n" +
            "            LEFT JOIN sys_dept sd on sd.id = srld.dept_id \n" +
            "            WHERE sulr.user_id = :userId\n" +
            "            order by ssg.ord ASC,sslsg.ord ASC", nativeQuery = true)
    List<SysSrcDto> querySrcOfUser(long userId);



}
