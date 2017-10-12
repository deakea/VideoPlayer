package com.android.deak.videoplayer.utils;

/**
 * Created by deak on 2016/8/3.
 */
public class CommonUtils {
    public static String formatDuration(long duration){
        int secondAll = (int)(duration / 1000);
        int minute = secondAll / 60;
        int second = secondAll % 60;
        //%1:填充第一个参数到这里
        //$d:参数是一个Int型
        //$s:参数是一个String型
        return String.format("%1$d分%2$d秒", minute, second);
    }
}
