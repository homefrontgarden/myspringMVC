package com.jsalpha.utils.common;

/**
 * 数学工具类
 * @author dengjingsi
 */
public class MathUtil {
    /**
     * 返回a,b的最大公约数
     * @param a
     * @param b
     * @return
     */
    public static int highestCommonDivissor(int a, int b){
        int max,min;
        if(a==b){
            return a;
        }
        if(a == 1){
            return a;
        }
        if(b == 1){
            return b;
        }
        if(a>b){
            max=a;
            min=b;
        }else{
            max=b;
            min=a;
        }
        //循环出两个数最大的公因子
        for(int i=min;i>=2;i--){
            if((max%i==0)&&(min%i==0))
            {
                return i;
            }
        }
        return 1;
    }

    /**
     * 获取a，b的比值
     * @param a
     * @param b
     * @return a:b
     */
    public static int[] specificValue(int a, int b){
        if(a == 0 || b == 0){
            return new int [] {a, b};
        }
        int[] specific = new int [2];
        int divissor = highestCommonDivissor(a,b);
        specific [0] = a/divissor;
        specific [1] = b/divissor;
        return specific;
    }
    public static void main(String[] args){
        System.out.println(highestCommonDivissor(2,3));
    }
}

