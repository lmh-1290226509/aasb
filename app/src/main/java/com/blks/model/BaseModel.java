package com.blks.model;

import java.io.Serializable;

public class BaseModel implements Serializable {

    public boolean IsSuccess;
    public boolean HasMore;
    public int iTotalCnt;
    public Object ErrCode;
}
