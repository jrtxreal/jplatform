package com.dev.platform.common.util;

import static java.util.regex.Pattern.compile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

/**
 * 通用验证类<br>
 * isNullOrEmpty(Object value),判断元素是否为空
 *
 * @author liujj</ a>
 * @version 1.0
 * @since 1.0
 */
public final class ValidUtils {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(ValidUtils.class);

    /**
     * Don't let anyone instantiate this class.
     */
    private ValidUtils() {
    }

    /**
     * 判断元素是否为Null或者Empty
     * <p>
     * 目前可以判断一下元素
     * <ul>
     * <li>Collection,使用其isEmpty()</li>
     * <li>Map,使用其isEmpty()</li>
     * <li>Object[],判断length==0</li>
     * <li>String,使用.trim().length()效率高</li>
     * <li>Enumeration,使用hasMoreElements()</li>
     * <li>Iterator,使用hasNext()</li>
     * </ul>
     *
     * @param value 可以是Collection,Map,Object[],String,Enumeration,Iterator类型
     * @return 空返回true
     * @since 1.0
     */
    @SuppressWarnings("rawtypes")
    public static final boolean isNullOrEmpty(Object value) {
        if (null == value) {
            return true;
        }
        boolean flag = false;
        // 字符串
        if (value instanceof String) {
            // 比较字符串长度, 效率高
            flag = value.toString().trim().length() <= 0;
        }
        // Object[]object数组
        else if (value instanceof Object[]) {
            flag = ((Object[]) value).length == 0;
        }
        // 集合
        else if (value instanceof Collection) {
            flag = ((Collection) value).isEmpty();
        }
        // map
        else if (value instanceof Map) {
            flag = ((Map) value).isEmpty();
        }
        // 枚举
        else if (value instanceof Enumeration) {
            flag = !((Enumeration) value).hasMoreElements();
        }
        // Iterator迭代器
        else if (value instanceof Iterator) {
            flag = !((Iterator) value).hasNext();
        }
        return flag;
    }

    /**
     * 判断元素是否不为Null或者Empty,调用<code>!isNullOrEmpty</code>方法
     * <p>
     * 目前可以判断一下元素
     * <ul>
     * <li>Collection,使用其isEmpty()</li>
     * <li>Map,使用其isEmpty()</li>
     * <li>Object[],判断length==0</li>
     * <li>String,使用.trim().length()效率高</li>
     * <li>Enumeration,使用hasMoreElements()</li>
     * <li>Iterator,使用hasNext()</li>
     * </ul>
     *
     * @param value 可以是Collection,Map,Object[],String,Enumeration,Iterator类型
     * @return 不为空返回true
     * @since 1.0
     */
    public static final boolean isNotNullOrEmpty(Object value) {
        return !isNullOrEmpty(value);
    }

    public static final boolean isNullOrEmpty(Object... objs) {
        if (objs == null) {
            return true;
        } else {
            for (Object obj : objs) {
                if (isNullOrEmpty(obj)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static final boolean isNotNullOrEmpty(Object... objs) {
        if (objs == null) {
            return false;
        } else {
            for (Object obj : objs) {
                if (isNullOrEmpty(obj)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 判断邮箱
     *
     * @param s
     * @return
     */
    public static boolean isEmail(String s) {
        if (isNullOrEmpty(s)) {
            return Boolean.FALSE;
        }
        return compile("\\w+@{1}\\w+\\.{1}\\w+").matcher(s).matches();
    }


    public static boolean isOverseaEmail(String s) {
        if (isNullOrEmpty(s)) {
            return Boolean.FALSE;
        }
        return compile("^[A-Z a-z 0-9_.-]+@{1}[A-Z a-z 0-9_.-]+\\.{1}[A-Z a-z 0-9_.-]+").matcher(s).matches();
    }


}
