package com.ddadai.baseviewlibrary.utils;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RecyclerViewUitl {

	
	public static void setVertcal(RecyclerView rv){
		setLinearLayoutManager(rv, LinearLayoutManager.VERTICAL);
	}
	
	public static void setHorizontal(RecyclerView rv){
		setLinearLayoutManager(rv, LinearLayoutManager.HORIZONTAL);
	}
	
	public static void setGrid(RecyclerView rv,int spanCount){
		if(rv==null){
			return;
		}
		GridLayoutManager manager=new GridLayoutManager(rv.getContext(), spanCount);
		rv.setLayoutManager(manager);
	}
	
	
	
	private static void setLinearLayoutManager(RecyclerView rv,int orientation){
		if(rv==null){
			return;
		}
		LinearLayoutManager manager=new LinearLayoutManager(rv.getContext());
		manager.setOrientation(orientation);
		rv.setLayoutManager(manager);
	}











	/**
	 * Get center child in X Axes
	 */
	public static View getCenterXChild(RecyclerView recyclerView) {
		int childCount = recyclerView.getChildCount();
		if (childCount > 0) {
			for (int i = 0; i < childCount; i++) {
				View child = recyclerView.getChildAt(i);
				if (isChildInCenterX(recyclerView, child)) {
					return child;
				}
			}
		}
		return null;
	}

	/**
	 * Get position of center child in X Axes
	 */
	public static int getCenterXChildPosition(RecyclerView recyclerView) {
		int childCount = recyclerView.getChildCount();
		if (childCount > 0) {
			for (int i = 0; i < childCount; i++) {
				View child = recyclerView.getChildAt(i);
				if (isChildInCenterX(recyclerView, child)) {
					return recyclerView.getChildAdapterPosition(child);
				}
			}
		}
		return childCount;
	}

	/**
	 * Get center child in Y Axes
	 */
	public static View getCenterYChild(RecyclerView recyclerView) {
		int childCount = recyclerView.getChildCount();
		if (childCount > 0) {
			for (int i = 0; i < childCount; i++) {
				View child = recyclerView.getChildAt(i);
				if (isChildInCenterY(recyclerView, child)) {
					return child;
				}
			}
		}
		return null;
	}

	/**
	 * Get position of center child in Y Axes
	 */
	public static int getCenterYChildPosition(RecyclerView recyclerView) {
		int childCount = recyclerView.getChildCount();
		if (childCount > 0) {
			for (int i = 0; i < childCount; i++) {
				View child = recyclerView.getChildAt(i);
				if (isChildInCenterY(recyclerView, child)) {
					return recyclerView.getChildAdapterPosition(child);
				}
			}
		}
		return childCount;
	}

	public static boolean isChildInCenterX(RecyclerView recyclerView, View view) {
		int childCount = recyclerView.getChildCount();
		int[] lvLocationOnScreen = new int[2];
		int[] vLocationOnScreen = new int[2];
		recyclerView.getLocationOnScreen(lvLocationOnScreen);
		int middleX = lvLocationOnScreen[0] + recyclerView.getWidth() / 2;
		if (childCount > 0) {
			view.getLocationOnScreen(vLocationOnScreen);
			if (vLocationOnScreen[0] <= middleX && vLocationOnScreen[0] + view.getWidth() >= middleX) {
				return true;
			}
		}
		return false;
	}

	public static boolean isChildInCenterY(RecyclerView recyclerView, View view) {
		int childCount = recyclerView.getChildCount();
		int[] lvLocationOnScreen = new int[2];
		int[] vLocationOnScreen = new int[2];
		recyclerView.getLocationOnScreen(lvLocationOnScreen);
		int middleY = lvLocationOnScreen[1] + recyclerView.getHeight() / 2;
		if (childCount > 0) {
			view.getLocationOnScreen(vLocationOnScreen);
			if (vLocationOnScreen[1] <= middleY && vLocationOnScreen[1] + view.getHeight() >= middleY) {
				return true;
			}
		}
		return false;
	}
}
