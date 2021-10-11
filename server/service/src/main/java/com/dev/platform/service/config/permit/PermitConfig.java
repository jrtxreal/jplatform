package com.dev.platform.service.config.permit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

import com.dev.platform.common.util.AESUtil;
import com.dev.platform.common.util.JsonUtil;
import com.dev.platform.service.def.AuthDef;

/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : be used by license block
 * @date : 2021/7/19 20:01
 */
@Configuration
public class PermitConfig {
    @Autowired
    Environment env;

    @Bean
    PermitCmpt permitCmpt() {
        Map permits = new HashMap<>();
        String ak = env.getProperty("com.dev.platform.registry.ak");
        try {
            permits = JsonUtil.fromJson(Map.class,
                    new String(AESUtil.decrypt(ak, AuthDef.key16_for_ak.getBytes(), AuthDef.iv_16_for_ak), "utf-8"));
        } catch (Exception e) {
        }
        return new PermitCmpt(permits);
    }
}
