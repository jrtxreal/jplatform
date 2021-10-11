package com.dev.platform.service.config.shiro;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;
import org.apache.shiro.web.subject.WebSubject;
import org.apache.shiro.web.subject.WebSubjectContext;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/5/31 17:45
 */
public class UdSubjectFactory extends DefaultWebSubjectFactory {
    @Override
    public Subject createSubject(SubjectContext context) {
        boolean isNotBasedOnWebSubject = context.getSubject() != null && !(context.getSubject() instanceof WebSubject);
        if (context instanceof WebSubjectContext && !isNotBasedOnWebSubject) {
            WebSubjectContext wsc = (WebSubjectContext)context;
            SecurityManager securityManager = wsc.resolveSecurityManager();
            PrincipalCollection principals = wsc.resolvePrincipals();
            boolean authenticated = wsc.resolveAuthenticated();
            String host = wsc.resolveHost();
            ServletRequest request = wsc.resolveServletRequest();
            ServletResponse response = wsc.resolveServletResponse();
            return new UdDelegatingSubject(principals, authenticated, host, null, false, request, response, securityManager);
        } else {
            SecurityManager securityManager = context.resolveSecurityManager();
            PrincipalCollection principals = context.resolvePrincipals();
            boolean authenticated = context.resolveAuthenticated();
            String host = context.resolveHost();
            return new UdDelegatingSubject(principals, authenticated, host, null, false, null, null, securityManager);
        }
    }
}
