package com.ddadai.baseviewlibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ddadai.baseviewlibrary.R;

public class EditTextView extends LinearLayout{
	private ImageView imgIcon;
	private TextView tvName;
	private int textColor;

	public EditTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.editTextView);
		imgIcon = new ImageView(context);
		imgIcon.setImageResource(a.getResourceId(R.styleable.tabWidget_TabIcon,
				R.drawable.ic_launcher));
		tvName = new TextView(context);
		tvName.setText(a.getString(R.styleable.tabWidget_text));
		tvName.setGravity(Gravity.CENTER);
		tvName.setTextSize(a.getDimension(R.styleable.tabWidget_textSize, 10));
		textColor = a.getColor(R.styleable.tabWidget_textColor, context.getResources().getColor(android.R.color.white))
				| a.getInteger(R.styleable.tabWidget_textColor, context.getResources().getColor(android.R.color.white));
		tvName.setTextColor(textColor);
		addView(imgIcon,
				new LayoutParams(a.getLayoutDimension(
						R.styleable.tabWidget_iconWidth, 30),
						a.getLayoutDimension(R.styleable.tabWidget_iconHeight,
								30)));
		addView(tvName, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		a.recycle();
	}


	
}
