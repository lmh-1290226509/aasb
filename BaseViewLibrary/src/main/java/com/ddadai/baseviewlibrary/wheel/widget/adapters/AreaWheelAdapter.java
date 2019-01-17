//package com.ddadai.baseviewlibrary.wheel.widget.adapters;
//
//import java.util.List;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.fuiou.mgr.R;
//import com.fuiou.mgr.model.RegionInfModel;
//
//public  class AreaWheelAdapter extends AbstractWheelAdapter {
//
//	
//	public static interface AreaType{
//		public static final int TYPE_PROV = 1;
//		public static final int TYPE_CITY = 2;
//		public static final int TYPE_COUNTRY=3;
//	}
//	
//	private int type;
//	private List<RegionInfModel> models;
//	private int current=0;
//	private Context mContext;
//	
//	public AreaWheelAdapter(Context mContext,int type) {
//		this.type=type;
//		this.mContext=mContext;
//	}
//	
//	public void setModels(List<RegionInfModel> models){
//		this.models=models;
//		notifyDataChangedEvent();
//	}
//	
//	public List<RegionInfModel> getModels(){
//		return models;
//	}
//	
//	public void setIndex(int current){
//		this.current=current;
//		notifyDataChangedEvent();
//	}
//	
//	@Override
//	public int getItemsCount() {
//		return models==null?0:models.size();
//	}
//
//	@Override
//	public View getItem(int index, View convertView, ViewGroup parent) {
//		Holder h=null;
//		if(convertView==null){
//			convertView=LayoutInflater.from(mContext).inflate(R.layout.item_birth_year, parent,false);
//			h=new Holder();
//			h.tv=(TextView) convertView.findViewById(R.id.tempValue);
//			convertView.setTag(h);
//		}else{
//			h=(Holder) convertView.getTag();
//		}
//		
//		
//		String text="";
//		switch (type) {
//		case AreaType.TYPE_PROV:
//			text=models.get(index).getProv_nm_cn();
//			break;
//		case AreaType.TYPE_CITY:
//			text=models.get(index).getRegion_nm_cn();
//			break;
//		case AreaType.TYPE_COUNTRY:
//			text=models.get(index).getCounty_nm_cn();
//			break;
//		}
//		h.tv.setText(text);
////		h.tv.setTextSize(12);
//		
//		
////		//控制字体大小变化
////		if(current==index){
////			h.tv.setTextSize(24);	
////		}else{
////			h.tv.setTextSize(12);	
////		}
//		//控制字体颜色变化
//		if(current==index){
//			h.tv.setTextColor(mContext.getResources().getColor(R.color.red_btn));
//			h.tv.getPaint().setFakeBoldText(true);
////			h.tv.setTextColor(0xff000000);
//		}else{
//			h.tv.getPaint().setFakeBoldText(false);
////			h.tv.setTextColor(0xffcccccc);
//			h.tv.setTextColor(0xff000000);
//		}
//		return convertView;
//	}
//	
//	
//	private class Holder{
//		TextView tv;
//	}
//}
