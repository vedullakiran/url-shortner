package com.intuit.urlshortner.utils;


import lombok.experimental.UtilityClass;

@UtilityClass
public class Base62Utils {

    private static final String BASE62_CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = 62;

    public static String toBase62(long value) {
        StringBuilder sb = new StringBuilder();
        do {
            sb.insert(0, BASE62_CHARACTERS.charAt((int) (value % BASE)));
            value /= BASE;
        } while (value > 0);
        return sb.toString();
    }
}