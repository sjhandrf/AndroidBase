package com.sjh.common_utils.common;

import android.text.TextUtils;

public class ParamUtils {

    public static String getDefaultString(String str, String defaultStr) {
        return TextUtils.isEmpty(str) ? defaultStr : str;
    }

    public static String getDefaultString(String str) {
        return getDefaultString(str, "");
    }
}
