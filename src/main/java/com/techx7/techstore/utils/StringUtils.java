package com.techx7.techstore.utils;

import java.util.Locale;

public class StringUtils {

    public static boolean isNullOrEmpty(String text) {
        return text == null || text.isBlank();
    }

    public static String capitalize(String text) {
        return Character.toUpperCase(text.charAt(0)) +
                text.substring(1).toLowerCase(Locale.getDefault());
    }

}
