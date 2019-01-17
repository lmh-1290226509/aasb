package com.blks.model;

import java.io.Serializable;
import java.util.List;

public class WorkOrderModel extends BaseModel {

    private boolean showDialog;
    public List<DataListModel> DataList;

    public static class DataListModel implements Serializable {
/*
            \"WO_NO\": \"CS181030133633238\",
            \"WO_DATE\": \"2018/10/30 13:36:33\",
            \"WO_OWNER_NO\": \"145\",
            \"WO_FROM\": \"3\",
            \"DATA_FROM\": \"47\",
            \"OUT_SOURCE_NO\": \"\",
            \"IS_TEAM\": \"\",
            \"CUS_NO\": \"21122323\",
            \"CUS_NAME\": \"彭士杰02\",
            \"CUS_MOBILE\": \"15618213123\",
            \"CAR_NO\": \"沪BN0393\",
            \"CAR_LICENSE_NO\": \"众泰-保时泰\",
            \"CAR_COLOR\": \"\",
            \"CAR_VIN\": \"khgasdalkjgwe\",
            \"SETTLE_OBJECT\": \"\",
            \"RESCUE_COMMENT\": \"\",
            \"WO_TAKE_DATE\": \"2018/10/30 14:05:57\",
            \"WO_TAKE_PSN\": \"24430\",
            \"SERVICE_ITEM_NO\": \"F\",
            \"RESCUE_REASON\": \"52\",
            \"APPOINTMENT_DATE\": \"2018/10/31 13:00:00\",
            \"ADD_AUX_TIRE\": \"\",
            \"RESCUE_RMK\": \"\",
            \"FROM_ADDR_PROVINCE\": \"31\",
            \"FROM_ADDR_CITY\": \"3101\",
            \"FROM_ADDR_REGION\": \"310105\",
            \"FROM_ADDR_DETAIL\": \"祁连山南路/同普路(路口)\",
            \"TO_ADDR_PROVINCE\": \"31\",
            \"TO_ADDR_CITY\": \"3101\",
            \"TO_ADDR_REGION\": \"310105\",
            \"TO_ADDR_DETAIL\": \"建滔商业广场\",
            \"RE_CONNECT_DATE\": \"2018/10/30 13:37:00\",
            \"DISPATCH_DATE\": \"2018/10/30 13:42:00\",
            \"EST_ARRIVE_DATE\": \"2018/10/30 14:21:00\",
            \"ACT_ARRIVE_DATE\": \"2018/10/30 15:06:15\",
            \"ACT_FINISH_DATE\": \"2018/10/30 15:15:02\",
            \"CANCEL_DATE\": \"\",
            \"CANCEL_RSN\": \"\",
            \"ARRIVE_MILEAGE\": \"4.20\",
            \"TRAILER_MILEAGE\": \"1.60\",
            \"BACK_MILEAGE\": \"\",
            \"EMPTY_RUN_MILEAGE\": \"\",
            \"ARRIVE_MAP_DISTANCE\": \"\",
            \"TRAILER_MAP_DISTANCE\": \"\",
            \"BACK_MAP_DISTANCE\": \"\",
            \"EMPTY_RUN_MAP_DISTANCE\": \"\",
            \"DISPATCH_SP_NO\": \"2018090501\",
            \"REF_SP_NO\": \"\",
            \"ACT_SP_NO\": \"2018090501\",
            \"SP_MOBILE\": \"15201905997\",
            \"RSC_WORKER_NO\": \"30706\",
            \"RSC_WORKER_HANDLER\": \"拖车1号\",
            \"RSC_WORKER_MOBILE\": \"15800988876\",
            \"RSC_PHOTO_MOBILE\": \"\",
            \"RSC_CAR_NO\": \"沪B0900M\",
            \"DISPATCH_CAR_NO\": \"沪B0900M\",
            \"OIL_FEE\": \"\",
            \"HIGHWAY_FEE\": \"\",
            \"ADD_FEE\": \"\",
            \"ADD_FEE_RMK\": \"\",
            \"IS_EXPENSE\": \"\",
            \"CSI_VALUE\": \"5\",
            \"WO_STATUS\": \"已完成\",
            \"CREATE_ORG_NO\": \"21\",
            \"CREATE_PSN\": \"24430\",
            \"CREATE_DATE\": \"2018/10/30 13:36:33\",
            \"UPDATE_PSN\": \"30706\",
            \"UPDATE_DATE\": \"2018/10/30 15:15:02\",
            \"DEL_FLAG\": \"1\",
            \"UDF1\": \"4\",
            \"UDF2\": \"已录音\",
            \"UDF3\": \"\",
            \"UDF4\": \"\",
            \"UDF5\": \"\",
            \"UDF6\": \"\",
            \"UDF7\": \"\",
            \"UDF8\": \"\",
            \"UDF9\": \"\",
            \"UDF10\": \"\",
            \"WO_FROM_TEXT\": \"上海平安非事故\",
            \"DATA_FROM_TEXT\": \"手工创建\",
            \"WO_OWNER_NAME\": \"开发账号\",
            \"FROM_ADDR\": \"上海市 上海市 长宁区祁连山南路/同普路(路口)\",
            \"TO_ADDR\": \"上海市 上海市 长宁区建滔商业广场\",
            \"GOODS_NAME\": \"拖车牵引\",
            \"GOODS_PRINT_NAME\": \"\",
            \"TO_ADDR_LNG\": \"121.36093068525234\",
            \"TO_ADDR_LAT\": \"31.231407736940245\",
            \"FROM_ADDR_LNG\": \"121.37791482985146\",
            \"FROM_ADDR_LAT\": \"31.254615446727982\",
            \"CAR_START_LNG\": \"\",
            \"CAR_START_LAT\": \"\",
            \"RSC_STEP\": \"已完成\",
            \"EST_ARRIVE_MILEAGE\": \"\",
            \"DISPATCH_SP\": \"拖车小分队\",
            \"ACT_SP\": \"拖车小分队\",
            \"CAR_START_ADDR_PROVINCE\": \"\",
            \"CAR_START_ADDR_CITY\": \"\",
            \"CAR_START_ADDR_REGION\": \"\",
            \"CAR_START_ADDR_DETAIL\": \"\",
            \"RESCUE_REASON_TEXT\": \"机械故障\",
            \"SVC_TERMS\": \"\",
            \"OUT_SOURCE_SUB_NO\": \"\",
            \"ASSIGN_MODE\": \"\",
            \"PUSH_READ_FLAG\": \"\",
            \"DATA_VERSION\": \"cd54c970-4126-40fc-98a7-4bb693804127\"
* */
        public String WO_NO; //工单号
        public String RSC_STEP; //救援状态

