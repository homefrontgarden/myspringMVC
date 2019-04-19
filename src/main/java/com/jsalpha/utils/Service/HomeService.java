package com.jsalpha.utils.Service;

import com.jsalpha.utils.servlet.annotation.MyService;

@MyService("homeService")
public class HomeService {
    public String say(){
        return "hello";
    }
}
