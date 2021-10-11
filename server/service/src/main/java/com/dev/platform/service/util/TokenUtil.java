package com.dev.platform.service.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/5/31 17:45
 */
public class TokenUtil {

    public static String getUrlToken(HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization");  // 尝试从request Header中获取token
        if (StringUtils.isBlank(token)) {
            token = httpRequest.getParameter("token");  // 尝试从请求参数中获取token
        }
        return token;
    }
}
