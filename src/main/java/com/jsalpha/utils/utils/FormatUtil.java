package com.jsalpha.utils.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dengjingsi
 * 格式化工具类
 * K为需要作为主键的类型map的key类型（T中的一个属性类型），V为map的value值
 */
public class FormatUtil {

    /**
     * 把list格式的对象以property方法名获取到的属性为主键，转化为map格式
     * @param list
     * @param property
     * @param <K> K为需要作为主键的类型map的key类型（T中的一个属性类型）
     * @param <V> V为map的value值
     * @return
     */
    public static <K,V> Map<K,V> formatList(List<V> list, String property){
        if(null == list || list.size() == 0){
            return new HashMap<>(0);
        }
        Map<K,V> map = new HashMap<>(list.size());
        try{
            for(V element : list){
                Method method = element.getClass().getMethod(property,new Class[]{});
                map.put((K)method.invoke(element,new Object[]{}),element);
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
     * 把list格式的对象以Id属性为主键，转化为map格式
     * @param list
     * @return
     */
    public static <K,V> Map<K,V> formatList(List<V> list){
        return formatList(list, "getId");
    }

    /**
     * 把list集合以R对象的getProperty方法名获取到的属性为key、R为value，生成map集合
     * @param list R的list集合
     * @param getProperty 获取key属性方法名
     * @param <K> key属性的类型
     * @param <V> 元素
     * @return R以getProperty方法获取到的值为key的map集合
     */
    public static <K,V> Map<K,V> get(List<V> list,String getProperty){
        Map<K,V> map = new HashMap<>(list.size());
        for(V element : list){
            Method method = null;
            try {
                method = element.getClass().getMethod(getProperty,new Class[]{});
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            try {
                map.put((K)method.invoke(element,new Object[]{}),element);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}

