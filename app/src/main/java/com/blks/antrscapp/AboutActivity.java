package com.blks.antrscapp;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blks.app.BaseActivity;
import com.blks.utils.ApkUpdateUtils;

public class AboutActivity extends BaseActivity implements OnClickListener {

	private Context context;
	private ImageView about_back;
	private LinearLayout ll_about_our, ll_about_checkUpdate;

	private TextView versionTv;
	private TextView version1Tv;
	private ApkUpdateUtils apkUpdateUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_about);
		context = this;
		initView();

	}

	private void initView() {
		apkUpdateUtils = new ApkUpdateUtils(this, true);

		versionTv = (TextView) findViewById(R.id.versionTv);
		version1Tv = (TextView) findViewById(R.id.version1Tv);

		about_back = (ImageView) findViewById(R.id.about_back);
		ll_about_our = (LinearLayout) findViewById(R.id.ll_about_our);
		ll_about_checkUpdate = (LinearLayout) findViewById(R.id.ll_about_checkUpdate);
		ll_about_checkUpdate.setOnClickListener(this);
		ll_about_our.setOnClickListener(this);
		about_back.setOnClickListener(this);

		version1Tv.setText("");
		try {
			PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			if(packageInfo!=null){
				versionTv.setText(packageInfo.versionName);

			}
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.about, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.about_back:
			finish();
			break;
		case R.id.ll_about_our:
			openActivity(AboutOurActivity.class);
			break;
		case R.id.ll_about_checkUpdate:
			apkUpdateUtils.checkVersionRequest();
//
			break;
		default:
			break;
		}
	}

}
