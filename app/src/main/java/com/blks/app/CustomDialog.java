package com.blks.app;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.blks.antrscapp.R;
import com.blks.utils.SystemUtils;

public class CustomDialog extends Dialog implements
		android.view.View.OnClickListener {

	Context context;
	/** 确定按钮 **/
	private Button confirmBtn;
	/** 取消按钮 **/
	private Button cancelBtn;
	//标题
	private TextView dialogTitle;
	//提示消息
	private TextView messageTv;

	private View contentView;
	private CustomDialogListener listener;
	private ViewGroup.LayoutParams LayoutParams;

	public CustomDialog(Context context) {
		super(context);
		this.context = context;
		init();
	}

	public interface CustomDialogListener {
		void onClick(View view);
	}

	public CustomDialog(Context context, int theme,
			CustomDialogListener listener) {
		super(context, theme);
		this.context = context;
		this.listener = listener;
		init();
	}

	public CustomDialog SetTitle (CharSequence title) {
		dialogTitle.setText(title);
		return this;
	}

	public CustomDialog SetMessage (CharSequence msg) {
		messageTv.setText(msg);
		return this;
	}

	public CustomDialog SetOkText(CharSequence msg) {
		confirmBtn.setText(msg);
		return this;
	}

	public CustomDialog SetCancelText(CharSequence msg) {
		cancelBtn.setText(msg);
		return this;
	}

	public CustomDialog SetCanceledOnTouchOutside (boolean b) {
		setCanceledOnTouchOutside(b);
		return this;
	}

	private void init() {
		contentView = LayoutInflater.from(context).inflate(R.layout.customdialog, null);
		// 根据id在布局中找到控件对象
		confirmBtn = (Button) contentView.findViewById(R.id.confirm_btn);
		cancelBtn = (Button) contentView.findViewById(R.id.cancel_btn);
		dialogTitle = contentView.findViewById(R.id.dialogTitle);
		messageTv = contentView.findViewById(R.id.messageTv);

		LayoutParams = new ViewGroup.LayoutParams((int) (SystemUtils.widthPs * 0.8), ViewGroup.LayoutParams.WRAP_CONTENT);

		// 为按钮绑定点击事件监听器
		confirmBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(contentView, LayoutParams);
	}

	@Override
	public void onClick(View v) {
		listener.onClick(v);
	}
}