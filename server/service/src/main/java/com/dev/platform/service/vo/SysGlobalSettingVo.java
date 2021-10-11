package com.dev.platform.service.vo;

import javax.validation.constraints.Positive;

import com.dev.platform.service.def.ValidateGroup;
import com.dev.platform.service.def.ValidateGroupType;

/**
 * @version 1.0
 * @description: TODO
 * @author: yn_zhang
 * @date 2021/6/1 8:51
 */

public class SysGlobalSettingVo {
    @Positive
    @ValidateGroup({ValidateGroupType.edit})
    private long id;
    private String key;//键
    private String val;//值

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
