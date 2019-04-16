package com.jsalpha.utils.common;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RequestUtil {
    public static String getRequestBody(HttpServletRequest request){
        StringBuilder dataString = new StringBuilder();
        String line;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
            while(null != (line = bufferedReader.readLine())){
                dataString.append(line);
            }
            System.out.println(dataString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataString.toString();
    }
}
