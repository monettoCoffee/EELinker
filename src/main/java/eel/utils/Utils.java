package eel.utils;

import java.io.*;

public class Utils {

    public static boolean isEmpty(String str){
        return "".equals(str) || str == null;
    }

    public static String lowerFirstWord(String name){
        char[] charArray = name.toCharArray();
        charArray[0] += 32;
        return String.valueOf(charArray);
    }

    public static String inputStreamToString(InputStream inputStream) throws IOException {
        int count = 0;
        while (count == 0) {
            // 如果发送的请求没有结束, 那么值为0
            count = inputStream.available();
        }

        byte[] bt = new byte[count];
        int readCount = 0;
        while (readCount < count) {
            readCount += inputStream.read(bt, readCount, count - readCount);
        }
        String xx = new String(bt);
        return xx;
    }

    public static InputStream StringToInputStream(String str){
        return new ByteArrayInputStream(str.getBytes());
    }
}
