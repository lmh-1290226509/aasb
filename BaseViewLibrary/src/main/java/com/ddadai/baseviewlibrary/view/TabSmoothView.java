package com.ddadai.baseviewlibrary.view;

import java.util.ArrayList;
import java.util.List;

import com.ddadai.baseviewlibrary.R;
import com.ddadai.baseviewlibrary.view.interfaces.OnTabItemClickListener;
import com.ddadai.baseviewlibrary.view.interfaces.OnTabItemClickListener.TabViewItem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;

public class TabSmoothView extends View {

	
	private Paint mPaint;//画笔
	
	private TextPaint mTextPaint;//文字的画笔
	
	private int mTextColor=Color.BLUE;//文字的颜色
	
	
	private int mCheckTextColor=Color.RED;//选中的字体的颜色
	
	private int mLineColor=Color.BLACK;//线的颜色
	
	private int mTabColor=Color.parseColor("#00ff00");
	
	private float mTextSize=20;//文字大小    
	
	
	private float mTabHeight=10;//底部的那个滑块的高
	
	private float mLineSize=1;//线的宽度
	
	private float mTextPadding=30;//文字的padding
	
	private List<TabViewItem> mTabs;//
	
	private List<Float> mTextWidths;//宽度
	
	private GestureDetector mGestureDetector;//手势的
	
	private int mIndex=0;
	
	private int lastIndex=0;
	
	private RectF mLineRect;//tab的底部
	private RectF lastRect;//tab的底部
	
	private float mLeft=0;
	
	private float maxWidth;//文字的最大宽度
	
	private float mScreenWidth;//屏幕的最大宽度
	
	private OnTabItemClickListener mOnTabItemClickListener;//点击事件
	
