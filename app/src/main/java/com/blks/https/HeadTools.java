package com.blks.https;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;

import com.blks.utils.MD5ChangeUtile;

public class HeadTools {

	public static Map<String, Object> GetSign(String param) {
		// Map<String, Object> map = new HashMap<String, Object>();
		String sign = "";

		long time = System.currentTimeMillis() / 1000;// 获取系统时间的10位的时间戳
		String str = String.valueOf(time);
		Log.i("dsfasdsa:", str + "");
		// /设定值
		String Timestamp = str; // 当前系统的时间戳
		String AppKey = "9zk6ZMQMSUYq16xqxYP8cQ==";
		String AppSecret = "+n06OaZlF0Yo16xPjp/0Ow==";
		String Authorization = "BTBPM " + "65a26dc72ad373ert44976bbafd6e3da";
		String SignMethod = "md5";
		// param = "{\"key\":\"123\"}";
		String strConcat = "";
		// concat($AppSecret,
		// "App-Key",$App-Key,
		// "Authorization",$Authorization,
		// "Sign-Method",$Sign-Method,
		// "Timestamp",$ Timestamp,
		// "param",$param,
		// $AppSecret)
		strConcat = AppSecret + "App-Key" + AppKey + "Authorization"
				+ Authorization + "param" + param + "Sign-Method" + SignMethod
				+ "Timestamp" + Timestamp + AppSecret;
		Log.i("strConcat:", strConcat);
		sign = MD5ChangeUtile.Md5_32(strConcat);
		// /拼接headers
		Map<String, Object> header = new HashMap<String, Object>();

		header.put("Authorization", Authorization);
		header.put("App-Key", AppKey);
		header.put("Timestamp", Timestamp);
		header.put("Sign-Method", SignMethod);
		header.put("Sign", sign.toUpperCase());

		System.out.print(sign);
		Log.i("32456789:", sign);
		return header;
	}

}
