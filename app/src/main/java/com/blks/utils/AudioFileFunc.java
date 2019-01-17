package com.blks.utils;

import android.media.MediaRecorder;
import android.os.Environment;

import java.io.File;

import static com.yongchun.library.utils.FileUtils.APP_NAME;

/**
 * Created by 12902 on 2018/10/20 0020.
 */

public class AudioFileFunc {
    //音频输入-麦克风
    public final static int AUDIO_INPUT = MediaRecorder.AudioSource.MIC;

    //采用频率
    //44100是目前的标准，但是某些设备仍然支持22050，16000，11025
    public final static int AUDIO_SAMPLE_RATE = 8000;  //44.1KHz,普遍使用的频率  8,000 Hz - 电话所用采样率, 对于人的说话已经足够
    //录音输出文件
    private final static String AUDIO_RAW_FILENAME = "RawAudio.raw";
    public static String AUDIO_WAV_FILENAME = "FinalAudio.wav";
    private final static String AUDIO_AMR_FILENAME = "FinalAudio.amr";

    /**
     * 判断是否有外部存储设备sdcard
     * @return true | false
     */
    public static boolean isSdcardExit(){
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    /**
     * 获取麦克风输入的原始音频流文件路径
     * @return
     */
    public static String getRawFilePath(){
        String mAudioRawPath = "";
        if(isSdcardExit()){
            mAudioRawPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                    +"/"+AUDIO_RAW_FILENAME;
        }

        return mAudioRawPath;
    }

    /**
     * 获取编码后的WAV格式音频文件路径
     * @return
     */
    public static String getWavFilePath(){
        String mAudioWavPath = "";
        if(isSdcardExit()){
            mAudioWavPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                    +File.separator+APP_NAME+File.separator+"audio";
            File file = new File(mAudioWavPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            mAudioWavPath = mAudioWavPath+File.separator+AUDIO_WAV_FILENAME;
        }
        return mAudioWavPath;
    }


    /**
     * 获取编码后的AMR格式音频文件路径
     * @return
     */
    public static String getAMRFilePath(){
        String mAudioAMRPath = "";
        if(isSdcardExit()){
            mAudioAMRPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                    +"/"+AUDIO_AMR_FILENAME;
        }
        return mAudioAMRPath;
    }


    /**
     * 获取文件大小
     * @param path,文件的绝对路径
     * @return
     */
    public static long getFileSize(String path){
        File mFile = new File(path);
        if(!mFile.exists())
            return -1;
        return mFile.length();
    }
}
