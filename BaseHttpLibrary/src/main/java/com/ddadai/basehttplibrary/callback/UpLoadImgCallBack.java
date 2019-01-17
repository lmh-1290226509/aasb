//package com.ddadai.basehttplibrary.callback;
//
//import android.content.Context;
//
//import net.pingzhuo.yfgb.net.HttpUri;
//
//import org.json.JSONObject;
//
///**
// * Created by shi on 2017/3/14.
// */
//
//public abstract class UpLoadImgCallBack extends JsonRequestCallBack {
//    public UpLoadImgCallBack(Context context) {
//        super(context);
//    }
//
//    @Override
//    public void requestSuccess(HttpUri url, JSONObject jsonObject) {
//        requestSuccess(url,jsonObject.optString("data"));
//    }
//
//    public abstract void requestSuccess(HttpUri url,String img);
//}
