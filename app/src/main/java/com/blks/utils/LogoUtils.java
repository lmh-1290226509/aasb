package com.blks.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.text.TextUtils;

import com.blks.model.LogoModel;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import id.zelory.compressor.Compressor;

public class LogoUtils {

    public static String addLogo(Context c, String file, LogoModel logoModel){

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//只读取图片大小，不加载到内存中
        options.inPreferredConfig = Bitmap.Config.RGB_565;//相对于ARGB_8888内存占用小
        BitmapFactory.decodeFile(file, options);
        options.inSampleSize = computeSampleSize(options, -1, SystemUtils.widthPs * SystemUtils.heightPs);
        options.inJustDecodeBounds = false;
        Bitmap sourceBitmap = BitmapFactory.decodeFile(file, options);

        if (TextUtils.isEmpty(logoModel.address) || "null".equals(logoModel.address)) {
            logoModel.address = "";
        }

        String address="拍摄地："+logoModel.address;
        String time="拍摄时间："+logoModel.time;

        int textSize= (int) (sourceBitmap.getWidth()*0.6f/address.length());

        Bitmap watermarkBitmap = ImageUtil.drawTextToLeftTop(c, sourceBitmap,
                address+"\n"+time, textSize, Color.RED, 5, 10);

        String dir=Environment.getExternalStorageDirectory().getAbsolutePath()+"/resapp/img";
        File dirFile=new File(dir);
        if(!dirFile.exists()){
            dirFile.mkdirs();
        }
        String path=dir+"/"+System.currentTimeMillis()+".jpg";
        saveBitmap(watermarkBitmap, path);

        File compressFile = null;
        try {
            compressFile = new Compressor(c)
                    .setDestinationDirectoryPath(dir)
                    .setQuality(30)
                    .compressToFile(new File(path), System.currentTimeMillis()+".jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (sourceBitmap != null && !sourceBitmap.isRecycled()) {
            sourceBitmap.recycle();
            sourceBitmap = null;
        }
        if (watermarkBitmap != null && !watermarkBitmap.isRecycled()){
            watermarkBitmap.recycle();
            watermarkBitmap = null;
        }

        if (compressFile != null) {
            new File(path).delete();
            return compressFile.getAbsolutePath();
        } else {
            return path;
        }

    }

    private static boolean saveBitmap(Bitmap bitmap, String path) {

        File file = new File(path);
        try {
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(file));

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);

            bos.flush();
            bos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    //获取一个较为合理的inSampleSize值得方法
    private static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                               int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 :
                (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 :
                (int) Math.min(Math.floor(w / minSideLength),
                        Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) &&
                (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

}