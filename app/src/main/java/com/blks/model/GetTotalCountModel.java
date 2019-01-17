package com.blks.model;

import java.io.Serializable;
import java.util.List;

public class GetTotalCountModel extends BaseModel {


    /**
     * ErrCode : null
     * DataList : [{"TODAY_CNT":"0","MONTH_CNT":"0","YEAR_CNT":"2"}]
     */

    public List<DataListModel> DataList;

    public static class DataListModel implements Serializable {
        /**
         * TODAY_CNT : 0
         * MONTH_CNT : 0
         * YEAR_CNT : 2
         */

        public String TODAY_CNT;
        public String MONTH_CNT;
        public String YEAR_CNT;
    }
}
