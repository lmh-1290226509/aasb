package com.blks.antrscapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blks.app.BaseActivity;
import com.blks.fragment.CustomerListFragment;
import com.blks.fragment.HomeFragment;
import com.blks.fragment.MyFragment;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class HomePagerActivity extends BaseActivity implements OnClickListener {

	private Context context;
	private ImageView iv_home;// 首页
	private TextView tv_home;
	private ImageView iv_list;// 列表
	private TextView tv_list;
	private ImageView iv_my;// 我
	private TextView tv_my;
	private ArrayList<Fragment> fragmentList;
	private int index = 0;
	private int currenTab;
	private Boolean isExit = false;
	private HomeFragment homeFragment;
	private CustomerListFragment customerListFragment;
	private MyFragment myFragment;

	private WifiManager wifiManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_home_pager);
		context = this;
		if (savedInstanceState != null) {
			getSupportFragmentManager().popBackStackImmediate(null, 1);
		}
		initView();// 初始化数据
	}

	private void initView() {
		iv_home = (ImageView) findViewById(R.id.iv_home);
		iv_list = (ImageView) findViewById(R.id.iv_list);
		iv_my = (ImageView) findViewById(R.id.iv_my);
		tv_home = (TextView) findViewById(R.id.tv_home);
		tv_list = (TextView) findViewById(R.id.tv_list);
		tv_my = (TextView) findViewById(R.id.tv_my);

		findViewById(R.id.ll_home).setOnClickListener(this);
		findViewById(R.id.ll_list).setOnClickListener(this);
		findViewById(R.id.ll_my).setOnClickListener(this);

		fragmentList = new ArrayList<Fragment>();
		homeFragment = new HomeFragment();// 首页
		customerListFragment = new CustomerListFragment();// 列表
		myFragment = new MyFragment();// 我

		fragmentList.add(homeFragment);
		fragmentList.add(customerListFragment);
		fragmentList.add(myFragment);


		int ids = getIntent().getIntExtra("bar_id", 0);
		if (ids == 2) {
			resetBottomButton();
			iv_list.setBackgroundResource(R.drawable.icon_list);
			tv_list.setTextColor(getResources()
					.getColor(R.color.home_actionBar));
			showTab(1);
		} else {

			showTab(1);
			if (index == 0) {
				// 默认显示第一页
				resetBottomButton();
				iv_home.setBackgroundResource(R.drawable.icon_home);
				tv_home.setTextColor(getResources()
						.getColor(R.color.home_actionBar));
				showTab(index);
			}
		}


		//位置采集周期
		// 在Android 6.0及以上系统，若定制手机使用到doze模式，请求将应用添加到白名单。
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			String packageName = getPackageName();
			boolean isIgnoring = powerManager.isIgnoringBatteryOptimizations(packageName);
			if (!isIgnoring) {
				Intent intent = new Intent(
						Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
				intent.setData(Uri.parse("package:" + packageName));
				try {
					startActivity(intent);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

		wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
		if (wifiManager != null && !wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(true);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_pager, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_home:
			resetBottomButton();
			iv_home.setBackgroundResource(R.drawable.icon_home);
			tv_home.setTextColor(getResources()
					.getColor(R.color.home_actionBar));
			index = 0;
			showTab(index);
			break;
		case R.id.ll_list:
			resetBottomButton();
			iv_list.setBackgroundResource(R.drawable.icon_list);
			tv_list.setTextColor(getResources()
					.getColor(R.color.home_actionBar));
			index = 1;
			showTab(index);
			break;
		case R.id.ll_my:
			resetBottomButton();
			iv_my.setBackgroundResource(R.drawable.icon_my);
			tv_my.setTextColor(getResources().getColor(R.color.home_actionBar));
			index = 2;
			showTab(index);
			break;
		default:
			break;
		}
	}

	private void show(int paramInt) {
		for (int i = 0; i < fragmentList.size(); i++) {
			Fragment fragment = fragmentList.get(i);
			FragmentTransaction fragmentTransaction = getSupportFragmentManager()
					.beginTransaction();
			if (paramInt == i) {
				fragmentTransaction.show(fragment);

			} else {
				fragmentTransaction.hide(fragment);
			}
			fragmentTransaction.commit();
		}
		currenTab = paramInt;
	}

	private void showTab(int idx) {
		Fragment fragment = fragmentList.get(idx);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		getCurrentFragment().onPause();
		if (fragment.isAdded()) {
			fragment.onResume();
		} else {
			ft.add(R.id.content_frame, fragment);
		}
		show(idx);

		ft.commit();
	}

	public Fragment getCurrentFragment() {
		return fragmentList.get(currenTab);
	}

	private void resetBottomButton() {
		iv_home.setBackgroundResource(R.drawable.icon_home_un);
		iv_list.setBackgroundResource(R.drawable.icon_list_un);
		iv_my.setBackgroundResource(R.drawable.icon_my_un);

		tv_home.setTextColor(getResources().getColor(R.color.actionBar_gray));
		tv_list.setTextColor(getResources().getColor(R.color.actionBar_gray));
		tv_my.setTextColor(getResources().getColor(R.color.actionBar_gray));
	}

	/**
	 * 菜单、返回键响应
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitBy2Click(); // 调用双击退出函数
		}
		return false;
	}

	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // 准备退出
			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

		} else {
			// PreferencesUtil.setPreferences(this, "isFresh", "0");
			finish();
			System.exit(0);
		}
	}
}
