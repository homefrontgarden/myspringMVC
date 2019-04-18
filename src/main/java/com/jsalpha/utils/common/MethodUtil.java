package com.jsalpha.utils.common;

import com.jsalpha.utils.servlet.annotation.MyParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 操作反射方法工具类
 */
public class MethodUtil {
    /**
     * 获取Method的形参名字
     * @param method
     * @return
     */
    public static String[] getParamNameMethod(Method method){
        Annotation[][] annotations = method.getParameterAnnotations();
        if(null != annotations && annotations.length>0){
            String[] paramNames = new String[annotations.length];
            int index = 0;
            for(Annotation[] annotationArray : annotations){
                for(Annotation annotation : annotationArray){
                    if(annotation instanceof MyParam){
                        paramNames[index] = ((MyParam)annotation).value();
                    }
                }
                index++;
            }
            return paramNames;
        }
        return new String[0];
    }
    public static Object[] getParamMethod(HttpServletRequest request, HttpServletResponse response, Method method){
        Class<?>[] classes = method.getParameterTypes();
        String[] paramNames = getParamNameMethod(method);
        int index = 0;
        if(classes.length>0){
            if(classes.length == paramNames.length){
                Object[] params = new Object[classes.length];
                for(String paramName : paramNames){
                    if(null != paramName) {
                        params[index] = request.getParameter(paramName);
                    }else if(classes[index].getName().equals(HttpServletRequest.class.getName())){
                        params[index] = request;
                    }else if(classes[index].getName().equals(HttpServletResponse.class.getName())){
                        params[index] = response;
                    }
                    index ++;
                }
                return params;
            }else{
                Object[] params = new Object[classes.length];
                int paramIndex = 0;
                for(Class c : classes){
                    if(c.getName().equals(HttpServletRequest.class.getName())){
                        params[index++] = request;
                    }else if(c.getName().equals(HttpServletResponse.class.getName())){
                        params[index++] = response;
                    }else{
                        params[index++] = request.getParameter(paramNames[paramIndex++]);
                    }
                }
                return params;
            }
        }else {
            return new Object[0];
        }
    }
}
