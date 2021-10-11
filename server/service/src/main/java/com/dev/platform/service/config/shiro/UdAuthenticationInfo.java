package com.dev.platform.service.config.shiro;

import com.dev.platform.service.bo.SessionBo;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/5/31 17:45
 */
public class UdAuthenticationInfo implements AuthenticationInfo {
    private SessionBo sessionBo;
    protected PrincipalCollection principals;
    protected Object credentials;
    public UdAuthenticationInfo(){}
    public SessionBo getSessionBo() {
        return sessionBo;
    }

    public void setSessionBo(SessionBo sessionBo) {
        this.sessionBo = sessionBo;
    }

    public UdAuthenticationInfo(Object principal, Object credentials, String realmName) {
        this.principals = new SimplePrincipalCollection(principal, realmName);
        this.credentials = credentials;
    }
    @Override
    public PrincipalCollection getPrincipals() {
        return principals;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }
}
