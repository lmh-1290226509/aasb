package com.ddadai.basehttplibrary.dialog;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shi on 2017/2/6.
 */

public class DialogManager {
    private DialogManager(){
        dialogUtils=new HashMap();
    }

    private static DialogManager mInstance;
    private Map<Context,DialogUtil> dialogUtils;

    public static DialogManager getInstance(){
        if(mInstance==null){
            synchronized (DialogManager.class){
                if(mInstance==null){
                    mInstance=new DialogManager();
                }
            }
        }
        return mInstance;
    }


    public  DialogUtil getDiaglogUtil(Context context){
        if(mInstance.dialogUtils.get(context)==null){
            DialogUtil dialogUtil=new DialogUtil(context);
            mInstance. dialogUtils.put(context,dialogUtil);
        }
        return mInstance.dialogUtils.get(context);
    }

    public  void removeDialogUtil(Context context){
        if(mInstance.dialogUtils.get(context)!=null){
            DialogUtil dialogUtil = mInstance.dialogUtils.get(context);
            dialogUtil.destroyDialog();
            mInstance.dialogUtils.remove(context);
        }
    }
}
