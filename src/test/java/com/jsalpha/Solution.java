package com.jsalpha;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Solution {
    /**
     * @param a: An integer
     * @param b: An integer
     * @return: The sum of a and b
     */
    public int aplusb(int a, int b) {
        // write your code here
        int a_ = a^b;
        int b_ = (a&b)<<1;
        while(b != 0){
            a_ = a^b;
            b_ = (a&b)<<1;
            a = a_;
            b = b_;
        }
        return a_;
    }
    public int singleNumber(int[] A) {
        // write your code here
        int max = 0;
        for(int a : A){
            if(a > max){
                max = a;
            }
        }
        Integer[] array = new Integer[max+1];
        for(int a : A){
            if(null != array[a]){
                array[a] = null;
            }else{
                array[a] = a;
            }
        }
        for(Integer a : array){
            if(a != null){
                return a;
            }
        }
        return A[0];
    }
    public int singleNumber2(int[] A){
        quickStart(0,A.length-1,A);
        Stack<Integer> stack = new Stack();
        for(int a : A){
            if(stack.empty()) {
                stack.push(a);
            }else{
                if(stack.peek() == a){
                    stack.pop();
                }else{
                    stack.push(a);
                }
            }
        }
        return stack.peek();
    }
    public void quickStart(int frist,int last,int[] A){
        if(frist>=last){
            return;
        }
        int key = A[frist];
        int i = frist,j = last;
        while(frist < last){
            while(frist < last&&A[last] >= key){
                last--;
            }
            if(A[last] <= key&&frist < last){
                A[frist] = A[last];
                frist ++;
            }
            while(frist < last&&A[frist] <= key){
                frist ++;
            }
            if(A[frist] > key&&frist < last){
                A[last] = A[frist];
                last--;
            }
        }
        A[last] = key;
        quickStart(i,last - 1,A);
        quickStart(last +1,j,A);
    }
    public void rotateString(char[] str, int offset) {
        // write your code here
        int length = str.length;
        int realOffset = offset%length;
        int index = 0;
        char[] temp = new char[length];
        for(int size = length + realOffset; realOffset < size ; realOffset ++ ){
            temp [index++] = str[realOffset%length];
        }
        for(int i = 0; i < length; i++){
            str[i] = temp [i];
        }
    }
    public int[] twoSum(int[] numbers, int target) {
        // write your code here
        Stack<Integer> stack;
        int[] result;
        for(int index = 0; index < numbers.length; index++){
            stack = new Stack<Integer> ();
            run(numbers,index,stack,target);
            if(stack.size() == 2){
                result = new int[2];
                result[1] = stack.pop();
                result [0] = stack.pop();
                if(target == numbers[result[0]] + numbers[result[1]]){
                    return result;
                }
            }
        }
        return null;
    }
    public void run(int[] numbers,int index,Stack<Integer> stack,int target){
        if(!stack.isEmpty() && index < numbers.length){
            if(numbers [index] + numbers[stack.peek()] == target){
                stack.push(index);
                return;
            }
            run(numbers,index +1,stack,target);
        }else if(index < numbers.length){
            stack.push(index);
            run(numbers,index +1,stack,target);
        }



    }
//    public List<Integer> getNarcissisticNumbers(int n) {
//        // write your code here
//        int begin = power(10,n-1);
//        int end = power(10,n)-1;
//        List<Integer> results = new ArrayList<>();
//        while(begin<=end){
//            if(check(begin,n)){
//                results.add(begin);
//            }
//            begin ++;
//        }
//        return results;
//    }
//    public boolean check(int count,int n){
//        int remainder = count%10;
//        int currentCount = count/10;
//        int result = 0;
//        while(remainder>0||currentCount >0){
//            result += power(remainder,n);
//            remainder = currentCount%10;
//            currentCount = currentCount/10;
//        }
//        return result == count;
//    }
//    public int power(int base,int n){
//        int temp = 1;
//        for(int i = 0;i<n;i++){
//            temp *= base;
//        }
//        return temp;
//    }
public List<Integer> getNarcissisticNumbers(int n) {
    // write your code here
    int begin;
    if(n==1){
        begin = 0;
    }else{
        begin = power(10,n-1);
    }
    int end = power(10,n)-1;
    List<Integer> results = new ArrayList<>();
    while(begin<=end){
        if(check(begin,n)){
            results.add(begin);
        }
        begin ++;
    }
    return results;
}
    public boolean check(int count,int n){
        int remainder = count%10;
        int currentCount = count/10;
        int result = 0;
        while(remainder>0||currentCount >0){
            result += power(remainder,n);
            remainder = currentCount%10;
            currentCount = currentCount/10;
        }
        return result == count;
    }
    public int power(int base,int n){
         if(base == 0 || n == 0){
             return 0;
         }
        int temp = 1;
        for(int i = 0;i<n;i++){
            temp *= base;
        }
        return temp;
    }
    public static void main(String[] args){
        int a = 5,b = 7;
        char[] array = "abcdefg".toCharArray();
        new Solution().rotateString(array,3);
        int[] ints = { 7, 11, 15,2};
        List<Integer> results = new Solution().getNarcissisticNumbers(1);
        new Solution().check(0,1);
        System.out.println(results);
    }
}
