//package com.blks.app;
//
//import android.app.Application;
//
//import com.google.gson.Gson;
//import com.loonandroid.pc.internet.InternetConfig;
//
//public class App extends Application {
//
//	private static App instance;
//	public static String AppFilePath = "app";
//	public static InternetConfig config = InternetConfig.defaultConfig();
//	private Gson gson;
//
//	@Override
//	public void onCreate() {
//		instance = this;
//		super.onCreate();
//		gson = new Gson();
//
//	}
//
//
//
//	public static App getInstance() {
//		return instance;
//	}
//
//	public Gson getGson() {
//		return gson;
//	}
//
//}
