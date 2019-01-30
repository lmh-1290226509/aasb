package com.blks.utils;

import com.blks.model.CarModel;
import com.blks.model.LoginModel;

public class LoginUtils {

    private static LoginModel.DataListModel loginModel;
    private static CarModel carModel;
    private static String loginStatus = "0";//0-离线/1-在线
    private static String userStatus = "0"; //0-离线/1-在线/2-待命/3-离开/4-繁忙

    public static boolean isNetwork;
    public static  boolean canRequest = true;

    public static void setLoginModel(LoginModel.DataListModel loginModel){
        LoginUtils.loginModel=loginModel;
        isNetwork = true;
    }

    public static LoginModel.DataListModel getLoginModel(){
        return loginModel;
    }

    public static void setCarModel(CarModel model) {
        carModel = model;
    }

    public static CarModel getCarModel() {
        return carModel;
    }

    public static String getLoginStatus() {
        return loginStatus;
    }

    public static void setLoginStatus(String loginStatus) {
        LoginUtils.loginStatus = loginStatus;
    }

    public static String getUserStatus() {
        return userStatus;
    }

    public static void setUserStatus(String userStatus) {
        LoginUtils.userStatus = userStatus;
    }
}
