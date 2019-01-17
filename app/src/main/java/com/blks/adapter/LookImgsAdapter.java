package com.blks.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.blks.antrscapp.R;
import com.blks.application.RoadSideCarApplication;
import com.blks.model.FileModel;
import com.bumptech.glide.Glide;
import com.ddadai.baseviewlibrary.adapter.BaseRecyclerAdapter;

public class LookImgsAdapter extends BaseRecyclerAdapter {
    public LookImgsAdapter(Context c) {
        super(c);
    }

    @Override
    protected int getRootViewLayoutId(int itemType) {
        return R.layout.item_look_img;
    }

    @Override
    protected MyViewHolder newViewHolder(View rootView, int itemType) {
        return new Holder(rootView);
    }

    @Override
    protected void initViewHolder(MyViewHolder holder, int position, Object item) {
        Holder h = (Holder) holder;
        FileModel.DataListModel model = (FileModel.DataListModel) item;

        String imgPath;

        if (RoadSideCarApplication.getInstance().isTEST()) {
            imgPath = "http://test-antrsc.bzmaster.cn" + model.FILE_PATH;  //测试地址
        } else {
            imgPath = "http://antrsc.bzmaster.cn:8070" + model.FILE_PATH;  //正式地址
        }

        Glide.with(mContext)
                .load(imgPath)
                .placeholder(R.drawable.img_loading)
                .error(R.drawable.img_loading_failed)
                .into(h.img);
    }

    private class Holder extends MyViewHolder {

        ImageView img;

        public Holder(View arg0) {
            super(arg0);
        }

        @Override
        protected void initView() {
            img = findViewById(R.id.img);
        }
    }

}
