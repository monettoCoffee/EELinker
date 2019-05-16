package cafe.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

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
