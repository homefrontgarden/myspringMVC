package com.jsalpha.utils.servlet;

public @interface MyRequestMapping {
    String value();
    String method() default "GET";
}
