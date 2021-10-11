package com.dev.platform.data.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.dev.platform.data.dao.RedisDao;
/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/5/31 17:45
 */
@Repository
public class RedisDaoImpl implements RedisDao {
    @Autowired
    private StringRedisTemplate redisTemplate;
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }
    public void put(String key,String val) {
         redisTemplate.opsForValue().set(key,val);
    }
    public void putWithTimeOut(String key, String val,long timeOut){
        redisTemplate.opsForValue().set(key, val, timeOut, TimeUnit.SECONDS);
    }
    public boolean delete(String key){
        return redisTemplate.delete(key);
    }

    public long multiDelete(String pattern){
        return redisTemplate.delete(keys(pattern));
    }
    @Override
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }
}
