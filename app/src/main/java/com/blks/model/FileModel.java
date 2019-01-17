package com.blks.model;

import java.io.Serializable;
import java.util.List;

public class FileModel extends BaseModel {


    /**
     * ErrCode : null
     * DataList : [{"FILE_ID":"89cd7b80-6add-4f88-ab94-4aaf08f96cd7","FILE_NAME":"89cd7b80-6add-4f88-ab94-4aaf08f96cd7.jpg","FILE_SIZE":"","FILE_CLASS":"图片","BIZ_NO":"CS181119202446488","CREATE_PSN":"30702","CREATE_DATE":"2018/11/19 20:25:55","UPDATE_PSN":"30702","UPDATE_DATE":"2018/11/19 20:25:55","CREATE_ORG_NO":"2018061401","DEL_FLAG":"1","FILE_PATH":"/Upload/Rescue/Images/20181119/89cd7b80-6add-4f88-ab94-4aaf08f96cd7.jpg","FILE_SORT":"1","FILE_SDATE":"","FILE_EDATE":"","FILE_ATTR1":"救援图片","FILE_ATTR2":"","FILE_ATTR3":"","FILE_ATTR4":"","FILE_ATTR5":""},{"FILE_ID":"0fa83581-2c0f-4f36-9ddb-c09af09c4efa","FILE_NAME":"0fa83581-2c0f-4f36-9ddb-c09af09c4efa.jpg","FILE_SIZE":"","FILE_CLASS":"图片","BIZ_NO":"CS181119202446488","CREATE_PSN":"30702","CREATE_DATE":"2018/11/19 20:25:55","UPDATE_PSN":"30702","UPDATE_DATE":"2018/11/19 20:25:55","CREATE_ORG_NO":"2018061401","DEL_FLAG":"1","FILE_PATH":"/Upload/Rescue/Images/20181119/0fa83581-2c0f-4f36-9ddb-c09af09c4efa.jpg","FILE_SORT":"1","FILE_SDATE":"","FILE_EDATE":"","FILE_ATTR1":"救援图片","FILE_ATTR2":"","FILE_ATTR3":"","FILE_ATTR4":"","FILE_ATTR5":""},{"FILE_ID":"5a6d7010-2189-43fc-aea6-3ee318f20fb2","FILE_NAME":"5a6d7010-2189-43fc-aea6-3ee318f20fb2.jpg","FILE_SIZE":"","FILE_CLASS":"图片","BIZ_NO":"CS181119202446488","CREATE_PSN":"30702","CREATE_DATE":"2018/11/19 20:25:55","UPDATE_PSN":"30702","UPDATE_DATE":"2018/11/19 20:25:55","CREATE_ORG_NO":"2018061401","DEL_FLAG":"1","FILE_PATH":"/Upload/Rescue/Images/20181119/5a6d7010-2189-43fc-aea6-3ee318f20fb2.jpg","FILE_SORT":"1","FILE_SDATE":"","FILE_EDATE":"","FILE_ATTR1":"救援图片","FILE_ATTR2":"","FILE_ATTR3":"","FILE_ATTR4":"","FILE_ATTR5":""}]
     */

    public List<DataListModel> DataList;

    public static class DataListModel implements Serializable {
        /**
         * FILE_ID : 89cd7b80-6add-4f88-ab94-4aaf08f96cd7
         * FILE_NAME : 89cd7b80-6add-4f88-ab94-4aaf08f96cd7.jpg
         * FILE_SIZE :
         * FILE_CLASS : 图片
         * BIZ_NO : CS181119202446488
         * CREATE_PSN : 30702
         * CREATE_DATE : 2018/11/19 20:25:55
         * UPDATE_PSN : 30702
         * UPDATE_DATE : 2018/11/19 20:25:55
         * CREATE_ORG_NO : 2018061401
         * DEL_FLAG : 1
         * FILE_PATH : /Upload/Rescue/Images/20181119/89cd7b80-6add-4f88-ab94-4aaf08f96cd7.jpg
         * FILE_SORT : 1
         * FILE_SDATE :
         * FILE_EDATE :
         * FILE_ATTR1 : 救援图片
         * FILE_ATTR2 :
         * FILE_ATTR3 :
         * FILE_ATTR4 :
         * FILE_ATTR5 :
         */

        public String FILE_ID;
        public String FILE_NAME;
        public String FILE_SIZE;
        public String FILE_CLASS;
        public String BIZ_NO;
        public String CREATE_PSN;
        public String CREATE_DATE;
        public String UPDATE_PSN;
        public String UPDATE_DATE;
        public String CREATE_ORG_NO;
        public String DEL_FLAG;
        public String FILE_PATH;
        public String FILE_SORT;
        public String FILE_SDATE;
        public String FILE_EDATE;
        public String FILE_ATTR1;
        public String FILE_ATTR2;
        public String FILE_ATTR3;
        public String FILE_ATTR4;
        public String FILE_ATTR5;
    }
}
