package com.ddadai.baseviewlibrary.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ProgressBar;


public class MyProgressView extends ProgressBar implements android.os.Handler.Callback {

	
	private int curProgress;
	private int progress;
	public MyProgressView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public MyProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MyProgressView(Context context) {
		super(context);
		init();
	}
	
	void init(){
		handler = new Handler(this);
		setProgress(0);
	}
	
	private Handler handler;

	@Override
	public boolean handleMessage(Message msg) {
		setProgress(curProgress);
		startAnimaction();
		return true;
	}
	
	@Override
	public synchronized void setProgress(int progress) {
		super.setProgress(progress);
		if(progressListener != null){
			if(this.progress < progress){
				progress = this.progress;
			}
			progressListener.progressChange(progress, getMax());
		}
	}
	
	public void setAnimactionProgress(int progress){
		setProgress(0);
		this.progress = progress;
		curProgress = 0;
		setMax(10000);
		startAnimaction();
	}

	private void startAnimaction() {
		if(curProgress < progress){
			curProgress += 100;
			handler.sendEmptyMessageDelayed(1, 10);
		}
	}
	
	private ProgressListener progressListener;
	
	public void setProgressListener(ProgressListener progressListener) {
		this.progressListener = progressListener;
	}
	
	public static interface ProgressListener{
		void progressChange(int progress,int maxProgress);
		
	}

	
	
}
