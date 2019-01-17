package com.ddadai.baseviewlibrary.view;


import android.content.Context;
import android.graphics.Point;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by user on 2015/9/22.
 * 上下滑动的抽屉效果
 * 
 * 在xml布局里面放2个view
 * 第一个是不滑动的
 * 第二个是滑动布局,默认是隐藏的
 */
public class CustomDrawerLayout extends RelativeLayout {

    private View mContent, mChoose;
    private ViewDragHelper mViewDragHelper;

    private boolean isOpen = false;
    private boolean isScroll = true;

    private int openStatus = -1;
    private final int STATUS_CLOSE = 0;
    private final int STATUS_OPEN_SOME = 1;
    private final int STATUS_OPEN_FULL = 2;
    private Point point;

    

    public CustomDrawerLayout(Context context) {
        this(context, null);
    }

    public CustomDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public CustomDrawerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContent = getChildAt(0);
        mChoose = getChildAt(1);
        mViewDragHelper = ViewDragHelper.create(this, new ViewDragHelperCallBack());
       
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mContent.layout(0, 0, mChoose.getMeasuredWidth(), mContent.getMeasuredHeight());
        
        if(point!=null){
        	mChoose.layout(point.x, point.y, point.x+mChoose.getMeasuredWidth(), point.y+mChoose.getMeasuredHeight());
		}else{
			mChoose.layout(0, -mChoose.getMeasuredHeight(), mChoose.getMeasuredWidth(), 0);
		}
        
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    public class ViewDragHelperCallBack extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View view, int i) {
            return mChoose == view;
        }


        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            if (top > 0) {
                top = 0;
            }
            if (top < -mChoose.getMeasuredHeight()) {
                top = -mChoose.getMeasuredHeight();
            }
            return top;
        }


        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            if (top == 0) {
                openStatus = STATUS_OPEN_FULL;
            } else if (top == -mChoose.getMeasuredHeight()) {
                openStatus = STATUS_CLOSE;
            } else {
                openStatus = STATUS_OPEN_SOME;
            }
            
            
            if (point == null) {
				point = new Point();
			}
			point.y=top;
			point.x=left;
            
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            switch (openStatus) {
                case STATUS_CLOSE:
                case STATUS_OPEN_SOME:
                    close();
                    break;
                case STATUS_OPEN_FULL:
                    open();
                    break;
            }
        }


        @Override
        public int getViewVerticalDragRange(View child) {
            return isScroll ? 1 : 0;
        }
    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void open() {
        isOpen = true;
        mViewDragHelper.smoothSlideViewTo(mChoose, 0, 0);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public void close() {
        isOpen = false;
        mViewDragHelper.smoothSlideViewTo(mChoose, 0, -mChoose.getMeasuredHeight());
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void change() {
        if (isOpen) {
            close();
        } else {
            open();
        }
    }

    public void setScroll(boolean isScroll) {
        this.isScroll = isScroll;
    }
}
