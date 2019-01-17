package com.blks.pop;

import com.blks.antrscapp.R;
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

public class RecordCachePopupWindow extends PopupWindow{
	private View cacheView;
	private TextView tv_record_cancel,tv_record_continue;
	public RecordCachePopupWindow(Context context, OnClickListener cacheOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		cacheView = inflater.inflate(R.layout.record_keep, null);
		tv_record_cancel = (TextView) cacheView.findViewById(R.id.tv_record_cancel);
		tv_record_continue = (TextView) cacheView.findViewById(R.id.tv_record_continue);
	
		// 取消按钮
//		tv_record_cancel.setOnClickListener(new OnClickListener() {
//
//			public void onClick(View v) {
//				// 销毁弹出框
//				dismiss();
//			}
//		});
		// 设置按钮监听
		tv_record_cancel.setOnClickListener(cacheOnClick);
		tv_record_continue.setOnClickListener(cacheOnClick);
		// 设置SelectPicPopupWindow的View
		this.setContentView(cacheView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimBottom);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);

		this.setOutsideTouchable(false);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		cacheView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				int height = cacheView.findViewById(R.id.pop_record).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
//						dismiss();
					}
				}
				return true;
			}
		});
	}
}
