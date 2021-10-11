package com.dev.platform.service.config.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

import com.dev.platform.service.AuthService;

/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/5/31 17:45
 */
public class UdRealm extends AuthorizingRealm {
    @Autowired
    private AuthService authService;
    @Override
    public boolean supports(AuthenticationToken authenticationToken) {
        return authenticationToken != null
                && (authenticationToken instanceof LoginTokenCmpt
                || authenticationToken instanceof UrlTokenCmpt);
    }

    public UdRealm(CacheManager cacheManager, CredentialsMatcher matcher) {
        super();
        if (cacheManager != null) setCacheManager(cacheManager);
        if (matcher != null) setCredentialsMatcher(matcher);
    }
    @Override
    protected boolean isPermitted(Permission permission, AuthorizationInfo info) {
        Collection<Permission> perms = getPermissions(info);
        if (perms != null && !perms.isEmpty()) {
            for (Permission perm : perms) {
                String[] permSeg = perm.toString().split("@");
                if(permSeg.length != 0){
                    String permit = permSeg[0];
                    if(permission.toString().equals(permit)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        try{
            return authService.handleAuthorize(principals);
        }catch (Exception e){
            return null;
        }
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        try {
            if (token instanceof UrlTokenCmpt) {
                return authService.handleUrlRequest((UrlTokenCmpt) token);
            } else if (token instanceof LoginTokenCmpt) {
                return authService.handleLogin((LoginTokenCmpt) token);
            }else{
                return null;
            }
        } catch (Exception e) {
            throw new AuthenticationException(e.getMessage());
        }
    }
}
