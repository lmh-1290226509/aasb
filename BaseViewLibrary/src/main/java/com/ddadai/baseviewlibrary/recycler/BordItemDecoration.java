package com.ddadai.baseviewlibrary.recycler;

import android.graphics.Rect;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by shi on 2017/11/6.
 */

public class BordItemDecoration extends RecyclerView.ItemDecoration {

    private int left=16;
    private int right=16;
    private int top=8;
    private int bottom=8;
    private boolean isOnlyFirstTop=true;

    private int  orientation=VERTICAL;

    public static final int HORIZONTAL = OrientationHelper.HORIZONTAL;

    public static final int VERTICAL = OrientationHelper.VERTICAL;

    public BordItemDecoration(int left, int right, int top, int bottom, boolean isOnlyFirstTop, int orientation) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        this.isOnlyFirstTop = isOnlyFirstTop;
        this.orientation = orientation;
    }

    public BordItemDecoration(int left, int right, int top, int bottom, boolean isOnlyFirstTop) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        this.isOnlyFirstTop = isOnlyFirstTop;
    }

    public BordItemDecoration() {
    }

    private float density=-1;
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if(density==-1){
             density = view.getResources().getDisplayMetrics().density;
        }

        if(orientation==VERTICAL){
            outRect.left= (int) (left*density);
            outRect.right= (int) (right*density);
            outRect.bottom= (int) (bottom*density);
            if(isOnlyFirstTop&&parent.getChildAdapterPosition(view)==0){
                outRect.top= (int) (top*density);
            }else if(!isOnlyFirstTop){
                outRect.top= (int) (top*density);
            }
        }else if(orientation==HORIZONTAL){
            outRect.top= (int) (top*density);
            outRect.right= (int) (right*density);
            outRect.bottom= (int) (bottom*density);
            if(isOnlyFirstTop&&parent.getChildAdapterPosition(view)==0){
                outRect.left= (int) (left*density);
            }else if(!isOnlyFirstTop){
                outRect.left= (int) (left*density);
            }
        }


    }
}
