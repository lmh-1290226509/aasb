package com.ddadai.baseviewlibrary.view;

import com.ddadai.baseviewlibrary.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BottomItemView extends LinearLayout{

	private TextView text;
	private ImageView icon;
	
	@SuppressLint("NewApi")
	public BottomItemView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public BottomItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public BottomItemView(Context context) {
		super(context);
		init();
	}
	
	private void init(){
		inflate(getContext(), R.layout.view_bottom_item, this);
		setOrientation(LinearLayout.VERTICAL);
		text=(TextView) findViewById(R.id.text);
		icon=(ImageView) findViewById(R.id.icon);
	}

}
