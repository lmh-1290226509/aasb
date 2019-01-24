package com.blks.antrscapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blks.app.BaseActivity;
import com.blks.https.HttpUri;
import com.blks.https.JsonRequestCallBack;
import com.blks.pop.ClearCachePopupWindow;
import com.blks.pop.ModfiyPasswordPopupWindow;
import com.blks.utils.Constants;
import com.blks.utils.IntentUtil;
import com.blks.utils.LoginUtils;
import com.blks.utils.SharePreferenceUtil;
import com.blks.utils.ToastUtil;
import com.ddadai.basehttplibrary.HttpUtils;
import com.ddadai.basehttplibrary.response.Response_;

import org.json.JSONObject;

import java.io.File;

/**
 * @author shaoshuai
 *	通用
 */
public class SetActivity extends BaseActivity implements OnClickListener {

	private Context context;
	private LinearLayout ll_pwd, ll_cache;
	private ImageView set_back;
	private ModfiyPasswordPopupWindow modifyWindow;
	private ClearCachePopupWindow cacheWindow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_set);
		context = this;
		initView();
	}

	private void initView() {
		ll_pwd = (LinearLayout) findViewById(R.id.ll_pwd);
		ll_cache = (LinearLayout) findViewById(R.id.ll_cache);
		set_back = (ImageView) findViewById(R.id.set_back);
		set_back.setOnClickListener(this);
		ll_cache.setOnClickListener(this);
		ll_pwd.setOnClickListener(this);
		findViewById(R.id.ll_setting).setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.set, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.set_back:
				finish();
				break;
			case R.id.ll_pwd://修改密码
				modifyWindow = new ModfiyPasswordPopupWindow(SetActivity.this, new ModfiyPasswordPopupWindow.OnPwdChangeClick() {
					@Override
					public void pwdChangeClick(String oldPwd, String pwd) {
						savePwd(oldPwd, pwd);
					}
				});
				// 显示窗口
				modifyWindow.showAtLocation(
						((Activity) context).findViewById(R.id.set), Gravity.BOTTOM
								| Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
				break;
			case R.id.ll_cache:
				cacheWindow = new ClearCachePopupWindow(context, cacheOnClick);
				cacheWindow.showAtLocation(
						((Activity) context).findViewById(R.id.set), Gravity.BOTTOM
								| Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
				break;

			case R.id.ll_setting:
					IntentUtil.gotoAppSetting(mThis);
				break;

			default:
				break;
		}
	}


	private void savePwd(String oldPwd, String pwd){
		HttpUtils.get(HttpUri.RESET_PWD)
				.dialog(true)
				.data("usrPwdOld",oldPwd)
				.data("usrPwdNew",pwd)
				.data("usrId",LoginUtils.getLoginModel().USR_ID)
//				.mulKey("usrId")
				.callBack(new JsonRequestCallBack(mThis) {
					@Override
					public void requestSuccess(String url, JSONObject jsonObject) {
						ToastUtil.showShort(mThis,"修改成功");
						modifyWindow.dismiss();

						SharePreferenceUtil.remove(context, "passWord");

						LoginUtils.setLoginModel(null);//清空登录信息
						LoginUtils.setLoginStatus(Constants.LoginStatus.OFFLINE);//登录状态
						modifyUserState(Constants.UserStatus.OFFLINE);//用户状态
						Intent intent = new Intent(context, MainActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
								| Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
						finish();

					}

					@Override
					public void requestFail(String url, Response_<JSONObject> response) {
						super.requestFail(url, response);
						ToastUtil.showLong(context, response.msg);
					}
				})
				.request();
	}

	// 为弹出窗口实现监听类
	private OnClickListener cacheOnClick = new OnClickListener() {

		public void onClick(View v) {
			cacheWindow.dismiss();
			switch (v.getId()) {
			case R.id.tv_cache_continue:
				String imgs=Environment.getExternalStorageDirectory().getAbsolutePath()+"/resapp/";
				File imgFile=new File(imgs);
				deleteFile(imgFile);
//				if(imgFile.exists()){
//					File[] files = imgFile.listFiles();
//					if(files!=null){
//						for (File f:files) {
//							f.delete();
//						}
//					}
//				}
//				File amrFile=new File(AudioFileFunc.getAMRFilePath());
//				if(amrFile.exists()){
//					amrFile.delete();
//				}
//
//				File rawFile=new File(AudioFileFunc.getRawFilePath());
//				if(rawFile.exists()){
//					rawFile.delete();
//				}

//				File wavFile=new File(AudioFileFunc.getWavFilePath());
//				if(wavFile.exists()){
//					wavFile.delete();
//				}

				cacheWindow.dismiss();
				break;
			default:
				break;
			}
		}
	};

	private void deleteFile(File file) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				File f = files[i];
				deleteFile(f);
			}
			file.delete();//如要保留文件夹，只删除文件，请注释这行
		} else if (file.exists()) {
			file.delete();
		}
	}
}
