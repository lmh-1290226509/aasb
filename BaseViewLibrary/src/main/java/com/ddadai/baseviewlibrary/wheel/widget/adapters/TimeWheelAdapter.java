package com.ddadai.baseviewlibrary.wheel.widget.adapters;

import java.util.List;

import com.ddadai.baseviewlibrary.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public  class TimeWheelAdapter extends AbstractWheelAdapter {

	
	public static interface TimeType{
		public static final int TYPE_YEAR = 1;//年
		public static final int TYPE_MONTH = 2;//月
		public static final int TYPE_DAY=3;//日
		public static final int TYPE_HOUR=4;//时
		public static final int TYPE_MIN=5;//分
		public static final int TYPE_SEC=6;//秒
	}
	
	private int timeType;
	private List<String> models;
	private int current=0;
	private Context mContext;
	
	public TimeWheelAdapter(Context mContext,int timeType) {
		this.timeType=timeType;
		this.mContext=mContext;
	}
	
	public void setModels(List<String> models){
		this.models=models;
		notifyDataChangedEvent();
	}
	
	public List<String> getModels(){
		return models;
	}
	
	public void setIndex(int current){
		this.current=current;
		notifyDataChangedEvent();
	}
	
	@Override
	public int getItemsCount() {
		return models==null?0:models.size();
	}

	@Override
	public View getItem(int index, View convertView, ViewGroup parent) {
		Holder h=null;
		if(convertView==null){
			convertView=LayoutInflater.from(mContext).inflate(R.layout.item_birth_year, parent,false);
			h=new Holder();
			h.tv=(TextView) convertView.findViewById(R.id.tempValue);
			convertView.setTag(h);
		}else{
			h=(Holder) convertView.getTag();
		}
		
		
		h.tv.setText(models.get(index));
		
		
//		//控制字体大小变化
//		if(current==index){
//			h.tv.setTextSize(24);	
//		}else{
//			h.tv.setTextSize(12);	
//		}
		//控制字体颜色变化
		if(current==index){
			h.tv.setTextColor(0xff000000);
			
			switch (timeType) {
			case TimeType.TYPE_YEAR:
				h.tv.append("年");
				break;
			case TimeType.TYPE_MONTH:
				h.tv.append("月");
				break;
			case TimeType.TYPE_DAY:
				h.tv.append("日");
				break;
			case TimeType.TYPE_HOUR:
				h.tv.append("时");
				break;
			case TimeType.TYPE_MIN:
				h.tv.append("分");
				break;
			case TimeType.TYPE_SEC:
				h.tv.append("秒");
				break;
			}
			
		}else{
			h.tv.setTextColor(0xffcccccc);
		}
		return convertView;
	}
	
	
	private class Holder{
		TextView tv;
	}
}
