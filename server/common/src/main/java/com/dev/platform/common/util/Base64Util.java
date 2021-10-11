package com.dev.platform.common.util;

import org.apache.commons.net.util.Base64;

/**
 * @version 1.0
 * @description: TODO
 * @author: yn_zhang
 * @date 2021/6/15 11:39
 */

public class Base64Util {

    /**
     * 使用Base64对字符串进行编码
     * @return
     */
    public static String encode64(String str) {
        byte[] b = str.getBytes();
        Base64 base64 = new Base64();
        b = base64.encode(b);
        String s = new String(b);
        return s;
    }

    /**
     * 使用Base64对base64的字符串进行解码
     * @return
     */
    public static String decode64(String str) {
        byte[] b = str.getBytes();
        Base64 base64 = new Base64();
        b = base64.decode(b);
        String s = new String(b);
        return s;
    }
}
