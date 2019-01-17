package com.yongchun.library.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by dee on 15/11/20.
 */
public class FileUtils {
    public static final String POSTFIX = ".JPEG";
    public static final String APP_NAME = "resapp";
    public static final String CAMERA_PATH = "/" + APP_NAME + "/CameraImage/";
    public static final String CROP_PATH = "/" + APP_NAME + "/CropImage/";
    public static final String COMPRESS_PATH = "/" + APP_NAME + "/CompressImage/";


    public static File createCameraFile(Context context) {
        return createMediaFile(context,CAMERA_PATH,null);
    }
    public static File createCropFile(Context context) {
        return createMediaFile(context,CROP_PATH,null);
    }
    public static File createCompressFile(Context context) {
        return createMediaFile(context,COMPRESS_PATH,null);
    }
    public static File createCompressFile(Context context,String random) {
        return createMediaFile(context,COMPRESS_PATH,random);
    }

    private static File createMediaFile(Context context,String parentPath,String random){
        String state = Environment.getExternalStorageState();
        File rootDir = state.equals(Environment.MEDIA_MOUNTED)?Environment.getExternalStorageDirectory():context.getCacheDir();

        File folderDir = new File(rootDir.getAbsolutePath() + parentPath);
        if (!folderDir.exists() && folderDir.mkdirs()){

        }

        String timeStamp = System.currentTimeMillis()+"";
        String fileName = APP_NAME + "_" + timeStamp + "_"+(random==null?"":random);
        File tmpFile = new File(folderDir, fileName + POSTFIX);
        return tmpFile;
    }
}
