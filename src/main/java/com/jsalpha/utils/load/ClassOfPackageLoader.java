package com.jsalpha.utils.load;

import com.jsalpha.utils.common.DirectoryFileFilter;
import com.jsalpha.utils.common.SuffixFilenameFilter;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * @author dengjingsi
 * 加载指定包下的类的工具类
 */
public class ClassOfPackageLoader {

    /**
     * 加载path目录下的packageName中所有类
     * @param path package所在的地址
     * @param packageName package的名字
     * @throws ClassNotFoundException
     */
    public void loadInnerClassOfpackage(String path,String packageName) throws ClassNotFoundException {
        //存储扫描到的类名变量
        LinkedList<String> classNames = new LinkedList<>();
        //获取包下的所有的类名
        collectClassOfPackageInner(path,packageName,classNames);
        //加载所有扫描到的类
        loadClass(classNames);
    }

    /**
     * 根据提供的包名，以及包所在的地址，扫描并存储package下的所有类的类名
     * @param path package的地址
     * @param packageName 包名
     * @param classes 供存储类名的变量
     * @throws ClassNotFoundException
     */
    public void collectClassOfPackageInner(String path,String packageName,LinkedList<String> classes){
        String packagePath = packageName.replace(".",File.separator);
        File f = new File(path+File.separator+packagePath);
        String[] fileNames = null;
        if(f.exists()){
            //获取该路径下所有的.class类文件
            fileNames = f.list(new SuffixFilenameFilter());
            for(String classFileName : fileNames){
                classFileName = classFileName.substring(0,classFileName.length()-6);
                classes.add(packageName+"."+classFileName);
            }
            //获取该路径下的所有目录
            File[] files = f.listFiles(new DirectoryFileFilter());
            for(File file : files){
                collectClassOfPackageInner(path,packageName+"."+file.getName(),classes);
            }
        }
    }

    /**
     * 根据classNames中类名，加载所有类
     * @param classNames 需要加载的类名集合
     * @throws ClassNotFoundException
     */
    public void loadClass(List<String> classNames) throws ClassNotFoundException {
        for(String className : classNames){
            loadClass(className);
        }
    }

    /**
     * 加载类名为className的类
     * @param className 需要加载的类名
     * @return
     * @throws ClassNotFoundException
     */
    public Class<?> loadClass(String className) throws ClassNotFoundException {
        return this.getClass().getClassLoader().loadClass(className);
    }


    public static void main(String[] args) throws ClassNotFoundException{
        /**
         * 测试
         * 注意：如果你的测试结果报ClassNotFoundException异常，请检查你的classPath参数对应的路径下有没有指定的包
         */
        ClassOfPackageLoader myClassLoader = new ClassOfPackageLoader();
        //下边是我测试的包所在绝对地址和包名
        myClassLoader.loadInnerClassOfpackage("/Users/iyunxiao/IdeaProjects/utils/target/classes","com.jsalpha.utils");
    }
}
