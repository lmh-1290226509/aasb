package com.ddadai.baseviewlibrary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ddadai.baseviewlibrary.R;
import com.ddadai.baseviewlibrary.utils.AnimationUtils;
import com.ddadai.baseviewlibrary.utils.ViewUtils;

public class DotNumberView extends LinearLayout{

	

	private static final String TAG="DotNumberView";
	private Context mContext;
	private TextView currentTv,allNmTv;
	
	private int mSize;
	
	public DotNumberView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		
		mContext=context;
		View.inflate(getContext(),R.layout.view_dot_number, this);
		currentTv=(TextView) findViewById(R.id.currentTv);
		allNmTv=(TextView) findViewById(R.id.allNmTv);
		
		ViewUtils.setViewSize(currentTv, 24, 24);
		ViewUtils.setTextSize(currentTv, 13);
		
		ViewUtils.setViewSize(allNmTv, 55, 24);
		ViewUtils.setTextSize(allNmTv, 13);
		
	}

	public DotNumberView(Context context) {
		this(context, null);		
	}
	
//	public DotNumberView(Context context, AttributeSet attrs, int defStyle) {
//		super(context, attrs, defStyle);
//		
//	}

	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
//		Log.d(TAG, "onFinishInflate");
	}
	
	
	/**
	 * 初始化设置
	 * @param size
	 */
	public void init(int size){
		mSize=size;
		if(mSize<10){
			allNmTv.setText("of 0"+mSize);
		}else{
			allNmTv.setText("of "+mSize);
		}
		setCurrentIndex(0);
	}
	
	/**
	 * 设置下标  下标从0开始
	 * @param index
	 */
	public void setCurrentIndex(int index){
		index++;
		if(index<1||index>mSize){
			index=1;
		}
		if(index<10){
			currentTv.setText("0"+index);
		}else{
			currentTv.setText(""+index);
		}
		AnimationUtils.getInstance().startCenterYRotate(currentTv);
	}

}
