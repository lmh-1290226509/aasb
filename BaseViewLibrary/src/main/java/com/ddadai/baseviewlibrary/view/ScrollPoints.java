package com.ddadai.baseviewlibrary.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.ddadai.baseviewlibrary.R;
import com.ddadai.baseviewlibrary.utils.ViewUtils;


public class ScrollPoints extends LinearLayout implements SelectPoint {
	private List<ImageView> points;
	private LinearLayout pointBox;
	private Context mContext;

	private int point_normal, point_focused;

	public ScrollPoints(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	public void initPoints(Context context, int count, int selected,int drawable_normal, int drawable_focused) {
		this.initPoints(context, count, Gravity.CENTER,selected, drawable_normal, drawable_focused);
	}
	public void initPoints(Context context,int gravity, int count, int selected,int drawable_normal, int drawable_focused) {
		this.removeAllViews();
		if(gravity!=0){
			LinearLayout.LayoutParams layoutParams = (LayoutParams) getLayoutParams();
			layoutParams.gravity=gravity;
		}
		pointBox = new LinearLayout(getContext(),null);
		points = new ArrayList<ImageView>();
		point_normal = drawable_normal;
		point_focused = drawable_focused;
		for (int i = 0; i < count && count > 1; i++) { // when only 1 screen
														// don`t add points
			ImageView slidePot = new ImageView(getContext());
			slidePot.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			slidePot.setScaleType(ScaleType.FIT_XY);
			ViewUtils.setViewSize(slidePot, 20, 20);
			
			slidePot.setPadding(5, 5, 5, 5);
			if (i == selected) {
				slidePot.setImageResource(point_focused);
				slidePot.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_in));
			} else {
				slidePot.setImageResource(point_normal);
			}
			points.add(slidePot);
			pointBox.addView(slidePot);
		}

		addView(pointBox);
	}

	public void addPoint(Context context, int count) {
		if (count > points.size()) {
			ImageView slidePot = new ImageView(getContext());
			slidePot.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			slidePot.setScaleType(ScaleType.FIT_XY);
			ViewUtils.setViewSize(slidePot, 20, 20);
			points.add(slidePot);
			pointBox.addView(slidePot);
		}
	}
	
	@Override
	public void changeSelectedPoint(int position) {
		for (int i = 0; i < points.size(); i++) {
			ImageView point = points.get(i);
			if (i == position) {
				point.setImageResource(point_focused);
				point.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_in));
			} else {
				point.setImageResource(point_normal);
			}
		}
	}

	@Override
	public int getPointSize() {
		return points == null ? 0 : points.size();
	}

}
