package com.dev.platform.service.def;
/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/5/31 17:45
 */
public @interface ErrorType {
    /**
     * 校验结果的错误类型定义，只能指定ExceptionBo中的错误类型；
     * @return
     */
    int value();
}
