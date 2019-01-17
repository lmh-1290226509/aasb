package com.ddadai.baseviewlibrary.recycler;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by shi on 2017/11/3.
 */

public class LineItemDecoration extends RecyclerView.ItemDecoration {

    private int lineWidth=1;

    public LineItemDecoration(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public LineItemDecoration() {
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom= (int) (lineWidth*view.getResources().getDisplayMetrics().density);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }
}
