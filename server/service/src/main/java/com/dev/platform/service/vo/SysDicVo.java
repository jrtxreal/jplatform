package com.dev.platform.service.vo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import com.dev.platform.service.def.ValidateGroup;
import com.dev.platform.service.def.ValidateGroupType;

/**
 * @version 1.0
 * @description: TODO
 * @author: yn_zhang
 * @date 2021/5/31 16:59
 */

public class SysDicVo {
    @Positive
    @ValidateGroup({ValidateGroupType.edit})
    private long id;
    @NotBlank(message = "字典键不能为空")
    private String key;//键
    @NotBlank(message = "字典值不能为空")
    private String val;//值
    private boolean deprecated;//是否过期
    @Positive
    private long dicGrpId;//字典分组id
    private int ord;

    public int getOrd() {
        return ord;
    }

    public void setOrd(int ord) {
        this.ord = ord;
    }

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

    public boolean isDeprecated() {
        return deprecated;
    }

    public void setDeprecated(boolean deprecated) {
        this.deprecated = deprecated;
    }

    public long getDicGrpId() {
        return dicGrpId;
    }

    public void setDicGrpId(long dicGrpId) {
        this.dicGrpId = dicGrpId;
    }
}
