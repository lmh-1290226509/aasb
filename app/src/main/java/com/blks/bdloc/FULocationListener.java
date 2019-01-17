package com.blks.bdloc;

import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.blks.application.RoadSideCarApplication;

public class FULocationListener extends BDAbstractLocationListener {
	private BDLocation location;

	@Override
	public void onReceiveLocation(BDLocation bdLocation) {
		location = bdLocation;
//		if (BuildConfig.LOG_DEBUG) {
//			print();
//		}
	}

	@Override
	public void onConnectHotSpotMessage(String s, int i) {
		super.onConnectHotSpotMessage(s, i);
//		if (BuildConfig.LOG_DEBUG) {
//			Log.d("test", "onConnectHotSpotMessage: "+s);
//			print();
//		}
	}

	@Override
	public void onLocDiagnosticMessage(int locType, int diagnosticType, String diagnosticMessage) {
		super.onLocDiagnosticMessage(locType, diagnosticType, diagnosticMessage);
		switch (diagnosticType) {
			case LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_CHECK_LOC_PERMISSION:
				RoadSideCarApplication.getInstance().showToast("定位失败，请授予定位权限");
				break;
			case LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_CHECK_NET:
				RoadSideCarApplication.getInstance().showToast("网络异常，定位失败");
				break;
			case LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_CLOSE_FLYMODE:
				RoadSideCarApplication.getInstance().showToast("定位失败，建议关闭飞行模式");
				break;
			case LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_OPEN_PHONE_LOC_SWITCH:
				RoadSideCarApplication.getInstance().showToast("定位失败，建议打开手机设置里的定位开关后重试！");
				break;
			case LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_INSERT_SIMCARD_OR_OPEN_WIFI:
				RoadSideCarApplication.getInstance().showToast("定位失败，建议打开wifi或者插入sim卡重试！");
				break;
			case LocationClient.LOC_DIAGNOSTIC_TYPE_SERVER_FAIL:
				RoadSideCarApplication.getInstance().showToast("定位失败！");
				break;
			case LocationClient.LOC_DIAGNOSTIC_TYPE_BETTER_OPEN_GPS:
				if(location == null)
					RoadSideCarApplication.getInstance().showToast("打开GPS定位更精确");
				break;
		}
	}

	private void print() {
		StringBuffer sb = new StringBuffer(256);

		sb.append("code");
		sb.append(location.getLocType());

		sb.append("\nLocationType:");
		sb.append(location.getNetworkLocationType());

		sb.append("\nProvince:");
		sb.append(location.getProvince());

		sb.append("\ncity:");
		sb.append(location.getCity());

		sb.append("\nDistrict:");
		sb.append(location.getDistrict());

		sb.append("\nStreet:");
		sb.append(location.getStreet());

		sb.append("\nStreetNumber:");
		sb.append(location.getStreetNumber());

		sb.append("\naddstr:");
		sb.append(location.getAddrStr());

		sb.append("\nLatitude:");
		sb.append(location.getLatitude());

		sb.append("\nLongitude:");
		sb.append(location.getLongitude());

		Log.i("test", sb.toString());
	}

}
