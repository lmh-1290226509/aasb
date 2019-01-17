package com.blks.antrscapp;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.blks.adapter.LookImgsAdapter;
import com.blks.app.BaseActivity;
import com.blks.model.FileModel;
import com.ddadai.baseviewlibrary.view.RecyclerViewPager;

import java.util.List;

public class LookImgsActivity extends BaseActivity {

    private TextView titleTv;
    private ImageView aboutBack;
    private RecyclerViewPager rvp;
    private LookImgsAdapter lookImgsAdapter;

    private int max=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.act_look_imgs);
        List<FileModel.DataListModel> imgs = (List<FileModel.DataListModel>) getIntent().getSerializableExtra("imgs");

        titleTv = (TextView) findViewById(R.id.titleTv);
        aboutBack = (ImageView) findViewById(R.id.about_back);
//        img = (ImageView) findViewById(R.id.img);
        rvp = (RecyclerViewPager) findViewById(R.id.rvp);
        max=imgs.size();
        titleTv.setText("1/" + max);

        lookImgsAdapter=new LookImgsAdapter(mThis);
        rvp.setAdapter(lookImgsAdapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mThis);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvp.setLayoutManager(linearLayoutManager);
        rvp.setSinglePageFling(true);
        lookImgsAdapter.setModels(imgs);

//        Glide.with(mThis)
//                .load("http://test-antrsc.bzmaster.cn/" + imgs.get(0).FILE_PATH)
//                .into(img);
        aboutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rvp.addOnPageChangedListener(new RecyclerViewPager.OnPageChangedListener() {
            @Override
            public void OnPageChanged(int oldPosition, int newPosition) {
                titleTv.setText((newPosition+1)+"/" + max);
            }
        });
    }
}
