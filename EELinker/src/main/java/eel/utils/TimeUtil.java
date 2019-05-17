package eel.utils;

public class TimeUtil {

    public static long martStartTime;
    public static long martEndTime;

    public static void startTiming(){
        TimeUtil.martStartTime = System.currentTimeMillis();
    }

    public static long endTiming(){
        TimeUtil.martEndTime = System.currentTimeMillis();
        return TimeUtil.martEndTime - TimeUtil.martStartTime;
    }

    public static void endTimingAndPrint(){
        TimeUtil.martEndTime = System.currentTimeMillis();
        Long timing = TimeUtil.martEndTime - TimeUtil.martStartTime;
        System.out.println("Timing: " + timing.toString());
    }

}
