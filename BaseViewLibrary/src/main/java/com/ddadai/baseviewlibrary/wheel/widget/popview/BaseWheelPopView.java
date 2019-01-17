package com.ddadai.baseviewlibrary.wheel.widget.popview;


import com.ddadai.baseviewlibrary.R;
import com.ddadai.baseviewlibrary.wheel.widget.view.WheelView;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;

public abstract class BaseWheelPopView extends PopupWindow{

	
	
	
	
//	public static final int TYPE_WHEEL_ONE=1;
//	public static final int TYPE_WHEEL_TWO=2;
//	public static final int TYPE_WHEEL_THREE=3;
//	public static final int TYPE_WHEEL_FOUR=4;
//	public static final int TYPE_WHEEL_FIVE=5;
//	public static final int TYPE_WHEEL_SIX=6;
	
	
//	protected int type;
	protected Activity mContext;
	protected View rootView;
	protected int width;
	protected int height;
	protected int Status_Height;
	protected boolean isShowCenter=true;//显示中间的那条横线
	
	protected WheelView wv1,wv2,wv3;
	protected WheelView wv4,wv5,wv6;
	protected View centerView,okView,noView;
	
	protected WheelView wvs[];
	
//	protected OnAddresWheelClickListenr mOnAddresWheelClickListenr;
	
	
	public BaseWheelPopView(Activity context) {
		mContext=context;
		initSize();
		initView();
		initPopView();
//		this(context, TYPE_WHEEL_ONE);
	}
//	protected BaseWheelPopView(Activity context,int type) {
////		this.type=type;
//		mContext=context;
//		initSize();
//		initView();
//		initPopView();
//	}
	
	
	/**
	 * 获得手机屏幕大小
	 */
	private void initSize(){
		DisplayMetrics metrics = new DisplayMetrics();
		mContext.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		height = metrics.heightPixels;
		width = metrics.widthPixels;
		
		try {
			Rect outRect=new Rect();
			mContext.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
			Status_Height=outRect.top;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 初始化布局
	 */
	protected void initView(){
		rootView=LayoutInflater.from(mContext).inflate(R.layout.wheel_pop_view, null);
		centerView= rootView.findViewById(R.id.centerView);
		okView= rootView.findViewById(R.id.okView);
		noView= rootView.findViewById(R.id.noView);
		wv1=(WheelView) rootView.findViewById(R.id.wv1);
		wv2=(WheelView) rootView.findViewById(R.id.wv2);
		wv3=(WheelView) rootView.findViewById(R.id.wv3);
		wv4=(WheelView) rootView.findViewById(R.id.wv4);
		wv5=(WheelView) rootView.findViewById(R.id.wv5);
		wv6=(WheelView) rootView.findViewById(R.id.wv6);
		
		wvs=new WheelView[6];
		wvs[0]=wv1;
		wvs[1]=wv2;
		wvs[2]=wv3;
		wvs[3]=wv4;
		wvs[4]=wv5;
		wvs[5]=wv6;
		
		okView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				okClick();
			}
		});
		rootView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				rootViewClick();
			}
		});
		rootView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				noViewClick();
			}
		});
	}
	
	protected void noViewClick() {
		dismiss();
	}
	/**
	 * 初始化popview的设置
	 */
	protected void initPopView(){
		setContentView(rootView);
		setWidth(width);
		setHeight((int)(height-Status_Height));
		setBackgroundDrawable(new ColorDrawable(Color.parseColor("#cc000000")));// 设置背景图片，不能在布局中设置，要通过代码来设置
		setFocusable(true);
		setOutsideTouchable(true);// 触摸popupwindow外部，popupwindow消失。这个要求你的popupwindow要有背景图片才可以成功，如上
		update();
	}
	
	/**
	 * 设置是否显示中间的横条
	 * @param color 输入0xffffffff 格式的8位16进制的色值
	 */
	public void setShowCengter(int color){
		try {
			centerView.setVisibility(View.VISIBLE);
			centerView.setBackgroundColor(color);
		} catch (Exception e) {
			e.printStackTrace();
			centerView.setVisibility(View.GONE);
		}
	}
	
	
	/**
	 * 设置是否显示中间的横条
	 * @param show
	 */
	public void setShowCengter(boolean show){
		if(show){
			centerView.setVisibility(View.VISIBLE);
			centerView.setBackgroundColor(0x11111111);
		}else{
			centerView.setVisibility(View.GONE);
		}
	}
	
	
	
	protected void okClick(){
		dismiss();
	}
	protected void rootViewClick(){
//		dismiss();
	}
	
	
	/**
	 * 设置是否循环
	 * @param isCyclic
	 */
	public void setCyclic(boolean isCyclic){
		for (int i = 0; i < wvs.length; i++) {
			wvs[i].setCyclic(isCyclic);
		}
	}
	/**
	 * 设置是否循环
	 * @param isCyclic
	 */
	public void setCyclic(boolean[] isCyclics){
		for (int i = 0; i < wvs.length; i++) {
			wvs[i].setCyclic(isCyclics[i]);
		}
	}
	
	
//	public void setOnAddresWheelClickListenr(OnAddresWheelClickListenr l){
//		mOnAddresWheelClickListenr=l;
//	}
	
//	public interface OnAddresWheelClickListenr{
//		void onOkClick(BaseWheelPopView popView,RegionInfModel model);
//	}
	
	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		super.showAtLocation(parent, gravity, x, y);
	}
	
	
	public void show(){
		
		if(mContext instanceof Activity){
			showAtLocation(mContext.getWindow().getDecorView().getRootView(), Gravity.BOTTOM, 0, 0);
			//隐藏键盘
//			FyApplication.getInputMethodManager().hideSoftInputFromWindow(mContext.getWindow().getDecorView().getWindowToken(), 0);
		}
	}
}
