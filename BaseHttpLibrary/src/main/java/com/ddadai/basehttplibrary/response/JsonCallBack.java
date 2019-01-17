package com.ddadai.basehttplibrary.response;


import android.util.Log;

import com.ddadai.basehttplibrary.HttpUtils;
import com.ddadai.basehttplibrary.interfaces.CheckSignInterface;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shi on 2017/2/8.
 */

public class JsonCallBack extends BaseStringCallBack<JSONObject> {
    @Override
    protected void makeResponse(Response_<JSONObject> result, String response) {
        try {
            if(response!=null){
                char c = response.charAt(0);
                if(c=='"'){
                    response=response.substring(1,response.length()-1);
//                    Log.d("test", "makeResponse: response=="+response);
                }
                response= response.replace("\\","");
            }
//            Log.d("test", "makeResponse: response=="+response);
            JSONObject jsonObject = new JSONObject(response);
            result.data=jsonObject;
            if (result.data != null) {
                result.IsSuccess=jsonObject.optBoolean("IsSuccess");
//                result.code = result.data.optString(HttpUtils.RSP_CODE);
//                if(!result.code.equals(HttpCode.SUCESS)){
////                    result.codeNo = result.data.optString("resultNo");
//                }
                result.msg = result.data.optString(HttpUtils.RSP_DESC);
//                result.isShowMsg=result.data.optBoolean("Message_Is_Show",false);
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
