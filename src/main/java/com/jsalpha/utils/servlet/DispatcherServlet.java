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
 * 实现springMVC的servlet
 * 1.通过配置文件扫描需要依赖注入包（packageNames）
 * 2.扫描包中的所有类（classNames）
 * 3.过滤并实例化需要依赖注入的类（aliasBeans）
 * 4.控制反转，将实例化的对象，注入到实例化对象需要依赖注入的属性中去
 * 5.保存类名与实例化对象的对应关系（注意：这部不是必须的，可以通过修改方法实现省略这部）
 * 6.建立path与method的映射关系
 * @author dengjingsi
 */
public class DispatcherServlet extends HttpServlet {
    /**
     * 扫描配置文件的参数name
     */
    private final static String initFileName = "contextConfigLocation";
    /**
     * 通过配置文件，扫描到的所有类的类名
     */
    private LinkedList<String> classNames = new LinkedList<>();
    /**
     * 类别名对应的map对象
     */
    private Map<String,Object> aliasBeans = new HashMap<>();
    /**
     * 请求地址对应方法map对象
     */
    private Map<String,Method> handlerMap = new HashMap<>();
    /**
     * 类名对应的map对象
     */
    private Map<String,Object> classNameObject = new HashMap<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        System.out.println("init()............");
        // 1.通过配置，获取所有需要依赖注入类的包名
        String[] packageNames = getPackageNames(config);
        System.out.println("需要扫描的包............");
        for(String packageName : packageNames){
            System.out.println(packageName);
        }

        // 2.扫描包中的所有类
        try {
            doScanPackage(packageNames);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("扫描到的包中，所有的class类.......");
        for(String name: classNames) {
            System.out.println(name);
        }

        // 3.过滤MyController，MyService注解修饰的类，并实例化
        try {
            doInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("当前实例化的对象信息.........");
        for(Map.Entry<String,Object> map : aliasBeans.entrySet()){
            System.out.println("key:" + map.getKey() + "; value:" + map.getValue());
        }

        // 4.将IOC容器中的service对象设置给controller层定义的field上
        try {
            doIoc();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        // 5.以map数据结构（类名->实例化对象）保存实例化对象
        collcetClassObject();

        // 6.建立path与method的映射关系
        handlerMapping();
        System.out.println("Controller层的path和方法映射.........");
        for(Map.Entry<String, Method> map: handlerMap.entrySet()) {
            System.out.println("key:" + map.getKey() + "; value:" + map.getValue());
        }

    }

    /**
     * 从配置文件中获取需要扫描的包
     * @param config
     * @return
     */
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
     * 扫描需要依赖注入包中的类
     * @param packageNames 需要依赖注入的包名
     * @throws ClassNotFoundException
     */
    private void doScanPackage(String[] packageNames) throws ClassNotFoundException {
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        ClassOfPackageLoader classOfPackageLoader = new ClassOfPackageLoader();
        for(String packageName : packageNames){
            classOfPackageLoader.collectClassOfPackageInner(path,packageName,classNames);
        }
    }

    /**
     * 过滤MyController，MyService注解修饰的类，并实例化
     */
    private void doInstance() throws ClassNotFoundException {
        for(String className : classNames){
            ClassUtil.addControllerBean(aliasBeans,className);
        }
        for(String className : classNames){
            ClassUtil.addServiceBean(aliasBeans,className);
        }
    }

    /**
     * 依赖注入
     * @throws IllegalAccessException
     */
    private void doIoc() throws IllegalAccessException {
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
     * 以map数据结构（类名->实例化对象）保存实例化对象
     */
    private void collcetClassObject(){
        String className;
        for(Map.Entry<String,Object> entry : aliasBeans.entrySet()){
            className = entry.getValue().getClass().getName();
            classNameObject.put(className,entry.getValue());
        }
    }

    /**
     * 保存请求地址与方法的对应关系
     */
    private void handlerMapping(){
        Method[] methods ;
        Set<Method> methodList = new HashSet<>();
        Annotation myController;
        Class c;
        for(Map.Entry<String,Object> map : aliasBeans.entrySet()){
            c = map.getValue().getClass();
            methods = c.getMethods();
            //过滤并获取methods中，MyRequestMapping注解修饰的方法
            methodList.addAll(MethodUtil.filterReuqestMethod(methods,MyRequestMapping.class));
            myController = c.getAnnotation(MyRequestMapping.class);
            if(null != myController) {
                //保存method注解中的地址与method对应关系
                setPathMethod(((MyRequestMapping)myController).value(), methodList);
            }
        }
    }
    /**
     * 补全，并保存methods方法对应的url地址
     * @param value
     * @param methods
     */
    private void setPathMethod(String value, Set<Method> methods){
        MyRequestMapping annotation;
        String url;
        for(Method method : methods){
            annotation = method.getDeclaredAnnotation(MyRequestMapping.class);
            url = annotation.value();
            handlerMap.put(value+url,method);
        }
    }

    /**
     * get方法
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

    /**
     * post方法
     * @param req
     * @param resp
     * @throws IOException
     */
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
