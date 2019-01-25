package com.blks.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class AudioPlayer {

    private boolean isRecord = false;// 设置正在录制的状态
    private String tempFile = null;
    private String audioFile = null;
    private MediaRecorder recorder = null;
    private MediaPlayer mPlayer = null;
    private Context mContext;

    public AudioPlayer(Context context, String fileName) {
        this.mContext = context;
        this.audioFile = getWavFilePath(fileName+".wav");
        this.tempFile = generateTempFile(fileName);
    }


    public void startRecording() {
//        this.audioFile = getWavFilePath(fileName+".wav");
        this.recorder = new MediaRecorder();
        this.recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        this.recorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS); // RAW_AMR);
        this.recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC); //AMR_NB);
        this.recorder.setOutputFile(this.tempFile);
        try {
            this.recorder.prepare();
            this.recorder.start();
            isRecord = true;
            return;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopRecording() {
        if (this.recorder != null && isRecord) {
            try{
                this.recorder.stop();
                this.recorder.reset();
                isRecord = false;//停止文件写入
                Log.d("TAG", "stopping recording");
                this.moveFile(this.audioFile);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String generateTempFile(String fileName) {
        String tempFileName = null;
        tempFileName = getWavFilePath(fileName+".3gp");
        return tempFileName;
    }

    public void moveFile(String file) {
        /* this is a hack to save the file as the specified name */

//        if (!file.startsWith("/")) {
//            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//                file = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + file;
//            } else {
//                file = "/data/data/" + mContext.getPackageName() + "/cache/" + file;
//            }
//        }

        // only one file so just copy it
//        String LogMsg = "renaming " + this.tempFile + " to " + file;
//        Log.d(Log_TAG, LogMsg);
        File f = new File(this.tempFile);
        f.renameTo(new File(file));
        // more than one file so the user must have pause recording. We'll need to concat files.
//        else {
//            FileOutputStream outputStream = null;
//            try {
//                outputStream = new FileOutputStream(new File(file));
//                FileInputStream inputStream = null;
//                File inputFile = null;
//                for (int i = 0; i < size; i++) {
//                    try {
//                        inputFile = new File(this.tempFiles.get(i));
//                        inputStream = new FileInputStream(inputFile);
//                        copy(inputStream, outputStream, (i>0));
//                    } catch(Exception e) {
//                        Log.e(Log_TAG, e.getLocalizedMessage(), e);
//                    } finally {
//                        if (inputStream != null) try {
//                            inputStream.close();
//                            inputFile.delete();
//                            inputFile = null;
//                        } catch (Exception e) {
//                            Log.e(Log_TAG, e.getLocalizedMessage(), e);
//                        }
//                    }
//                }
//            } catch(Exception e) {
//                e.printStackTrace();
//            } finally {
//                if (outputStream != null) try {
//                    outputStream.close();
//                } catch (Exception e) {
//                    Log.e(Log_TAG, e.getLocalizedMessage(), e);
//                }
//            }
//        }
    }

    //开始播放
    public void StartPlaying(){
        if(TextUtils.isEmpty(audioFile)){
            return;
        }
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
            try {
                mPlayer.setDataSource(audioFile);
                mPlayer.prepareAsync();
                mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        if (mPlayer != null) {
                            mPlayer.start();
                        }
                    }
                });

                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        if (mPlayer != null) {
                            mPlayer.reset();
                            mPlayer.release();
                            mPlayer = null;
                        }
                    }
                });

            } catch (IOException e) {
                Log.e("TAG", "prepare() failed");
            }
        }

    }

    //停止播放
    public void StopPlaying(){
        if (mPlayer != null){

            if (mPlayer.isPlaying()) {
                mPlayer.stop();
            }
            mPlayer.reset();
            mPlayer.release();
            mPlayer = null;
        }
    }

    //删除录音
    public void DeletePlaying(){}

    public boolean isRecord() {
        return isRecord;
    }

    public String getAudioPath(){
        return audioFile;
    }

    /**
     * 获取编码后的WAV格式音频文件路径
     * @return
     */
    public String getWavFilePath(String wavName){
        String mAudioWavPath = "";
        if(isSdcardExit()){
            mAudioWavPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator+"resapp"+File.separator+"audio";
            File file = new File(mAudioWavPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            mAudioWavPath = mAudioWavPath+File.separator+wavName;
        }
        return mAudioWavPath;
    }

    /**
     * 判断是否有外部存储设备sdcard
     * @return true | false
     */
    public boolean isSdcardExit(){
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }
}
