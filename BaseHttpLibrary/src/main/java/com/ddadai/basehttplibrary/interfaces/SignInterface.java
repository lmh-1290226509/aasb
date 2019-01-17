package com.ddadai.basehttplibrary.interfaces;

import com.ddadai.basehttplibrary.HttpMap;

/**
 * Created by shi on 2017/11/18.
 */

public interface SignInterface {

//    boolean isOrder();
//
//    void order(Map<String,Object> map);

    String sign(HttpMap map, String reqssn);
}
