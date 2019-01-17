package com.blks.model;

import com.baidu.location.BDLocation;

import java.io.Serializable;

/**
 * EventBus传递工单
 */
public class EventOrderModel_2 implements Serializable {

    public EventOrderModel_2() {
    }

    private String option;
    private String woNo;
    private String outNo;
    private String eventTime;
    private BDLocation bdLocation;

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

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public BDLocation getBdLocation() {
        return bdLocation;
    }

    public void setBdLocation(BDLocation bdLocation) {
        this.bdLocation = bdLocation;
    }
}
