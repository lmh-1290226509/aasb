package com.ddadai.basehttplibrary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;

import com.ddadai.basehttplibrary.R;


/***
 * 请求服务器加载数据的dialog
 * @author smz
 *
 */
public class LoadingDialog extends Dialog {

	public LoadingDialog(Context context) {
		super(context, R.style.Theme_CustomDialog);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bal_dialog_activity);//布局跳转
		setCancelable(false);
	}

	
	

	@Override
	public void show() {
		if(isShowing())
		{
			cancel();
		}
		try {
			super.show();
		} catch (Exception e) {
			e.printStackTrace();
			return ;
		}
	}

	@Override
	public void cancel() {
		try {
			super.cancel();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean isCancel=true;

	public void setIsCancel(boolean isCancel){
		this.isCancel=isCancel;
	}
	/**
	 * 不处理返回键
	 * 
	 * @author wanggh
	 */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if((keyCode == KeyEvent.KEYCODE_BACK) && (event.getRepeatCount() == 0)){
//        	Log.d("xyb", "dialog onKeyDown");
        	if(isCancel&&this.isShowing()){
        		dismiss();
        	}
            return true;
        }
        return true;
    }

}
