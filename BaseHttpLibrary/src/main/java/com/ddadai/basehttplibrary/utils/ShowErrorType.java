package com.ddadai.basehttplibrary.utils;

/**
 * Created by shi on 2017/2/8.
 */

public enum  ShowErrorType {

    NO_SHOW(0),
    SHOW_TOAST(1),
    SHOW_DIALOG(2)

    ;


    int value;

    ShowErrorType(int valule){
        this.value=valule;
    }
}
