package com.ddadai.baseviewlibrary.view;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import android.content.Context;
import android.support.v4.app.Fragment;

public class TabItem {

	/***这个item在数组中的位置，从0开始**/
	public int index;
	
	/**需要一个fragment的类名**/
	private Class<? extends Fragment> tagFragment;
	
	private Fragment fragment;
	
	
	public int imageSelectId;//图标被选择时候的资源文件
	public int imageDefaultId;//默认时候的资源文件
	
	public int textColorSelectId;//文字颜色，被选择的时候
	public int textColorDefaultId;//文字颜色，默认
	
	public String textLabel;//图标的文字
	
	
	/***直接传入整个xml的id */
	private Class<? extends TabView> tabView;
	
	
	public Fragment getFragment(){
		try {
			if(fragment==null){
				fragment = tagFragment.newInstance();
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return fragment;
	}
	
	
	
	
	public TabView getTabView(Context c){
		if(tabView==null){
			return null;
		}
		TabView tabview=null;
		try {
			Constructor<? extends TabView> constructor = tabView.getConstructor(Context.class);
			tabview=constructor.newInstance(c);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} finally {
		}
		return tabview;
	}
	
	
	
	public TabItem(int imageSelectId,int imageDefaultId,int textColorSelectId,int textColorDefaultId,String textLabel,Class<? extends TabView> tabView,Class<? extends Fragment> tagFragment){
		this.imageSelectId=imageSelectId;
		this.imageDefaultId=imageDefaultId;
		this.textColorSelectId=textColorSelectId;
		this.textColorDefaultId=textColorDefaultId;
		this.textLabel=textLabel;
		this.tabView=tabView;
		this.tagFragment=tagFragment;
	}
}
