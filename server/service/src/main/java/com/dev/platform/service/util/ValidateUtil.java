package com.dev.platform.service.util;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import com.dev.platform.service.config.ExceptionCmpt;
import com.dev.platform.service.def.ErrorType;
import com.dev.platform.service.def.ValidateGroup;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Set;
/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/5/31 17:45
 */
public class ValidateUtil {
    /**
     * Hibernate 表单验证,此函数对validate(Object vo,int gt)做了一层
     * 封装，其中gt默认为-1,所以在设定groupTag的时候不要用负数，以免出现错
     * 误。
     *
     * @param vo 要验证的对象
     */
    public static void validate(Object vo) throws Exception {
        validate(vo, -1);
    }

    /**
     * Hibernate 表单验证
     *
     * @param vo 要验证的对象
     * @param gt 验证分组标签
     */
    public static void validate(Object vo, int gt) throws Exception {
        Class clazz = vo.getClass();
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Set<ConstraintViolation<Object>> constraintViolations = validator.validateProperty(vo, field.getName());
            ValidateGroup validateGroup = field.getAnnotation(ValidateGroup.class);
            if(validateGroup != null){
                int[] vgs = validateGroup.value();
                if (vgs.length > 0) {
                    boolean contain = false;
                    for (int item : vgs) {
                        if (item == gt) {
                            contain = true;
                            break;
                        }
                    }
                    if (!contain) {
                        continue;
                    }
                }
            }
            ErrorType errorType = field.getAnnotation(ErrorType.class);
            int et = ExceptionCmpt.BIZ;
            if(errorType != null){
                 et = errorType.value();
                 et = et==ExceptionCmpt.SUC?ExceptionCmpt.BIZ:et;
            }
            Iterator<ConstraintViolation<Object>> iterator = constraintViolations.iterator();
            while (iterator.hasNext()) {
                ConstraintViolation<Object> c = iterator.next();
                String msg = c.getMessage();
                throw new ExceptionCmpt(et, msg);
            }
        }
    }
}
