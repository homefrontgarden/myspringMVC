package com.jsalpha.demo.controller;

import com.jsalpha.demo.service.HomeService;
import com.jsalpha.utils.annotation.MyController;
import com.jsalpha.utils.annotation.MyParam;
import com.jsalpha.utils.annotation.MyQualifier;
import com.jsalpha.utils.annotation.MyRequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author dengjingsi
 * 对外接口类
 */
@MyController("homeController")
@MyRequestMapping("/home")
public class HomeController {
    @MyQualifier("homeService")
    private HomeService homeService;

    /**
     *
     * @param httpServletRequest
     * @param name
     * @param response
     * @return
     */
    @MyRequestMapping(value = "/hello")
    public String getSay(HttpServletRequest httpServletRequest, @MyParam("name") String name, HttpServletResponse response){
        return homeService.say(name);
    }
}
