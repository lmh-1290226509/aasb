//package com.ddadai.baseviewlibrary.wheel.widget.popview;
//
//import java.util.List;
//
//import com.fuiou.mgr.FyApplication;
//import com.fuiou.mgr.database.CacheDbAgent;
//import com.fuiou.mgr.model.RegionInfModel;
//import com.ddadai.baseviewlibrary.wheel.widget.adapters.AreaWheelAdapter;
//import com.ddadai.baseviewlibrary.wheel.widget.adapters.AreaWheelAdapter.AreaType;
//import com.ddadai.baseviewlibrary.wheel.widget.view.OnWheelChangedListener;
//import com.ddadai.baseviewlibrary.wheel.widget.view.OnWheelScrollListener;
//import com.ddadai.baseviewlibrary.wheel.widget.view.WheelView;
//
//import android.app.Activity;
//import android.view.View;
//
//public class ThreeAreaWheelPopView extends BaseWheelPopView{
//
//	
//	
//	public ThreeAreaWheelPopView(Activity context) {
//		super(context);
//		
//		initWheelView();
//		
//		CacheDbAgent cacheDbAgent = FyApplication.getCacheDbAgent();
//		
//		List<RegionInfModel> provList=null;
//		List<RegionInfModel> cityList=null;
//		List<RegionInfModel> countryList=null;
//		setShowCengter(0x11111111);
//		
//		
//		provList = cacheDbAgent.qryRegionProv();
//		((AreaWheelAdapter)wv1.getViewAdapter()).setModels(provList);
//		if(provList==null||provList.isEmpty()){
//			return;
//		}
//		cityList = cacheDbAgent.qryCityByProv(provList.get(0).getProv_cd());
//		((AreaWheelAdapter)wv2.getViewAdapter()).setModels(cityList);
//		if(cityList==null||cityList.isEmpty()){
//			return;
//		}
//		countryList = cacheDbAgent.qryCountyByCity(cityList.get(0).getRegion_cd());
//		((AreaWheelAdapter)wv3.getViewAdapter()).setModels(countryList);
//		
//	}
//
//
//	private void initWheelView() {
//		wv3.setVisibility(View.VISIBLE);
//		setWheel(wv3);
//		wv2.setVisibility(View.VISIBLE);
//		setWheel(wv2);
//		wv1.setVisibility(View.VISIBLE);
//		setWheel(wv1);
//	}
//
//	
//	private void setWheel(WheelView wv){
//		wv.setVisibleItems(5);
//		int textType=0;
//		if(wv==wv1){
//			textType=AreaType.TYPE_PROV;
//		}else if(wv==wv2){
//			textType=AreaType.TYPE_CITY;
//		}else if(wv==wv3){
//			textType=AreaType.TYPE_COUNTRY;
//		}
//		wv.setViewAdapter(new AreaWheelAdapter(mContext,textType));
//		wv.setCurrentItem(0);
//		wv.addChangingListener(new OnWheelChangedListener() {
//			
//			@Override
//			public void onChanged(WheelView wheel, int oldValue, int newValue) {
//				((AreaWheelAdapter)wheel.getViewAdapter()).setIndex(wheel.getCurrentItem());
//				RegionInfModel regionInfModel = ((AreaWheelAdapter)wheel.getViewAdapter()).getModels().get(wheel.getCurrentItem());
//				if(wheel==wv1){
//					List<RegionInfModel> cityList = FyApplication.getCacheDbAgent().qryCityByProv(regionInfModel.getProv_cd());
//					((AreaWheelAdapter)wv2.getViewAdapter()).setModels(cityList);
//					wv2.setCurrentItem(0);
//					if(cityList==null||cityList.isEmpty()){
//						return;
//					}
//					List<RegionInfModel> countryList = FyApplication.getCacheDbAgent().qryCountyByCity(cityList.get(0).getRegion_cd());
//					((AreaWheelAdapter)wv3.getViewAdapter()).setModels(countryList);
//					wv3.setCurrentItem(0);
//				}else if(wheel==wv2){
//					List<RegionInfModel> countryList = FyApplication.getCacheDbAgent().qryCountyByCity(regionInfModel.getRegion_cd());
//					((AreaWheelAdapter)wv3.getViewAdapter()).setModels(countryList);
//					wv3.setCurrentItem(0);
//				}
//			}
//		});
//		wv.addScrollingListener(new OnWheelScrollListener() {
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
//	
//	@Override
//	protected void okClick() {
//		super.okClick();
//		RegionInfModel model=((AreaWheelAdapter)wv3.getViewAdapter()).getModels().get(wv3.getCurrentItem());
//		if(mOnAddresWheelClickListenr!=null){
//			mOnAddresWheelClickListenr.onOkClick(this,model);
//		}
//	}
//	
//	
//	
//}
