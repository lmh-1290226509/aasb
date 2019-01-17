package com.ddadai.baseviewlibrary.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

public class AmountEditText extends EditText {

	private static final int AMOUNT_MAX_LEN = 10;

	public AmountEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public AmountEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public AmountEditText(Context context) {
		super(context);
		initView();
	}
	
	
	private void initView() {
		setSingleLine(); // 设置显示一行
//		setText("");
//		setSelection(0);
		addTextChangedListener(amountWatcher);
//		setKeyListener(new DigitsKeyListener(true,false));
////		setFilters(new InputFilter[] { new InputFilter.LengthFilter(AMOUNT_MAX_LEN + 2) }); // 12 + 金额符号 + 小数点
//		// editText.setGravity(Gravity.RIGHT);
//
		setLongClickable(false); // 设置这个金额的editText 没有长按事件 也就不能复制粘贴了
	}


	
	public void setText(String text){
		removeTextChangedListener(amountWatcher);
		super.setText(text);
		addTextChangedListener(amountWatcher);
	}
	TextWatcher amountWatcher = new TextWatcher() {
		private boolean hasPoint = false;
		@Override
		public void afterTextChanged(Editable s) {
			String text = s.toString();
			int length = text.length();
			if(length > 0){
				String last = text.substring(length-1);
				if(last.equals(".")){
					if(hasPoint){
						s.delete(length-1, length);
					} 
				}else{
					int point = text.indexOf(".");
					if(point > 0 && length - point > 3){
						s.delete(length-1, length);
					}
				}
			}
			hasPoint = text.contains(".");
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,int count) {

		}
	};
}
