package com.blks.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

public class IntentUtil {

	public static void gotoAppSetting(Context context) {
		Intent localIntent = new Intent();
//		localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (Build.VERSION.SDK_INT >= 9) {
			localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
			localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
		} else {
			localIntent.setAction(Intent.ACTION_VIEW);
			localIntent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
			localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
		}
		context.startActivity(localIntent);
	}

}
