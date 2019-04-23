package com.jsalpha.utils.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 把集合能够以其属性以键值对（k->v）的形式存储的工具类
 * @author dengjingsi
 */
public class BeanMapUtil {
    /**
     * 把集合能够以其属性以键值对（k->v）
     * @param list
     * @param propertyKey 最为K的获取属性的方法名
     * @param propertyValue 最为V的获取属性的方法名
     * @param <T> 类元素
     * @param <K> 作为key的类的属性类型
     * @param <V> 作为value的类的属性类型
     * @return
     */
    public static <T,K,V> Map<K,V> formatProperty(List<T> list, String propertyKey, String propertyValue){
        if(null == list || list.size() == 0){
            return new HashMap<>(0);
        }
        Map<K,V> map = new HashMap<>(list.size());
        try{
            for(T element : list){
                Method methodKey = element.getClass().getMethod(propertyKey,new Class[]{});
                Method methodValue = element.getClass().getMethod(propertyValue,new Class[]{});
                map.put((K)methodKey.invoke(element,new Object[]{}),(V)methodValue.invoke(element));
            }
        }catch(NoSuchMethodException n){
            n.printStackTrace();
        }catch(InvocationTargetException it){
            it.printStackTrace();
        }catch(IllegalAccessException i){
            i.printStackTrace();
        }
        return map;
    }

    /**
     * 把集合能够以其属性以键值对（k->v）默认K为getId
     * @param list
     * @param propertyValue
     * @param <T> 类元素
     * @param <K> 作为key的类的属性类型
     * @param <V> 作为value的类的属性类型
     * @return
     */
    public static <T,K,V> Map<K,V> formatProperty(List<T> list,String propertyValue){
        return formatProperty(list,"getId",propertyValue);
    }
}
