package com.ddadai.baseviewlibrary.view;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.ddadai.baseviewlibrary.R;
import com.ddadai.baseviewlibrary.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

public class CustomBannerView extends FrameLayout {

	
	public static final int DOT_SHOW_TYPE_DEFAULT=0;
	public static final int DOT_SHOW_TYPE_NUMBER=1;
	
	private Context mContext;
	
	private View rootView;
	
	private ViewPager viewPager;
	
	private CustomBannerAdapter mAdapter = null;
	
	private List<ImageView> imageViews; // 滑动的图片集合
	
	private ScrollPoints dotLayout;//圆点的View
	
//	private DisplayImageOptions options;
	
//	private TimerTaskUtil mTimerTaskUtil;//定时器工具
	private int[] timers;//需要传给定时器的int数组,来确认图片显示的时间
	
	private int oldTemp;// 记录变化状态的最大值
	private int currentItem = 0; // 当前图片的索引号
	private int imageSize = 0;// /** 广告 图片数量 */
	
	private int dotFocusDrawable=R.drawable.icon_home_point_focus;//圆点图片,获取焦点
	private int dotNormalDrawable=R.drawable.icon_home_point_normal;//圆点图片,没有焦点
	
	private int dotGravity=Gravity.CENTER;//圆点布局的默认位置
	
	private int imgWidth=320;//图片的默认宽度
	private int imgHeight=180;//图片的默认高度
	
	private int defaultImageShowTime=5;//图片默认显示的时间
	
	private int imageDefaultRes=0;
	
	private static final long Sec=1000;
	private Handler mHandler=new Handler();

//	private boolean isShowDot=true;
	
	private int dotType=DOT_SHOW_TYPE_DEFAULT;
	
	private DotNumberView dotNmView;
	
	private OnCustomBannerImageLisitener mOnCustomBannerImageLisitener;//图片的点击监听
	
	private CustomBannerImageAdapter mCustomBannerImageAdapter;

	

	public CustomBannerView(Context context) {
		this(context, null);
	}
	
	public CustomBannerView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	public CustomBannerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext=context;
		rootView=View.inflate(context, R.layout.custon_view_banner, this);
		
