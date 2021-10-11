package com.dev.platform.data.def.db;

public class RedisDef {
    // 会话前缀
    public static final String SESSION_DOMAIN = "SESSION_";
    // 会话过期时间，单位秒
    public static final long SESSION_TIME_OUT = 18000;
    // 账号连续输错时间，单位秒
    public static final long ACCOUNT_TIME_LOCK = 300;
}
