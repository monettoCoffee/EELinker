package eel.scanner;

import java.util.HashMap;
import java.util.HashSet;

public class BeanMapping {
    /*
    *  todo Bean容器里到底要放啥？
    *
    *  todo RouterHandler 路由类
    *  todo ConfigClass 配置类
    *  todo ORMCoreClass ORM核心类
    *  todo ORMMapper SQLMapper类
    *  todo ORMSupportClass MapperDao类
    *  todo Customize EEL Bean
    *  todo Intercepter 拦截器
    *
    *  todo 有几个Bean容器
    *
    *  todo Handler 容器
    *  todo BeanMapper容器
    *  todo 配置Config容器
    *  todo 自定义容器
     *
    * */


    private static HashMap<String, Object> beanMapping = new HashMap<>();
    private static HashSet<String> beanMappingKeyword;

    public static void initBeanMappingKayword(HashSet<String> hashSet) {
        BeanMapping.beanMappingKeyword = hashSet;
    }

    public static <T> T getBean(String beanName) {
        return (T)BeanMapping.beanMapping.get(beanName);
    }

    public static void setBean(String beanName, Object bean) {
        if (BeanMapping.beanMappingKeyword.contains(beanName)) {
            System.out.println("Bean Logger : Bean Key Word Conflict");
        } else {
            if (BeanMapping.beanMapping.get(beanName) != null) {
                // todo 什么情况下会发生Bean覆盖？
                System.out.println();
            }
        }
    }

}
