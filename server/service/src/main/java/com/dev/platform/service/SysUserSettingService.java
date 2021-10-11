package com.dev.platform.service;

import java.util.Map;

import com.dev.platform.service.vo.SysUserSettingVo;

/**
 * @version 1.0
 * @description: TODO
 * @author: yn_zhang
 * @date 2021/6/1 9:35
 */
public interface SysUserSettingService extends BasicService{

    //修改用户设置
    void editSysUserSetting(SysUserSettingVo sysUserSettingVo) throws Exception;
    //根据用户id查询用户设置
    SysUserSettingVo findUserSetting() throws Exception;
    //获取用户文档排序方式设置
    Map getOrderSortTypeSetting() throws Exception;

    void updateOrderSortTypeSetting(String order_type, String sort_type) throws Exception;
}
