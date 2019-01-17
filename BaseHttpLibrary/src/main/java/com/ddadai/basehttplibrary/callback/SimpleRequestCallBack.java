package com.ddadai.basehttplibrary.callback;

import android.content.Context;
import android.text.TextUtils;

import com.ddadai.basehttplibrary.dialog.DialogManager;
import com.ddadai.basehttplibrary.dialog.DialogUtil;
import com.ddadai.basehttplibrary.response.Response_;


/**
 * Created by shi on 2017/1/18.
 */

public abstract class SimpleRequestCallBack<T> implements OnRequestCallBack_<T> {


    private DialogUtil dialogUtil;
    private Context context;
    public SimpleRequestCallBack(Context context) {
        if (context != null) {
            this.context=context;
            dialogUtil = DialogManager.getInstance().getDiaglogUtil(context);
        }
    }

    @Override
    public void requestStart(String url, boolean isShowDialog, String dialogMsg) {
        if(isShowDialog){
            if(TextUtils.isEmpty(dialogMsg)){
                dialogUtil.showLoadingDialog();
            }else{
                dialogUtil.showLoadingDialog(dialogMsg);
            }
        }
    }

    @Override
    public void requestSuccess(String url, Response_<T> response) {
        requestFinish();
        requestSuccess(url,response.data);
    }

    @Override
    public void requestFail(String url, Response_<T> response) {
        requestFinish();
    }

    @Override
    public void requestFinish() {
        dialogUtil.cancelLoadingDialog();
    }


    public abstract void requestSuccess(String url, T t);
}
