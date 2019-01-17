package com.ddadai.basehttplibrary;


import com.ddadai.basehttplibrary.request.BaseRequest_;
import com.ddadai.basehttplibrary.response.BaseStringCallBack;
import com.ddadai.basehttplibrary.response.JsonCallBack;

/**
 * Created by shi on 2017/2/8.
 */

public class JsonPost extends BaseRequest_ {

    @Override
    public BaseStringCallBack getCallBack() {
        return new JsonCallBack();
    }
}
