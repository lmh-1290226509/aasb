package com.ddadai.basehttplibrary.utils;

/**
 * Created by shi on 2017/1/19.
 */

public class CheckAlive {

    private boolean isAlive=true;
    private boolean isClose=false;

    public boolean isAlive() {
        return isAlive;
    }

    public boolean isClose() {
        return isClose;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public void setClose(boolean close) {
        isClose = close;
    }
}
