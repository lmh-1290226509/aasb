package com.ddadai.baseviewlibrary.view;

import com.ddadai.baseviewlibrary.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;



/*****
 * 
 * @author shi
 * 底部的item，可以继承这个类，然后子类去实现
 */
public class TabView extends RelativeLayout {

	protected ImageView mTabImage;
	protected TextView mTabLable;

	public TabView(Context context) {
		super(context);
		initView(context,R.layout.view_tab_item);
	}

	public TabView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context,R.layout.view_tab_item);
	}

	@SuppressLint("NewApi")
	public TabView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context,R.layout.view_tab_item);
	}

	protected void initView(Context context,int layoutId) {
//		setOrientation(LinearLayout.VERTICAL);
		setGravity(Gravity.CENTER);
		setItemView(layoutId);
		mTabImage = (ImageView) findViewById(R.id.tab_image);
		mTabLable = (TextView) findViewById(R.id.tab_lable);

	}

	public void initData(TabItem tabItem) {
		
		if(mTabImage!=null){
			mTabImage.setImageResource(tabItem.imageDefaultId);
		}
		if(mTabLable!=null){
			mTabLable.setTextColor(getResources().getColor(tabItem.textColorDefaultId));
			mTabLable.setText(tabItem.textLabel);
		}
	}
	
	public void setItemView(int viewId){
		if(getChildCount()!=0){
			removeAllViews();
		}
		LayoutInflater.from(getContext()).inflate(viewId, this, true);
	}
	
	public void setItemView(View v){
		if(getChildCount()!=0){
			removeAllViews();
		}
		addView(v);
	}
	
	
	public void setSelect(){
		if(getTag()!=null&& getTag() instanceof TabItem){
			TabItem tabItem=(TabItem) getTag();
			mTabImage.setImageResource(tabItem.imageSelectId);
			mTabLable.setTextColor(getResources().getColor(tabItem.textColorSelectId));
		}
	}
	
	public void setNotSelect(){
		if(getTag()!=null&& getTag() instanceof TabItem){
			TabItem tabItem=(TabItem) getTag();
			mTabImage.setImageResource(tabItem.imageDefaultId);
			mTabLable.setTextColor(getResources().getColor(tabItem.textColorDefaultId));
		}
	}
}
