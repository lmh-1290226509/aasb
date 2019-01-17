package com.ddadai.baseviewlibrary.utils;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

public class AnimationUtils {
	
	private CenterYRotateAnimation mCenterYRotate;
	
	
	/**
	 * 沿着Y轴做180的翻转动画
	 * @param v
	 */
	public  void startCenterYRotate(final View v) {

		v.setEnabled(false);
		if(mCenterYRotate==null){
			mCenterYRotate=new CenterYRotateAnimation();
		}
		mCenterYRotate.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {

			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {

			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				v.setEnabled(true);
			}
		});
		v.startAnimation(mCenterYRotate);
	}

	private static class CenterYRotateAnimation extends Animation {
		int mCenterX;// 记录View的中间坐标
		int mCenterY;
		Camera camera = new Camera();


		@Override
		public void initialize(int width, int height, int parentWidth,int parentHeight) {
			super.initialize(width, height, parentWidth, parentHeight);
			// 初始化中间坐标值
			mCenterX = width / 2;
			mCenterY = height / 2;
			setDuration(1000);
			setFillAfter(true);
			setInterpolator(new LinearInterpolator());
		}

		@Override
		protected void applyTransformation(float interpolatedTime,Transformation t) {
			final Matrix matrix = t.getMatrix();
			camera.save();
			camera.rotateY(360 * interpolatedTime);
			camera.getMatrix(matrix);
			// 通过坐标变换，把参考点（0,0）移动到View中间
			matrix.preTranslate(-mCenterX, -mCenterY);
			// 动画完成后再移回来
			matrix.postTranslate(mCenterX, mCenterY);
			camera.restore();
		}
	}
	
	
	
	/**
	 * 使用一个内部类来维护单例 
	 * @author user
	 *
	 */
	private static class AnimationUtilsFactory{
		private static AnimationUtils instance=new AnimationUtils();
	}
	private AnimationUtils(){};
	public static AnimationUtils getInstance(){
		return  AnimationUtilsFactory.instance;
	}
}
