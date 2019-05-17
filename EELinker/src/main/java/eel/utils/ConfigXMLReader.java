package eel.utils;

import java.io.*;
import java.util.Map;

public class ConfigXMLReader {

    private static int hasSonLabel = 1;
    /**
     *  表示这个标签是否为根标签, 即 其是否存在子标签
     *  0 没有子标签, 标签为根标签
     *  1 存在子标签
     */

    public static boolean existSonLabel(){
        if (ConfigXMLReader.hasSonLabel == 1){
            ConfigXMLReader.hasSonLabel = 0;
            return true;
        }
        return false;
    }

    public static Map xmlToMap(String xmlPath){
        try {
            File xmlFile = new File(ClassLoader.getSystemResource("").getPath() + xmlPath);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(xmlFile));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.replaceAll(" ","");
                if (validLine(line)){
                    if (existSonLabel()){
                        sectionToMap(bufferedReader);
                    }
                    // todo line有效, 判断里面的变量是否应该再搞一个map
                }
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Telvres Logger: can't find xml file "+ xmlPath);
        } catch (IOException e){
            System.out.println("Telvres Logger: stream load exception in load " + xmlPath);
        }

        return null;
    }

//    public static String getLinePropertyName(String line){
//        int length = line.length();
//        boolean labelEnd = false;
//        StringBuilder name = new StringBuilder();
//        for (int index = 0; index < length; ++index){
//            char c = line.charAt(index);
//            if (c == '<'){
//                if (!labelEnd){
//
//                }
//                continue;
//            } else if (c == '>'){
//                return name.toString();
//            } else if (isRoot) {
//                // 如果有空格出现, 说明这个标签不一定为根标签, 读取结束
//                name.append(c);
//            } else if (c == ' '){
//                isRoot = false;
//            } else if (c == '='){
//                ConfigXMLReader.hasSonLabel = 1;
//                return name.toString();
//            }
//        }
//        // 非规范标签返回空
//        return "";
//    }

    public static Map sectionToMap(BufferedReader bufferedReader){
        return null;
    }

    public static boolean validLine(String line){
        if (line == null || "".equals(line)){
            return false;
        }
        return !(line.startsWith("<!--") && line.endsWith("-->"));
    }
}
