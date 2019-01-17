package com.ddadai.basehttplibrary.dialog;

import android.content.Context;

/**
 * Created by shi on 2017/1/18.
 */

public class DialogUtil {


    private Context mContext;

    private NormalDialog tipDialog;
    private ProgressDialog loadingDialog;

    public DialogUtil(Context context) {
        mContext = context;
        if (mContext != null) {
            loadingDialog = new ProgressDialog(mContext);
            tipDialog = new NormalDialog(mContext);
        }
    }

    //显示loadingdialog
    public void showLoadingDialog() {
        if (loadingDialog != null && !loadingDialog.isShowing()) {
            loadingDialog.show();
            loadingDialog.setCanceledOnTouchOutside(false);
        }
    }

    //显示loadingdialog
    public void showLoadingDialog(String msg) {
        if (loadingDialog != null && !loadingDialog.isShowing()) {
            loadingDialog.show(msg);
            loadingDialog.setCanceledOnTouchOutside(false);
        }
    }

    //取消loadingDialog，与dismiss的区别，如果有cancelcallback,会执行
    public void cancelLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.cancel();
        }
    }

    public void dismissLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }


    public void showTipDialog(String msg) {
        if (tipDialog != null) {
            tipDialog.builder().setTitle("温馨提示")
                    .setMsg(msg)
                    .setBtnType(NormalDialog.DIALOG_SHOW_OK_BUTTON)
                    .show();
        }
    }

    public void cancelTipDialog() {
        if (tipDialog != null && tipDialog.isShowing()) {
            tipDialog.cancel();
        }
    }

    public void dismissTipDialog() {
        if (tipDialog != null && tipDialog.isShowing()) {
            tipDialog.dismiss();
        }
    }


    //销毁dialog
    public void destroyDialog() {
        try {
            dismissTipDialog();
            dismissLoadingDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