	private Handler mHandler=new Handler(Looper.getMainLooper()){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}
	};
	
	public TabSmoothView(Context context, AttributeSet attrs, int defStyle)  {
		super(context, attrs, defStyle);
		
		TypedArray ta=context.obtainStyledAttributes(attrs, R.styleable.TabSmoothView);
		mTextSize =ta.getDimension(R.styleable.TabSmoothView_txtSize, mTextSize);
		mTextPadding =ta.getDimension(R.styleable.TabSmoothView_textPadding, mTextPadding);
		mLineSize =ta.getDimension(R.styleable.TabSmoothView_lineWidth, mLineSize);
		mTextColor=ta.getColor(R.styleable.TabSmoothView_txtColor, mTextColor);
		mLineColor=ta.getColor(R.styleable.TabSmoothView_lineColor, mLineColor);
		mCheckTextColor=ta.getColor(R.styleable.TabSmoothView_checkTextColor, mCheckTextColor);
		mTabColor=ta.getColor(R.styleable.TabSmoothView_tabColor, mTabColor);
		mTabHeight=ta.getDimension(R.styleable.TabSmoothView_tabHeight, mTabHeight);
		CharSequence[] items = ta.getTextArray(R.styleable.TabSmoothView_items);	
		ta.recycle();
		
		
		mTextPaint=new TextPaint(Paint.ANTI_ALIAS_FLAG);
		mTextPaint.setTextSize(mTextSize);
		mTextPaint.setAntiAlias(true);
		mTextPaint.density=getResources().getDisplayMetrics().density;
		
		mPaint=new Paint();
		mPaint.setColor(mTextColor);
		mPaint.setAntiAlias(true);
		
		mLineRect=new RectF();
		lastRect=new RectF();
		
		mTabs=new ArrayList<TabViewItem>();
		mTextWidths=new ArrayList<Float>();
		mGestureDetector=new GestureDetector(getContext(), new MyGestureListener(), mHandler);
		
		if(getPaddingTop()==0){
			setPadding(getPaddingLeft(), 10, getPaddingRight(), getPaddingBottom());
		}
		if(getPaddingBottom()==0){
			setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), 10);
		}
		
		if(items!=null){
			for (CharSequence item : items) {
				mTabs.add(new TabViewItem(item.toString()));
			}
		}
	}

	public TabSmoothView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public TabSmoothView(Context context)  {
		this(context,null);
	}
	
	public void addTab(String text){
		addTab(new TabViewItem(text));
	}
	public void addTab(TabViewItem item){
		mTabs.add(item);
		requestLayout();
	}
	public void addTabs(List<TabViewItem> tabs){
		if(tabs==null){
			return;
		}
		mTabs.addAll(tabs);
		requestLayout();
	}
	public void clearTab(){
		mTabs.clear();
	}
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);  
		    
		float width=0;
		float height=0;
		
		float widthChild=0;
		mTextWidths.clear();
		for (int i = 0; i < mTabs.size(); i++) {
			mTextPaint.setTextSize(mTextSize);
			mTextPaint.setColor(mTextColor);
			float measureText = mTextPaint.measureText(mTabs.get(i).text);
			mTextWidths.add(measureText);
			widthChild+=measureText;
			widthChild+=(2*mTextPadding);
			if(i!=mTabs.size()-1){
				widthChild+=mLineSize/2;
			}
		}
		
		
		if(widthMode==MeasureSpec.EXACTLY){
			width=widthSize;
		}else{
			width=widthChild;
		}
		
		
		if(heightMode==MeasureSpec.EXACTLY){
			height=heightSize;
		}else{
//			height=getSuggestedMinimumHeight();
			
			height=Math.max(getSuggestedMinimumHeight(), mTextSize+getPaddingTop()+getPaddingBottom()+mTabHeight);
		}
		
		mScreenWidth = width;
		maxWidth=widthChild;
		setMeasuredDimension((int)width,(int) height);
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		float lastX=mLeft;
		for (int i = 0; i < mTabs.size(); i++) {
			mTextPaint.setTextSize(mTextSize);
			mTextPaint.setTextAlign(Align.LEFT);
			FontMetrics fm = mTextPaint.getFontMetrics();
			Float width = mTextWidths.get(i);
			
			//只把在窗口范围的界面画出来
			if(lastX+mTextPadding>mScreenWidth){
				break;
			}
			if(lastX+width+2*mTextPadding<0){
				lastX+=width+2*mTextPadding;
				lastX += mLineSize / 2;
				continue;
			}
			
			if(mIndex==i){
				mTextPaint.setColor(mCheckTextColor);
			}else{
				mTextPaint.setColor(mTextColor);
			}
			
			canvas.drawText(mTabs.get(i).text, lastX+mTextPadding,(fm.bottom-fm.top)/2-fm.bottom +(getMeasuredHeight()-mTabHeight)/2, mTextPaint);
			if(mIndex==i){
				mLineRect.left=(lastX+mTextPadding);
				mLineRect.right= (lastX+width+mTextPadding);
				mLineRect.top= (getMeasuredHeight()-mTabHeight);
				mLineRect.bottom=getMeasuredHeight();
//				if(mIndex!=lastIndex){
//					lastRect.left=(lastX+mTextPadding);
//					lastRect.right= (lastX+width+mTextPadding);
//					lastRect.top= (getMeasuredHeight()-mTabHeight);
//					lastRect.bottom=getMeasuredHeight();
//				}
//				if(lastRect.left==0){
//					lastRect.left=(lastX+mTextPadding);
//					lastRect.right= (lastX+width+mTextPadding);
//					lastRect.top= (getMeasuredHeight()-mTabHeight);
//					lastRect.bottom=getMeasuredHeight();
//				}
			}
			if(i!=mTabs.size()-1){
				mPaint.setStrokeWidth(mLineSize);
				mPaint.setColor(mLineColor);
				canvas.drawLine(lastX+width+2*mTextPadding, getPaddingTop(), lastX+width+2*mTextPadding, getMeasuredHeight()-getPaddingBottom()-mTabHeight, mPaint);
			}
			lastX+=width+2*mTextPadding;
			lastX += mLineSize / 2;
		}
		
		drawTab(canvas);
	}
	
	private void drawTab(Canvas canvas){
		mPaint.setColor(mTabColor);
		if(mIndex==lastIndex){
			canvas.drawRect(mLineRect, mPaint);
		}else{
			currentTimes++;
			if(currentTimes<mTimes){
				canvas.drawRect((mLineRect.left-lastRect.left)*currentTimes/mTimes+lastRect.left, mLineRect.top, (mLineRect.right-lastRect.right)*currentTimes/mTimes+lastRect.right, mLineRect.bottom, mPaint);
				postInvalidate();
			}else{
				canvas.drawRect(mLineRect, mPaint);
			}
		}
		
	}
	
	public void setCheckIndex(int position){
		if(position==mIndex){
			//跟上次一样。
			return;
		}
		lastIndex=mIndex;
		lastRect.left=mLineRect.left;
		lastRect.top=mLineRect.top;
		lastRect.bottom=mLineRect.bottom;
		lastRect.right=mLineRect.right;
		
		
		mIndex=position;
		currentTimes=0;
		postInvalidate();
		if(mOnTabItemClickListener!=null){
			mOnTabItemClickListener.onTabItemClick(mIndex, mTabs.get(mIndex));
		}
	}
	
	private int mTimes=20;
	private int currentTimes=0;
	
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return mGestureDetector.onTouchEvent(event);
	}
	
	
	private  class MyGestureListener implements OnGestureListener{

		private float downX;
		
		private float lastLeft;
		
		@Override
		public boolean onDown(MotionEvent e) {
			downX=e.getX();
			lastLeft=mLeft;
			return true;
		}

		@Override
		public void onShowPress(MotionEvent e) {
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			float realX = e.getX()-mLeft;
			float left=0;
			for (int i = 0; i < mTextWidths.size(); i++) {
				Float width = mTextWidths.get(i);
				if(realX>=left&&realX<left+width+2*mTextPadding){
					setCheckIndex(i);
					break;
				}
				left+=width+2*mTextPadding;
//				if(i!=0){
					left+=mLineSize/2;
//				}
			}
			return true;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			mLeft= (e2.getX()-downX+lastLeft);
			if (maxWidth <= mScreenWidth) {
				mLeft = 0;
			} else {
				if (mLeft > 0) {
					mLeft = 0;
				}
				if (mLeft + maxWidth < mScreenWidth) {
					mLeft = mScreenWidth-maxWidth;
				}
			}
			postInvalidate();
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			return false;
		}
	}
	
	public void setOnTabItemClickListener(OnTabItemClickListener l){
		this.mOnTabItemClickListener=l;
	}
}
