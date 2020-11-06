package com.chequer.phoenixsql.generator.util;

public class StringUtil {
    public static boolean isMatch(String value, String pattern) {
        int i = 0;
        int j = 0;
        int starIndex = -1;
        int iIndex = -1;

        while (i < value.length()) {
            if (j < pattern.length() && (pattern.charAt(j) == '?' || pattern.charAt(j) == value.charAt(i))) {
                ++i;
                ++j;
            } else if (j < pattern.length() && pattern.charAt(j) == '*') {
                starIndex = j;
                iIndex = i;
                j++;
            } else if (starIndex != -1) {
                j = starIndex + 1;
                i = iIndex + 1;
                iIndex++;
            } else {
                return false;
            }
        }

        while (j < pattern.length() && pattern.charAt(j) == '*') {
            ++j;
        }

        return j == pattern.length();
    }
}
