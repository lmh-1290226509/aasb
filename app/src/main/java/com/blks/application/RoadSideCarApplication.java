package com.blks.application;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Gravity;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.blks.bdloc.FULocationService;
import com.blks.utils.CrashHandler;
import com.blks.utils.EUExUtil;
import com.blks.utils.LoginUtils;
import com.ddadai.basehttplibrary.HttpUtils;
import com.ddadai.basehttplibrary.request.BaseUrl;

import org.greenrobot.eventbus.EventBus;


public class RoadSideCarApplication extends Application {

	private static RoadSideCarApplication application;
	private FULocationService locationService;
	private Toast mToast;

	private int count = 0;

	private boolean TEST = true;

	@Override
	public void onCreate() {
		super.onCreate();
		application = this;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			initGlobeActivity();
		}
		SDKInitializer.initialize(this);
		locationService = new FULocationService(getApplicationContext());

		initHttp();

		initPhotoError();
		EUExUtil.init(this);

		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(this);
	}


	private void initPhotoError(){
		// android 7.0系统解决拍照的问题
		StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
		StrictMode.setVmPolicy(builder.build());
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
			builder.detectFileUriExposure();
		}
	}

	private void initHttp(){
		HttpUtils.init(this);
		HttpUtils.initBaseUrl(new BaseUrl() {
			//➢	测试环境地址：http://47.98.227.240:8072/HostPublic/
			//➢	上传文件地址：http://test-antrsc.bzmaster.cn/Handler/rscfileupload.ashx
			//➢	上传图像地址：http://test-antrsc.bzmaster.cn/Handler/ProtraitUpload.ashx
			//3.1.2 正式环境地址
			//➢	生产环境地址：http://118.31.228.108:8072/HostPublic/
			//➢	上传文件地址：http://antrsc.bzmaster.cn:8070/Handler/rscfileupload.ashx
			//➢	上传图像地址：http://antrsc.bzmaster.cn:8070/Handler/ProtraitUpload.ashx
			@Override
			public String getUrl() {
				if (TEST) {
					return "http://47.98.227.240:8072/HostPublic/"; //测试地址
				} else {
					return "http://118.31.228.108:8072/HostPublic/"; //正式地址
				}
			}

			@Override
			public String getHeadImgUrl() {
				if (TEST) {
					return "http://test-antrsc.bzmaster.cn/Handler/ProtraitUpload.ashx"; //测试地址
				} else {
					return "http://antrsc.bzmaster.cn:8070/Handler/ProtraitUpload.ashx"; //正式地址
				}
			}

			@Override
			public String getFileUrl() {
				if (TEST) {
					return "http://test-antrsc.bzmaster.cn/Handler/rscfileupload.ashx"; //测试地址
				} else {
					return "http://antrsc.bzmaster.cn:8070/Handler/rscfileupload.ashx";  //正式地址
				}
			}

			@Override
			public String getBaiduUrl() {
				return "http://yingyan.baidu.com/api/v3/track/addpoint";
			}

			@Override
			public String postImgInfoUrl() {
				if (TEST) {
					return "http://test-antrsc-openapi.bzmaster.cn:8073/RescueAppRSC/SaveRscPhotoInfo";
				} else {
					return "http://antrsc-openapi.bzmaster.cn:8073/RescueAppRSC/SaveRscPhotoInfo";
				}
			}
		});
	}

	public String HeadUrl() {
		if (LoginUtils.getLoginModel() != null) {
			String path;
			if (TEST) {
				path = "http://test-antrsc.bzmaster.cn/Upload/Protrait/" + LoginUtils.getLoginModel().USR_AVATAR_PATH;  //测试地址
			} else {
				path = "http://antrsc.bzmaster.cn:8070/Upload/Protrait/" + LoginUtils.getLoginModel().USR_AVATAR_PATH;  //正式地址
			}

			return path;
		}
		return null;
	}

	public boolean isTEST() {
		return TEST;
	}

	public static RoadSideCarApplication getInstance() {
		return application;
	}

	public FULocationService getLocationService() {
		return locationService;
	}

	public void showToast(String text){
		if(mToast == null){
			mToast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
			mToast.setGravity(Gravity.CENTER, 0, 0);
		}
		mToast.setText(text);
		mToast.show();
	}


	private void initGlobeActivity() {
		registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
			@Override
			public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
			}

			@Override
			public void onActivityDestroyed(Activity activity) {
			}

			/** Unused implementation **/
			@Override
			public void onActivityStarted(Activity activity) {
				if (count == 0) {
					//前台
					EventBus.getDefault().post(false);
				}
				count++;
			}

			@Override
			public void onActivityResumed(Activity activity) {
			}

			@Override
			public void onActivityPaused(Activity activity) {
			}

			@Override
			public void onActivityStopped(Activity activity) {
				count--;
				if (count == 0) {
					//后台
					EventBus.getDefault().post(true);
				}
			}

			@Override
			public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
			}
		});
	}


}
