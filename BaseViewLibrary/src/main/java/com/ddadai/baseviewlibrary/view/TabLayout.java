package com.ddadai.baseviewlibrary.view;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class TabLayout extends LinearLayout implements View.OnClickListener{


	private OnTabClickListener listener;
    
    private int lastSelect=-1;
    
    public TabLayout(Context context) {
        super(context);
        initView();
    }

    public TabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    @SuppressLint("NewApi")
	public TabLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView(){
        setOrientation(HORIZONTAL);
    }

    public void initData(List<TabItem>tabs,OnTabClickListener listener){
        this.listener=listener;
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight=1;
        if(tabs!=null&&tabs.size()>0){
            TabView mTabView=null;
            for(int i=0;i< tabs.size();i++){
            	com.ddadai.baseviewlibrary.view.TabItem tabItem = tabs.get(i);
            	mTabView = tabItem.getTabView(getContext());
            	if(mTabView==null){
            		mTabView=new TabView(getContext());
            	}
            	mTabView.setTag(tabs.get(i));
            	mTabView.initData(tabs.get(i));
            	tabs.get(i).index=i;
                mTabView.setOnClickListener(this);
                addView(mTabView,params);
            }

        }else{
            throw new IllegalArgumentException("tabs can not be empty");
        }
    }

    @Override
    public void onClick(View v) {
    	Log.d("test", "click");
    	listener.onTabClick((TabItem)v.getTag());
    	
    	
//    	TabItem item =(TabItem) v.getTag();
//    	if(lastSelect!=-1){
//			getChildAt(lastSelect).setSelected(false);
//		}
//    	getChildAt(item.index).setSelected(true);
//		lastSelect=item.index;
    }
    
    
    
    public void setCurrentTab(int position){
//    	getChildAt(position).performClick();
    	
    	if(lastSelect!=-1){
    		((TabView)getChildAt(lastSelect)).setNotSelect();;
    	}
    	((TabView)getChildAt(position)).setSelect();
    	lastSelect=position;
    };

    public interface OnTabClickListener{

        boolean onTabClick(TabItem tabItem);
    }
}
