package com.blks.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.blks.antrscapp.CancelWorkOrderActivity;
import com.blks.antrscapp.R;
import com.blks.antrscapp.WorkOrdersIndexActivity;
import com.blks.model.WorkOrderModel;
import com.blks.route.Route;
import com.blks.utils.Constants;
import com.ddadai.baseviewlibrary.adapter.BaseRecyclerAdapter;

public class TaskListAdapter extends BaseRecyclerAdapter {
    public TaskListAdapter(Context c) {
        super(c);
    }

    @Override
    protected int getRootViewLayoutId(int itemType) {
        return R.layout.item_task_list;
    }

    @Override
    protected MyViewHolder newViewHolder(View rootView, int itemType) {
        return new Holder(rootView);
    }

    @Override
    protected void initViewHolder(MyViewHolder holder, int position, Object item) {
        Holder h= (Holder) holder;
        final WorkOrderModel.DataListModel model= (WorkOrderModel.DataListModel) item;
        h.noTv.setText(model.WO_NO);
        h.statusTv.setText(model.WO_STATUS);
        h.areaTv.setText(model.FROM_ADDR);
        h.typeTv.setText(model.GOODS_NAME);
        h.timeTv.setText(model.WO_DATE);
        h.orderStatusTv.setText(model.RSC_STEP);
        h.cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CancelWorkOrderActivity.class);
                intent.putExtra(Constants._KEY_B, false);
                intent.putExtra(Constants._WO_NO, model.WO_NO);
                mContext.startActivity(intent);
            }
        });
        h.detailTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Route.builder(mContext)
                        .class_(WorkOrdersIndexActivity.class)
                        .putExtra("_WO_NO",model.WO_NO)
                        .startActivity();
//                Intent intent = new Intent(mContext, CancelWorkOrderActivity.class);
//                intent.putExtra(Constants._KEY_B, false);
//                intent.putExtra(Constants._WO_NO, model.WO_NO);
//                mContext.startActivity(intent);
            }
        });


        if ("已完成".equals(model.WO_STATUS)) {
            h.layoutBootom.setVisibility(View.GONE);
        } else {
            h.layoutBootom.setVisibility(View.VISIBLE);
        }
    }

    private class Holder extends MyViewHolder{
        private TextView noTv;
        private TextView statusTv;
        private TextView areaTv;
        private TextView typeTv;
        private TextView timeTv;
        private TextView orderStatusTv;
        private TextView detailTv;
        private TextView cancelTv;
        private View layoutBootom;


        public Holder(View arg0) {
            super(arg0);
        }

        @Override
        protected void initView() {
            noTv = findViewById(R.id.noTv);
            statusTv = findViewById(R.id.statusTv);
            areaTv = findViewById(R.id.areaTv);
            typeTv = findViewById(R.id.typeTv);
            timeTv = findViewById(R.id.timeTv);
            orderStatusTv = findViewById(R.id.orderStatusTv);
            detailTv = findViewById(R.id.detailTv);
            cancelTv = findViewById(R.id.cancelTv);
            layoutBootom = findViewById(R.id.layoutBootom);
        }
    }

}
