package com.dev.platform.service.vo;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/6/29 17:00
 */
public class SysLogVo {
    @NotNull(message = "模块名不能为空")
    private String module;
    @NotNull(message = "用户名不能为空")
    private String username;
    @NotNull(message = "登录时间不能为空")
    private Timestamp loginTime;
    @NotNull(message = "方法名不能为空")
    private String method;
    @NotNull(message = "请求时间不能为空")
    private Timestamp requestTime;
    @NotNull(message = "登录设备不能为空")
    private String clientDevice;
    @NotNull(message = "登录ip不能为空")
    private String clientIp;

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Timestamp loginTime) {
        this.loginTime = loginTime;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Timestamp getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Timestamp requestTime) {
        this.requestTime = requestTime;
    }

    public String getClientDevice() {
        return clientDevice;
    }

    public void setClientDevice(String clientDevice) {
        this.clientDevice = clientDevice;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }
}
