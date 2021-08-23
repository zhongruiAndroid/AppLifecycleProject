package com.github.lifecycle;


public class Logger {
    public static boolean isDebug=true;
    public static void i(String msg){
        if(!isDebug){
            return;
        }
        System.out.println("[log]"+msg);
    }
}
