package com.jsalpha.utils.servlet.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 请求参数注解
 */
@Retention(value = RetentionPolicy.RUNTIME)
public @interface MyParam {
    public String value();
}
