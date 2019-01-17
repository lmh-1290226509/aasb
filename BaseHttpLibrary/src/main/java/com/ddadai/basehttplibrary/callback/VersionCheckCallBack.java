package com.ddadai.basehttplibrary.callback;


import com.ddadai.basehttplibrary.response.Response_;

/**
 * Created by shi on 2017/2/8.
 */

public interface VersionCheckCallBack<T> {

    /**
     *
     * @param url 超时的请求地址，非完整地址
     * @return 是否处理完成，false则是交给上级处理
     */
    boolean versionOld(String url, Response_<T> t);
}
