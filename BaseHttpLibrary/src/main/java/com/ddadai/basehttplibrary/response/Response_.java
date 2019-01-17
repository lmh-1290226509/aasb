package com.ddadai.basehttplibrary.response;

import com.ddadai.basehttplibrary.utils.ShowErrorType;

import java.io.Serializable;

/**
 * Created by shi on 2017/1/12.
 */

public class Response_<T> implements Serializable {

    public String finalUrl;
    public String url;//请求的url
    public boolean IsSuccess;
    public String code="";//返回的代码
//    public String codeNo="";//返回的代码
    public String msg="";//返回的代码的解释
    public T  data;//返回的数据
    public ShowErrorType showErrorType;
    public boolean isShowMsg=false;


    public Response_() {
    }
}
