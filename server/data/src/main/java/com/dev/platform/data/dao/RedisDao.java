package com.dev.platform.data.dao;
import java.util.Set;
/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/5/31 17:45
 */
public interface RedisDao {
    String get(String key);
    void put(String key,String val);
    void putWithTimeOut(String key, String val,long timeOut);
    boolean delete(String key);
    long multiDelete(String pattern);
    Set<String> keys(String pattern);
}
