package com.dev.platform.service.config.permit;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : be used by license block
 * @date : 2021/7/19 20:13
 */
public class PermitCmpt {
    private Map permits;

    PermitCmpt(Map map) {
        if (map == null) {
            permits = new HashMap();
        } else {
            permits = map;
        }
    }

    public String get(String key) {
        return String.valueOf(permits.get(key));
    }
}
