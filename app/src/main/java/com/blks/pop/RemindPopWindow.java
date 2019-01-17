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

public class RemindPopWindow extends PopupWindow {
	private View cacheView;
	private TextView title_pop, message_pop, tv_remind_cancel, tv_remind_continue;

	public RemindPopWindow(Context context, OnClickListener cacheOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		cacheView = inflater.inflate(R.layout.remind_pop, null);
		title_pop = (TextView) cacheView.findViewById(R.id.title_pop);
		message_pop = (TextView) cacheView.findViewById(R.id.message_pop);
		tv_remind_cancel = (TextView) cacheView
				.findViewById(R.id.tv_remind_cancel);
		tv_remind_continue = (TextView) cacheView
				.findViewById(R.id.tv_remind_continue);

		// 取消按钮
		// tv_record_cancel.setOnClickListener(new OnClickListener() {
		//
		// public void onClick(View v) {
		// // 销毁弹出框
		// dismiss();
		// }
		// });
		// 设置按钮监听
		tv_remind_cancel.setOnClickListener(cacheOnClick);
		tv_remind_continue.setOnClickListener(cacheOnClick);
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
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框

		this.setOutsideTouchable(false);

		cacheView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				int height = cacheView.findViewById(R.id.pop_remind).getTop();
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

	public void setCacheGone() {
		tv_remind_cancel.setVisibility(View.GONE);
	}

	public void setTitlePop(CharSequence str) {
		title_pop.setText(str);
	}

	public void setMessagePop(CharSequence str) {
		message_pop.setText(str);
	}

}
