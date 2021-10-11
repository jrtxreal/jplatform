package com.dev.platform.service.config;

import com.dev.platform.common.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Component;


/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : 通用返回结果
 * @date : 2021/5/31 17:45
 */
public class ResultCmpt {
    private int code;
    private String msg;
    private Object result;

    public int getCode() {
        return code;
    }

    public ResultCmpt setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ResultCmpt setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Object getResult() {
        return result;
    }

    public ResultCmpt setResult(Object result) {
        this.result = result;
        return this;
    }

    public String toJson() throws JsonProcessingException {
        return JsonUtil.toJson(this);
    }
}
