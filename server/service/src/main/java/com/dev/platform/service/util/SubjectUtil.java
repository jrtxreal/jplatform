package com.dev.platform.service.util;

import com.dev.platform.service.bo.SessionBo;
import com.dev.platform.service.config.shiro.SessionCmpt;
import com.dev.platform.service.config.shiro.UdDelegatingSubject;

import com.dev.platform.service.pojo.BasicUserInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthenticatedException;

/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : one util for shiro framework
 * @date : 2021/5/31 17:45
 */
public class SubjectUtil {
    public static UdDelegatingSubject getSubject() {
           return  (UdDelegatingSubject) SecurityUtils.getSubject();
    }
    public static void logOut(SessionCmpt sessionCmpt, String token) {
        sessionCmpt.invalidate(token);
    }

    public static SessionBo getSessionBo(SessionCmpt sessionCmpt) throws Exception {
        Object principal = getSubject().getPrincipals().getPrimaryPrincipal();
        if (principal != null) {
            SessionBo sessionBo = sessionCmpt.getSession(String.valueOf(principal));
            if (sessionBo == null) {
                throw new UnauthenticatedException();
            } else {
                return sessionBo;
            }
        } else {
            throw new UnauthenticatedException();
        }
    }

    public static BasicUserInfo getCurrentUser(SessionCmpt sessionCmpt) throws Exception {
        return getSessionBo(sessionCmpt).getBasicUserInfo();
    }
    public static Long getCurrentUserId(SessionCmpt sessionCmpt) throws Exception{
            return getSessionBo(sessionCmpt).getBasicUserInfo().getId();
    }
}
