package com.ddadai.basehttplibrary.utils;

/**
 * Created by shi on 2017/1/12.
 */

public interface HttpCode {


    /*** 响应成功的响应码 */
    String SUCESS="0000";

    /*** 验证签名失败 */
    String CHECK_SIG_FAIL="-9919";


    //跳转登陆页面的响应吗
    String TO_LOGIN="2";
//
    /*** 响应失败的响应码 */
    String FAIL="fail";
//
//
//    String TOKEN_NOT_USE="100201";



    /** 交易出现网络错误 */
    String NETWORK_ERROR = "-1";
}
