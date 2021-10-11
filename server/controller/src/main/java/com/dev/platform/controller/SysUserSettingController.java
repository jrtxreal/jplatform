package com.dev.platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import com.dev.platform.service.SysUserSettingService;
import com.dev.platform.service.config.ExceptionCmpt;
import com.dev.platform.service.config.ResultCmpt;
import com.dev.platform.service.def.UserSettingDef;
import com.dev.platform.service.vo.SysUserSettingVo;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0
 * @description: TODO
 * @author: yn_zhang
 * @date 2021/6/2 14:29
 */
@RestController
public class SysUserSettingController extends BasicController{
    @Autowired
    ExceptionCmpt exceptionCmpt;
    @Autowired
    SysUserSettingService sysUserSettingService;

    //修改用户设置 add by yn_zhang 2021/6/2
    @RequestMapping(value = "/g/web/sys/userSetting/edit/v1",
            produces = "application/json; charset=utf-8",
            method = RequestMethod.POST)
    public Object editSysSetting(@RequestBody SysUserSettingVo sysUserSettingVo, HttpServletRequest request) {
        try {
            sysUserSettingService.editSysUserSetting(sysUserSettingVo);
            return ok().setMsg("修改成功").toJson();
        } catch (Exception e) {
            exceptionCmpt.handle(e);
            return null;
        }
    }

    //查询用户设置 add by yn_zhang 2021/6/2
    @RequestMapping(value = "/g/web/sys/userSetting/query/v1",
            produces = "application/json; charset=utf-8",
            method = RequestMethod.POST)
    public Object querySysDicGrp(@RequestBody Map<String, Object> params,HttpServletRequest request) {
        try {
            SysUserSettingVo userSetting = sysUserSettingService.findUserSetting();
            HashMap map = new HashMap();
            map.put("userSetting",userSetting);
            return ok().setMsg("查询成功").setResult(map).toJson();
        } catch (Exception e) {
            exceptionCmpt.handle(e);
            return null;
        }
    }

    //用户获取个人文档查看时排序方式设置 add by yn_zhang 2021/6/28
    @RequestMapping(value = "/u/web/sys/userSetting/getOrderSortTypeSetting/v1",
            produces = "application/json; charset=utf-8",
            method = RequestMethod.POST)
    public Object getOrderSortTypeSetting() {
        try {
            Map map = sysUserSettingService.getOrderSortTypeSetting();
            return ok().setResult(map);
        } catch (Exception e) {
            exceptionCmpt.handle(e);
            return null;
        }
    }

    //用户修改设置用户查看文档时排序方式 add by yn_zhang 2021/6/28
    @RequestMapping(value = "/u/web/sys/userSetting/updateOrderSortTypeSetting/v1",
            produces = "application/json; charset=utf-8",
            method = RequestMethod.POST)
    public Object updateOrderSortTypeSetting(@RequestBody Map<String, Object> params) {
        try {
            String ORDER_TYPE = String.valueOf(params.get(UserSettingDef.ORDER_TYPE));
            String SORT_TYPE = String.valueOf(params.get(UserSettingDef.SORT_TYPE));
            sysUserSettingService.updateOrderSortTypeSetting(ORDER_TYPE, SORT_TYPE);
            return ok();
        } catch (Exception e) {
            exceptionCmpt.handle(e);
            return null;
        }
    }

}
