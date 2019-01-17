package com.ddadai.baseviewlibrary.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

public class DiyLayoutManager extends GridLayoutManager{

	public DiyLayoutManager(Context spanCount, int orientation) {
		super(spanCount, orientation);
		// TODO Auto-generated constructor stub
	}

	
	
//	@Override
//	public void setSpanCount(int arg0) {
//		super.setSpanCount(arg0);
//
//
//		if(getItemViewType(getChildAt(arg0))==0){
//		}else{
//			super.setSpanCount(1);
//		}
//		
//	}
	
//	@Override
//	public int getPosition(View view) {
//		// TODO Auto-generated method stub
//		return super.getPosition(view);
//	}
//	
//	@Override
//	public int getSpanCount() {
//		if(getItemViewType(getChildAt(getPosition(view)))==0){
//			super.setSpanCount(arg0);
//		}else{
//			super.setSpanCount(1);
//		}
//		// TODO Auto-generated method stub
//		return super.getSpanCount();
//	}
}
