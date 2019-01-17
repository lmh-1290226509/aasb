package com.blks.app;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.IOException;

public class MediaReocderOpt{

	private static final String LOG_TAG = "MediaRcorderTest";

	private MediaRecorder mRecorder = null;
	private MediaPlayer   mPlayer = null;
	private String  PATH_NAME = null;
	
	//初始化
    @SuppressLint("SdCardPath")
	public void Init(){
    	if(PATH_NAME==null){
			PATH_NAME  = Environment.getExternalStorageDirectory().getAbsolutePath();
			PATH_NAME += "/audiorecordtest.amr";
		}
    }

    public String getPathName(){
    	return PATH_NAME;
	}
    
    //开始录音
	public void StartRecording(){       
    	
		Log.e("test", "" + PATH_NAME);
		
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mRecorder.setOutputFile(PATH_NAME);
        mRecorder.setAudioSamplingRate(8000);
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
        	     
        mRecorder.start();
	}
	
	//停止录音
	public void StopRecording(){
		
		if (mRecorder != null){
			mRecorder.stop();
			mRecorder.release();
		}

		mRecorder = null;
	}
	
	//开始播放
	public void StartPlaying(){
	  mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(PATH_NAME);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
	}
	//删除录音
	public void DeletePlaying(){
		mPlayer = new MediaPlayer();
		try {
            mPlayer.setDataSource(PATH_NAME);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
	}
	//停止播放
	public void StopPlaying(){
		if (mPlayer != null){
			mPlayer.stop();
			mPlayer.release();
		}
	      mPlayer = null;
	}
	
	//是否播放结束
	public boolean IsStopPlaying(){
		return !mPlayer.isPlaying();
	}
	
	//释放资源
	public void Destory(){
		StopRecording();
		StopPlaying();
	}
}
