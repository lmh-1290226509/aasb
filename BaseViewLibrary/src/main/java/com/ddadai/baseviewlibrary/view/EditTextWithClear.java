package com.ddadai.baseviewlibrary.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

import com.ddadai.baseviewlibrary.R;

@SuppressLint("AppCompatCustomView")
public class EditTextWithClear extends EditText implements OnFocusChangeListener{

	private Drawable imgEnable;
	private Drawable leftDrawable;
	
	public EditTextWithClear(Context context) {
		super(context);
		init();
	}
	
	public EditTextWithClear(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public EditTextWithClear(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init() {
		//获取图片资源
		imgEnable = this.getResources().getDrawable(R.drawable.cleanicon);
		
		addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				setDrawable();
			}
		});
		
//		setDrawable();
		setOnFocusChangeListener(this);
		leftDrawable = getCompoundDrawables()[0];
		setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, null, null);
	}
	
	private void setDrawable() {
		if (length() == 0) {
			setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, null, null);
		} else if(isFocused()){
			setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, imgEnable, null);
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			setDrawable();
		}else{
			setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, null, null);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(imgEnable != null && event.getAction() == MotionEvent.ACTION_UP) {
			int x = (int) event.getX() ;
			//判断触摸点是否在水平范围内
			boolean isInnerWidth = (x > (getWidth() - getTotalPaddingRight())) &&
			(x < (getWidth() - getPaddingRight()));
			//获取删除图标的边界，返回一个Rect对象
			Rect rect = imgEnable.getBounds();
			//获取删除图标的高度
			int height = rect.height();
			int y = (int) event.getY();
			//计算图标底部到控件底部的距离
			int distance = (getHeight() - height) /2;
			//判断触摸点是否在竖直范围内(可能会有点误差)
			//触摸点的纵坐标在distance到（distance+图标自身的高度）之内，则视为点中删除图标
			boolean isInnerHeight = (y > distance) && (y < (distance + height));
			if(isInnerWidth && isInnerHeight) {
				setText("");
				if(onClearListener != null){
					onClearListener.onCLear(this);
				}
			}
		}
		return super.onTouchEvent(event);
	}
	
	public void setOnClearListener(OnClearListener onClearListener) {
		this.onClearListener = onClearListener;
	}
	
	private OnClearListener onClearListener;
	public  interface OnClearListener{
		void onCLear(View view);
	}

}
