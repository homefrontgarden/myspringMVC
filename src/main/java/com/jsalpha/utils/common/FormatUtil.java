package com.jsalpha.utils.common;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

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
            Method method;
            for(V element : list){
                method = element.getClass().getMethod(property,new Class[]{});
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
     * 把list格式的对象以property方法名获取到的属性为主键，转化为map格式
     * @param list
     * @param property
     * @param <K> K为需要作为主键的类型map的key类型（T中的一个属性类型）
     * @param <V> V为map的value值
     * @return
     */
    public static <K,V,E> Map<K,E> formatPropertyList(List<V> list, String property, String valueName){
        if(null == list || list.size() == 0){
            return new HashMap<>(0);
        }
        Map<K,E> map = new HashMap<>(list.size());
        try{
            Method key;
            Method value;
            for(V element : list){
                key = element.getClass().getMethod(property,new Class[]{});
                value = element.getClass().getMethod(valueName,new Class[]{});
                map.put((K)key.invoke(element,new Object[]{}),(E)value.invoke(element,new Object[]{}));
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
     * 把list格式的对象以property方法名获取到的属性为主键，归类list
     * @param list
     * @param property
     * @param <K> K为需要作为主键的类型map的key类型（T中的一个属性类型）
     * @param <V> V为map的value元素值
     * @return
     */
    public static <K,V> Map<K,List<V>> formatGroupingList(List<V> list, String property){
        if(null == list || list.size() == 0){
            return new HashMap<>(0);
        }
        Map<K,List<V>> map = new HashMap<>(list.size());
        try{
            Method method;
            List<V> values;
            for(V element : list){
                method = element.getClass().getMethod(property,new Class[]{});
                K key = (K)method.invoke(element,new Object[]{});
                values = map.computeIfAbsent(key,k -> new ArrayList<>());
                values.add(element);
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
     * 把list格式的对象以property方法名获取到的属性为主键，归类list的数量
     * @param list
     * @param property
     * @param <K> K为需要作为主键的类型map的key类型（T中的一个属性类型）
     * @param <V> V为map的value元素值
     * @return
     */
    public static <K,V> Map<K,Integer> formatGroupingCount(List<V> list, String property){
        if(null == list || list.size() == 0){
            return new HashMap<>(0);
        }
        Map<K,Integer> map = new HashMap<>();
        try{
            Method method;
            for(V element : list){
                method = element.getClass().getMethod(property,new Class[]{});
                K key = (K)method.invoke(element,new Object[]{});
                map.compute(key,(k,v)-> v == null ? 1 : v + 1 );
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
     * 从list集合中获取V元素以K为键的E的集合
     * @param list
     * @param keyName
     * @param eName
     * @param <K>
     * @param <V>
     * @param <E>
     * @return
     */
    public static <K,V,E> Map<K,List<E>> formatGroupingList(List<V> list,String keyName,String eName){
        if(null == list || list.size() == 0){
            return new HashMap<>(0);
        }
        Map<K,List<E>> map = new HashMap<>(list.size());
        try{
            Method method;
            List<E> values;
            for(V element : list){
                method = element.getClass().getMethod(keyName,new Class[]{});
                K key = (K)method.invoke(element,new Object[]{});
                values = map.computeIfAbsent(key,k -> new ArrayList<>());
                method = element.getClass().getMethod(eName,new Class[]{});
                E e = (E)method.invoke(element,new Object[]{});
                values.add(e);
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
     * 从list集合中获取V元素以K为键的E的集合
     * @param list
     * @param keyName
     * @param eName
     * @param <K>
     * @param <V>
     * @param <E>
     * @return
     */
    public static <K,V,E> Map<K,Set<E>> formatGroupingSet(List<V> list, String keyName, String eName){
        if(null == list || list.size() == 0){
            return new HashMap<>(0);
        }
        Map<K,Set<E>> map = new HashMap<>(list.size());
        try{
            Method method;
            Set<E> values;
            for(V element : list){
                method = element.getClass().getMethod(keyName,new Class[]{});
                K key = (K)method.invoke(element,new Object[]{});
                values = map.computeIfAbsent(key, k -> new HashSet<> ());
                method = element.getClass().getMethod(eName,new Class[]{});
                E e = (E)method.invoke(element,new Object[]{});
                values.add(e);
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
}
