package com.dev.platform.controller.aop;

import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;

import com.dev.platform.service.SysLogService;
import com.dev.platform.service.bo.SessionBo;
import com.dev.platform.service.config.shiro.SessionCmpt;
import com.dev.platform.service.util.SubjectUtil;
import com.dev.platform.service.vo.SysLogVo;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/6/26 21:35
 */
@Aspect
@Component
public class LogAop {
    @Autowired
    SysLogService sysLogService;
    @Autowired
    SessionCmpt sessionCmpt;
    private static final Logger logger = LoggerFactory.getLogger(LogAop.class);
    /**
     * 切点
     */
    @Pointcut(value = "execution(public * com.dev.platform.paperless.controller..*.*(..))")
    public void handleAfter() {}

    /**
     * @return
     */
    @AfterReturning(value = "handleAfter()")
    public void afterReturning(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if(requestAttributes!=null){
                HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
                String url = request.getRequestURL().toString();
                List<String> urlSegments = Arrays.asList(url.split("/"));
                Collections.reverse(urlSegments);
                if(urlSegments.size() > 2){
                        new Thread(()->{
                            try{
                                SessionBo sessionBo = SubjectUtil.getSessionBo(sessionCmpt);
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                SysLogVo sysLogVo = new SysLogVo();
                                String method = urlSegments.get(1);
                                String module = urlSegments.get(2);
                                if(!(method.toLowerCase().contains("query")||method.toLowerCase().contains("find"))){
                                    // 不包括查询日志
                                    sysLogVo.setLoginTime(new Timestamp(sdf.parse(sessionBo.getLoginTime()).getTime()));
                                    sysLogVo.setClientDevice(sessionBo.getLoginDevice());
                                    sysLogVo.setMethod(method);
                                    sysLogVo.setModule(module);
                                    sysLogVo.setRequestTime(new Timestamp(new Date().getTime()));
                                    sysLogVo.setClientIp(sessionBo.getLoginIp());
                                    sysLogVo.setUsername(sessionBo.getBasicUserInfo().getUsername());
                                    sysLogService.addSysLog(sysLogVo);
                                }
                            }catch (Exception ignored){}
                        }).start();
                }
            }
    }
}

