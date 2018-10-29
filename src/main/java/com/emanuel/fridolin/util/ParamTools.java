package com.emanuel.fridolin.util;

public class ParamTools {
    public static String paramsToString(String[] params) {
        return paramsToString(params, 0);
    }

    public static String paramsToString(String[] params, int startingIndex) {
        if (startingIndex < params.length) {
            StringBuilder builder = new StringBuilder();
            for (int pos = startingIndex; pos < params.length; pos++) {
                builder.append(params[pos]).append(" ");
            }
            return builder.substring(0, builder.length() - 1);
        } else {
            return "";
        }
    }
}
