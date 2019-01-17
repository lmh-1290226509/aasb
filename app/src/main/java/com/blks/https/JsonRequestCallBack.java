package com.blks.https;

import android.content.Context;

import com.ddadai.basehttplibrary.callback.SimpleRequestCallBack;
import com.ddadai.basehttplibrary.response.Response_;

import org.json.JSONObject;

public abstract class JsonRequestCallBack extends SimpleRequestCallBack<JSONObject> {
    Context context;
    public JsonRequestCallBack(Context context) {
        super(context);
        this.context=context;
    }

    @Override
    public void requestFail(String url, Response_<JSONObject> response) {
        super.requestFail(url, response);
//        ToastUtil.showShort(context,response.msg);
    }
}
