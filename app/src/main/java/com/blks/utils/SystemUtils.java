package com.blks.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;
import android.view.Display;

public class SystemUtils {

    public static int versionCode;
    public static String versionName;
    /** 得到分辨率高度 */
    public static int heightPs = -1;
    /** 得到分辨率宽度 */
    public static int widthPs = -1;


    public static void getScreen(Activity context) {
        DisplayMetrics metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        heightPs = metrics.heightPixels;
        widthPs = metrics.widthPixels;
        getVersion(context);
    }
    /***
     * 获取客户端版本
     *
     * @param context
     * @return
     */
    private static void getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                    0);
            versionName = info.versionName;
            versionCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
