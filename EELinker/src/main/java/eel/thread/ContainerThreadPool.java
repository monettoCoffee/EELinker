package eel.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author monetto
 * Plan Done Version 0.1.3
 */
public class ContainerThreadPool {

    private static ExecutorService pool;
    private static LongAdder threadNum;

    public static void initContainerThreadPool(){
        System.out.println("Telvres Logger: init thread pool");
        pool = Executors.newCachedThreadPool();
        threadNum = new LongAdder();
    }

    private void addThread(){

    }

    private static int getThreadNum(){
        return threadNum.intValue();
    }


}
