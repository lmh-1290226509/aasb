package com.blks.model;

import java.io.Serializable;
import java.util.List;

public class VersionModel extends BaseModel {


    /**
     * ErrCode : null
     * DataList : [{"VN_ID":"394152923A8824F2E05398162D0A1BE8","VERSION_SYSTEM":"rscapp","VERSION_NO":"2.0.4","VERSION_CONTENT":"1. 修改录音。u003cbr/u003e2. 修改BUG若干。","VERSION_PACKAGE_URL":"http://test-antrsc.bzmaster.cn/AppPackage/antrescue_2.0.4.apk","VERSION_PUBLISH_DATE":"2018/8/21 14:13:34","VERSION_STATUS":"1","IS_FORCE":"0","CREATE_PSN":"0","CREATE_DATE":"2018/8/21 14:14:27","UPDATE_PSN":"0","UPDATE_DATE":"2018/8/21 14:14:42","CREATE_ORG_NO":"21","UDF1":"8.90MB","UDF2":"","UDF3":"","UDF4":"","UDF5":""}]
     */

    public List<DataListModel> DataList;

    public static class DataListModel implements Serializable{
        /**
         * VN_ID : 394152923A8824F2E05398162D0A1BE8
         * VERSION_SYSTEM : rscapp
         * VERSION_NO : 2.0.4
         * VERSION_CONTENT : 1. 修改录音。u003cbr/u003e2. 修改BUG若干。
         * VERSION_PACKAGE_URL : http://test-antrsc.bzmaster.cn/AppPackage/antrescue_2.0.4.apk
         * VERSION_PUBLISH_DATE : 2018/8/21 14:13:34
         * VERSION_STATUS : 1
         * IS_FORCE : 0
         * CREATE_PSN : 0
         * CREATE_DATE : 2018/8/21 14:14:27
         * UPDATE_PSN : 0
         * UPDATE_DATE : 2018/8/21 14:14:42
         * CREATE_ORG_NO : 21
         * UDF1 : 8.90MB
         * UDF2 :
         * UDF3 :
         * UDF4 :
         * UDF5 :
         */

        public String VN_ID;
        public String VERSION_SYSTEM;
        public String VERSION_NO;
        public String VERSION_CONTENT;
        public String VERSION_PACKAGE_URL;
        public String VERSION_PUBLISH_DATE;
        public String VERSION_STATUS;
        public String IS_FORCE;
        public String CREATE_PSN;
        public String CREATE_DATE;
        public String UPDATE_PSN;
        public String UPDATE_DATE;
        public String CREATE_ORG_NO;
        public String UDF1;
        public String UDF2;
        public String UDF3;
        public String UDF4;
        public String UDF5;
    }
}
