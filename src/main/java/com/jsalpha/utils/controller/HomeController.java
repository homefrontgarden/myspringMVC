package com.jsalpha.utils.controller;

import com.jsalpha.utils.Service.HomeService;
import com.jsalpha.utils.servlet.annotation.MyController;
import com.jsalpha.utils.servlet.annotation.MyParam;
import com.jsalpha.utils.servlet.annotation.MyQualifier;
import com.jsalpha.utils.servlet.annotation.MyRequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author dengjingsi
 * 对外接口类
 */
@MyController("homeController")
public class HomeController {
    @MyQualifier("homeService")
    private HomeService homeService;
    @MyRequestMapping(value = "/hello")
    public String getSay(HttpServletRequest httpServletRequest, @MyParam("s") String s, HttpServletResponse response,@MyParam("1") String deng){
        return homeService.say();
    }
}
