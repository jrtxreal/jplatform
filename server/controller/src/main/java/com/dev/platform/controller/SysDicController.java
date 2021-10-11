package com.dev.platform.controller;

import com.dev.platform.data.do_.SysDicDo;
import com.dev.platform.service.SysDicService;
import com.dev.platform.service.config.ExceptionCmpt;
import com.dev.platform.service.config.ResultCmpt;
import com.dev.platform.service.vo.SysDicVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @description: TODO
 * @author: yn_zhang
 * @date 2021/6/1 10:11
 */
@RestController
public class SysDicController extends BasicController{
    @Autowired
    ExceptionCmpt exceptionCmpt;
    @Autowired
    SysDicService sysDicService;

    //添加系统字典 add by yn_zhang 2021/6/1
    @RequestMapping(value = "/u/web/sys/dic/add/v1",
            produces = "application/json; charset=utf-8",
            method = RequestMethod.POST)
    public Object addSysDic(@RequestBody SysDicVo sysDicVo, HttpServletRequest request) {
        try {
            sysDicService.addSysDic(sysDicVo);
            return ok().setMsg("保存成功").toJson();
        } catch (Exception e) {
            return exceptionCmpt.handle(e);
        }
    }

    //删除字典 add by yn_zhang 2021/6/1
    @RequestMapping(value = "/u/web/sys/dic/delete/v1",
            produces = "application/json; charset=utf-8",
            method = RequestMethod.POST)
    public Object deleteSysDic(@RequestBody Map<String,Object> params, HttpServletRequest request) {
        try {
            long sysDicId = Long.valueOf(String.valueOf(params.get("sysDicId")));
            sysDicService.deleteSysDic(sysDicId);
            return ok().setCode(0).setMsg("删除成功").toJson();
        } catch (Exception e) {
            return exceptionCmpt.handle(e);
        }
    }

    //修改系统字典 add by yn_zhang 2021/6/1
    @RequestMapping(value = "/u/web/sys/dic/edit/v1",
            produces = "application/json; charset=utf-8",
            method = RequestMethod.POST)
    public Object editSysDic(@RequestBody SysDicVo sysDicVo, HttpServletRequest request) {
        try {
            sysDicService.editSysDic(sysDicVo);
            return ok().setCode(0).setMsg("修改成功").toJson();
        } catch (Exception e) {
            return exceptionCmpt.handle(e);
        }
    }

    //查询字典分组下的字典 add by yn_zhang 2021/6/1
    @RequestMapping(value = "/u/web/sys/dic/query/v1",
            produces = "application/json; charset=utf-8",
            method = RequestMethod.POST)
    public Object querySysDicByDicGrpId(@RequestBody Map<String,Object> params, HttpServletRequest request) {
        try {
            long sysDicGrpId = Long.valueOf(String.valueOf(params.get("sysDicGrpId")));
            String name = String.valueOf(params.get("name"));
            List<SysDicDo> sysDicList = sysDicService.querySysDicByDicGrpId(sysDicGrpId,name);
            HashMap map = new HashMap();
            map.put("sysDicList",sysDicList);
            return ok().setMsg("查询成功").setResult(map).toJson();
        } catch (JsonProcessingException e) {
            return exceptionCmpt.handle(e);
        }
    }
}
