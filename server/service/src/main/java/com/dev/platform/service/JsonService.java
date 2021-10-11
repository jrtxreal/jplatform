package com.dev.platform.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/5/31 17:45
 */
public interface JsonService {
    String toJson(Object o) throws JsonProcessingException;
    <T> T fromJson(Class<T> tClass, String json) throws IOException;
}
