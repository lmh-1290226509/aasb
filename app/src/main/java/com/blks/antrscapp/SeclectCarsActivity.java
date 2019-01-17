package com.blks.antrscapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.blks.app.BaseActivity;
import com.blks.https.HttpUri;
import com.blks.https.JsonRequestCallBack;
import com.blks.model.CarModel;
import com.blks.model.UserInfoModel;
import com.blks.service.TaskService;
import com.blks.service.UpdateService;
import com.blks.utils.LoginUtils;
import com.blks.utils.ToastUtil;
import com.ddadai.basehttplibrary.HttpUtils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author shaoshuai 选择车辆
 */
public class SeclectCarsActivity extends BaseActivity implements
		OnClickListener {

	private Context context;
	private Spinner select_spinner;
	private Button btn_sure;
	private TextView tv_backLogin;
	private static Boolean isExit = false;

	private String car;
	private List<String> list;
	private List<CarModel> carModels;
	private ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_seclect_cars);
		context = this;
		initCtrol();
		requestCarCtrol();
		getUserInfo();
	}

	private void initCtrol() {
		select_spinner = (Spinner) findViewById(R.id.select_spinner);
        carModels = new ArrayList<>();
		list = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(context,
				R.layout.spinner_item, list);
		select_spinner.setAdapter(adapter);
		select_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
                car= (String) select_spinner.getSelectedItem();
				ToastUtil.showShort(context, "你选择了" + car);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		btn_sure = (Button) findViewById(R.id.btn_sure);
		btn_sure.setOnClickListener(this);
		tv_backLogin = (TextView) findViewById(R.id.tv_backLogin);
		tv_backLogin.setOnClickListener(this);
	}

	/**
	 * 获取车牌列表
	 */
	private void requestCarCtrol() {
		if (LoginUtils.getLoginModel() == null) {
			return;
		}

		HttpUtils.get(HttpUri.GET_RSC_SPCAR_LIST_BY_SPNO)
				.dialog(true)
				.data("supNo", LoginUtils.getLoginModel().SUP_NO)
				.mulKey("supNo")
				.callBack(new JsonRequestCallBack(this) {
					@Override
					public void requestSuccess(String url, JSONObject jsonObject) {
					    JSONArray array = jsonObject.optJSONArray("DataList");
                        Gson gson = new Gson();
					    if (array != null) {
                            for (int i = 0; i < array.length(); i++) {
                                CarModel model = gson.fromJson(array.optJSONObject(i).toString(), CarModel.class);
                                list.add(model.ASSETS_NO);
                                carModels.add(model);
                            }
                        }
                        adapter.notifyDataSetChanged();
					}
				})
				.request();

	}

	private void getUserInfo() {
		if (LoginUtils.getLoginModel() == null) {
			return;
		}

		HttpUtils.get(HttpUri.GET_USER_INFO_BY_KEY)
				.dialog(false)
				.data("usrId", LoginUtils.getLoginModel().USR_ID)
				.onlyKey("usrId")
				.callBack(new JsonRequestCallBack(this) {
					@Override
					public void requestSuccess(String url, JSONObject jsonObject) {
						UserInfoModel infoModel = new Gson().fromJson(jsonObject.toString(), UserInfoModel.class);
						if (infoModel != null && infoModel.DataList != null && infoModel.DataList.size() > 0) {
							LoginUtils.getLoginModel().USR_GRADE = infoModel.DataList.get(0).USR_GRADE;
						}
					}
				})
				.request();
	}

	/**
	 * 保存救援工与车辆的关系
	 */
	private void saveVehToWkr() {
		if (TextUtils.isEmpty(car)) {
			return;
		}

		HttpUtils.get(HttpUri.SAVE_VEH_TO_WKR)
				.dialog(true)
				.data("usrId", LoginUtils.getLoginModel().USR_ID)
				.data("carNo", car)
				.mulKey("usrId")
				.callBack(new JsonRequestCallBack(this) {
					@Override
					public void requestSuccess(String url, JSONObject jsonObject) {
						openActivity(HomePagerActivity.class);
						startService(new Intent(mThis,TaskService.class));
						startService(new Intent(mThis,UpdateService.class));
						LoginUtils.setCarModel(carModels.get(select_spinner.getSelectedItemPosition()));
						finish();
					}
				})
				.request();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.seclect_cars, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_sure:
			saveVehToWkr();
			break;
		case R.id.tv_backLogin:
			openActivity(MainActivity.class);
			finish();
			break;
		default:
			break;
		}
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
			ToastUtil.showShort(this, "再按一次退出程序");
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

		} else {
			finish();
			System.exit(0);
		}
	}
}
