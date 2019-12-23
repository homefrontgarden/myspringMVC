package com.jsalpha.utils.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Map集合的相关操作工具集合
 * @author dengjingsi
 */
public class MapUtil {
    /**
     * 清理map集合中Key键为k的List<V>中的v的记录
     * @param map
     * @param k
     * @param v
     * @param <K>
     * @param <V>
     */
    public static <K,V> void removeValue(Map<K,List<V>> map, K k, V v){
        map.computeIfPresent(k,(key,value)->{
            value.remove(v);
            return value;
        });
    }
    /**
     * 添加map集合中Key键为k的List<V>中的v的记录
     * @param map
     * @param k
     * @param v
     * @param <K>
     * @param <V>
     */
    public static <K,V> void addValue(Map<K,List<V>> map, K k, V v){
        map.compute(k,(key,value) ->{
            if(null == value){
                value = new ArrayList<>(16);
            }
            value.add(v);
            return value;
        });
    }

    /**
     * 返回map集合中value值最大的key
     * @param map
     * @param <T>
     * @return
     */
    public static <T> T getMaxKey(Map<T,Integer> map){
        if(null == map){
            return null;
        }
        int max = Integer.MIN_VALUE;
        int temp ;
        T tempT = null;
        for(Map.Entry<T,Integer> entry : map.entrySet()){
            temp = entry.getValue();
            if(temp > max){
                max = temp;
                tempT = entry.getKey();
            }
        }
        return tempT;
    }
}

