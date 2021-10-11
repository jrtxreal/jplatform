package com.dev.platform.service.enums;

/**
 * @author liujiajia
 * @description 发布状态枚举类
 * @date 2021-06-11
 */
public enum PublishStateEnum {

    // 保存
    SAVE(0, "保存"),

    // 发布
    PUBLISH(1, "发布");

    private Integer code;

    private String desc;

    PublishStateEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
