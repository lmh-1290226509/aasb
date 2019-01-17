package com.ddadai.baseviewlibrary.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/***
 * 重写viewpager，控制能不能滑动，默认跟原本的一样
 * 
 * @author shi
 *
 */
public class EnableViewPager extends ViewPager {

	public boolean scrollEnable = true;

	public EnableViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public EnableViewPager(Context context) {
		super(context);
	}

	public void setScrollEnable(boolean enable) {
		scrollEnable = enable;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (!scrollEnable) {
			return true;
		}
		return super.onTouchEvent(ev);
	}
}
