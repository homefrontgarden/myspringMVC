package com.jsalpha.utils.servlet.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author dengjingsi
 * controller用的注解
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MyController {
    /**
     * 提供注释controller类的别名值
     * @return 返回注解类的别名
     */
    String value();
}
