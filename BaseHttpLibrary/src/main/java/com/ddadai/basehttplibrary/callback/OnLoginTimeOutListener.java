package com.ddadai.basehttplibrary.callback;

import com.ddadai.basehttplibrary.response.Response_;

/**
 *
 *登录超时的接口
 * Created by shi on 2017/1/18.
 */

public interface OnLoginTimeOutListener<T> {

    /**
     *
     * @param url 超时的请求地址，非完整地址
     * @return 是否处理完成，false则是交给上级处理
     */
    boolean loginTimeOut(String url, Response_<T> t);
}
