package com.jsalpha.utils.servlet.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value = RetentionPolicy.RUNTIME)
public @interface MyRequestMapping {
    String value();
    String method() default "GET";
}
