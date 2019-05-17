package eel.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ReflactUtil {

    public static Map<String, String> getFieldAndValue(Object object) {
        Map<String, String> map = new HashMap<String, String>();
        if (object == null){
            return map;
        }
        Class clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object val = field.get(object);
                map.put(field.getName(), val == null ? "" : val.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public static Object getValueByField(Object obj, String key) {
        Class clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (field.getName().endsWith(key)) {
                    return field.get(obj);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Method getMethod(Object object, String methodName, Object...args) throws NoSuchMethodException {
        int argLength = args.length;
        Class<?>[] argTypes = new Class<?>[argLength];
        for (int index = 0; index < argLength; ++index) {
            argTypes[index] = args[index].getClass();
        }
        Method method = object.getClass().getDeclaredMethod(methodName, argTypes);
        method.setAccessible(true);
        return method;
    }

    public static Object runMethodByName(Object object, String methodName, Object...args) throws Exception {
        Method method = ReflactUtil.getMethod(object, methodName,args);
        return method.invoke(object, args);
    }

}
