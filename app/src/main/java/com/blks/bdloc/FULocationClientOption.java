package com.blks.bdloc;

import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

public class FULocationClientOption {

	private static LocationClientOption locOption;
	static {
		locOption = new LocationClientOption();
	}

	public static boolean isNeedAddress = true;
	public static int scanSpan = 3000;
	public static LocationMode locationMode = LocationMode.Hight_Accuracy;
	public static boolean isOpenGps = true;

	public static LocationClientOption getLocOption() {
		locOption.setCoorType("bd09ll");
		locOption.setIsNeedAddress(isNeedAddress);
		locOption.setScanSpan(scanSpan);
		locOption.setLocationMode(locationMode);
		locOption.setOpenGps(isOpenGps);
		locOption.setLocationNotify(true);
		locOption.setWifiCacheTimeOut(5*60*1000);
		locOption.setIsNeedLocationDescribe(true);
		return locOption;
	}

}
