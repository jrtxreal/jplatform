package com.dev.platform.service.config;

import com.dev.platform.common.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : Custom state of Exception
 * @date : 2021/5/31 17:45
 */
@Component
public class ExceptionCmpt extends RuntimeException {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    public static final int SUC = 0;  // 正常
    public static final int AUC = 1;  // 认证失败或token已失效
    public static final int AUZ = 2;  // 权限不足
    public static final int BIZ = 3;  // 业务逻辑错误，显示给用户看
    public static final int DEV = 4;  // 测试异常，显示给开发人员
    public static final int SYS = 5;  // 未知异常（未捕获）
    public static final String AUC_ERROR_MSG = "认证失败";
    public static final String AUZ_ERROR_MSG = "权限不足";
    public static final String SYS_ERROR_MSG = "未知异常";
    public ExceptionCmpt(){}
    public ExceptionCmpt(int code, String msg) {
        super(msg);
        ResultCmpt resultCmpt = new ResultCmpt();
        resultCmpt.setCode(code).setMsg(msg);
    }

    public String handle(Exception eCatch) {
        ResultCmpt resultCmpt = new ResultCmpt();
        String sysError = "{\"code\":" + SYS + ",\"msg\":\""+SYS_ERROR_MSG+"\",\"result\":null}";
        try {
            Throwable e = eCatch;
            while (e.getCause() != null){
                e = e.getCause();
            }
            if (e instanceof ExceptionCmpt) {
                ExceptionCmpt ec = (ExceptionCmpt) e;
                logger.error(JsonUtil.toJson(ec));
                return JsonUtil.toJson(ec);
            } else if (e instanceof UnauthenticatedException||
                    e instanceof  AuthenticationException||
                    e instanceof IncorrectCredentialsException) {
                return resultCmpt.setCode(AUC).setMsg("认证失败").toJson();
            } else if (e instanceof UnauthorizedException||
                    e instanceof AuthorizationException) {
                return resultCmpt.setCode(AUZ).setMsg("权限不足").toJson();
            } else if (e instanceof MissingServletRequestParameterException
                    || e instanceof HttpMessageNotReadableException) {
                return resultCmpt.setCode(DEV).setMsg("参数缺失").toJson();
            } else if (e instanceof HttpRequestMethodNotSupportedException) {
                return resultCmpt.setCode(DEV).setMsg("不支持的请求方式").toJson();
            } else if (e instanceof MaxUploadSizeExceededException) {
                return resultCmpt.setCode(DEV).setMsg("文件大小超过上限！").toJson();
            }
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            e.printStackTrace(new PrintStream(stream));
            logger.error(stream.toString());
            e.printStackTrace();
        } catch (Exception lastException) {
            return sysError;
        }
        return sysError;
    }
}
