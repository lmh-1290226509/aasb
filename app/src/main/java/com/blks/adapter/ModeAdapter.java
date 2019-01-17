package com.blks.adapter;

import android.content.Context;
import android.view.View;

import com.ddadai.baseviewlibrary.adapter.BaseRecyclerAdapter;

public class ModeAdapter extends BaseRecyclerAdapter {
    public ModeAdapter(Context c) {
        super(c);
    }

    @Override
    protected int getRootViewLayoutId(int itemType) {
        return 0;
    }

    @Override
    protected MyViewHolder newViewHolder(View rootView, int itemType) {
        return new Holder(rootView);
    }

    @Override
    protected void initViewHolder(MyViewHolder holder, int position, Object item) {
        Holder h= (Holder) holder;
    }

    private class Holder extends MyViewHolder{

        public Holder(View arg0) {
            super(arg0);
        }

        @Override
        protected void initView() {

        }
    }

}
