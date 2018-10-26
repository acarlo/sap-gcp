package com.util;

public class Util {

    public static final String EMPTY = "";

    public static boolean isNullOrEmpty(String text){
        if(text == null || text.equals(EMPTY))
            return true;

        return false;
    }
}
