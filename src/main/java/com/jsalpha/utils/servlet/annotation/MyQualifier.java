package com.jsalpha.utils.servlet.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 依赖注入依赖次注解的运用
 * @author dengjingsi
 */
@Retention(value = RetentionPolicy.RUNTIME)
public @interface MyQualifier {
    /**
     * 通过此value的值，注入相对应的对象
     * @return
     */
    String value();
}
