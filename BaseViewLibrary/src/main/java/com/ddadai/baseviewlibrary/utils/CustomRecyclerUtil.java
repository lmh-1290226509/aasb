package com.ddadai.baseviewlibrary.utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ddadai.baseviewlibrary.view.CustomScrollView;
import com.ddadai.baseviewlibrary.view.OnRefreshFooterListener;
import com.ddadai.baseviewlibrary.view.OnRefreshHeadListener;

import java.util.HashMap;
import java.util.Map;

public class CustomRecyclerUtil implements OnRefreshHeadListener, OnRefreshFooterListener {

	private CustomScrollView cs;
	private RecyclerView recyclerView;
	
	private Context mContextc;
	private Map<String,Object> params;
	private int page=1;
	public int pageSize=10;
	private boolean isRefresh = false;


	public static CustomRecyclerUtil factory(Context context, View rootView){
		CustomRecyclerUtil customRecyclerUtil=new CustomRecyclerUtil();
		customRecyclerUtil.init(context,rootView);
		return customRecyclerUtil;
	}



	public RecyclerView getRecyclerView(){
		return recyclerView;
	}
	private void init(Context context, View rootView){
		mContextc=context;
		if(rootView==null){
			return;
		}
		params=new HashMap<>();
		cs= (CustomScrollView) rootView.findViewById(com.ddadai.baseviewlibrary.R.id.cs);
		recyclerView= (RecyclerView) rootView.findViewById(com.ddadai.baseviewlibrary.R.id.recyclerView);
		
		LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
		linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerView.setLayoutManager(linearLayoutManager);
		
		
		
		cs.setModelName(context.getClass().getName());
		cs.setHeadView(false);
		cs.setFootView(true);
		
		cs.setOnRefreshHeadListener(this);
		cs.setOnRefreshFooterListener(this);
	}

	@Override
	public void headRefresh() {
		isRefresh = true;
		page = 1;

		params.put("Page_Index",page);
		if(onRefreshListener!=null){
			onRefreshListener.onRefresh(isRefresh,page,params);
		}
	}

	@Override
	public void refreshFooter() {
		isRefresh = false;
		page++;

		params.put("Page_Index",page);
		if(onRefreshListener!=null){
			onRefreshListener.onRefresh(isRefresh,page,params);
		}
	}

	public void refreshDone(){
		cs.pullShow();
	}


	public OnRefreshListener onRefreshListener;

	public CustomRecyclerUtil setOnRefreshListener(OnRefreshListener onRefreshListener){
		this.onRefreshListener=onRefreshListener;
		return this;
	}

	public CustomRecyclerUtil setHead(boolean head){
		cs.setHeadView(head);
		return this;
	}
	public CustomRecyclerUtil setFoot(boolean foot){
		cs.setFootView(foot);
		return this;
	}

	public interface OnRefreshListener{
		void onRefresh(boolean isRefresh, int page, Map<String, Object> params);
	}


}
