package com.ddadai.basehttplibrary.response;


import android.text.TextUtils;

import com.ddadai.basehttplibrary.HttpUtils;
import com.ddadai.basehttplibrary.interfaces.CheckSignInterface;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shi on 2017/2/8.
 */

public class FileCallBack extends BaseStringCallBack<JSONObject> {
    @Override
    protected void makeResponse(Response_<JSONObject> result, String response) {
        try {
            if(response!=null){
                char c = response.charAt(0);
                if(c=='"'){
                    response=response.substring(1,response.length()-1);
                }
                response= response.replace("\\","");
            }
//            Log.d("test", "makeResponse: response=="+response);
            JSONObject jsonObject = new JSONObject(response);
            result.data=jsonObject;
            if (result.data != null) {
                result.IsSuccess=!TextUtils.isEmpty(jsonObject.optString("saveid")) || !TextUtils.isEmpty(jsonObject.optString("savename"));
                result.msg = result.data.optString(HttpUtils.RSP_DESC);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected boolean checkSign(Response_<JSONObject> result, CheckSignInterface checkSignInterface) {
        return false;
    }

}
