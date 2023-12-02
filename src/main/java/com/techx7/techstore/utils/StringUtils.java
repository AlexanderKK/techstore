package com.techx7.techstore.utils;

import java.util.Locale;

import static com.techx7.techstore.constant.Messages.INPUT_NOT_NULL;

public class StringUtils {

    public static boolean isNullOrEmpty(String text) {
        return text == null || text.isBlank();
    }

    public static String capitalize(String text) {
        if(text == null) {
            throw new IllegalArgumentException(INPUT_NOT_NULL);
        }

        return Character.toUpperCase(text.charAt(0)) +
                text.substring(1).toLowerCase(Locale.getDefault());
    }

    public static String getClassNameLowerCase(Class<?> clazz) {
        return clazz.getSimpleName().toLowerCase();
    }

    public static String replaceAllWhiteSpacesWithUnderscores(String text) {
        return text.toLowerCase().replaceAll("\\s+", "_");
    }

}
