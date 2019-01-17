package com.ddadai.basehttplibrary.dialog;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.ddadai.basehttplibrary.R;


public class ProgressDialog extends android.app.ProgressDialog {

	public ProgressDialog(Context context) {
		super(context);
	}

	public ProgressDialog(Context context, int theme) {
		super(context, R.style.Theme_CustomDialog);
	}



	TextView messageTv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bal_dialog_progress);

		messageTv= (TextView) findViewById(R.id.message);
	}

//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		this.dismiss();
//		return super.onTouchEvent(event);
//	}

	@Override
	public void show() {
		super.show();
		if(messageTv!=null){
			messageTv.setText("正在努力加载中...      ");
		}
	}


	public void show(String msg){
		super.show();
		if(messageTv!=null){
			messageTv.setText(msg);
		}
	}

}
