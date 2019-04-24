package com.jsalpha.utils.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author dengjingsi
 * 方法参数注解
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface MyParam {
    /**
     * 修饰形参的别名的值
     * @return
     */
    String value();
}
