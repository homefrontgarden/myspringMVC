package com.jsalpha.utils.common;

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
    /**
     * 根据mun获取一个不小于1的数，如果mun >1 ，返回一个等于mun的数，反之返回Integer.MAX_VALUE
     * @param mun
     * @return
     */
    public static int generateMaxLimit(int mun){
        return mun < 1 ? Integer.MAX_VALUE : mun;
    }
    /**
     * 判断longParam与stringParam的字面类型值是否相等
     * @param longParam
     * @param stringParam
     * @return
     */
    public static boolean paramEquals(long longParam,String stringParam){
        return String.valueOf(longParam).equals(stringParam);
    }

    /**
     * 计算a,b的商，商采取进一法取值
     * @param a 被除数
     * @param b 除数
     * @return 返回a与b的商
     */
    public static int quotientCeil(int a,int b){
        return a%b > 0 ? a/b +1 : a/b;
    }
    public static void main(String[] args){
        System.out.println(highestCommonDivissor(2,3));
    }
}
