package com.ddadai.baseviewlibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.ddadai.baseviewlibrary.R;

public class RedPointView extends View{

	
	private String mText;//要画的文字
	
	private int mPointColor;//圆的颜色
	
	private int mTextColor;//文字的颜色
	
	private float mTextSize=8;//文字的大小
	
	private Paint mPaint;

	private int mTimes=30;
	private int currentTimes=0;
	private  final int type_disappear=-1;//消失
	private final int type_appear=1;//出现
	private final int type_change=0;//只是单纯的变化
	private int mType=type_change;


	public RedPointView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray ta =context.obtainStyledAttributes(attrs, R.styleable.redPointView);
		mPointColor = ta.getColor(R.styleable.redPointView_pointColor, Color.RED);
		mTextColor = ta.getColor(R.styleable.redPointView_pointTextColor, Color.WHITE);
		mText=ta.getString(R.styleable.redPointView_number);
		
		if(mText==null){
			mText="";
		}
		
		ta.recycle();
		mPaint=new Paint();
	}
	
	

	public RedPointView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public RedPointView(Context context) {
		this(context,null);
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		switch (mType) {
		case type_appear:
			drawAppear(canvas);
			break;
		case type_disappear:
			drawDisappear(canvas);
			break;
		case type_change:
			drawChange(canvas);
			break;
		default:
			break;
		}
	}
	
	
	private void drawDisappear(Canvas canvas){
		currentTimes--;
		float width=getMeasuredWidth();
		float height=getMeasuredHeight();
		
		if(width<=0){
			width=20;
		}
		if(height<=0){
			height=20;
		}
		//画圆,圆心在中间
		mPaint.setColor(mPointColor);
		mPaint.setAntiAlias(true);
		canvas.drawCircle(width/2, height/2, width/mTimes*currentTimes/2, mPaint);
		
		if(currentTimes>0){
			invalidate();
		}
	}
	private void drawAppear(Canvas canvas){
		currentTimes++;
		float width=getMeasuredWidth();
		float height=getMeasuredHeight();
		
		if(width<=0){
			width=20;
		}
		if(height<=0){
			height=20;
		}
		//画圆,圆心在中间
		mPaint.setColor(mPointColor);
		mPaint.setAntiAlias(true);
		canvas.drawCircle(width/2, height/2, width/mTimes*currentTimes/2, mPaint);
		
		
		if(currentTimes<mTimes){
			invalidate();
		}else{
			//把文字画在圆心位置
			mPaint.setColor(mTextColor);
			float v = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mTextSize, getResources().getDisplayMetrics());
			mPaint.setTextSize(v);
			FontMetricsInt fm = mPaint.getFontMetricsInt();
			float center=height/2;
			float baselineY=center + (fm.bottom - fm.top)/2 - fm.bottom; //通过中线获取baseLine的y坐标
			mPaint.setTextAlign(Align.CENTER);
			canvas.drawText(mText,width/2,baselineY, mPaint);
		}
	}
	
	
	
	private void drawChange(Canvas canvas){
		int width=getMeasuredWidth();
		int height=getMeasuredHeight();
		
		if(width<=0){
			width=20;
		}
		if(height<=0){
			height=20;
		}
		//画圆,圆心在中间
		mPaint.setColor(mPointColor);
		mPaint.setAntiAlias(true);
		canvas.drawCircle(width/2, height/2, width/2, mPaint);
		
		//把文字画在圆心位置
		mPaint.setColor(mTextColor);
		mPaint.setTextSize(mTextSize  * getResources().getDisplayMetrics().density);
		
		FontMetricsInt fm = mPaint.getFontMetricsInt();
		int center=height/2;
		int baselineY=center + (fm.bottom - fm.top)/2 - fm.bottom; //通过中线获取baseLine的y坐标
		mPaint.setTextAlign(Align.CENTER);
		canvas.drawText(mText,width/2,baselineY, mPaint);
	}
	
	

	
	public void setNumber(int number){
		if(number<=0){
			if(mText.length()>0){
				currentTimes=mTimes;
				mType=type_disappear;
			}else{
				mType=type_change;
			}
			mText="";
		}else if(number>99){
			if(mText.length()>0){
				mType=type_change;
			}else{
				currentTimes=0;
				mType=type_appear;
			}
			mText=number+"";
		}else{
			if(mText.length()>0){
				mType=type_change;
			}else{
				currentTimes=0;
				mType=type_appear;
			}
			mText=number+"";
		}
		postInvalidate();
	}
	public void setNumberNotAnim(int number){
		mType=type_change;
		mText=number+"";
		invalidate();
	}

}
