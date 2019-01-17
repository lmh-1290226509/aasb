package com.ddadai.baseviewlibrary.wheel.widget.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by shi on 2017/11/22.
 */

public class FixBotttomSheetDialog extends BottomSheetDialog {



    protected View rootContentView;

    public FixBotttomSheetDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        fixBehavior();

    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        fixBehavior();
    }

    @Override
    public void setContentView(int layoutResId) {
        super.setContentView(layoutResId);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        fixBehavior();
    }


    protected void fixBehavior(){
        // 解决下滑隐藏dialog 后，再次调用show 方法显示时，不能弹出Dialog
        View view1 = getDelegate().findViewById(android.support.design.R.id.design_bottom_sheet);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(view1);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    Log.i("BottomSheet","onStateChanged");
                    dismiss();
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }


    public void setContentViewByRes(int layoutId){
        rootContentView = LayoutInflater.from(getContext()).inflate(layoutId,null);
        setContentView(rootContentView);

        setCancelable(true);
        setCanceledOnTouchOutside(true);


    }
}
