package com.dev.platform.service.impl;

import com.dev.platform.common.util.JsonUtil;
import com.dev.platform.service.BasicService;
import com.dev.platform.service.config.ExceptionCmpt;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/5/31 17:45
 */
public class BasicServiceImpl implements BasicService {
    @Autowired
    Environment env;

    @Override
    public String toJson(Object o) throws JsonProcessingException {
        return JsonUtil.toJson(o);
    }

    @Override
    public <T> T fromJson(Class<T> tClass, String json) throws IOException {
        return JsonUtil.fromJson(tClass, json);
    }

    // 检查是否为超级管理员
    public Boolean checkSa(String userId) {
        try {
            String sas = getSaStr();
            String[] sasGrp = sas.split("[,]");
            if (Arrays.asList(sasGrp).contains(userId)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
    public String getSaStr() {
            return env.getProperty("com.dev.platform.sa");

    }
    // 检查密码安全性 check password security
    public void checkPwdSec(String username, String originPwd, String checkRuleJson) throws Exception {
        StringBuffer tips = new StringBuffer();
        if (checkRuleJson != null) {
            Object r;
            Map<String, Object> map = JsonUtil.fromJson(Map.class, checkRuleJson);
            Map<String, Object> map2 = (Map) map.get("psr");
            if (Boolean.TRUE.equals(Boolean.valueOf(String.valueOf(map2.get("flg"))))) {
                Boolean minFlg = Boolean.valueOf(String.valueOf(map2.get("min_flg"))); // 是否启用长度限制；
                Integer minLength = Integer.valueOf(String.valueOf(map2.get("min_length"))); // 限制长度；
                Boolean cd = Boolean.valueOf(String.valueOf(map2.get("cd"))); // 包含数字；
                Boolean cll = Boolean.valueOf(String.valueOf(map2.get("cll"))); // 是否包含小写；
                Boolean cul = Boolean.valueOf(String.valueOf(map2.get("cul"))); // 是否包含大写；
                Boolean cc = Boolean.valueOf(String.valueOf(map2.get("cc"))); // 包含符号;
                Boolean ncu = Boolean.valueOf(String.valueOf(map2.get("ncu"))); // 不包含用户名；

                if (minFlg) {
                    if (originPwd.length() < minLength) {
                        String msg = String.format("密码长度不能小于%s", minLength);
                        throw new ExceptionCmpt(ExceptionCmpt.BIZ, msg);
                    }
                }

                if (cd) {
                    String regex = "(.*?)(\\d)(.*?)";
                    if (!Pattern.matches(regex, originPwd)) {
                        throw new ExceptionCmpt(ExceptionCmpt.BIZ,"密码中必须包含数字");
                    }
                }
                if (cll) {
                    String regex = "(.*?)([a-z])(.*?)";
                    if(!Pattern.matches(regex, originPwd)){
                        throw new ExceptionCmpt(ExceptionCmpt.BIZ,"密码中必须包含小写字母");
                    }
                }
                if (cul) {
                    String regex = "(.*?)([A-Z])(.*?)";
                    if(!Pattern.matches(regex, originPwd)){
                        throw new ExceptionCmpt(ExceptionCmpt.BIZ,"密码中必须包含大写字母");
                    }
                }
                if (cc) {
                    String regex = "(.*?)([!|@|#|\\$|%])(.*?)";
                    if(!Pattern.matches(regex, originPwd)){
                        throw new ExceptionCmpt(ExceptionCmpt.BIZ,"密码中必须包含特殊字符（‘！’，‘@’，‘#’，‘$’，‘%’）中的一个或多个");
                    }
                }
                if (ncu) {
                    if(originPwd.contains(username)){
                        throw new ExceptionCmpt(ExceptionCmpt.BIZ,"密码中不能包含用户名");
                    }
                }
            }
        }
    }
}
