package com.ddadai.baseviewlibrary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerAdapter extends Adapter<BaseRecyclerAdapter.MyViewHolder>{
	
	protected Context mContext;
	protected List models;
	protected OnItemClickRecycleListener mClickRecycleListener;
	public static final int ItemType_Default=0;
	
	public BaseRecyclerAdapter(Context c){
		this.mContext=c;
	}
	
	public void setModels(List models){
		this.models=models;
		notifyDataSetChanged();
	}
	
	public List getModels(){
		return models;
	}
	
	public void addModels(List models){
		if(this.models==null){
			setModels(models);
		}else{
			this.models.addAll(models);
			notifyDataSetChanged();
		}
	}
	
	
	
	public void addItem(Object item){
		if(models==null){
			models=new ArrayList();
		}
		models.add(item);
		notifyItemInserted(getItemCount()-1);
	}
	
	public void removeItem(int position){
		if(models==null||models.size()<=position||position<0){
			return;
		}
		if(models.remove(position)!=null){
			notifyItemRemoved(position);
		}
	}
	public void removeItem(Object item){
		if(models==null){
			return;
		}
		int position=models.indexOf(item);
		if(position!=-1){
			removeItem(position);
		}
	}
	
	public void removeAllItem(){
		if(models==null){
			return;
		}
		models.clear();
		notifyDataSetChanged();
	}
	@Override
	public int getItemCount() {
		return models==null?0:models.size();
	}
	
	/**遇到多布局的时候需要子类的实现去重写*/
	public Object getItem(int position){
		try {
			return models.get(position);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	protected abstract class MyViewHolder extends ViewHolder{

		View rootView;
		
		public MyViewHolder(View arg0) {
			super(arg0);
			rootView=arg0;
			initView();
		}

		protected   <T extends View> T findViewById(int id) {
			return rootView.findViewById(id);
		}
//		protected <T extends View> T $(int id) {
//			return rootView.findViewById(id);
//		}

		public View getRootView(){
			return rootView;
		}
		protected abstract void initView();
		
	}
	
	@Override
	public void onBindViewHolder(MyViewHolder arg0, int arg1) {
		try {
			setItemClick(arg0,arg1);
			initViewHolder(arg0, arg1,getItem(arg1));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup arg0, int itemType) {
		try {
			return newViewHolder(LayoutInflater.from(mContext).inflate(getRootViewLayoutId(itemType), arg0,false),itemType);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
	protected abstract  int getRootViewLayoutId(int itemType);
	protected abstract  MyViewHolder newViewHolder(View rootView,int itemType);
	protected abstract  void initViewHolder(MyViewHolder holder,int position,Object item);
	
	
	protected void setItemClick(MyViewHolder arg0,final int position) {
		if(mClickRecycleListener!=null&&arg0!=null){
			arg0.rootView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mClickRecycleListener.itemClick(position, getItem(position));
				}
			});
		}
	}
	
	
	public void setOnItemClickRecycleListener(OnItemClickRecycleListener l){
		this.mClickRecycleListener=l;
	}
	public static interface OnItemClickRecycleListener{
		void itemClick(int position,Object model);
	}
}
