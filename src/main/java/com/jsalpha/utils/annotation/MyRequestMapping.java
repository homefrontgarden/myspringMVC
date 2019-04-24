package com.jsalpha.utils.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 修饰controller类以及方法，通过此注解提供请求地址
 * @author dengjingsi
 */
@Retention(value = RetentionPolicy.RUNTIME)
public @interface MyRequestMapping {
    String value();
    String method() default "GET";
}