        public String WO_DATE;
        public String WO_OWNER_NO;
        public String WO_FROM;
        public String DATA_FROM;
        public String OUT_SOURCE_NO;
        public String IS_TEAM;
        public String CUS_NO;
        public String CUS_NAME;
        public String CUS_MOBILE;
        public String CAR_NO;
        public String CAR_LICENSE_NO;
        public String CAR_COLOR;
        public String CAR_VIN;
        public String SETTLE_OBJECT;
        public String RESCUE_COMMENT;
        public String WO_TAKE_DATE;
        public String WO_TAKE_PSN;
        public String SERVICE_ITEM_NO;
        public String RESCUE_REASON;
        public String APPOINTMENT_DATE;
        public String ADD_AUX_TIRE;
        public String RESCUE_RMK;
        public String RE_CONNECT_DATE;
        public String DISPATCH_DATE;
        public String EST_ARRIVE_DATE;
        public String ACT_ARRIVE_DATE;
        public String ACT_FINISH_DATE;
        public String CANCEL_DATE;
        public String CANCEL_RSN;
        public String BACK_MILEAGE;
        public String EMPTY_RUN_MILEAGE;
        public String ARRIVE_MAP_DISTANCE;
        public String TRAILER_MAP_DISTANCE;
        public String BACK_MAP_DISTANCE;
        public String EMPTY_RUN_MAP_DISTANCE;
        public String DISPATCH_SP_NO;
        public String REF_SP_NO;
        public String ACT_SP_NO;
        public String SP_MOBILE;
        public String RSC_WORKER_NO;
        public String RSC_WORKER_HANDLER;
        public String RSC_WORKER_MOBILE;
        public String RSC_PHOTO_MOBILE;
        public String RSC_CAR_NO;
        public String DISPATCH_CAR_NO;
        public String OIL_FEE;
        public String HIGHWAY_FEE;
        public String ADD_FEE;
        public String ADD_FEE_RMK;
        public String IS_EXPENSE;
        public String CSI_VALUE;
        public String CREATE_ORG_NO;
        public String CREATE_PSN;
        public String CREATE_DATE;
        public String UPDATE_PSN;
        public String UPDATE_DATE;
        public String DEL_FLAG;
        public String UDF1;
        public String UDF2;
        public String UDF3;
        public String UDF4;
        public String UDF5;
        public String UDF6;
        public String UDF7;
        public String UDF8;
        public String UDF9;
        public String UDF10;
        public String WO_FROM_TEXT;
        public String DATA_FROM_TEXT;
        public String WO_OWNER_NAME;
        public String GOODS_NAME;
        public String GOODS_PRINT_NAME;
        public String CAR_START_LNG;
        public String CAR_START_LAT;
        public String EST_ARRIVE_MILEAGE;
        public String DISPATCH_SP;
        public String ACT_SP;
        public String CAR_START_ADDR_PROVINCE;
        public String CAR_START_ADDR_CITY;
        public String CAR_START_ADDR_REGION;
        public String CAR_START_ADDR_DETAIL;
        public String RESCUE_REASON_TEXT;
        public String SVC_TERMS;
        public String OUT_SOURCE_SUB_NO;
        public String ASSIGN_MODE;
        public String PUSH_READ_FLAG;
        public String DATA_VERSION;

        public String FROM_ADDR_PROVINCE; //故障省
        public String FROM_ADDR_CITY; //故障市
        public String FROM_ADDR_REGION; //故障区
        public String FROM_ADDR_DETAIL; //故障地址
        public String FROM_ADDR; //故障详细地址
        public String FROM_ADDR_LNG; //故障地点经度
        public String FROM_ADDR_LAT; //故障地点纬度

        public String TO_ADDR_PROVINCE; //拖至省
        public String TO_ADDR_CITY; //拖至市
        public String TO_ADDR_REGION; //拖至区
        public String TO_ADDR_DETAIL; //拖至地址
        public String TO_ADDR; //拖至详细地址
        public String TO_ADDR_LNG; //拖至地点经度
        public String TO_ADDR_LAT; //拖至地点纬度

        public String ARRIVE_MILEAGE; //到达里程
        public String TRAILER_MILEAGE; //拖车里程

        public String WO_STATUS; //工单状态
    }

    public boolean isShowDialog() {
        return showDialog;
    }

    public void setShowDialog(boolean showDialog) {
        this.showDialog = showDialog;
    }
}