		dotNmView=(DotNumberView) rootView.findViewById(R.id.dotNmView);
		dotLayout = (ScrollPoints) rootView.findViewById(R.id.dot_layout);
		mAdapter = new CustomBannerAdapter(mContext);
		viewPager = (ViewPager) rootView.findViewById(R.id.vp);
		viewPager.setAdapter(mAdapter);// 设置填充ViewPager页面的适配器
		// 设置一个监听器，当ViewPager中的页面改变时调用
		viewPager.setOnPageChangeListener(new MyPageChangeListener());
	}

	
	
	

	/**当ViewPager中页面的状态发生改变时调用*/
	private class MyPageChangeListener implements OnPageChangeListener {
//		private int currentIndex;
		
		/**
		 * mActivity method will be invoked when a new page becomes selected.
		 * position: Position index of the new selected page.
		 */
		public void onPageSelected(int position) {
			currentItem = position;
			dotLayout.changeSelectedPoint(currentItem);
			dotNmView.setCurrentIndex(currentItem);
			
			mHandler.removeCallbacks(timerTaskRunnale);
			if(timers!=null&&timers.length!=0){
				mHandler.postDelayed(timerTaskRunnale, timers[currentItem]*Sec);
			}
			
			
			if(mOnCustomBannerImageLisitener!=null){
				mOnCustomBannerImageLisitener.imageChange(CustomBannerView.this, position);
			}
		}

		public void onPageScrollStateChanged(int arg0) {
			// 记录状态变化的最大值
			int i = arg0;
			if (i >= oldTemp) {
				oldTemp = i;
			}
			if (i == 0) {
				//
				if (currentItem == imageSize - 1 && oldTemp == 1) {
					viewPager.setCurrentItem(0);
				}
				if (currentItem == 0 && oldTemp == 1) {
					viewPager.setCurrentItem(imageSize - 1);
				}
				oldTemp = 0;
			}
		}
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
	}
	

	private void initViewPager() {
		if(mCustomBannerImageAdapter==null){
			return;
		}
//		if(options==null){
//			options=getDefaultOptions();
//		}
		if(imageViews==null){
			imageViews = new ArrayList<ImageView>();
		}
		
		imageViews.clear();
		imageSize=mCustomBannerImageAdapter.getCount();
		
		if(imageSize==0){
			ImageView imageView = new ImageView(mContext);
			if(imageDefaultRes!=0){
				imageView.setImageResource(imageDefaultRes);
			}
			imageView.setAdjustViewBounds(true);
			ViewUtils.setViewSize(imageView, imgWidth, imgHeight);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageViews.add(imageView);
			viewPager.removeAllViews();
			mAdapter.setData(imageViews, 1);
			viewPager.setAdapter(mAdapter);
			dotLayout.initPoints(mContext,dotGravity, 1, 0,dotNormalDrawable, dotFocusDrawable);
			dotNmView.setVisibility(View.GONE);
			return;
		}else{
			dotNmView.setVisibility(View.VISIBLE);
			timers= new int[imageSize];
		}
		
		// 初始化图片资源
		for (int i = 0; i <imageSize; i++) {
			ImageView imageView = new ImageView(mContext);
			mCustomBannerImageAdapter.showImage(imageView,i);
//			if(imageUrl!=null){
//				ImageLoader.getInstance().displayImage( imageUrl, imageView, options);
//			}
			imageView.setAdjustViewBounds(false);
//			ViewUtils.setViewSize(imageView, imgWidth, imgHeight);
			imageView.setScaleType(ScaleType.FIT_XY);
			if(imageView.getLayoutParams()==null){
				imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			}else{
				imageView.getLayoutParams().width=LayoutParams.MATCH_PARENT;
				imageView.getLayoutParams().height=LayoutParams.MATCH_PARENT;
			}
			imageViews.add(imageView);
			
			int showTime=mCustomBannerImageAdapter.getImageShowTime(i);
			showTime=showTime<5?defaultImageShowTime:showTime;
//			timers[i]=3;
			timers[i]=showTime; //TODO 暂时注销
		}
		
		viewPager.removeAllViews();
		mAdapter.setData(imageViews, imageSize);
		viewPager.setAdapter(mAdapter);
		dotLayout.initPoints(mContext,dotGravity, imageSize, 0,dotNormalDrawable, dotFocusDrawable);
		dotNmView.init(imageSize);
		if(mOnCustomBannerImageLisitener!=null){
			mOnCustomBannerImageLisitener.imageChange(CustomBannerView.this, 0);
		}
		
		switch (dotType) {
		case DOT_SHOW_TYPE_DEFAULT:
			dotLayout.setVisibility(View.VISIBLE);
			dotNmView.setVisibility(View.GONE);
			break;
		case DOT_SHOW_TYPE_NUMBER:
			dotLayout.setVisibility(View.GONE);
			dotNmView.setVisibility(View.VISIBLE);
			LinearLayout parent = (LinearLayout) dotNmView.getParent();
			parent.setGravity(dotGravity);
			ViewUtils.setViewMargin(parent, 0, 0, 20, 20);
			break;
		}
		
		mHandler.removeCallbacks(timerTaskRunnale);
		if(timers!=null&&timers.length!=0){
			mHandler.postDelayed(timerTaskRunnale, timers[currentItem=0]*Sec);
		}
		
		
	}
	
	
