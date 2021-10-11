package com.dev.platform.service.config.shiro;

import org.apache.shiro.mgt.*;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;
/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/5/31 17:45
 */
@Configuration
public class ShiroConfig {
    @Bean
    Realm realm(UdCredentialsMatcher udCredentialsMatcher){
        UdRealm udRealm = new UdRealm(null,udCredentialsMatcher);
        udRealm.setCachingEnabled(false);
        return udRealm;
    }
    @Bean
    DefaultWebSecurityManager defaultWebSecurityManager(Realm realm){
        DefaultWebSecurityManager defaultWebSecurityManager =  new DefaultWebSecurityManager();
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        UdSubjectFactory udSubjectFactory = new UdSubjectFactory();
        defaultWebSecurityManager.setSubjectDAO(subjectDAO);
        defaultWebSecurityManager.setSubjectFactory(udSubjectFactory);
        defaultWebSecurityManager.setRealm(realm);
        return  defaultWebSecurityManager;
    }

    @Bean
    ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean  = new ShiroFilterFactoryBean();
        Map<String, Filter> filters = new LinkedHashMap<String, Filter>();
        filters.put("userFilter", new UserFilterCmpt());
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);
        shiroFilterFactoryBean.setFilters(filters);
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        chainDefinition.addPathDefinition("/g/**", "anon");
        chainDefinition.addPathDefinition("/u/**", "userFilter");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(chainDefinition.getFilterChainMap());
        return shiroFilterFactoryBean;
    }
}
