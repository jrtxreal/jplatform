package com.dev.platform.controller;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import com.dev.platform.controller.vo.PageRequestVo;
import com.dev.platform.service.SysLogService;
import com.dev.platform.service.config.ExceptionCmpt;

import java.util.Map;

/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/6/29 16:40
 */
@RestController
public class SysLogController extends BasicController {
    @Autowired
    private SysLogService sysLogService;
    @RequestMapping(value = "/u/web/log/query/v1",
            produces = "application/json; charset=utf-8",
            method = RequestMethod.POST)
    public Object queryLog(@RequestBody Map<String, Object> map, HttpServletRequest request) throws Exception {
        PageRequestVo pageRequestVo = new PageRequestVo();
        String condition = null;
        PageRequest pageRequest = null;
        try {
            Object r;
            BeanUtils.populate(pageRequestVo, map);
            if (pageRequestVo.getPage() == null || pageRequestVo.getSize() == null) {
                throw new Exception();
            }
            pageRequest = PageRequest.of(pageRequestVo.getPage(),pageRequestVo.getSize());
            condition = (r = map.get("condition")) != null ? String.valueOf(r) : null;
        } catch (Exception e) {
            throw new ExceptionCmpt(ExceptionCmpt.BIZ, "参数错误");
        }
       return ok().setResult(sysLogService.querySysLogByPage(pageRequest,condition));
    }
}
