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

import java.util.Timer;
import java.util.TimerTask;

import static com.blks.utils.LoginUtils.canRequest;

public class HomePagerActivity extends BaseActivity implements OnClickListener {

	private View ll_home, ll_list, ll_my;
	private ImageView iv_home, iv_list, iv_my;// 首页 // 列表// 我
	private TextView tv_home, tv_list, tv_my;
	private int index = -1;
	private Boolean isExit = false;
	private Fragment homeFragment, customerListFragment, myFragment;

	private WifiManager wifiManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_home_pager);
		initView();// 初始化数据
	}

	@Override
	protected void onResume() {
		super.onResume();
		canRequest = true;
	}

	private void initView() {
		iv_home = (ImageView) findViewById(R.id.iv_home);
		iv_list = (ImageView) findViewById(R.id.iv_list);
		iv_my = (ImageView) findViewById(R.id.iv_my);
		tv_home = (TextView) findViewById(R.id.tv_home);
		tv_list = (TextView) findViewById(R.id.tv_list);
		tv_my = (TextView) findViewById(R.id.tv_my);

		ll_home = findViewById(R.id.ll_home);
		ll_list = findViewById(R.id.ll_list);
		ll_my = findViewById(R.id.ll_my);
		ll_home.setOnClickListener(this);
		ll_list.setOnClickListener(this);
		ll_my.setOnClickListener(this);

		int ids = getIntent().getIntExtra("bar_id", 0);
		if (ids == 2) {
			onClick(ll_list);
		} else {
			onClick(ll_list);
//			if (index == 0) {
				// 默认显示第一页
				onClick(ll_home);
//			}
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_home:
			if (index == 0) {
				return;
			}
			hideFragments();
			resetBottomButton();
			index = 0;
			iv_home.setBackgroundResource(R.drawable.icon_home);
			tv_home.setTextColor(getResources()
					.getColor(R.color.home_actionBar));

			if (homeFragment == null){
				homeFragment = new HomeFragment();// 首页
				getSupportFragmentManager().beginTransaction()
						.add(R.id.content_frame, homeFragment).commit();
			} else {
				getSupportFragmentManager().beginTransaction().show(homeFragment)
						.commit();
			}
			break;
		case R.id.ll_list:
			if (index == 1) {
				return;
			}
			hideFragments();
			resetBottomButton();
			index = 1;
			iv_list.setBackgroundResource(R.drawable.icon_list);
			tv_list.setTextColor(getResources()
					.getColor(R.color.home_actionBar));

			if (customerListFragment == null){
				customerListFragment = new CustomerListFragment();// 列表
				getSupportFragmentManager().beginTransaction()
						.add(R.id.content_frame, customerListFragment).commit();
			} else {
				getSupportFragmentManager().beginTransaction().show(customerListFragment)
						.commit();
			}
			break;
		case R.id.ll_my:
			if (index == 2) {
				return;
			}
			hideFragments();
			resetBottomButton();
			index = 2;
			iv_my.setBackgroundResource(R.drawable.icon_my);
			tv_my.setTextColor(getResources().getColor(R.color.home_actionBar));

			if (myFragment == null){
				myFragment = new MyFragment();// 我
				getSupportFragmentManager().beginTransaction()
						.add(R.id.content_frame, myFragment).commit();
			} else {
				getSupportFragmentManager().beginTransaction().show(myFragment)
						.commit();
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 隐藏fragment
	 *
	 */
	private void hideFragments() {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

		if (homeFragment != null) {
			transaction.hide(homeFragment);
		}
		if (customerListFragment != null) {
			transaction.hide(customerListFragment);
		}
		if (myFragment != null) {
			transaction.hide(myFragment);
		}
		transaction.commit();
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
