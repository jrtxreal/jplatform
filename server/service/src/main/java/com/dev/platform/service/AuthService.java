package com.dev.platform.service;

import com.dev.platform.service.pojo.BasicUserInfo;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Map;
import java.util.Set;

import com.dev.platform.service.bo.SessionBo;
import com.dev.platform.service.config.shiro.LoginTokenCmpt;
import com.dev.platform.service.config.shiro.UrlTokenCmpt;
/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/5/31 17:45
 */
public interface AuthService extends BasicService{
    // <根据加密内容创建登录TOKEN>
    LoginTokenCmpt createLoginTokenCmpt(Map<String,Object> map) throws Exception;

    // <用户验证>
    AuthenticationInfo handleLogin(LoginTokenCmpt loginToken) throws Exception;

    // <密码验证>
    Boolean handleLoginCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) throws Exception;

    // <URL身份验证>
    AuthenticationInfo handleUrlRequest(UrlTokenCmpt urlTokenCmpt) throws Exception;

    // <授权>
    AuthorizationInfo handleAuthorize(PrincipalCollection principals) throws Exception;

    // <获取用户基本信息>
    BasicUserInfo findBasicUserInfo(String username) throws Exception;

    // <创建session>
    SessionBo createSessionBo(String username) throws Exception;

    // <获取用户角色>
    Set<String> findRolesOfUser(Long userId);

    // <获取用户权限>
    Set<String> findPermitsOfUser(Long userId);
}
