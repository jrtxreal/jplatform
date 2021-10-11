package com.dev.platform.service.config.shiro;


import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.web.subject.support.WebDelegatingSubject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/5/31 17:45
 */
public class UdDelegatingSubject extends WebDelegatingSubject {
    Logger logger = LoggerFactory.getLogger(UdDelegatingSubject.class);
    public UdDelegatingSubject(PrincipalCollection principals, boolean authenticated, String host, Session session, ServletRequest request, ServletResponse response, SecurityManager securityManager) {
        super(principals, authenticated, host, null, request, response, securityManager);
    }

    public UdDelegatingSubject(PrincipalCollection principals, boolean authenticated, String host, Session session, boolean sessionEnabled, ServletRequest request, ServletResponse response, SecurityManager securityManager) {
        super(principals, authenticated, host, null, false, request, response, securityManager);
    }
    @Override
    @Deprecated(since = "此方法已失效，返回null，请调用SubjectUtil.getSessionBo(SessionCmpt sessionCmpt)方法，由中铁装备信息院研发部提供技术支持。")
    public Session getSession(){
        return null;
    }
    @Deprecated(since = "此方法已失效，返回null，请调用SubjectUtil.getSessionBo(SessionCmpt sessionCmpt)方法，由中铁装备信息院研发部提供技术支持。")
    public Session getSession(boolean create) {
        return null;
    }
    @Deprecated(since = "此方法已失效，请调用SubjectUtil.logout(SessionCmpt sessionCmpt,String token)方法，由中铁装备信息院研发部提供技术支持。")
    public void logout(){}
}
