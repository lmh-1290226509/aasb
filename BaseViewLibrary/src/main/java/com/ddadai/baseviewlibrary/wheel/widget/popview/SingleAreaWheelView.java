//package com.ddadai.baseviewlibrary.wheel.widget.popview;
//
//import java.util.List;
//
//import android.app.Activity;
//import android.view.View;
//
//import com.fuiou.mgr.model.RegionInfModel;
//import com.ddadai.baseviewlibrary.wheel.widget.adapters.AreaWheelAdapter;
//import com.ddadai.baseviewlibrary.wheel.widget.view.OnWheelChangedListener;
//import com.ddadai.baseviewlibrary.wheel.widget.view.OnWheelScrollListener;
//import com.ddadai.baseviewlibrary.wheel.widget.view.WheelView;
//
//public class SingleAreaWheelView extends BaseWheelPopView{
//	
//	private int areaType;
//	
//	public SingleAreaWheelView(Activity context,int areaType) {
//		super(context);
//		this.areaType=areaType;
//		initWheelView();
//		setShowCengter(true);
//	}
//
//	private void initWheelView() {
//		wv1.setVisibility(View.VISIBLE);
//		wv1.setVisibleItems(5);
//		wv1.setViewAdapter(new AreaWheelAdapter(mContext,areaType));
//		wv1.setCurrentItem(0);
//		wv1.addChangingListener(new OnWheelChangedListener() {
//			
//			@Override
//			public void onChanged(WheelView wheel, int oldValue, int newValue) {
//				((AreaWheelAdapter)wheel.getViewAdapter()).setIndex(wheel.getCurrentItem());
//			}
//		});
//		wv1.addScrollingListener(new OnWheelScrollListener() {
//			
//			@Override
//			public void onScrollingStarted(WheelView wheel) {
//			}
//			
//			@Override
//			public void onScrollingFinished(WheelView wheel) {
//				((AreaWheelAdapter)wheel.getViewAdapter()).setIndex(wheel.getCurrentItem());
//			}
//		});
//	}
//
//	
//	
//	@Override
//	protected void okClick() {
//		super.okClick();
//		RegionInfModel model=((AreaWheelAdapter)wv1.getViewAdapter()).getModels().get(wv1.getCurrentItem());
//		if(mOnAddresWheelClickListenr!=null){
//			mOnAddresWheelClickListenr.onOkClick(this,model);
//		}
//	}
//	
//	
//	public  void setModels(List<RegionInfModel> models,int index){
//		((AreaWheelAdapter)wv1.getViewAdapter()).setModels(models);
//		setIndex(index);
////		
//	}
//	
//	public void setModels(List<RegionInfModel> models){
//		setModels(models,0);
//	}
//	
//	public void setIndex(int index){
//		wv1.setCurrentItem(index);
//	}
//
//}
