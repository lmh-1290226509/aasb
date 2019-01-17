package com.ddadai.baseviewlibrary.wheel.widget.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import com.ddadai.baseviewlibrary.R;
import com.ddadai.baseviewlibrary.wheel.widget.view.WheelView;

/**
 * Created by shi on 2017/11/22.
 */

public class BaseWheelBottomSheetDialog extends FixBotttomSheetDialog {



    protected int type;
    protected Context mContext;
    protected View rootView;
    protected int width;
    protected int height;
    protected int Status_Height;
    protected boolean isShowCenter=true;//显示中间的那条横线

    protected WheelView wv1,wv2,wv3;
    protected WheelView wv4,wv5,wv6;
    protected View centerView,okView,noView;

    protected WheelView wvs[];


    public BaseWheelBottomSheetDialog(@NonNull Context context) {
        super(context);
        mContext=context;
        initView();
    }


    /**
     * 初始化布局
     */
    protected void initView(){
        rootView= LayoutInflater.from(mContext).inflate(R.layout.wheel_pop_view, null);
        setContentView(rootView);
        centerView= rootView.findViewById(R.id.centerView);
        okView= rootView.findViewById(R.id.okView);
        noView= rootView.findViewById(R.id.noView);
        wv1= rootView.findViewById(R.id.wv1);
        wv2= rootView.findViewById(R.id.wv2);
        wv3= rootView.findViewById(R.id.wv3);
        wv4= rootView.findViewById(R.id.wv4);
        wv5= rootView.findViewById(R.id.wv5);
        wv6= rootView.findViewById(R.id.wv6);

        wvs=new WheelView[6];
        wvs[0]=wv1;
        wvs[1]=wv2;
        wvs[2]=wv3;
        wvs[3]=wv4;
        wvs[4]=wv5;
        wvs[5]=wv6;

        okView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                okClick();
            }
        });
        rootView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                rootViewClick();
            }
        });
        noView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                noViewClick();
            }
        });
    }



    /**
     * 设置是否显示中间的横条
     * @param color 输入0xffffffff 格式的8位16进制的色值
     */
    public void setShowCengter(int color){
        try {
            centerView.setVisibility(View.VISIBLE);
            centerView.setBackgroundColor(color);
        } catch (Exception e) {
            e.printStackTrace();
            centerView.setVisibility(View.GONE);
        }
    }


    /**
     * 设置是否显示中间的横条
     * @param show
     */
    public void setShowCengter(boolean show){
        if(show){
            centerView.setVisibility(View.VISIBLE);
            centerView.setBackgroundColor(0x11111111);
        }else{
            centerView.setVisibility(View.GONE);
        }
    }

    protected void okClick(){
        dismiss();
    }
    protected void rootViewClick(){
//		dismiss();
    }

    protected void noViewClick() {
        dismiss();
    }


    /**
     * 设置是否循环
     * @param isCyclic
     */
    public void setCyclic(boolean isCyclic){
        for (int i = 0; i < wvs.length; i++) {
            wvs[i].setCyclic(isCyclic);
        }
    }


    /**
     * 设置是否循环
     * @param isCyclics
     */
    public void setCyclic(boolean[] isCyclics){
        for (int i = 0; i < wvs.length; i++) {
            wvs[i].setCyclic(isCyclics[i]);
        }
    }
}
