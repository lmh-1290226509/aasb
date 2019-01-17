package com.blks.antrscapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.blks.app.BaseActivity;
import com.blks.app.CustomDialog;
import com.blks.application.RoadSideCarApplication;
import com.blks.https.HttpUri;
import com.blks.https.JsonRequestCallBack;
import com.blks.utils.Constants;
import com.blks.utils.EUExUtil;
import com.blks.utils.LoginUtils;
import com.blks.utils.ToastUtil;
import com.ddadai.basehttplibrary.HttpUtils;
import com.ddadai.basehttplibrary.response.Response_;
import com.ddadai.basehttplibrary.utils.HttpCode;

import org.json.JSONObject;

/**
 * 取消/退回工单
 */
public class CancelWorkOrderActivity extends BaseActivity implements View.OnClickListener {

    private TextView[] reasonViews = new TextView[5];
    private CustomDialog dialog;
    private boolean needReply = false;
    private String woNo;
    private String cancelReason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.act_cancel_work_order);
        initVIew();
    }

    private void initVIew() {

        woNo = getIntent().getStringExtra(Constants._WO_NO);
        needReply = getIntent().getBooleanExtra(Constants._KEY_B, false);

        for (int i = 0; i < 5; i++) {
            reasonViews[i] = findViewById(EUExUtil.getResIdID("cancel_reason_"+i));
            reasonViews[i].setOnClickListener(this);
        }

        dialog = new CustomDialog(this, R.style.mystyle, new CustomDialog.CustomDialogListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.confirm_btn:// 确定
                        if (needReply) {
                            replyForRscWOMstr();
                        } else {
                            cancelRscWoMstr();
                        }
                        break;
                    case R.id.cancel_btn:// 取消
                        dialog.dismiss();
                        break;
                }
            }
        })
        .SetOkText("确定")
        .SetCancelText("取消");

        //取消
        findViewById(R.id.tv_order_cancel).setOnClickListener(this);
        //继续
        findViewById(R.id.continue_btn).setOnClickListener(this);
    }


    /**
     * 取消/退回工单
     */
    private void cancelRscWoMstr() {
        if (TextUtils.isEmpty(woNo)) {
            return;
        }

        HttpUtils.get(HttpUri.CANCEL_RSCWO_MSTR)
                .dialog(true)
                .data("woNo",woNo)
                .data("reason", cancelReason)
                .data("usrId", LoginUtils.getLoginModel().USR_ID)
                .data("usrOrgId", LoginUtils.getLoginModel().ORG_NO)
                .data("usrName", LoginUtils.getLoginModel().USR_NAME)
                .data("empNo", LoginUtils.getLoginModel().USR_EMP_NO)
                .mulKey("woNo")
                .callBack(new JsonRequestCallBack(this) {
                    @Override
                    public void requestSuccess(String url, JSONObject jsonObject) {
                        dialog.dismiss();

                        Intent intent = new Intent(CancelWorkOrderActivity.this, HomePagerActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("bar_id", 2);
                        CancelWorkOrderActivity.this.startActivity(intent);
                        finish();
                    }

                    @Override
                    public void requestFail(String url, Response_<JSONObject> response) {
                        super.requestFail(url, response);
                        if (HttpCode.NETWORK_ERROR.equals(response.code)) {
                            ToastUtil.showShort(mThis,response.msg);
                            return;
                        }

                        dialog.dismiss();
                        RoadSideCarApplication.getInstance().showToast("取消失败！");

                        Intent intent = new Intent(CancelWorkOrderActivity.this, HomePagerActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("bar_id", 2);
                        CancelWorkOrderActivity.this.startActivity(intent);
                        finish();
                    }
                })
                .request();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_order_cancel:
                finish();
                break;
            case R.id.continue_btn:
                finish();
                break;
            case R.id.cancel_reason_0:
            case R.id.cancel_reason_1:
            case R.id.cancel_reason_2:
            case R.id.cancel_reason_3:
                cancelReason = ((TextView)v).getText().toString();
                dialog.SetMessage("您确认退回该救援工单？");
                dialog.show();
                break;
            case R.id.cancel_reason_4:
                cancelReason = ((TextView)v).getText().toString();
                dialog.SetMessage("您确认取消该救援工单？");
                dialog.show();
                break;

        }
    }

    /**
     * 30s无响应
     * 操作状态1:接单；-1:拒接；0:无响应
     * 回复工单分配状态
     */
    private void replyForRscWOMstr() {

        if (TextUtils.isEmpty(woNo)) {
            return;
        }

        HttpUtils.get(HttpUri.REPLY_FOR_RSC_WOMSTR)
                .dialog(true)
                .data("woNo", woNo)
                .data("reply", "-1")
                .data("usrId", LoginUtils.getLoginModel().USR_ID)
                .data("usrOrgId", LoginUtils.getLoginModel().ORG_NO)
                .data("usrName", LoginUtils.getLoginModel().USR_NAME)
                .data("usrMobile", LoginUtils.getLoginModel().USR_MOBILE)
                .data("supNo", LoginUtils.getLoginModel().SUP_NO)
                .data("supName", LoginUtils.getLoginModel().SUP_NAME)
                .data("supMobile", LoginUtils.getLoginModel().SUP_MOBILE)
                .data("supManageUsrId", LoginUtils.getLoginModel().SUP_MANAGE_USRID)
                .data("supManageEmpNo", LoginUtils.getLoginModel().SUP_MANAGE_EMPNO)
                .data("usrRealName", LoginUtils.getLoginModel().USR_REAL_NAME)
                .data("empNo", LoginUtils.getLoginModel().USR_EMP_NO)
                .data("carNo", LoginUtils.getCarModel().ASSETS_NO)
                .mulKey("woNo")
                .callBack(new JsonRequestCallBack(this) {
                    @Override
                    public void requestSuccess(String url, JSONObject jsonObject) {
                        RoadSideCarApplication.getInstance().showToast("你已拒绝该救援单！");
                        modifyUserState(Constants.UserStatus.READY);

                        Intent intent = new Intent(CancelWorkOrderActivity.this, HomePagerActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("bar_id", 2);
                        CancelWorkOrderActivity.this.startActivity(intent);
                        finish();
                    }

                    @Override
                    public void requestFail(String url, Response_<JSONObject> response) {
                        super.requestFail(url, response);
                        if (HttpCode.NETWORK_ERROR.equals(response.code)) {
                            ToastUtil.showShort(mThis,response.msg);
                            return;
                        }
                        RoadSideCarApplication.getInstance().showToast("拒绝失败！");

                        Intent intent = new Intent(CancelWorkOrderActivity.this, HomePagerActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("bar_id", 2);
                        CancelWorkOrderActivity.this.startActivity(intent);
                        finish();
                    }
                })
                .request();
    }

}
