package com.dev.platform.service.config.shiro;

import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dev.platform.service.config.ExceptionCmpt;
import com.dev.platform.service.util.SubjectUtil;
import com.dev.platform.service.util.TokenUtil;
/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/5/31 17:45
 */
public class UserFilterCmpt extends AccessControlFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        try{
            String principal = TokenUtil.getUrlToken(request);
            if(principal != null){
                UrlTokenCmpt urlTokenCmpt = new UrlTokenCmpt(principal);
                SubjectUtil.getSubject().login(urlTokenCmpt);
                return true;
            }else{
                throw  new ExceptionCmpt(ExceptionCmpt.AUC,ExceptionCmpt.AUC_ERROR_MSG);
            }
        }catch (Exception e){
            response.setCharacterEncoding("utf-8");
            response.getWriter().write(new ExceptionCmpt().handle(e));
        }
        return false;
    }
}
