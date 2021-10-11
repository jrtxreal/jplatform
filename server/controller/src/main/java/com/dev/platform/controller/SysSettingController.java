package com.dev.platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import com.dev.platform.service.SysSettingService;
import com.dev.platform.service.config.ExceptionCmpt;
import com.dev.platform.service.config.ResultCmpt;
import com.dev.platform.service.vo.SysGlobalSettingVo;

/**
 * @version 1.0
 * @description: TODO
 * @author: yn_zhang
 * @date 2021/6/2 14:30
 */
@RestController
public class SysSettingController extends BasicController{
    @Autowired
    ExceptionCmpt exceptionCmpt;
    @Autowired
    SysSettingService sysSettingService;

    //修改系统设置 add by yn_zhang 2021/6/2
    @RequestMapping(value = "/u/web/sys/setting/edit/v1",
            produces = "application/json; charset=utf-8",
            method = RequestMethod.POST)
    public Object editSysSetting(@RequestBody SysGlobalSettingVo sysGlobalSettingVo, HttpServletRequest request) {
        try {
            sysSettingService.editSysGlobalSetting(sysGlobalSettingVo);
            return ok().setMsg("修改成功").toJson();
        } catch (Exception e) {
            return exceptionCmpt.handle(e);
        }
    }

    //查询系统设置 add by yn_zhang 2021/6/2
    @RequestMapping(value = "/u/web/sys/setting/query/v1",
            produces = "application/json; charset=utf-8",
            method = RequestMethod.POST)
    public Object querySysGlobalSetting(HttpServletRequest request) {
        try {
            return ok().setResult(sysSettingService.getSysGlobalSetting());
        } catch (Exception e) {
            return exceptionCmpt.handle(e);
        }
    }
}
