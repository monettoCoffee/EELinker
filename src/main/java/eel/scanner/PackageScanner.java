package eel.scanner;

import eel.router.MappingReflect;

import java.io.File;
import java.net.URL;

/**
 * @author monetto
 */
public class PackageScanner {
    public static void scanPackage(String packageName){
//        // todo 改为弱引用队列使用
//        URL url = PackageScanner.class.getClassLoader().getResource(packageName.replaceAll("\\.", "/"));
//        //将包路径由a.b.c变为文件URL绝对路径file:/...a/b/c的形式。
//        File dir = new File(url.getFile());
//        for (File file : dir.listFiles()) {
//            if(file.isDirectory()){
//                scanPackage(packageName+"."+file.getName());
//                //file是目录的情况下,递归扫包,扫描子包下其他类。
//            }else{
//                String className = packageName +"." +file.getName().replace(".class", "");
//                //扫描出来的形式为 包.类.class的形式。
//                MappingReflect.classNames.add(className);
//                //把包下的所有的类都添加到classNames里面。
//            }
//        }
    }
}
