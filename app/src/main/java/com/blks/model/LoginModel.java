package com.blks.model;

import java.io.Serializable;
import java.util.List;

public class LoginModel extends BaseModel{


    /**
     * IsSuccess : true
     * HasMore : false
     * iTotalCnt : 1
     * ErrCode : null
     * DataList : [{"USR_ID":"30702","USR_NAME":"bt0000042","USR_PWD":"e10adc3949ba59abbe56e057f20f883e","USR_STATUS":"1","USR_AVATAR_STATUS":"0","USR_REG_DATE":"2018/9/6 9:50:40","ORG_NO":"2018061401","CREATE_PSN":"30481","CREATE_DATE":"2018/9/6 9:50:40","UPDATE_PSN":"24430","UPDATE_DATE":"2018/9/6 9:50:40","CREATE_ORG_NO":"1","USR_REAL_NAME":"蚂蚁救援","USR_MOBILE":"","USR_EMAIL":"","USR_MOBILE_PASS":"0","USR_AVATAR_PATH":"","USR_ALIPAY":"","INVITATION_CODE":"","FROM_CHANNEL":"","FROM_INVITATION_CODE":"","UDF1":"沪A1234","UDF2":"","UDF3":"","UDF4":"","UDF5":"","USR_NICKNAME":"","USR_REGION":"","DEL_FLAG":"1","ERP_ORG_NO":"","ERP_EMP_ID":"","USR_DEVICE_ID":"F18Q932J932","USR_EMP_NO":"3241","USR_TYPE":"WKR","USR_BIZ_FROM":"","USR_CURRENT_STATUS":"0","BEAT_DATE":"2018/11/12 10:19:54","LOGIN_DATE":"2018/11/12 10:19:54","LOGOUT_DATE":"2018/11/12 10:20:57","SUP_NO":"2018061401","USR_ROLE_NAMES":"","USR_DESC":"","USR_JOB":"","USR_FIELD":"","USR_HOBBY":"","USR_LOCATION":"","USR_QRCODE_PATH":"","ERP_CUS_NO":"","ERP_MEMBER_NO":"","USR_SPELL":"","BG_NO":"","BU_IDS":"","BU_NAMES":"","OPEN_ID":"","EMP_NAME":"蚂蚁九","EMP_PHONE":"15201909944","SUP_NAME":"蚂蚁救援","SUP_MOBILE":"12345678901","SUP_MANAGE_USRID":"30481","SUP_MANAGE_EMPNO":"2018061401"}]
     */


    public List<DataListModel> DataList;

    public static class DataListModel implements Serializable {
        /**
         * USR_ID : 30702
         * USR_NAME : bt0000042
         * USR_PWD : e10adc3949ba59abbe56e057f20f883e
         * USR_STATUS : 1
         * USR_AVATAR_STATUS : 0
         * USR_REG_DATE : 2018/9/6 9:50:40
         * ORG_NO : 2018061401
         * CREATE_PSN : 30481
         * CREATE_DATE : 2018/9/6 9:50:40
         * UPDATE_PSN : 24430
         * UPDATE_DATE : 2018/9/6 9:50:40
         * CREATE_ORG_NO : 1
         * USR_REAL_NAME : 蚂蚁救援
         * USR_MOBILE :
         * USR_EMAIL :
         * USR_MOBILE_PASS : 0
         * USR_AVATAR_PATH :
         * USR_ALIPAY :
         * INVITATION_CODE :
         * FROM_CHANNEL :
         * FROM_INVITATION_CODE :
         * UDF1 : 沪A1234
         * UDF2 :
         * UDF3 :
         * UDF4 :
         * UDF5 :
         * USR_NICKNAME :
         * USR_REGION :
         * DEL_FLAG : 1
         * ERP_ORG_NO :
         * ERP_EMP_ID :
         * USR_DEVICE_ID : F18Q932J932
         * USR_EMP_NO : 3241
         * USR_TYPE : WKR
         * USR_BIZ_FROM :
         * USR_CURRENT_STATUS : 0
         * BEAT_DATE : 2018/11/12 10:19:54
         * LOGIN_DATE : 2018/11/12 10:19:54
         * LOGOUT_DATE : 2018/11/12 10:20:57
         * SUP_NO : 2018061401
         * USR_ROLE_NAMES :
         * USR_DESC :
         * USR_JOB :
         * USR_FIELD :
         * USR_HOBBY :
         * USR_LOCATION :
         * USR_QRCODE_PATH :
         * ERP_CUS_NO :
         * ERP_MEMBER_NO :
         * USR_SPELL :
         * BG_NO :
         * BU_IDS :
         * BU_NAMES :
         * OPEN_ID :
         * EMP_NAME : 蚂蚁九
         * EMP_PHONE : 15201909944
         * SUP_NAME : 蚂蚁救援
         * SUP_MOBILE : 12345678901
         * SUP_MANAGE_USRID : 30481
         * SUP_MANAGE_EMPNO : 2018061401
         */

        public String USR_ID;
        public String USR_NAME;
        public String USR_PWD;
        public String USR_STATUS;
        public String USR_AVATAR_STATUS;
        public String USR_REG_DATE;
        public String ORG_NO;
        public String CREATE_PSN;
        public String CREATE_DATE;
        public String UPDATE_PSN;
        public String UPDATE_DATE;
        public String CREATE_ORG_NO;
        public String USR_REAL_NAME;
        public String USR_MOBILE;
        public String USR_EMAIL;
        public String USR_MOBILE_PASS;
        public String USR_AVATAR_PATH;
        public String USR_ALIPAY;
        public String INVITATION_CODE;
        public String FROM_CHANNEL;
        public String FROM_INVITATION_CODE;
        public String UDF1;
        public String UDF2;
        public String UDF3;
        public String UDF4;
        public String UDF5;
        public String USR_NICKNAME;
        public String USR_REGION;
        public String DEL_FLAG;
        public String ERP_ORG_NO;
        public String ERP_EMP_ID;
        public String USR_DEVICE_ID;
        public String USR_EMP_NO;
        public String USR_TYPE;
        public String USR_BIZ_FROM;
        public String USR_CURRENT_STATUS;
        public String BEAT_DATE;
        public String LOGIN_DATE;
        public String LOGOUT_DATE;
        public String SUP_NO;
        public String USR_ROLE_NAMES;
        public String USR_DESC;
        public String USR_JOB;
        public String USR_FIELD;
        public String USR_HOBBY;
        public String USR_LOCATION;
        public String USR_QRCODE_PATH;
        public String ERP_CUS_NO;
        public String ERP_MEMBER_NO;
        public String USR_SPELL;
        public String BG_NO;
        public String BU_IDS;
        public String BU_NAMES;
        public String OPEN_ID;
        public String EMP_NAME;
        public String EMP_PHONE;
        public String SUP_NAME;
        public String SUP_MOBILE;
        public String SUP_MANAGE_USRID;
        public String SUP_MANAGE_EMPNO;

        public String USR_GRADE;//等级
    }
}
