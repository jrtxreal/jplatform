package com.dev.platform.service.def;

import java.lang.annotation.*;
/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/5/31 17:45
 */
@Target({ElementType.METHOD,
        ElementType.FIELD,
        ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR,
        ElementType.PARAMETER,
        ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidateGroup {


    /**
     * 根据指定的分组号校验，没有此标签时默认进行校验，有此标签时，
     * 校验需要传递分组值，当分组值包含在标签数组内时才进行校验。
     * @return
     */
    int[] value();
}
