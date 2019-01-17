package com.ddadai.baseviewlibrary.recycler;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by shi on 2017/11/3.
 */

public class LineGrayItemDecoration extends RecyclerView.ItemDecoration {

    private int lineWidth=1;

    private Paint mPaint;

    public LineGrayItemDecoration(int lineWidth) {
//

        this.lineWidth = lineWidth;
        mPaint=new Paint();
        mPaint.setAntiAlias(true);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(lineWidth);
    }

    public LineGrayItemDecoration() {
        this(1);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom= (int) (lineWidth*view.getResources().getDisplayMetrics().density);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount = parent.getChildCount();

        int color = Color.parseColor("#F3F3F3");
        mPaint.setColor(color);
        mPaint.setStrokeWidth(lineWidth*parent.getResources().getDisplayMetrics().density);
        for (int i = 0; i <childCount ; i++) {
            View view = parent.getChildAt(i);
            c.drawLine(0,0,view.getRight(),view.getBottom(),mPaint);
        }
    }
}
