package com.jsalpha.utils.load;

import java.io.File;
import java.util.List;

public class MyClassLoader {
    public void loadInnerClassOfpackage(String path,String packageName){
        //1.获取包下的所有的类名
        //2.加载所有扫描到的类
    }
//    public List<String> getInnerClassNameOfPackage(String path,String packageName){
//        File f = new File(path+File.separator+packageName);
//        String[] fileNames;
//        if(f.exists()){
//            fileNames = f.list();
//        }
//        return fileNames;
//    }
}
