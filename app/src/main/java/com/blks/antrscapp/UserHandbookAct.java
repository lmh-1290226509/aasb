package com.blks.antrscapp;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.blks.app.BaseActivity;
import com.blks.utils.Util;

public class UserHandbookAct extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.act_user_handbook);
        initView();
    }

    private void initView() {
        findViewById(R.id.handle_step).setOnClickListener(this);
        findViewById(R.id.hot_question).setOnClickListener(this);
        findViewById(R.id.img_back).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.handle_step:
                Util.moveTo(mThis, HelpActivity.class);
                break;
            case R.id.hot_question:
                Util.moveTo(mThis, HotQuestionAct.class);
                break;
        }
    }
}
