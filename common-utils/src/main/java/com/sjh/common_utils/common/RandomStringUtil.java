package com.sjh.common_utils.common;

import java.util.Random;

/**
 * Created by caish on 2017/8/28.
 */

public class RandomStringUtil {

    public static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdf0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public static String getRandomInt2String(int length) {
        if (length < 1) {
            throw new RuntimeException("too short");
        }
        int maxValue = (int) Math.pow(10, length);
        Random random = new Random();
        return String.valueOf(random.nextInt(maxValue));
    }
}
