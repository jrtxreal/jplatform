package com.dev.platform.service;

import java.util.List;

import com.dev.platform.data.do_.SysGlobalSettingDo;
import com.dev.platform.service.vo.SysGlobalSettingVo;

/**
 * @version 1.0
 * @description: TODO
 * @author: yn_zhang
 * @date 2021/6/1 8:49
 */
public interface SysSettingService extends BasicService {
    //修改系统设置
    void editSysGlobalSetting(SysGlobalSettingVo sysGlobalSettingVo) throws Exception;
    //获取系统设置
    List<SysGlobalSettingDo> getSysGlobalSetting();
}
