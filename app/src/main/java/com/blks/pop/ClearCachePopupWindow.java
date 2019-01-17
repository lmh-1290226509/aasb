package com.blks.pop;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blks.antrscapp.R;

public class ClearCachePopupWindow extends PopupWindow{
	private View cacheView;
	private TextView tv_cache_cancel,tv_cache_continue;
	public ClearCachePopupWindow(Context context, OnClickListener cacheOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		cacheView = inflater.inflate(R.layout.clear_cache, null);
		tv_cache_cancel = (TextView) cacheView.findViewById(R.id.tv_cache_cancel);
		tv_cache_continue = (TextView) cacheView.findViewById(R.id.tv_cache_continue);
	
		// 取消按钮
		tv_cache_cancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// 销毁弹出框
				dismiss();
			}
		});
		// 设置按钮监听
		tv_cache_continue.setOnClickListener(cacheOnClick);
		// 设置SelectPicPopupWindow的View
		this.setContentView(cacheView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimBottom);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		cacheView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				int height = cacheView.findViewById(R.id.pop_cache).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});
	}

}
