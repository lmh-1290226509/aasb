package com.ddadai.basehttplibrary.response;

import android.util.Log;

import com.ddadai.basehttplibrary.interfaces.CheckSignInterface;

import org.json.JSONObject;

public class TextCallBack extends BaseStringCallBack<JSONObject> {
    @Override
    protected void makeResponse(Response_<JSONObject> result, String response) {
        try {
//            Log.d("test", "makeResponse: response=="+response);
            JSONObject jsonObject = new JSONObject();
            result.data=jsonObject;
            if (result.data != null) {
                result.IsSuccess=Boolean.parseBoolean(response);
                result.data.put("text",response);
                jsonObject.put("IsSuccess",result.IsSuccess);
                result.msg = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected boolean checkSign(Response_<JSONObject> result, CheckSignInterface checkSignInterface) {
        return false;
    }
}
