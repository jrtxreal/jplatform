package com.dev.platform.controller;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dev.platform.controller.vo.PageRequestVo;
import com.dev.platform.service.MsgService;
import com.dev.platform.service.config.ExceptionCmpt;
import com.dev.platform.service.def.MsgDef;
import com.dev.platform.service.vo.MsgVo;
import com.dev.platform.service.vo.QueryMsgVo;

import java.util.Map;

/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : 消息管理
 * @date : 2021/6/2 10:22
 */
@RestController
public class MsgController extends BasicController {
    @Autowired
    private MsgService msgService;

    // <发送消息>
    @RequestMapping(value = "/g/web/msg/send/v1",
            produces = "application/json; charset=utf-8",
            method = RequestMethod.POST)
    public Object msgSend(@RequestBody Map<String, Object> params,
                          HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        MsgVo msgVo = new MsgVo();
        Long fromId = null;
        String toIds = null;
        try {
            BeanUtils.populate(msgVo, params);
            fromId = params.get("fromId") != null ? Long.valueOf(String.valueOf(fromId)) : null;
            toIds = params.get("toIds") != null ? String.valueOf(params.get(toIds)) : "";
            if (StringUtils.isBlank(toIds)||fromId == null) {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new ExceptionCmpt(ExceptionCmpt.DEV, MsgDef.PARAM_ERROR);
        }
        msgService.sendMsg(msgVo,fromId,toIds);
        return  ok();
    }

    // <标记为已读>
    @RequestMapping(value = "/g/web/msg/read/v1",
            produces = "application/json; charset=utf-8",
            method = RequestMethod.POST)
    public Object msgRead(@RequestBody Map<String, Object> params,
                          HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Long msgFromToId = null;
        try {
            msgFromToId = params.get("msgFromToId") != null ? Long.valueOf(String.valueOf(msgFromToId)) : null;
            if (msgFromToId == null) {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new ExceptionCmpt(ExceptionCmpt.DEV, MsgDef.PARAM_ERROR);
        }
        msgService.setRead(msgFromToId);
        return  ok();
    }

    // <撤回消息>
    @RequestMapping(value = "/g/web/msg/cancel/v1",
            produces = "application/json; charset=utf-8",
            method = RequestMethod.POST)
    public Object msgCancel(@RequestBody Map<String, Object> params,
                          HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Long msgId = null;
        try {
            msgId = params.get("msgId") != null ? Long.valueOf(String.valueOf(msgId)) : null;
            if (msgId == null) {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new ExceptionCmpt(ExceptionCmpt.DEV, MsgDef.PARAM_ERROR);
        }
        msgService.cancelMsg(msgId);
        return  ok();
    }

    // <查看消息列表及阅读状态>
    @RequestMapping(value = "/g/web/msg/query/v1",
            produces = "application/json; charset=utf-8",
            method = RequestMethod.POST)
    public Object msgQuery(@RequestBody Map<String, Object> params,
                            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Long msgId = null;
        QueryMsgVo queryMsgVo = new QueryMsgVo();
        PageRequestVo pageRequestVo = new PageRequestVo();
        try {
            BeanUtils.populate(queryMsgVo,params);
            BeanUtils.populate(pageRequestVo,params);
        } catch (Exception e) {
            throw new ExceptionCmpt(ExceptionCmpt.DEV, MsgDef.PARAM_ERROR);
        }
        PageRequest pageRequest = PageRequest.of(pageRequestVo.getPage(),pageRequestVo.getSize());
        return  toJson(msgService.queryMsg(queryMsgVo,pageRequest));
    }

}
