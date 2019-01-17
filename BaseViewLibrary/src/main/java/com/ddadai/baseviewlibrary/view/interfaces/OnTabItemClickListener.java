package com.ddadai.baseviewlibrary.view.interfaces;

public interface OnTabItemClickListener {
	void onTabItemClick(int postion,TabViewItem item);
	
	
	public static final class TabViewItem{
		public TabViewItem() {
		};
		public TabViewItem(String text) {
			this.text=text;
		};
		public String text;
	}
}
