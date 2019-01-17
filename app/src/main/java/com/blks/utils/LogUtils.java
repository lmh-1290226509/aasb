package com.blks.utils;

import android.util.Log;


/**
 * Created by shi on 2017/1/19.
 */

public class LogUtils {

    private static final String defaultTag="FUIOU";

    public static final void d(String tag, Object msg){
//        if(BuildConfig.LOG_DEBUG){
            Log.e(tag, msg==null?"\n":msg+"\n");
//        }
    }

    public static final void d(Object msg){
        d(defaultTag,msg);
    }
}
