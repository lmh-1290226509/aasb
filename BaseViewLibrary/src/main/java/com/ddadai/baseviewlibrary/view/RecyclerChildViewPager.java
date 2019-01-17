package com.ddadai.baseviewlibrary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * RecyclerViewPager
 *
 * @author Green
 */
public class RecyclerChildViewPager extends RecyclerViewPager {




    public RecyclerChildViewPager(Context context) {
        this(context, null);
    }

    public RecyclerChildViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerChildViewPager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
//        initAttrs(context, attrs, defStyle);
//        setNestedScrollingEnabled(false);
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        //每次进行onTouch事件都记录当前的按下的坐标
        curP.x = e.getX();
        curP.y = e.getY();

        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            //记录按下时候的坐标
            //切记不可用 downP = curP ，这样在改变curP的时候，downP也会改变
            downP.x = e.getX();
            downP.y = e.getY();
            //此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
            getParent().requestDisallowInterceptTouchEvent(true);
        }

        if (e.getAction() == MotionEvent.ACTION_MOVE) {
            //此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
            getParent().requestDisallowInterceptTouchEvent(true);
        }

        if (e.getAction() == MotionEvent.ACTION_UP) {
            //在up时判断是否按下和松手的坐标为一个点
            //如果是一个点，将执行点击事件，这是我自己写的点击事件，而不是onclick
            if (downP.x == curP.x && downP.y == curP.y) {
//                onSingleTouch();
                return true;
            }
        }

        
        return super.onTouchEvent(e);
    }



}
