package com.dev.platform.service.config.shiro;

import org.apache.shiro.cache.CacheException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import com.dev.platform.common.util.JsonUtil;
import com.dev.platform.data.dao.RedisDao;
import com.dev.platform.data.def.db.RedisDef;
import com.dev.platform.service.bo.SessionBo;

import java.util.*;
/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description :
 * session key 由两部分组成，格式，SESSION_A_B
 * SESSION：系统会话前缀标识
 * A:经过AES加密的用户名
 * B:UUID标识码
 * @date : 2021/5/31 17:45
 */
@Component
public class SessionCmpt{
    @Resource
    RedisDao redisDao;
    /**
     * 从redis中获取SessionBo
     * @param sessionKey
     * @return
     * @throws CacheException
     */
    public SessionBo getSession(String sessionKey){
        try{
            String json = redisDao.get(RedisDef.SESSION_DOMAIN + String.valueOf(sessionKey));
            return json==null?null: JsonUtil.fromJson(SessionBo.class,json);
        }catch (Exception e){
            return null;
        }

    }
    /**
     * 保存会话信息到redis中并设置超时时间
     * @param sessionKey token
     * @param sessionBo Session对象
     * @return
     * @throws CacheException
     */
    public Object saveSession(String sessionKey, SessionBo sessionBo) throws CacheException {

        return saveSession(sessionKey,sessionBo,RedisDef.SESSION_TIME_OUT);
    }

    /**
     * 保存会话信息到redis中并设置超时时间
     * @param sessionKey token
     * @param sessionBo Session对象
     * @param time 过期时长，单位秒
     * @return
     * @throws CacheException
     */
    public Boolean saveSession(String sessionKey, SessionBo sessionBo, long time) {
        try{
            redisDao.putWithTimeOut(RedisDef.SESSION_DOMAIN + sessionKey, String.valueOf(JsonUtil.toJson(sessionBo)),time);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * 从redis中删除用户会话信息
     * @param sessionKey
     * @return
     * @throws CacheException
     */
    public Object invalidate(String sessionKey) throws CacheException {
        return redisDao.delete(RedisDef.SESSION_DOMAIN + sessionKey);
    }

    /**
     *获取系统当前登录的用户集合
     * @return
     */
    public Set<String> getLoginUsers() {
        Set<String> keySet = redisDao.keys(RedisDef.SESSION_DOMAIN + "*");
        Set<String> userSegmentSet = new HashSet<>();
        keySet.forEach(item -> {
            String[] segments = item.split("_");
            userSegmentSet.add(segments[1]);
        });
        return userSegmentSet;
    }
}
