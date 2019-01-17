package com.ddadai.baseviewlibrary.view;

import java.util.ArrayList;
import java.util.List;

import com.ddadai.baseviewlibrary.view.interfaces.LabelCheck;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

public class LabelLayout extends ViewGroup{

	
	private Mode  mode=Mode.multi;//模式
	
	public static enum Mode{
		single(1),//单选
		multi(2),//多选
		;
		int index;
		Mode(int i){
			index=i;
		}
	};
	
	private int checkIndex_singleMode=-1;//单选模式下，记录被选中的位置
	
	private LabelAdapter adapter;
	

	
	public LabelLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}

	public LabelLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public LabelLayout(Context context) {
		super(context);
		initView();
	}
	
	
	public void initView(){
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		 //得到宽和高的MODE和SIZE
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);  
		
		float width=0;
		float height=0;
		
		 //测量所有子元素，先执行，不然后面拿不到第一个子元素的测量宽/高
		
		
		measureChildren(widthMeasureSpec, heightMeasureSpec);
		
		float maxLineWidth=widthSize-getPaddingLeft()-getPaddingRight();
		
		
		int lineWidth = 0;
		int lineMaxHeight = 0;
		int lineMaxWidth=0;
//		int index=0;//记录行数中的第n－1个
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			int childWidth = child.getMeasuredWidth();
			int childHeight = child.getMeasuredHeight();
			
			if (lineWidth + childWidth > maxLineWidth) {
				//换行
				if(lineMaxWidth<lineWidth){
					lineMaxWidth=lineWidth;
				}
				lineWidth=childWidth;
				height += lineMaxHeight;
				lineMaxHeight = childHeight;
				if (i == getChildCount() - 1) {
					height += childHeight;
				}
			} 
			else{
				if (lineMaxHeight < childHeight) {
					lineMaxHeight = childHeight;
				}
				lineWidth+=childWidth;
				if(i==getChildCount()-1){
					height+=lineMaxHeight;
				}
			}
			
		}
		
		if(widthMode==MeasureSpec.EXACTLY){
			width=widthSize;
		}else{
			width=Math.min(lineMaxWidth, maxLineWidth)+getPaddingLeft()+getPaddingRight();
		}
		if(heightMode==MeasureSpec.EXACTLY){
			height=heightSize;
		}
	    
	    setMeasuredDimension((int) width,(int) height);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if(getChildCount()!=0){
			
			int width = 0;
			int height=0;
			
			int maxHeight=0;
			
			int maxWidth=getMeasuredWidth()-getPaddingRight()-getPaddingLeft();
			
			
			for (int i = 0; i < getChildCount(); i++) {
				View child = getChildAt(i);
				int childWidth = child.getMeasuredWidth();
				int childHeight = child.getMeasuredHeight();
				
				if(width+childWidth>maxWidth){
					width=0;
					height+=maxHeight;
					child.layout(getPaddingLeft()+width, getPaddingTop()+height, getPaddingLeft()+width+childWidth, getPaddingTop()+height+childHeight);
					width+=childWidth;
					maxHeight=childHeight;
				}else{
					child.layout(getPaddingLeft()+width, getPaddingTop()+height, getPaddingLeft()+width+childWidth, getPaddingTop()+height+childHeight);
					width+=childWidth;
					if(maxHeight<childHeight){
						maxHeight=childHeight;
					}
				}
			}
		}
		
	}
	
	public void changeView(){
		if(adapter!=null){
			removeAllViews();
			for (int i = 0; i < adapter.getCount(); i++) {
				
				View view = adapter.getView(i, adapter.getItem(i).isCheck(), null);
				if(view!=null){
					addView(view);
					final int position=i;
					view.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							boolean isCheck = adapter.getItem(position).isCheck();
							adapter.getItem(position).setCheck(!isCheck);
							adapter.getView(position, !isCheck, getChildAt(position));
							switch (mode) {
							case multi:
								break;
							case single:
								if(checkIndex_singleMode!=-1){
									boolean lastCheck = adapter.getItem(checkIndex_singleMode).isCheck();
									adapter.getItem(checkIndex_singleMode).setCheck(!lastCheck);
									adapter.getView(checkIndex_singleMode, !lastCheck, getChildAt(checkIndex_singleMode));
								}
								checkIndex_singleMode=position;
								break;
							default:
								break;
							}
							
						}
					});
				}
			}
		}
		ViewParent parent = getParent();
		if(parent!=null){
			parent.requestLayout();
		}
	}
	
	
	
	public abstract static class LabelAdapter {
		
		private LabelLayout layout;
		private List<LabelCheck> models;
		
		public abstract View getView(int position,boolean check,View childView);
		
		public void setModels(List<LabelCheck> models){
			this.models=models;
			notifyDataChange();
		}
		
		public int getCount(){
			return models==null?0:models.size();
		}
		
		public LabelCheck getItem(int position){
			return models.get(position);
		}
		
		public void notifyDataChange(){
			if(layout!=null){
				layout.changeView();
			}
		}
		
		private void dataChange(LabelLayout view){
			layout=view;
		}
	}
	
	public void setAdapter(LabelAdapter adapter){
		this.adapter=adapter;
		this.adapter.dataChange(this);
		this.adapter.notifyDataChange();
	}
	
	public Mode getCheckMode(){
		return mode;
	}
	
	public void setMode(Mode mode){
		this.mode=mode;
	}
	
	public List<Integer> getMultiCheck(){
		List<Integer> list=new ArrayList<Integer>();
		
		for (int i = 0; i < getChildCount(); i++) {
			if(adapter.getItem(i).isCheck()){
				list.add(i);
			}
		}
		return list;
	}
	public List<LabelCheck> getMultiCheckModel(){
		List<LabelCheck> list=new ArrayList<LabelCheck>();
		
		for (int i = 0; i < getChildCount(); i++) {
			if(adapter.getItem(i).isCheck()){
				list.add(adapter.getItem(i));
			}
		}
		return list;
	}
	
	public int getSingleCheck(){
		if(checkIndex_singleMode==-1&&adapter.getCount()!=0){
			for (int i = 0; i < adapter.getCount(); i++) {
				if(adapter.getItem(i).isCheck()){
					checkIndex_singleMode=i;
					break;
				}
			}
		}
		return checkIndex_singleMode;
	}

	public LabelCheck getSingleCheckModel() {
		LabelCheck check = null;
		for (int i = 0; i < adapter.getCount(); i++) {
			if (adapter.getItem(i).isCheck()) {
				check = adapter.getItem(i);
				break;
			}
		}
		return check;
	}

	
	public static class SimpleLabel implements LabelCheck{

		private boolean isCheck;
		public String label;
		
		@Override
		public void setCheck(boolean isChceked) {
			isCheck=isChceked;
		}

		@Override
		public boolean isCheck() {
			return isCheck;
		}
		
	}
	

}
