package com.blks.utils;

public class Constants {

    public interface LoginStatus {
        String OFFLINE = "0";
        String ONLINE = "1";
    }

    /**
     * 0-离线/1-在线/2-待命/3-离开/4-繁忙
     */
    public interface UserStatus {
        String OFFLINE = "0";
        String ONLINE = "1";
        String READY = "2";
        String BREAK = "3";               //离开
        String BUSY = "4";
    }

    public interface OrderStatus {

        int TYPE_0 = 0;//新单
        int TYPE_1 = 1;//已接单
        int TYPE_2 = 2;//已出发
        int TYPE_3 = 3;//已到达 拖车出发 拖车到达
        int TYPE_4 = 4;//已拍照
        int TYPE_5 = 5;//已完成

    }

    public interface OrderAction {
        int ADD = 1;
        int REMOVE = 2;
        int UPDATE = 3;
    }

    public static final String _MODEL = "_model";
    public static final String _WO_NO = "_wo_no";
    public static final String _OUT_SOURCE_NO = "_out_no";
    public static final String _KEY_B = "_bool";
    public static final String _KEY_STR = "_str_key";
    public static final String _KEY_INT = "_int_key";

}
