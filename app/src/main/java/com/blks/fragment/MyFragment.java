package com.blks.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blks.antrscapp.AboutActivity;
import com.blks.antrscapp.PersonActivity;
import com.blks.antrscapp.R;
import com.blks.antrscapp.SetActivity;
import com.blks.antrscapp.UserHandbookAct;
import com.blks.application.RoadSideCarApplication;
import com.blks.model.LoginModel;
import com.blks.utils.LoginUtils;
import com.blks.utils.Util;
import com.blks.customer.CircleImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

public class MyFragment extends Fragment implements OnClickListener {

    private Context context;
    private LinearLayout ll_person, ll_set, ll_my_help, ll_about;
    private CircleImageView img_ss_head;
    private TextView userNameTv;
    private TextView superNameTv;
    private String headUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        context = this.getActivity();
        findView(view);
        return view;
    }

    private void findView(View view) {
        ll_person = (LinearLayout) view.findViewById(R.id.ll_person);
        ll_set = (LinearLayout) view.findViewById(R.id.ll_set);
        ll_my_help = (LinearLayout) view.findViewById(R.id.ll_my_help);
        ll_about = (LinearLayout) view.findViewById(R.id.ll_about);
        ll_person.setOnClickListener(this);
        ll_set.setOnClickListener(this);
        ll_my_help.setOnClickListener(this);
        ll_about.setOnClickListener(this);

        img_ss_head = view.findViewById(R.id.img_ss_head);

        userNameTv = (TextView) view.findViewById(R.id.userNameTv);
        superNameTv = (TextView) view.findViewById(R.id.superNameTv);

        LoginModel.DataListModel loginModel = LoginUtils.getLoginModel();
        if (loginModel != null) {
            userNameTv.setText(loginModel.USR_NAME);
            superNameTv.setText(loginModel.SUP_NAME);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        String urlImg = RoadSideCarApplication.getInstance().HeadUrl();
        if (urlImg != null && !urlImg.equals(headUrl)) {

            headUrl = urlImg;

            Glide.with(this)
                    .load(headUrl)
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {

                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            img_ss_head.setBitmap(resource);
                        }
                    });
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_person:
                Util.moveTo(context, PersonActivity.class);
                break;
            case R.id.ll_set:
                Util.moveTo(context, SetActivity.class);
                break;
            case R.id.ll_my_help:
                Util.moveTo(context, UserHandbookAct.class);
                break;
            case R.id.ll_about:
                Util.moveTo(context, AboutActivity.class);
                break;
            default:
                break;
        }

    }
}
