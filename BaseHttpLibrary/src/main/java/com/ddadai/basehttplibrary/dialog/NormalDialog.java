package com.ddadai.basehttplibrary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ddadai.basehttplibrary.R;


/**
 * Created by shi on 2017/1/16.
 * 重写app错误返回的dialog
 */

public class NormalDialog extends Dialog implements View.OnClickListener {


    public NormalDialog(Context context) {
        super(context, R.style.Theme_CustomDialog);

        builder = new Builder();

    }


    //不显示按钮
    public static final int DIALOG_NO_SHOW_BUTTON = 1;
    //显示确定按钮
    public static final int DIALOG_SHOW_OK_BUTTON = 2;
    //显示2个按钮
    public static final int DIALOG_SHOW_BOTH_BUTTON = 3;


//    private int btnType;
//
//    private String titleTxt,msgTxt,okTxt,noTxt;

    private View layout;
    private TextView titleTv, msgTv;
    private Button okBtn, noBtn;


    private Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bal_dialog_normal);
        initView();
    }


    private void initView() {
        setCancelable(true);
        layout = (LinearLayout) this.findViewById(R.id.dialog_layout);
        layout.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.bal_show_animation));
        titleTv = (TextView) this.findViewById(R.id.dialog_title);
        // 初始化刷卡手机温馨提示title背景图片
        msgTv = (TextView) this.findViewById(R.id.dialog_msg);
        okBtn = (Button) this.findViewById(R.id.dialog_bt_ok);
        okBtn.setOnClickListener(this);
        noBtn = (Button) this.findViewById(R.id.dialog_bt_no);
        noBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.dialog_bt_ok) {
            if (isShowing()) {
                dismiss();
            }
            if (builder.okClick != null) {
                builder.okClick.dialogBtnClick(OnDialogBtnClickListener.BTN_OK);
            }


        } else if (i == R.id.dialog_bt_no) {
            if (isShowing()) {
                dismiss();
            }
            if (builder.noClick != null) {
                builder.okClick.dialogBtnClick(OnDialogBtnClickListener.BTN_NO);
            }

        } else {
        }
    }


    @Override
    public void show() {
        if (this.isShowing()) {
            cancel();
        }
        try {
            super.show();
            layout.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.bal_anim_dialog_show));
            build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show(String msgTxt) {
        if (builder != null) {
            builder.msg = msgTxt;
        }
        show();
    }


    public Builder builder() {
        return builder;
    }


    public class Builder {

        String title = "温馨提示", msg, okbtn = "确定", nobtn = "取消";
        OnDialogBtnClickListener okClick, noClick;
        int btnType;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setMsg(String msg) {
            this.msg = msg;
            return this;
        }

        public Builder setOkBtnText(String okBtnText) {
            this.okbtn = okBtnText;
            return this;
        }

        public Builder setNoBtnText(String noBtnText) {
            this.nobtn = noBtnText;
            return this;
        }

        public Builder setOkClick(OnDialogBtnClickListener okClick) {
            this.okClick = okClick;
            return this;
        }

        public Builder setNoClick(OnDialogBtnClickListener noClick) {
            this.noClick = noClick;
            return this;
        }

        public Builder setBtnType(int btnType) {
            this.btnType = btnType;
            return this;
        }

        public void show() {
            NormalDialog.this.show();
        }
    }


    private void build() {
        if (builder != null) {
            titleTv.setText(builder.title);
            msgTv.setText(Html.fromHtml(builder.msg));
//            msgTv.setText(builder.msg);
            okBtn.setText(builder.okbtn);
            noBtn.setText(builder.nobtn);

            switch (builder.btnType) {
                case DIALOG_NO_SHOW_BUTTON:
                    okBtn.setVisibility(View.GONE);
                    noBtn.setVisibility(View.GONE);
                    break;
                case DIALOG_SHOW_BOTH_BUTTON:
                    okBtn.setVisibility(View.VISIBLE);
                    noBtn.setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams btOkLin = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f);
                    okBtn.setLayoutParams(btOkLin);
                    LinearLayout.LayoutParams btNoLin = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f);
                    noBtn.setLayoutParams(btNoLin);
                    break;
                case DIALOG_SHOW_OK_BUTTON:
                    okBtn.setVisibility(View.VISIBLE);
                    noBtn.setVisibility(View.GONE);
                    LinearLayout.LayoutParams btOkLin_ = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                    okBtn.setLayoutParams(btOkLin_);
                    break;
            }
        }
    }


    public interface OnDialogBtnClickListener {
        int BTN_OK = 1;
        int BTN_NO = 0;

        void dialogBtnClick(int btnType);
    }
}
