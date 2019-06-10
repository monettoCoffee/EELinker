package eel.router;

import app.GlobalConfig;
import eel.container.HttpServletRequestImpl;
import eel.container.HttpServletResponseImpl;
import eel.container.StaticResourceProcessor;
import eel.annotation.router.Handler;
import eel.annotation.router.ResponseData;
import eel.annotation.router.Router;
import eel.utils.Utils;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.*;

public class DispatcherServlet{

    private String prefix;
    private String suffix;

    public void service(HttpServletRequestImpl request, HttpServletResponseImpl response){
        if ("POST".equals(request.getMethod())){
            this.doPost(request, response);
        } else {
            this.doGet(request, response);
        }
    }

    public void doGet(HttpServletRequestImpl request, HttpServletResponseImpl response){
        try {
            doDispatcher(request, response, MappingReflect.getHandlerMapping);
        } catch (Exception e) {
            System.out.println("Router Logger : Servcer Occur 500 Exception");
            this.responseDataResolver("This Is 500 Page, Please Check Your Service", response);
        }
    }

    public void doPost(HttpServletRequestImpl request, HttpServletResponseImpl response){
        try {
            doDispatcher(request,response, MappingReflect.postHandlerMapping);
        } catch (Exception e) {
            System.out.println("Router Logger : Servcer Occur 500 Exception");
        }
    }

    private void doDispatcher(HttpServletRequestImpl request, HttpServletResponseImpl response, Map<String, Method> handlerMapping){
        String url = request.getRequestURI();
        if(handlerMapping.containsKey(url)){
            //匹配handlerMapping。
            Method method = handlerMapping.get(url);
            Map<String, String[]> parameterMap = request.getRequestParam();

            Parameter[] params = method.getParameters();
            Object []paramValues = new Object[params.length];
            for(int index = 0; index < params.length; ++index){
                String paramName = params[index].getName();

                if ("arg0".equals(paramName)){
                    System.out.println("Router Logger : Parameter Name Unknown, Please Sure Use Java8 -parameters");
                }

                if ("HttpServletResponseImpl".equals(params[index].getType().getSimpleName())){
                    paramValues[index] = response;
                    continue;
                } else if ("HttpServletRequestImpl".equals(params[index].getType().getSimpleName())){
                    paramValues[index] = request;
                    continue;
                }

                String []param = parameterMap.get(paramName);

                if (param != null){
                    paramValues[index] = parameterMap.get(params[index].getName())[0];
                }
            }

            try {
                String returnValue = (String)method.invoke(MappingReflect.controllers.get(url), paramValues);
                //由反射原理将请求参数映射到方法里执行。
                if(method.isAnnotationPresent(ResponseData.class)){
                    responseDataResolver(returnValue, response);
                } else {
                    resourceViewResolver(returnValue, request, response);
                }
            } catch (Exception e) {e.printStackTrace();}
        } else {
            System.out.println("Router Logger : Servcer Occur 404 Exception");
        }
    }

    public DispatcherServlet(){
        try {
            scanPackage(GlobalConfig.scanRouterPackage);
            initialInstance();
            initialHandlerMapping();
            this.prefix = GlobalConfig.viewPrefix;
            this.suffix = GlobalConfig.viewSuffix;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void scanPackage(String packageName){
        URL url = this.getClass().getClassLoader().getResource(packageName.replaceAll("\\.", "/"));
        //将包路径由a.b.c变为文件URL绝对路径file:/...a/b/c的形式。
        File dir = new File(url.getFile());
        for (File file : dir.listFiles()) {
            if(file.isDirectory()){
                scanPackage(packageName+"."+file.getName());
                //file是目录的情况下,递归扫包,扫描子包下其他类。
            }else{
                String className = packageName +"." +file.getName().replace(".class", "");
                //扫描出来的形式为 包.类.class的形式。
                MappingReflect.classNames.add(className);
                //把包下的所有的类都添加到classNames里面。
            }
        }
    }
    private void initialInstance() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        for(String className : MappingReflect.classNames){
            // className = com.monetto.controller.TestConmtroller.java
            Class<?> clazz = Class.forName(className); // 这是个 类声明 对象
            // 把上面的className 对应的类的位置 这个类的声明对象 即 Clazz对象 拿到
            if (clazz.isAnnotationPresent(Handler.class)){
                MappingReflect.beanFactory.put(Utils.lowerFirstWord(clazz.getSimpleName()), clazz);
                //获取含有Controller注解的class的名字,将第一个字母变成小写的"单例名",作为key放入HashMap。
                //而value则是通过反射生成的实例。
            }
        }
    }
    private void initialHandlerMapping() throws IllegalAccessException, InstantiationException {
        for(Map.Entry<String,Object> entry : MappingReflect.beanFactory.entrySet()){
            Class<?> clazz = (Class<?>) entry.getValue();
            String baseUrl = "";
            if(clazz.isAnnotationPresent(Handler.class)){
                if(clazz.isAnnotationPresent(Router.class)){
                    //如果类含有RequestMapping注解,则其值作为父URL提取。
                    Router annotation = clazz.getAnnotation(Router.class);
                    baseUrl=annotation.value();
                }
            }
            Method[] methods = clazz.getMethods();
            //其次扫描这个类中的方法,如果方法标有RequestMapping,则结合父url作为mapping。
            for (Method method : methods) {
                if(method.isAnnotationPresent(Router.class)){
                    String url = method.getAnnotation(Router.class).value();
                    String requestMethod = method.getAnnotation(Router.class).method();
                    url = (baseUrl+"/"+url).replaceAll("/+", "/");
                    if ("POST".equals(requestMethod)){
                        MappingReflect.postHandlerMapping.put(url, method);
                    } else {
                        MappingReflect.getHandlerMapping.put(url, method);
                    }
                    //mappingURL和其对应的方法。
                    MappingReflect.controllers.put(url, clazz.newInstance());
                    //记录哪个Router能导向那个URL。
                }
            }
        }
    }

    public void resourceViewResolver(String pageName,HttpServletRequestImpl request,HttpServletResponseImpl response){
        try {
            StaticResourceProcessor.returnPage((HttpServletResponseImpl) response, prefix+pageName+suffix);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void responseDataResolver(String data, HttpServletResponseImpl response){
        try {
            StaticResourceProcessor.returnData(response, data);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
