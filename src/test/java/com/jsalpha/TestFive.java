package com.jsalpha;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TestFive {
  public static void main(String [] args) {
    Integer[] intArrays = {1,2,2,5,6,7,8,10};
    Integer[] paramArrays = {8,7,6};
    List<Integer> list = new ArrayList<>(intArrays.length);
    List<Integer> param = new ArrayList<>(paramArrays.length);
    Collections.addAll(list, intArrays);
    Collections.addAll(param, paramArrays);
    Collections.sort(list,new OrderComparator());
    List<List<Integer>> results = new ArrayList<>();
    searchSingle(8,0,list,results,new ArrayList<>());
    for(List<Integer> result : results){
      System.out.println(result);
    }
  }
  public static void run(){
    Integer[] intArrays = {1,1,2,5,6,7,8,10};
  }
  /**
   * 搜索list集合中的元素，有多少组合，元素和能组成result
   * @param result
   * @param index 从第index个元素开始
   * @param list 数据源
   * @param results 组合种类
   * @param resultList 当前尝试的元素集合
   */
  public static void searchSingle(int result, int index, List<Integer> list, List<List<Integer>> results, List<Integer> resultList){
    //判断是否已经搜索到最后，如果搜索到最后，停止搜索，反之继续
    if(index + 1 > list.size()){
      return;
    }
    //当前选取元素等于目标结果判断:
    // 如果flag等于0，说明组合结果等于目标值；
    // 如果flag等于-1，说明组合结果小于目标值；
    // 如果flag等于1，说明组合结果大于目标值。
    int flag = sum(resultList,list.get(index),result);
    if(0 == flag){
      //找到能组成目标可能性的结果后，判断是否搜索到了最后，如果没有搜索到最后，继续搜索其他可能性 begin
      if(index < list.size()){
        List<Integer> copyList = new ArrayList<>(resultList);
        searchSingle(result, index + 1, list,results,copyList);
      }
      //找到能组成目标可能性的结果后，判断是否搜索到了最后，如果没有搜索到最后，继续搜索其他可能性 end
      //保存结果 begin
      resultList.add(list.get(index));
      results.add(resultList);
      //保存结果 end
    }else if (-1 == flag){
      //尝试剩余元素能组成结果的可能性 begin
      List<Integer> copyList = new ArrayList<>(resultList);
      searchSingle(result, index + 1, list, results, copyList);
      //尝试剩余元素能组成结果的可能性 end

      //尝试当前可行性状态 begin
      resultList.add(list.get(index));
      searchSingle(result, index + 1, list, results, resultList);
      //尝试当前可行性状态 end
    }else{
      searchSingle(result, index + 1, list, results, resultList);
    }
  }
  /**
   * 判断list中的元素和加上element的和与result的关系，
   * 1.如果等于result返回0，
   * 2.如果大于result返回1，
   * 3.如果小于result返回-1。
   * @param list
   * @param element
   * @param result
   * @return
   */
  public static int sum(List<Integer> list, int element, int result){
    int sum = element;
    for(int temp : list){
      sum += temp;
    }
    if(sum == result){
      return 0;
    }else{
      return sum > result ? 1 : -1;
    }
  }
}
class OrderComparator implements Comparator<Integer> {

  @Override
  public int compare(Integer o1, Integer o2) {
    return o1 < o2 ? 1 : -1;
  }
}
