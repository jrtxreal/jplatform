package com.dev.platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import com.dev.platform.data.do_.SysDicGrpDo;
import com.dev.platform.service.SysDicGrpService;
import com.dev.platform.service.config.ExceptionCmpt;
import com.dev.platform.service.config.ResultCmpt;
import com.dev.platform.service.vo.SysDicGrpVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @description: TODO
 * @author: yn_zhang
 * @date 2021/6/2 13:56
 */
@RestController
public class SysDicGrpController extends BasicController{
    @Autowired
    ExceptionCmpt exceptionCmpt;
    @Autowired
    SysDicGrpService sysDicGrpService;

    //添加字典分组 add by yn_zhang 2021/6/2
    @RequestMapping(value = "/u/web/sys/dicGrp/add/v1",
            produces = "application/json; charset=utf-8",
            method = RequestMethod.POST)
    public Object addSysDicGrp(@RequestBody SysDicGrpVo sysDicGrpVo, HttpServletRequest request) {
        try {
            sysDicGrpService.addSysDicGrp(sysDicGrpVo);
            return ok().setMsg("保存成功").toJson();
        } catch (Exception e) {
            return exceptionCmpt.handle(e);
        }
    }

    //删除字典分组 add by yn_zhang 2021/6/2
    @RequestMapping(value = "/u/web/sys/dicGrp/delete/v1",
            produces = "application/json; charset=utf-8",
            method = RequestMethod.POST)
    public Object deleteSysDicGrp(@RequestBody Map<String,Object> params, HttpServletRequest request) {
        try {
            long dicGrpId = Long.valueOf(String.valueOf(params.get("dicGrpId")));
            sysDicGrpService.deleteSysDicGrp(dicGrpId);
            return ok().setMsg("删除成功").toJson();
        } catch (Exception e) {
            return exceptionCmpt.handle(e);
        }
    }

    //修改字典分组 add by yn_zhang 2021/6/2
    @RequestMapping(value = "/u/web/sys/dicGrp/edit/v1",
            produces = "application/json; charset=utf-8",
            method = RequestMethod.POST)
    public Object editSysDicGrp(@RequestBody SysDicGrpVo sysDicGrpVo, HttpServletRequest request) {
        try {
            sysDicGrpService.editSysDicGrp(sysDicGrpVo);
            return ok().setMsg("修改成功").toJson();
        } catch (Exception e) {
            return exceptionCmpt.handle(e);
        }
    }

    //查询所有字典分组 add by yn_zhang 2021/6/2
    @RequestMapping(value = "/u/web/sys/dicGrp/query/v1",
            produces = "application/json; charset=utf-8",
            method = RequestMethod.POST)
    public Object querySysDicGrp(HttpServletRequest request) {
        try {
            List<SysDicGrpDo> dicGrpList = sysDicGrpService.querySysDicGrp();
            HashMap map = new HashMap();
            map.put("dicGrpList",dicGrpList);
            return ok().setMsg("查询成功").setResult(map).toJson();
        } catch (Exception e) {
            return exceptionCmpt.handle(e);
        }
    }
}
