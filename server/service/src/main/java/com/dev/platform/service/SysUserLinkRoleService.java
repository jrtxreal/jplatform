package com.dev.platform.service;

import java.util.List;

import com.dev.platform.data.dto.SysRoleDto;
import com.dev.platform.data.dto.SysUserDto;

/**
 * @Description:一句话描述这个类
 * @PackgeName: com.dev.platform.paperless.service
 * @ClassName: SysUserLinkRoleService
 * @Author: fanjunhui
 * @Date: 2021/06/02 21:46
 * @Version: V 1.0
 */
public interface SysUserLinkRoleService extends BasicService{
    //1.<给用户分配角色>
    void assignRoleToUser(long userId, String roleIds);
    void assignUserToRole(long roleId, String userIds);

    //2.<删除用户角色关系表>
    //'根据角色id
    void deleteUserLinkRoleByRoleId(long roleId);

   //'3.<删除用户角色关系表>
   //'根据用户id
   void deleteUserLinkRoleByUserId(long userId);

    //'3.<删除用户角色关系表>
    //'根据用户id和角色id
    void deleteUserLinkRoleByUserIdAndRoleId(long userId,long roleId);

   //4.<查询用户已有角色，带当前用户可操作标记>
   List<SysRoleDto> queryRoleOfUser(long userId) throws Exception;

    //'5.<查询角色下的用户，带当前用户可操作标记>
    List<SysUserDto> queryUserOfRole(long roleId) throws Exception;

    //6.<为当前用户查询可分配角色>
    List<SysRoleDto> queryRoleForCurrentUser() throws Exception;
}
