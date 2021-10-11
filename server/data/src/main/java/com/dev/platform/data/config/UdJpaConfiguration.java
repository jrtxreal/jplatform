package com.dev.platform.data.config;


import com.dev.platform.data.def.JpaDto;
import com.google.common.base.CaseFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;

import javax.annotation.PostConstruct;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.util.Map;
/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/5/31 17:45
 */
@Configuration
public class UdJpaConfiguration {
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 初始化注入@JpaDto对应的Converter
     */
    @PostConstruct
    public void init() {
        Map<String, Object> map = applicationContext.getBeansWithAnnotation(JpaDto.class);
        for (Object o : map.values()) {
            Class c = o.getClass();
            GenericConversionService genericConversionService = ((GenericConversionService) DefaultConversionService.getSharedInstance());
            genericConversionService.addConverter(Map.class, c, m -> {
                try {
                    Object obj = c.getDeclaredConstructor().newInstance();
                    return copyMapToObj(m, obj);
                } catch (Exception e) {
                    throw new FatalBeanException("Conversion error for " + c.getName(), e);
                }
            });
        }
    }


    // 将map中的值copy到bean中对应的字段上
    private Object copyMapToObj(Map<String, Object> map, Object target)
             throws InvocationTargetException, IllegalAccessException {
        if (map == null || target == null || map.isEmpty()) {
            return target;
        }
        Class<?> actualEditable = target.getClass();
        PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(actualEditable);
        for (PropertyDescriptor targetPd : targetPds) {
            if (targetPd.getWriteMethod() == null) {
                continue;
            }
                String key = targetPd.getName();
                Object value = map.get(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, key));
                if (value == null) {
                    continue;
                }
                Method writeMethod = targetPd.getWriteMethod();
                if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                    writeMethod.setAccessible(true);
                }

                if (value instanceof BigInteger) {// 这里手动将BigInteger类型转换成Long 类型
                    writeMethod.invoke(target, Long.valueOf(String.valueOf(value)));
                } else {
                    writeMethod.invoke(target, value);
                }
        }
        return target;
    }
}
