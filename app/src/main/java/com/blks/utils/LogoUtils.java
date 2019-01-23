package com.blks.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.text.TextUtils;

import com.blks.model.LogoModel;

import net.bither.util.NativeUtil;

import java.io.File;

public class LogoUtils {

    public static String addLogo(Context c, String file, LogoModel logoModel){

        Bitmap sourceBitmap = NativeUtil.getBitmapFromFile(file);

        if (TextUtils.isEmpty(logoModel.address) || "null".equals(logoModel.address)) {
            logoModel.address = "";
        }

        String address="拍摄地："+logoModel.address;
        String time="拍摄时间："+logoModel.time;

        int textSize= (int) (sourceBitmap.getWidth()*0.6f/address.length());

        Bitmap watermarkBitmap = ImageUtil.drawTextToLeftTop(c, sourceBitmap,
                address+"\n"+time, textSize, Color.RED, 5, 10);

        String dir=Environment.getExternalStorageDirectory().toString()+"/resapp/img";
        File dirFile=new File(dir);
        if(!dirFile.exists()){
            dirFile.mkdirs();
        }
        String path=dir+"/"+System.currentTimeMillis()+".jpg";
        File compressFile = new File(path);
        NativeUtil.compressBitmap(watermarkBitmap, compressFile.getAbsolutePath());

        if (compressFile.exists()) {
            return compressFile.getAbsolutePath();
        } else {
            return file;
        }

    }

}
