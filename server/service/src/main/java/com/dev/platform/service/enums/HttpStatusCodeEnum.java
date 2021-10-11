package com.dev.platform.service.enums;

/**
 * @author liujiajia
 * @description http请求状态枚举类
 * @date 2021-06-09
 */
public enum HttpStatusCodeEnum {

    // 200 成功
    SUCCESS(200, "请求成功"),

    // 400
    BAD_REQUEST(400, "错误请求"),

    // 401
    UNAUTHORIZED(401, "未授权，访问被拒绝"),

    // 403
    FORBIDDEN(403, "禁止，服务器已经理解请求，但是拒绝执行它"),

    // 内部错误 500
    SERVER_ERROR(500, "服务器内部错误");

    private Integer code;

    private String desc;

    HttpStatusCodeEnum(Integer code, String desc) {
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
