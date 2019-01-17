package com.ddadai.baseviewlibrary.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ddadai.baseviewlibrary.R;

@SuppressLint("ClickableViewAccessibility")
public class ClearEditText extends AppCompatEditText {

	private Drawable clearDrawable;
	private boolean isFirst=true;//第一次进来总是会显示
	private boolean isShowClear=true;


//	public void setShowClear(boolean b){
//		isShowClear=b;
//		invalidate();
//	}

	public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public ClearEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ClearEditText(Context context) {
		super(context);
		init();

	}
	
	private void init(){
		Drawable [] drawables = getCompoundDrawables();
		clearDrawable = drawables[2];
		
		if(clearDrawable==null){
			clearDrawable=getResources().getDrawable(R.drawable.cleanicon);
			setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], clearDrawable, drawables[3]);
			setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], null, getCompoundDrawables()[3]);
		}
		setOnFocusChangeListener(focusChangeListener);
		addTextChangedListener(new TextChange());
	}
	OnFocusChangeListener focusChangeListener = new OnFocusChangeListener() {
		
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if(hasFocus){
				boolean isVisible = getText().toString().length() >=1;
				setClearImgVisible(isVisible);
			}else{
				setClearImgVisible(false);

			}
		}
	};
	public class TextChange implements TextWatcher{

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,	int after) {
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			boolean isVisible = getText().toString().length() >=1;
			setClearImgVisible(isVisible&&isFocused());
//			if(isFirst){
//				isFirst=false;
//			}else {
//
//			}

		}

		@Override
		public void afterTextChanged(Editable s) {
			
		}
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			
			break;
		case MotionEvent.ACTION_UP:
		boolean isClean =(event.getX() > (getWidth() - getTotalPaddingRight()))&& (event.getX() < (getWidth() - getPaddingRight()));
		if (isClean) {
			setText("");
		}
			break;
		default:
			break;
		}
		return super.onTouchEvent(event);
	}
	
	private void setClearImgVisible(boolean isVisible){
		Drawable d = null;
		if(isVisible){
			d = clearDrawable;
		}
		setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], d, getCompoundDrawables()[3]);
	}
}
