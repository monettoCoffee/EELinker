package eel.router;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MappingReflect {

    // 所有类(Handler)的包.class 位置
    public static List<String> classNames = new ArrayList<String>();

    // 类(Handler)名字 和 具体的实例化对象 的对应关系
    public static Map<String, Object> beanFactory = new HashMap<String,Object>();

    // 网址 和 请求方式 的对应关系
    public static Map<String, Method> getHandlerMapping = new HashMap<String,Method>();
    public static Map<String, Method> postHandlerMapping = new HashMap<String,Method>();

    // 网址 和 Handler类 的对应关系
    public static Map<String, Object> controllers = new HashMap<String,Object>();


}
