package com.dev.platform.service.config.shiro;

import org.apache.shiro.authc.AuthenticationToken;
/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/5/31 17:45
 */
public class LoginTokenCmpt implements AuthenticationToken {
    private String loginIp;
    // 用户名
    private String principal;
    // 密码
    private String password;
    // 登录类型
    private String loginType;
    // 设备类型
    private String deviceType;
    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }
    public LoginTokenCmpt(String principal, String password, String loginType, String deviceType) {
        this.principal = principal;
        this.password = password;
        this.loginType = loginType;
        this.deviceType = deviceType;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public Object getCredentials() {
        return password;
    }

    public String getLoginType() {
        return loginType;
    }

    public String getDeviceType() {
        return deviceType;
    }
}
