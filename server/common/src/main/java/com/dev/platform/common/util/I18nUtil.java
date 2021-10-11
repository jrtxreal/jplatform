package com.dev.platform.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class I18nUtil {
    @Autowired
    private static MessageSource messageSource;
    public I18nUtil(MessageSource messageSource) {
        I18nUtil.messageSource = messageSource;
    }

    public static String get(String msgKey) {
        try {
            return messageSource.getMessage(msgKey, null, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            return msgKey;
        }
    }
    public static void setLocale(String lang) {
        try{
            String[] strAr = lang.split("_");
            Locale locale = new Locale(strAr[0],strAr[1]);
            LocaleContextHolder.setLocale(locale);
        }catch (Exception e){}
    }

    public static String getLocale(){
        Locale locale = LocaleContextHolder.getLocale();
        return locale.toString();
    }
}
