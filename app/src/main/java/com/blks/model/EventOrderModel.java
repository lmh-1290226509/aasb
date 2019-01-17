package com.blks.model;

import java.io.Serializable;

/**
 * EventBus传递工单
 */
public class EventOrderModel implements Serializable {

    public EventOrderModel() {
    }

    public EventOrderModel(String option, String woNo, String outNo, int action) {
        this.option = option;
        this.woNo = woNo;
        this.outNo = outNo;
        this.action = action;
    }

    private String option;
    private String woNo;
    private String outNo;
    private int action;//0替换全部，1增加，2移除，3更新

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getWoNo() {
        return woNo;
    }

    public void setWoNo(String woNo) {
        this.woNo = woNo;
    }

    public String getOutNo() {
        return outNo;
    }

    public void setOutNo(String outNo) {
        this.outNo = outNo;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
