package com.ddadai.basehttplibrary.callback;


import com.ddadai.basehttplibrary.response.Response_;

/**
 * Created by shi on 2017/1/12.
 */

public interface OnRequestCallBack_<T> {

    //请求开始
    void requestStart(String url, boolean isShowDialog, String dialogMsg);

    //请求成功
    void requestSuccess(String url, Response_<T> response);

    //请求失败
    void requestFail(String url, Response_<T> response);


    //不管失败或者成功，都会调用这个方法
    void requestFinish();

}
