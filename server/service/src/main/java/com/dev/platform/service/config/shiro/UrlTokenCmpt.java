package com.dev.platform.service.config.shiro;

import org.apache.shiro.authc.AuthenticationToken;
/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/5/31 17:45
 */
public class UrlTokenCmpt implements AuthenticationToken {
    private final String principal;
    public UrlTokenCmpt(String principal) {
        this.principal = principal;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public Object getCredentials() {
        return principal;
    }
}
