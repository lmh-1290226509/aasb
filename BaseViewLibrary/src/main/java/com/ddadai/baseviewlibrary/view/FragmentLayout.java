package com.ddadai.baseviewlibrary.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.ddadai.baseviewlibrary.R;
import com.ddadai.baseviewlibrary.utils.ViewUtils;
import com.ddadai.baseviewlibrary.view.TabLayout.OnTabClickListener;

import java.util.List;

/**
 * 这个是可以用viewpager做容器来显示fragment
 * 就是支持滑动跟点击来实现显示fragment
 * 业务上可能会遇到需要控制那几个fragment在特定的情况下不能显示的
 * 例如在首页，一些界面只有在登录的时候才能查看
 * 这个时候 滑动没有做处理。。
 * 所以只能把滑动先禁用掉，等到登录的时候再开启可以滑动的模式
 * */
public class FragmentLayout extends RelativeLayout implements OnTabClickListener{

	private EnableViewPager viewPager;
	private TabLayout tablayout;
	
	private boolean canScroll=false;//控制能不能使用viewpager滑动来显示页面
	
	public FragmentLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public FragmentLayout(Context context) {
		super(context);
		initView();
	}
	
	
	private void initView(){
		LayoutInflater.from(getContext()).inflate(R.layout.view_fragment_layout, this,true);
		viewPager=(EnableViewPager) findViewById(R.id.viewPager);
		tablayout=(TabLayout) findViewById(R.id.tablayout);
	}
	
	
	
	public void initData(final List<TabItem> tabs,FragmentManager fm){
		tablayout.initData(tabs, this);
		viewPager.setAdapter(new FragmentAdapter(fm, tabs));
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				Log.d("test", arg0+"");
				setTab(arg0);
//				tablayout.setCurrentTab(arg0);	
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		viewPager.setScrollEnable(canScroll);
		setCacheFragment(tabs.size()-1);
	}
	
	private class FragmentAdapter extends FragmentPagerAdapter{

		private List<TabItem> tabs;
//		private Map<Integer, Fragment> fragmentCache;
		public FragmentAdapter(FragmentManager fm,List<TabItem> tabs) {
			super(fm);
			this.tabs=tabs;
//			fragmentCache=new HashMap<Integer, Fragment>();
		}

		@Override
		public Fragment getItem(int arg0) {
//			Fragment fragment=null;
//			if(fragmentCache.get(arg0)==null){
//				fragment=tabs.get(arg0).getFragment();
//				fragmentCache.put(arg0, fragment);
//			}else{
//				fragment=fragmentCache.get(arg0);
//			}
//			return fragment;
			return tabs.get(arg0).getFragment();
		}

		@Override
		public int getCount() {
			return tabs==null?0:tabs.size();
		}
	}
	
	public void setTab(int index){
		tablayout.setCurrentTab(index);	
	}

	@Override
	public boolean onTabClick(TabItem tabItem) {
		if(mOnTabSelectListener==null){
			viewPager.setCurrentItem(tabItem.index);
			return true;
		}
		if(mOnTabSelectListener.getSelectIndex(tabItem)){
			viewPager.setCurrentItem(tabItem.index);
			return true;
		}else{
			return false;
		}
	}
	
	
	public void setCanSmooth(boolean isCanSmooth){
		this.canScroll=isCanSmooth;
		viewPager.setScrollEnable(canScroll);
	}

	
	public OnTabSelectListener mOnTabSelectListener;
	public void setOnTabSelectListener(OnTabSelectListener l){
		mOnTabSelectListener=l;
	}
	
	public static interface OnTabSelectListener{
		boolean getSelectIndex(TabItem item);
	}
	
	public void setBottomHeight(int dp){
		int px = ViewUtils.dipToPx(getContext(), dp);
		tablayout.getLayoutParams().height=px;
	}
	
	public void setCacheFragment(int count){
		viewPager.setOffscreenPageLimit(count);
	}
}
