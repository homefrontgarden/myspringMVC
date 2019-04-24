package com.jsalpha.utils.servlet;

import com.jsalpha.utils.common.ClassUtil;
import com.jsalpha.utils.common.MethodUtil;
import com.jsalpha.utils.annotation.MyQualifier;
import com.jsalpha.utils.annotation.MyRequestMapping;
import com.jsalpha.utils.load.ClassOfPackageLoader;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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
    private final static String initFileName = "contextConfigLocation";
    /**
     * 扫描到的所有类名
     */
    private LinkedList<String> classNames = new LinkedList<>();
    private List<Class<?>> classes = new ArrayList<>();
    /**
     * 类名对应的对象
     */
    private Map<String,Object> beans = new HashMap<>();
    private Map<String,Object> aliasBeans = new HashMap<>();
    private Map<String,Method> handlerMap = new HashMap<>();
    private Map<String,Object> classNameObject = new HashMap<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        System.out.println("init()............");
        String[] packageNames = getPackageNames(config);
        // 1.扫描需要的实例化的类
        try {
            doScanPackage(packageNames);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("当前文件下所有的class类.......");
        for(String name: classNames) {
            System.out.println(name);
        }

        // 2.实例化
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

        //5.收集实例化，并且已经依赖注入完成的对象
        collcetClassObject();
    }
    public String[] getPackageNames(ServletConfig config){
        String init = config.getInitParameter(initFileName);
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(init.split(":")[1]);
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String packageNames = properties.getProperty("package");
        return packageNames.split(";");
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
    public void doScanPackage(String[] packageNames) throws ClassNotFoundException {
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        ClassOfPackageLoader classOfPackageLoader = new ClassOfPackageLoader();
        for(String packageName : packageNames){
            classOfPackageLoader.collectClassOfPackageInner(path,packageName,classNames);
        }
        for(String className : classNames){
            classes.add(classOfPackageLoader.loadClass(className));
        }
    }
    public void handlerMapping(){
        Method[] methods ;
        Set<Method> methodList = new HashSet<>();
        Annotation myController;
        for(Class c : classes){
            methods = c.getMethods();
            methodList.addAll(filterReuqestMethod(methods));
            myController = c.getAnnotation(MyRequestMapping.class);
            if(null != myController) {
                setPathMethod(((MyRequestMapping)myController).value(), methodList);
            }
        }
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
     * 收集需要管理的实例化对象
     */
    public void collcetClassObject(){
        String className;
        for(Map.Entry<String,Object> entry : aliasBeans.entrySet()){
            className = entry.getValue().getClass().getName();
            classNameObject.put(className,entry.getValue());
        }
    }
    /**
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        System.out.println("doGet()............");
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        System.out.println("doPost()............");

        // 通过req获取请求的uri
        String uri = req.getRequestURI();

        // 替换掉项目目录
        String context = req.getContextPath();
        String path = uri.replaceAll(context, "");

        // 通过当前的path获取handlerMap的方法
        Method method = handlerMap.get(path);

        // 从请求与相应参数，获取method的形参参数
        Object[] params = MethodUtil.getParamMethod(req,resp,method);

        // 通过method反向获取调用此method的实例对象
        Object o = classNameObject.get(method.getDeclaringClass().getName());

        //通过反射执行method方法
        Object s =null;
        try {
            s = method.invoke(o,params);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //返回相应结果
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(resp.getOutputStream());
        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
        bufferedWriter.write(s.toString());
        bufferedWriter.flush();
        outputStreamWriter.close();
        bufferedWriter.close();

    }

}
