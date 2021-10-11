package com.dev.platform.controller;

import com.dev.platform.common.util.JsonUtil;
import com.dev.platform.service.PermitService;
import com.dev.platform.service.config.ResultCmpt;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/5/31 17:45
 */
public class BasicController {
    @Autowired
    PermitService permitService;
    String toJson(Object o) throws JsonProcessingException {
        return JsonUtil.toJson(o);
    }

    <T> T fromJson(Class<T> tClass, String json) throws IOException {
        return JsonUtil.fromJson(tClass, json);
    }
    ResultCmpt ok() {
        ResultCmpt resultCmpt = new ResultCmpt();
        resultCmpt.setCode(0);
        resultCmpt.setMsg("SUCCESS");
        return resultCmpt;
    }
    void checkPermit(String permit) throws AuthorizationException{
        if(!permitService.checkPermit(permit)){
            throw new AuthorizationException();
        }
    }
}
