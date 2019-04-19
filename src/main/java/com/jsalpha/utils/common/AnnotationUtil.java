package com.jsalpha.utils.common;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 注解工具类
 * @author dengjingsi
 */
public class AnnotationUtil {
    /**
     * 获得annotation注解的value方法的值
     * @param annotation
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static String getAnnotationValue(Annotation annotation) throws InvocationTargetException, IllegalAccessException {
        return getAnnotationMethodValue(annotation,"value");
    }

    /**
     * 获得annotation注解的名为methodName方法的值
     * @param annotation
     * @param methodName
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static String getAnnotationMethodValue(Annotation annotation, String methodName) throws InvocationTargetException, IllegalAccessException {
        Class c = annotation.getClass();
        Method[] methods = c.getDeclaredMethods();
        for(Method method : methods){
            if(methodName.equals(method.getName())) {
                return (String) method.invoke(annotation, new Class[]{});
            }
        }
        return null;
    }
}
