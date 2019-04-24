package com.jsalpha.demo.service;

import com.jsalpha.utils.annotation.MyService;

/**
 * @author dengjingsi
 * 业务层类
 */
@MyService("homeService")
public class HomeService {
    /**
     * 返回对name打招呼的话
     * @param name
     * @return
     */
    public String say(String name){
        return "hello,"+name;
    }
}
