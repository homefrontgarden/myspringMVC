package com.jsalpha.utils.servlet;

import com.jsalpha.utils.utils.ClassOfPackageLoader;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 在DispatcherServlet类中，实现下面几个步骤：
 *
 *     加载配置类
 *     扫描当前项目下的所有文件
 *     拿到扫描到的类，通过反射机制，实例化。并且放到ioc容器中(Map的键值对) beans
 *     初始化path与方法的映射
 * @author dengjingsi
 */
public class DispatcherServlet extends HttpServlet {
    private String path ="E:\\IdeaProjects\\hand_mvc\\target\\classes";
    /**
     * 扫描到的所有类名
     */
    private LinkedList<String> classNames;
    /**
     * 类名对应的对象
     */
    private Map<String,Object> beans = new HashMap<>();
    private Map<String,Method> handlerMap = new HashMap<>();
    Map<String,Object> pathMethod;
    @Override
    public void init(ServletConfig config) throws ServletException {
        System.out.println("init()............");

        // 1.扫描需要的实例化的类
        try {
            doScanPackage("com.jsalpha.utils.controller");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("当前文件下所有的class类.......");
        for(String name: classNames) {
            System.out.println(name);
        }

//        // 2.实例化
        try {
            doInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("当前实例化的对象信息.........");
        for(Map.Entry<String, Object> map: beans.entrySet()) {
            System.out.println("key:" + map.getKey() + "; value:" + map.getValue());
        }

        // 3.将IOC容器中的service对象设置给controller层定义的field上
//        doIoc();

        // 4.建立path与method的映射关系
        handlerMapping();
        System.out.println("Controller层的path和方法映射.........");
//        for(Map.Entry<String, Object> map: handlerMap.entrySet()) {
//            System.out.println("key:" + map.getKey() + "; value:" + map.getValue());
//        }
        for(Map.Entry<String, Method> map: handlerMap.entrySet()) {
            System.out.println("key:" + map.getKey() + "; value:" + map.getValue());
        }
    }
    /**
     * 实例化扫描到的类
     */
    public void doInstance() throws ClassNotFoundException {
        for(String className : classNames){
            beans.put(className,Class.forName(className));
        }
    }
    public void doScanPackage(String packageName) throws ClassNotFoundException {
        ClassOfPackageLoader classOfPackageLoader = new ClassOfPackageLoader();
        classNames = new LinkedList<>();
        classOfPackageLoader.collectClassOfPackageInner(path,packageName,classNames);
    }
    public void handlerMapping(){
        Method[] methods = null;
        Object leader;
        Method method;
        List<Object> params;
        for(Map.Entry<String,Object> entry : beans.entrySet()){
            leader = entry.getValue();
            methods = leader.getClass().getMethods();
        }
        List<Method> methodList = filterReuqestMethod(methods);
        setPathMethod(methodList);
    }
    public void setPathMethod(List<Method> methods){
        MyRequestMapping annotation;
        String url;
        for(Method method : methods){
            annotation = method.getDeclaredAnnotation(MyRequestMapping.class);
            url = annotation.value();
            pathMethod.put(url,method);
        }
    }

    public List<Method> filterReuqestMethod(Method[] methods){
        List<Method> methodList = new ArrayList<>();
        Annotation annotation;
        for(Method method : methods){
            annotation = method.getDeclaredAnnotation(MyRequestMapping.class);
            if(null != annotation){
                methodList.add(method);
            }
        }
        return methodList;
    }
    /**
     * 每一次请求将会调用doGet或doPost方法，它会根据url请求去HandlerMapping中匹配到对应的Method，然后利用反射机制调用Controller中的url对应的方法，并得到结果返回。按顺序包括以下功能：
     *
     *     获取请求传入的参数并处理参数
     *     通过初始化好的handlerMapping中拿出url对应的方法名，反射调用
     * ---------------------
     * 作者：伍婷
     * 来源：CSDN
     * 原文：https://blog.csdn.net/chyanwu68/article/details/81096910
     * 版权声明：本文为博主原创文章，转载请附上博文链接！
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        System.out.println("doGet()............");
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        System.out.println("doPost()............");

        // 通过req获取请求的uri /maven_handmvc/custom/query
        String uri = req.getRequestURI();

        // /maven_handmvc
        String context = req.getContextPath();
        String path = uri.replaceAll(context, "");

        // 通过当前的path获取handlerMap的方法名
        Method method = (Method) handlerMap.get(path);
//        // 获取beans容器中的bean
//        MyController instance = (MyController) beans.get("/" + path.split("/")[1]);
//
//        // 处理参数
//        HandlerAdapterService ha = (HandlerAdapterService) beans.get("customHandlerAdapter");
//        Object[] args = ha.handle(req, resp, method, beans);
//
//        // 通过反射来实现方法的调用
//        try {
//            method.invoke(instance, args);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
    }
//    private void doScanPackage(String packageName){
//        String
//        File packageFile = new File(packageName);
//        packageFile.
//    }
}
