package eel.utils;

public class CountTimeUtil {

    public static long martStartTime;
    public static long martEndTime;

    public static void startTiming(){
        CountTimeUtil.martStartTime = System.currentTimeMillis();
    }

    public static long endTiming(){
        CountTimeUtil.martEndTime = System.currentTimeMillis();
        return CountTimeUtil.martEndTime - CountTimeUtil.martStartTime;
    }

    public static void endTimingAndPrint(){
        CountTimeUtil.martEndTime = System.currentTimeMillis();
        Long timing = CountTimeUtil.martEndTime - CountTimeUtil.martStartTime;
        System.out.println("Timing: " + timing.toString());
    }

}
