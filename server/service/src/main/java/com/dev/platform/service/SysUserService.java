package com.dev.platform.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import com.dev.platform.data.do_.SysUserDo;
import com.dev.platform.data.dto.DeptGroupUserDto;
import com.dev.platform.data.dto.SysUserDto;
import com.dev.platform.data.dto.SysUserDto3;
import com.dev.platform.service.vo.SysUserVo;

/**
 * @Description:系统用户服务
 * @PackgeName: com.dev.platform.paperless.service
 * @ClassName: SysUserService
 * @Author: fanjunhui
 * @Date: 2021/05/31 14:10
 * @Version: V 1.0
 */
public interface SysUserService extends BasicService {

    // <添加用户>
    void addUser(SysUserVo sysUserVo) throws Exception;
    // <删除用户>
    void deleteUser(long userId);

    // <修改用户基本信息>
    void editBasicUserInfo(SysUserVo sysUserVo) throws Exception;

    // <由管理员为用户更改密码>
    void changeUserPwdByAdmin(long userId,String newUserPwd,String adminPwd) throws Exception;
    void changeUserPwd(String newUserPwd,String oldPwd) throws Exception;

    // <根据部门ID查询用户列表> 用于用户管理模块中的用户管理页
    Page<SysUserDto> queryUserByDeptIdByPage(PageRequest pageRequest, long deptId, String username) throws Exception;

    //<用户查询，带用户名过滤和部门Id过滤>  用户角色管理模块中用户tab中用户管理
    Page<SysUserDto> queryUserByPage(Pageable pageable,long deptId,String username) throws Exception;

    //<查询用户信息>
    SysUserDo queryUserById(long userId) throws Exception;//<根据用户ID,查询用户信息>

    //<查询用户信息>
    SysUserDo queryUserByPhone(String phone) throws Exception;//<根据用户手机号码,查询用户信息>

    List<SysUserDto3> queryUserByDeptId(long deptId, String condition);
    // 查找部门或者包含用户
    List<DeptGroupUserDto> searchDeptGroupUser(String condition) throws Exception;

    // <锁定账户>
    void lockUser(String username) throws Exception;
    /**
     *
     *@Description:
     *@Param mobile:  手机号
     *@param code:     验证码
     *@Return: java.lang.long  用户Id
     *@Author: Mr.zsf
     *@Date: 2021/6/25 11:08
     *@Version: 1.0
     */
    SysUserDo verifySMS(String mobile,String code);
    void forgotPwd(String mobile,String newPwd) throws Exception;
    //修改头像
    void editAvatar(String image) throws Exception;
}
