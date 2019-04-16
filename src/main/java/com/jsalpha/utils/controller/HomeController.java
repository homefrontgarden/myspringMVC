package com.jsalpha.utils.controller;

import com.jsalpha.utils.servlet.MyController;
import com.jsalpha.utils.servlet.MyRequestMapping;

/**
 * @author dengjingsi
 * 对外接口类
 */
@MyController
public class HomeController {
    @MyRequestMapping(value = "/hello")
    public String getSay(){
        return "helloworld";
    }
}
