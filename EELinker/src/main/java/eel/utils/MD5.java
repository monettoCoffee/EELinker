package eel.utils;

import java.security.MessageDigest;

public class MD5 {

    public static boolean compare(String str,String md5str) {
        return StringToMd5(str).equals(md5str);
    }

    public static String StringToMd5(String str) {
        {
            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                md5.update(str.getBytes("UTF-8"));
                byte[] encryption = md5.digest();

                StringBuffer strBuf = new StringBuffer();
                for (int i = 0; i < encryption.length; i++) {
                    if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                        strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                    } else {
                        strBuf.append(Integer.toHexString(0xff & encryption[i]));
                    }
                }
                return strBuf.toString();
            } catch (Exception e) {
                return "";
            }
        }
    }
}