//	private DisplayImageOptions getDefaultOptions(){
//		if(imageDefaultRes==0){
//			imageDefaultRes=R.drawable.img_loading_banner;
//		}
//		DisplayImageOptions options = new DisplayImageOptions.SpanColorBuilder()
//		// 设置图片在下载期间显示的图片
//				.showStubImage(imageDefaultRes)
//				// 设置图片Uri为空或是错误的时候显示的图片
//				.showImageForEmptyUri(imageDefaultRes)
//				// 设置图片加载/解码过程中错误时候显示的图片
//				.showImageOnFail(imageDefaultRes)
//				// 设置下载的图片是否缓存在内存中
//				.cacheInMemory(true)
//				// 设置下载的图片是否缓存在SD卡中
//				.cacheOnDisc(true)
//				// 设置图片的解码类型
//				.bitmapConfig(Bitmap.Config.RGB_565).build();
//		return options;
//	}
	
	
	
	private class CustomBannerAdapter extends PagerAdapter {

		private List<ImageView> imageViews; // 滑动的图片集合

		private int imageSize; // 图片数量

		public CustomBannerAdapter(Context context) {
		}
		
		public void setData( List<ImageView> imageViews,int imageSize){
			this.imageViews = imageViews;
			this.imageSize = imageSize;
			notifyDataSetChanged();
		}
		

		@Override
		public int getCount() {
			return imageSize; 
		}

		@Override
		public Object instantiateItem(View arg0, final int arg1) {
			
			View view = imageViews.get(arg1);
			view.setTag(arg1);
			((ViewPager) arg0).addView(view,0);
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int position=(Integer) v.getTag();
					if(mOnCustomBannerImageLisitener!=null){
						mOnCustomBannerImageLisitener.imageClick(CustomBannerView.this,position);
					}
				}
			});
			return imageViews.get(arg1);
		}
		
		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}
	
	
	private Runnable timerTaskRunnale=new Runnable() {
		
		@Override
		public void run() {
			currentItem = (currentItem + 1) % imageSize;
			viewPager.setCurrentItem(currentItem);
		}
	};

	
	
	
	
	
	//**************************定义接口************************************************
	
	public static interface OnCustomBannerImageLisitener{
		void imageClick(View v,int position);
		void imageChange(View v,int position);
	}
	
	
	public static abstract class CustomBannerImageAdapter{
		
		private CustomBannerView view;
		
		public abstract void setModels(List<?> models);
		
		public abstract int getCount();
		
		public abstract Object getItem(int position);	
		
		/**如果要自己实现image的显示，那返回值设置为null，否则会使用imageloader***/
		public abstract void showImage(ImageView img,int position);
		
		public abstract int getImageShowTime(int position);
		
		public void notifyDataChange(){
			if(view!=null){
				view.initViewPager();
			}
		}
		
		private void dataChange(CustomBannerView view){
			this.view=view;
		}
	}
	
	
	
	
	//***********************暴露给外部调用的方法***************************************/
	
	public void setOnCustomBannerImageLisitener(OnCustomBannerImageLisitener onCustomBannerImageLisitener){
		this.mOnCustomBannerImageLisitener=onCustomBannerImageLisitener;
	}
	
	
	public void setAdapter(CustomBannerImageAdapter CustomBannerImageAdapter){
		this.mCustomBannerImageAdapter=CustomBannerImageAdapter;
		this.mCustomBannerImageAdapter.dataChange(this);
	}
	
//	//设置图片的options
//	public void setOptions(DisplayImageOptions options){
//		this.options=options;
//	}
	
	public void setDotType(int type){
		this.dotType=type;
	}
	
	//设置底部圆点的资源,为0用默认的
	public void setDotRes(int focus,int normal){
		if(focus!=0){
			dotFocusDrawable=focus;
		}
		if(normal!=0){
			dotNormalDrawable=normal;
		}
	}
	
	
	public void setImageDefaultRes(int res){
		this.imageDefaultRes=res;
	}
	
	//设置图片的大小
	public void setImageSize(int width,int height){
		this.imgWidth=width;
		this.imgHeight=height;
	}
	
	public void setDotGravity(int gravity){
		this.dotGravity=gravity;
	}
	
	//在activity的onstart里面调用
	public void onStart(){
		mHandler.removeCallbacks(timerTaskRunnale);
		if(timers!=null&&timers.length!=0){
			mHandler.postDelayed(timerTaskRunnale, timers[currentItem]*Sec);
		}
//		Log.d("xyb", "onStart");
	}
	
	//在activity的onStop里面调用
	public void onStop(){
//		Log.d("xyb", "onStop");
		mHandler.removeCallbacks(timerTaskRunnale);
	}
	
	
	
	
	
	

}
