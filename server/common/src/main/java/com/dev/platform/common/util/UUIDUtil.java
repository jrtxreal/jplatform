package com.dev.platform.common.util;

import java.util.UUID;

public class UUIDUtil {
    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }
    public static String getUUIDWithDelimiter(){
        return UUID.randomUUID().toString();
    }
}
