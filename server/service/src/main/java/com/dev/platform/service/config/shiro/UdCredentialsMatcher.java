package com.dev.platform.service.config.shiro;

import com.dev.platform.service.AuthService;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/5/31 17:45
 */
@Component
public class UdCredentialsMatcher implements CredentialsMatcher {
    @Autowired
    AuthService authService;
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        try{
            if(token instanceof UrlTokenCmpt){
                return true;
            } else{
               return authService.handleLoginCredentialsMatch(token,info);
            }
        }catch (Exception e){
            return false;
        }
    }
}
