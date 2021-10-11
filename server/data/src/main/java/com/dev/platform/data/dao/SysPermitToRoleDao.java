package com.dev.platform.data.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import com.dev.platform.data.do_.SysPermitToRoleDo;
import com.dev.platform.data.dto.SysSrcDto;
/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/5/31 17:45
 */
public interface SysPermitToRoleDao extends JpaRepository<SysPermitToRoleDo, Long> {
    // 根据roleId删除角色权限
    @Modifying
    @Query("delete from SysPermitToRoleDo ptr where ptr.roleId = :roleId")
    void deletePermitToRoleByRoleId(long roleId);
    @Modifying
    @Query("delete from SysPermitToRoleDo ptr where ptr.roleId in :srcIds")
    void deletePermitToRoleBySrcIds(List<Long> srcIds);

    @Query(value = "select sc.* from sys_src sc \n" +
            "left join sys_permit_to_role pr on pr.src_id=sc.id\n" +
            "where pr.role_id=:roleId",nativeQuery = true)
    List<SysSrcDto> querySrcOfRole(Long roleId);
}
