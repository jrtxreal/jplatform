package com.dev.platform.service;

import java.util.List;

import com.dev.platform.data.dto.SysSrcDto;
/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/5/31 17:45
 */
public interface SysPermitToRoleService extends BasicService {
    // <批量授权>
    // 通过两层for循环将srcIds和roleIds拼接成SysPermitToRoleDo
    void grantPrivilegesToRole(List<Long> srcIds, List<Long> roleIds);

    List<SysSrcDto> querySrcOfRole(Long roleId);
}
