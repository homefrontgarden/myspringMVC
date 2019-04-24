package com.jsalpha.utils.common;

import com.jsalpha.utils.annotation.MyController;
import com.jsalpha.utils.annotation.MyService;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author dengjingsi
 * Class工具类
 */
public class ClassUtil {
    public static Object filterClass(String className,Annotation annotation) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class c = getClass(className);
        if(null != c){
            if(null != c.getAnnotation(annotation.getClass())){
                return c.newInstance();
            }
        }
        return null;
    }

    /**
     * 根据className类名，向beanMap中以注解的value为key添加类的实例
     * @param beanMap
     * @param className
     * @throws ClassNotFoundException
     */
    public static void addBean(Map<String,Object> beanMap, String className, Class<?> annotationClass) throws ClassNotFoundException {
        Class c = getClass(className);
        Annotation a =c.getAnnotation(annotationClass);
        if(null != a){
            String key = null;
            try {
                key = AnnotationUtil.getAnnotationValue(a);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if(null != key){
                try {
                    beanMap.put(key,c.newInstance());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void addServiceBean(Map<String,Object> beanMap, String className) throws ClassNotFoundException {
        addBean(beanMap,className,MyService.class);
    }
    public static void addControllerBean(Map<String,Object> beanMap, String className) throws ClassNotFoundException {
        addBean(beanMap,className,MyController.class);
//        Class c = getClass(className);
//        Annotation a =c.getAnnotation(MyController.class);
//        if(null != a){
//            String key = null;
//            try {
//                key = AnnotationUtil.getAnnotationValue(a);
//            } catch (InvocationTargetException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//            if(null != key){
//                try {
//                    beanMap.put(key,c.newInstance());
//                } catch (InstantiationException e) {
//                    e.printStackTrace();
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }
    public static String getAliasClass(Class c){
        return "";
    }
    /**
     * 通过class名，获取当前活动线程的JVM可加载到的Class
     * @param className
     * @return
     * @throws ClassNotFoundException
     */
    public static Class getClass(String className) throws ClassNotFoundException {
        return Thread.currentThread().getContextClassLoader().loadClass(className);
    }
    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        Annotation a = ClassUtil.class.getAnnotation(MyController.class);
        Class c = MyController.class;
        Method[] m = c.getDeclaredMethods();
        Field[] f = c.getDeclaredFields();
        System.out.println(a);
    }
}
