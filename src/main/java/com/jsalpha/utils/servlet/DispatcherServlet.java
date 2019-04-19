package com.jsalpha.utils.servlet;

import com.jsalpha.utils.common.ClassUtil;
import com.jsalpha.utils.common.MethodUtil;
import com.jsalpha.utils.servlet.annotation.MyController;
import com.jsalpha.utils.servlet.annotation.MyParam;
import com.jsalpha.utils.servlet.annotation.MyQualifier;
import com.jsalpha.utils.servlet.annotation.MyRequestMapping;
import com.jsalpha.utils.utils.ClassOfPackageLoader;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
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
    /**
     * 扫描到的所有类名
     */
    private LinkedList<String> classNames;
    private List<Class<?>> classes;
    /**
     * 类名对应的对象
     */
    private Map<String,Object> beans = new HashMap<>();
    private Map<String,Object> aliasBeans = new HashMap<>();
    private Map<String,Method> handlerMap = new HashMap<>();
    public void run(ServletConfig config){
        Enumeration e = config.getInitParameterNames();
        List<String> params = new ArrayList<>();
        while(e.hasMoreElements()){
            params.add(e.nextElement().toString());
        }
        for(String param : params){
        String s = config.getInitParameter(param);
        System.out.println(s);
        }
    }
    @Override
    public void init(ServletConfig config) throws ServletException {
        run(config);
        System.out.println("init()............");

        // 1.扫描需要的实例化的类
        try {
            doScanPackage("com.jsalpha.utils");
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
        try {
            doIoc();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        // 4.建立path与method的映射关系
        handlerMapping();
        System.out.println("Controller层的path和方法映射.........");
        for(Map.Entry<String, Method> map: handlerMap.entrySet()) {
            System.out.println("key:" + map.getKey() + "; value:" + map.getValue());
        }
    }

    /**
     * 依赖注入
     * @throws IllegalAccessException
     */
    public void doIoc() throws IllegalAccessException {
        Field[] fields;
        Annotation a;
        String name;
        Object fieldValue;
        Object bean;
        for(Map.Entry<String,Object> aliasBean : aliasBeans.entrySet()){
            bean = aliasBean.getValue();
            fields = bean.getClass().getDeclaredFields();
//            fields = getFields(bean);
            for(Field field : fields){
                a = field.getAnnotation(MyQualifier.class);
                if(null != a){
                    name = ((MyQualifier) a).value();
                    fieldValue = aliasBeans.get(name);
                    field.setAccessible(true);
                    field.set(bean,fieldValue);
                }
            }
        }
    }
    public <T> Field[] getFields(T object){
        return object.getClass().getDeclaredFields();
    }
    /**
     * 实例化扫描到的类
     */
    public void doInstance() throws ClassNotFoundException {
        for(String className : classNames){
            ClassUtil.addControllerBean(aliasBeans,className);
            beans.put(className,Class.forName(className));
        }
        for(String className : classNames){
            ClassUtil.addServiceBean(aliasBeans,className);
            beans.put(className,Class.forName(className));
        }
    }
    public void doScanPackage(String packageName) throws ClassNotFoundException {
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        ClassOfPackageLoader classOfPackageLoader = new ClassOfPackageLoader();
        classNames = new LinkedList<>();
        classOfPackageLoader.collectClassOfPackageInner(path,packageName,classNames);
        classes = new ArrayList<>(classNames.size());
        for(String className : classNames){
            classes.add(classOfPackageLoader.loadClass(className));
        }
    }
    public void handlerMapping(){
        Method[] methods = null;
        Set<Method> methodList = new HashSet<>();
        Annotation myController;
        for(Class c : classes){
            methods = c.getMethods();
            methodList.addAll(filterReuqestMethod(methods));
            myController = c.getAnnotation(MyController.class);
            if(null != myController) {
                setPathMethod("/"+((MyController)myController).value(), methodList);
            }
        }
//        setPathMethod(methodList);
    }
    public void setPathMethod(String value, Set<Method> methods){
        MyRequestMapping annotation;
        String url;
        for(Method method : methods){
            annotation = method.getDeclaredAnnotation(MyRequestMapping.class);
            url = annotation.value();
            handlerMap.put(value+url,method);
        }
    }
    public void setPathMethod(Set<Method> methods){
        MyRequestMapping annotation;
        String url;
        for(Method method : methods){
            annotation = method.getDeclaredAnnotation(MyRequestMapping.class);
            url = annotation.value();
            handlerMap.put(url,method);
        }
    }

    public List<Method> filterReuqestMethod(Method[] methods){
        List<Method> methodList = new ArrayList<>();
        Annotation annotation;
        for(Method method : methods){
            annotation = method.getAnnotation(MyRequestMapping.class);
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

        // /maven_handmvc 替换掉项目目录
        String context = req.getContextPath();
        String path = uri.replaceAll(context, "");

        // 通过当前的path获取handlerMap的方法名
        Method method = handlerMap.get(path);
//        Object[] params = getParams(method,req,resp);
        Object[] params = MethodUtil.getParamMethod(req,resp,method);
        Object o = aliasBeans.get(path.split("/")[1]);
        String s =null;
        try {
            s = (String) method.invoke(o,params);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(resp.getOutputStream());
        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
        bufferedWriter.write(s);
        bufferedWriter.flush();
        outputStreamWriter.close();
        bufferedWriter.close();
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
    public Object[] getParams(Method method, HttpServletRequest req, HttpServletResponse resp){
        Object[] params;
        Class<?>[] classes = method.getParameterTypes();
        Annotation[][] annotations = method.getParameterAnnotations();
        if(null != annotations && annotations.length>0){
            for(Annotation annotation : annotations[0]){
                ((MyParam)annotation).value();
            }
        }
        String[] paramNames = MethodUtil.getParamNameMethod(method);
        if(classes.length>0){
            for(Class type : classes){
                if(type.getName() == req.getClass().getName()){
                    System.out.println("1对了");
                }
                if(type.getName() == req.getClass().getGenericSuperclass().getTypeName()){
                    System.out.println("2对了");
                }
//                if(type. == req.getClass()){
//                    System.out.println("3对了");
//                }
                System.out.println(type.getName());
                System.out.println(req.getClass().getName());
                System.out.println(HttpServletRequest.class);
                System.out.println(HttpServletRequest.class.getName());
                System.out.println(req.getClass().getGenericSuperclass().getTypeName());
                String t = type.getTypeName();
                System.out.println(t);
                System.out.println(t.getClass().getName());
                Enumeration paramList = req.getParameterNames();
                while(paramList.hasMoreElements()){
                    Object o = paramList.nextElement();
                    System.out.println(o.toString());
                }
            }

        }
        return new Object[0];
    }
    public static void main(String[] args){
        Thread thread = Thread.currentThread();
        ClassLoader classLoader = thread.getContextClassLoader();
        URL url = classLoader.getResource("");
        String path = url.getPath();
        System.out.println(path);
    }
}
