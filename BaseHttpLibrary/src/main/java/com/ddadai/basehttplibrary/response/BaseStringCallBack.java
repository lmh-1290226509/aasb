package com.ddadai.basehttplibrary.response;


import android.os.Build;

import com.ddadai.basehttplibrary.HttpUtils;
import com.ddadai.basehttplibrary.interfaces.CheckSignInterface;
import com.ddadai.basehttplibrary.utils.HttpCode;
import com.lzy.okgo.callback.StringCallback;

import okhttp3.Headers;

/**
 * Created by shi on 2017/2/8.
 */

public abstract class BaseStringCallBack<T> extends StringCallback {

    protected HttpUtils.RequestBuilder_ postRequest_;


    public BaseStringCallBack() {
    }

    public void setPostRequest_(HttpUtils.RequestBuilder_ postRequest_) {
        this.postRequest_ = postRequest_;
    }

    @Override
    public void onStart(com.lzy.okgo.request.base.Request<String, ? extends com.lzy.okgo.request.base.Request> request) {
        super.onStart(request);
        try {
            if (postRequest_.httpCallBack != null) {
                postRequest_.httpCallBack.requestStart(postRequest_.uri, postRequest_.isShowDialog, postRequest_.dialogMsg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
        try {
            Headers headers = response.headers();
            if (postRequest_.httpCallBack != null) {
                Response_<T> result = new Response_();
                result.url = postRequest_.uri;
                result.finalUrl=postRequest_.uri;
                makeResponse(result,response.body());
                if (result.IsSuccess) {
                    //请求成功
                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT_WATCH){
                        postRequest_.httpCallBack.requestSuccess(postRequest_.uri, result);
//                        boolean b = checkSign(result, postRequest_.checkSignInterface);
//                        if(b){
//                            postRequest_.httpCallBack.requestSuccess(postRequest_.uri, result);
//                        }else{
//                            result.code=HttpCode.CHECK_SIG_FAIL;
//                            result.msg="验签失败";
//                            postRequest_.httpCallBack.requestFail(postRequest_.uri, result);
//                        }
                    }else{
                        postRequest_.httpCallBack.requestSuccess(postRequest_.uri, result);
                    }
                }
//                else if (result.code.equals(HttpCode.TO_LOGIN)
////                        &&result.codeNo.equals(HttpCode.TOKEN_NOT_USE)
//                        ) {
//                    //与服务器交互失败
//                    result.showErrorType=postRequest_.showErrorType;
//                    if(postRequest_.onLoginTimeOutListener!=null){
//                        boolean b = postRequest_.onLoginTimeOutListener.loginTimeOut(postRequest_.uri.toString(), result);
//                        if(!b){
//                            postRequest_.httpCallBack.requestFail(postRequest_.uri, result);
//                        }
//                    }else{
//                        postRequest_.httpCallBack.requestFail(postRequest_.uri, result);
//                    }
//                }
                else {
                    result.showErrorType=postRequest_.showErrorType;
                    //与服务器交互失败
                    postRequest_.httpCallBack.requestFail(postRequest_.uri, result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(com.lzy.okgo.model.Response<String> response) {
        super.onError(response);

        try {
            if (postRequest_.httpCallBack != null) {
                String message = "请求失败";
                if (response.getException() != null) message = response.getException() .getMessage();
                Response_<T> result = new Response_();
                result.url = postRequest_.uri;
                result.finalUrl=postRequest_.uri;
                if(message.contains("Failed to connect to ")){
                    result.msg="请检查网络";
                }else if(message.contains("Unable to resolve host")){
                    result.msg="请检查网络";
                }else{
                    result.msg=message;
                }
                result.code= HttpCode.NETWORK_ERROR;
                result.showErrorType=postRequest_.showErrorType;
                postRequest_.httpCallBack.requestFail(postRequest_.uri, result);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void onFinish() {
        super.onFinish();
        try {
            if (postRequest_.httpCallBack != null) {
                postRequest_.httpCallBack.requestFinish();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }


    protected abstract  void makeResponse(Response_<T> result, String response);
    protected abstract  boolean checkSign(Response_<T> result, CheckSignInterface checkSignInterface);
}
