package eel.thread;

import eel.utils.ReflactUtil;

import java.lang.reflect.Method;

/**
 * @author monetto
 */
public class AsyncThread{

    public static void run(Object object, String methodName, Object...args) throws Exception{
        AsyncThread.run(object, methodName, false, args);
    }

    public static void run(Object object, String methodName, Boolean join, Object...args) throws Exception{
        Method method = ReflactUtil.getMethod(object, methodName, args);
        if (join){
            method.invoke(object, args);
        } else {
            AsyncMethodThread asyncMethodThread = new AsyncMethodThread(object, method, args);
            Thread thread = new Thread(asyncMethodThread);
            thread.start();
        }
    }
}

class AsyncMethodThread implements Runnable {

    private Method method;
    private Object executor;
    private Object[] args;

    public AsyncMethodThread(Object executor, Method method, Object[] args){
        this.executor = executor;
        this.method = method;
        this.args = args;
    }

    @Override
    public void run() {
        try {
            method.invoke(executor, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}