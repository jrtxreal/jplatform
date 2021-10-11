package com.dev.platform.common.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.Date;

public class PwdUtil {
    public static String encryptPassword(String originPassword) {
        String salt = DigestUtils.md5Hex(new Date().getTime() + "").substring(8, 24);
        String encryptPassword = DigestUtils.md5Hex(originPassword + salt).substring(8, 24);
        encryptPassword = salt + encryptPassword;
        return encryptPassword;
    }
    public static boolean PwdEq(String originPwd, String encryptPwd) {
        try{
            String salt = encryptPwd.substring(0, 16);
            String myCryptographicPassword = salt + DigestUtils.md5Hex(originPwd + salt).substring(8, 24);
            if (myCryptographicPassword.equals(encryptPwd)) {
                return true;
            } else {
                return false;
            }
        }catch (Exception e){
            return false;
        }
    }
}

