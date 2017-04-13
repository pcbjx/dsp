package com.feasycom.s_port.model;

import android.util.Log;

/**
 * Created by yumingyue on 2016/12/29.
 */

public class MyLog {
    //打印开关 为了程序运行速度，发布时关闭Log打印
    private static boolean isEnableLog = true;
    //封装Log
    public static void i(String tag, String msg){
        if (isEnableLog) Log.i(tag,msg);
    }
    public static void w(String tag, String msg){
        if (isEnableLog) Log.w(tag,msg);
    }
    public static void e(String tag, String msg){
        if (isEnableLog) Log.e(tag,msg);
    }
    public static void d(String tag, String msg){
        if (isEnableLog) Log.d(tag,msg);
    }
    public static void v(String tag, String msg){
        if (isEnableLog) Log.v(tag,msg);
    }
    public static void hexString (String tag, byte [] data,int len){
        if (isEnableLog)
        {
            for (int i =0;i<len;i++)
            {
                v(tag,""+String.format("%02d",data[i]));
            }
        }
    }
}
