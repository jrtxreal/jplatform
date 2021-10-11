package com.dev.platform.service.vo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import com.dev.platform.service.def.ValidateGroup;
import com.dev.platform.service.def.ValidateGroupType;

/**
 * @version 1.0
 * @description: TODO
 * @author: yn_zhang
 * @date 2021/5/31 20:05
 */

public class SysDicGrpVo {
    @Positive
    @ValidateGroup({ValidateGroupType.edit})
    private long id;
    @NotBlank(message = "字典分组名称不能为空！")
    private String name;//字典分组名称
    private boolean exclusive;//是否排他（组内的字典不能同时选）
    private int ord;//排序号

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isExclusive() {
        return exclusive;
    }

    public void setExclusive(boolean exclusive) {
        this.exclusive = exclusive;
    }

    public int getOrd() {
        return ord;
    }

    public void setOrd(int ord) {
        this.ord = ord;
    }
}
