package com.blks.antrscapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.Window;

import com.blks.app.BaseActivity;

public class WelcomeActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		if (!isTaskRoot()
				&& intent != null
				&& intent.hasCategory(Intent.CATEGORY_LAUNCHER)
				&& intent.getAction() != null
				&& intent.getAction().equals(Intent.ACTION_MAIN)) {
			finish();
			return;
		}
		if((getIntent().getFlags()&Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)!=0){
			//结束你的activity
			finish();
			return;
		}

		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// /** 标题是属于View的，所以窗口所有的修饰部分被隐藏后标题依然有效,需要去掉标题 **/

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_welcome);
		initView();
		handler.sendEmptyMessageDelayed(0, 3000);
	}

	private void initView() {

	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			getHome();
			super.handleMessage(msg);
		}
	};

	public void getHome() {
		openActivity(MainActivity.class);
		overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome, menu);
		return true;
	}

}
