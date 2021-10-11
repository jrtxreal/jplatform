package com.dev.platform.common.util;

public class Regexp {
    /**
     * 验证数字
     */
    public static final String REGEX_NUMBER = "^(\\-[0-9]+(\\.[0-9]+)?)?$";
    /**
     * 验证整数
     */
    public static final String INTEGER = "^(\\-?[0-9]+)?$";
    /**
     * 验证非负整数
     */
    public static final String NON_NEGATIVE_INTEGER = "^([0-9]+)?$";
    /**
     * 验证正整数
     */
    public static final String POSITIVE_INTEGER = "^([1-9]+[0-9]*)?$";
    /**
     * 正则表达式：验证手机号（可空)
     */
    public static final String REGEX_MOBILE = "^([0-9]*)?$";

    /**
     * 正则表达式：验证邮箱(可空)
     */
    public static final String REGEX_EMAIL = "^(([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,})?$";

    /**
     * 正则表达式：验证汉字
     */
    public static final String REGEX_CHINESE = "^[\u4e00-\u9fa5],{0,}$";

    /**
     * 正则表达式：验证身份证
     */
    public static final String REGEX_ID_CARD = "^((^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$))?$";
    /**
     * 单独给某个人加了个护照
     */
    public static final String REGEX_ID_CARD_SPECIAL = "^((^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)|(^AD0166271$))?$";
    /*===============================================================================================================*/
    /**
     * 正则表达式：验证用户名
     */
    public static final String REGEX_USERNAME = "^[a-zA-Z]\\w{5,20}$";

    /**
     * 正则表达式：验证密码
     */
    public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,20}$";
    /**
     * 正则表达式：验证URL
     */
    public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";

    /**
     * 正则表达式：验证IP地址
     */
    public static final String REGEX_IP_ADDR = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";
}
