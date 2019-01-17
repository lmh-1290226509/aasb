package com.ddadai.baseviewlibrary.wheel.widget.dialog;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;

import com.ddadai.baseviewlibrary.wheel.widget.adapters.TimeWheelAdapter;
import com.ddadai.baseviewlibrary.wheel.widget.view.OnWheelChangedListener;
import com.ddadai.baseviewlibrary.wheel.widget.view.OnWheelScrollListener;
import com.ddadai.baseviewlibrary.wheel.widget.view.WheelView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TimeWheelBottomSheetDialog extends BaseWheelBottomSheetDialog{

	/**显示时间和日期-年月日时分秒**/
	public static final int TYPE_ALL=1;
	/**只显示日期-年月日**/
	public static final int TYPE_ONLY_DATE=2;
	/**只显示时间-时分秒**/
	public static final int TYPE_ONLY_TIME=3;
	/**只显示年月**/
	public static final int TYPE_ONLY_YEAR_MONTH=4;
	/**只显示年月日-时分**/
	public static final int TYPE_DATE_HOUR_MIN=5;

	protected int timeType;

	protected OnYearMonthChangeListener mOnYearMonthChangeListener;

	protected List<String> years,months,days,hours,mins,secs;

	protected int minYear;//最小的年份
	protected int maxYear;//最大的年份


	protected List<String> tempDays;//用来保存前28天


	protected boolean is24Hour;//24小时制还是12小时制

	protected Calendar mCalendar;

	protected OnTimeWheelViewClickListener mOnTimeWheelViewClickListener;

	protected TimeWheelBottomSheetDialog(Activity context, int timeType, int minYear, int maxYear, boolean is24Hour) {
		super(context);
		mCalendar=Calendar.getInstance();
		this.timeType=timeType;
		this.is24Hour=is24Hour;

		setYear(minYear, maxYear);

		initWheelView();
		setShowCengter(true);
		setCyclic(new boolean[]{false,true,false,true,true,true});


		//设置时间
		initYears();
		initMonths();
		initDays(mCalendar.get(Calendar.YEAR)+"",mCalendar.get(Calendar.MONTH)+1+"");
		initTime();
		//显示当前时间
		getCurrentTime();

	}


	public void getCurrentTime(){
		wv1.setCurrentItem(mCalendar.get(Calendar.YEAR)-minYear);
		wv2.setCurrentItem(mCalendar.get(Calendar.MONTH));
		wv3.setCurrentItem(mCalendar.get(Calendar.DAY_OF_MONTH)-1);
		wv4.setCurrentItem(mCalendar.get(Calendar.HOUR_OF_DAY));
		wv5.setCurrentItem(mCalendar.get(Calendar.MINUTE));
		wv6.setCurrentItem(mCalendar.get(Calendar.SECOND));
	}



	public TimeWheelBottomSheetDialog(Activity context, int timeType, int minYear, int maxYear) {
		this(context, timeType,minYear,maxYear,true);
	}


	public TimeWheelBottomSheetDialog(Activity context, int timeType) {
		this(context, timeType, 0, 0);
	}


	public void setYear(int minYear,int maxYear){
		if(minYear==0){
			this.minYear=mCalendar.get(Calendar.YEAR)-50;
		}else{
			this.minYear=minYear;
		}
		if(maxYear==0){
			this.maxYear=mCalendar.get(Calendar.YEAR)+100;
		}else{
			this.maxYear=maxYear;
		}
		if(this.minYear>this.maxYear){
			int temp=this.minYear;
			this.minYear=this.maxYear;
			this.maxYear=temp;
		}

		int currentYear=mCalendar.get(Calendar.YEAR);
		if(currentYear<this.minYear||currentYear>this.maxYear){
			mCalendar.set(Calendar.YEAR, this.minYear);
		}

	}

	protected void initWheelView(){
		switch (timeType) {
			case TYPE_ALL:
				wv1.setVisibility(View.VISIBLE);
				wv2.setVisibility(View.VISIBLE);
				wv3.setVisibility(View.VISIBLE);
				wv4.setVisibility(View.VISIBLE);
				wv5.setVisibility(View.VISIBLE);
				wv6.setVisibility(View.VISIBLE);
				mOnYearMonthChangeListener = new MyOnYearMonthChangeListener();
				break;
			case TYPE_ONLY_DATE:
				wv1.setVisibility(View.VISIBLE);
				wv2.setVisibility(View.VISIBLE);
				wv3.setVisibility(View.VISIBLE);
				mOnYearMonthChangeListener = new MyOnYearMonthChangeListener();
				break;
			case TYPE_ONLY_TIME:
				wv4.setVisibility(View.VISIBLE);
				wv5.setVisibility(View.VISIBLE);
				wv6.setVisibility(View.VISIBLE);
				break;
			case TYPE_ONLY_YEAR_MONTH:
				wv1.setVisibility(View.VISIBLE);
				wv2.setVisibility(View.VISIBLE);
				break;
			case TYPE_DATE_HOUR_MIN:
				wv1.setVisibility(View.VISIBLE);
				wv2.setVisibility(View.VISIBLE);
				wv3.setVisibility(View.VISIBLE);
				wv4.setVisibility(View.VISIBLE);
				wv5.setVisibility(View.VISIBLE);
				mOnYearMonthChangeListener=new MyOnYearMonthChangeListener();
				break;
		}


		for (int i = 0; i < wvs.length; i++) {
			setWv(i, wvs[i]);
		}
	}


	protected void setWv(final int index,final WheelView wv){
		if(wv.getVisibility()!=View.VISIBLE){
			return;
		}
		wv.setVisibleItems(5);
		wv.setViewAdapter(new TimeWheelAdapter(mContext,index+1));
		wv.setCurrentItem(0);
		wv.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				((TimeWheelAdapter)wheel.getViewAdapter()).setIndex(wheel.getCurrentItem());
				if((wheel==wv1||wv==wv2)&&mOnYearMonthChangeListener!=null){
					mOnYearMonthChangeListener.change(wheel, oldValue, newValue);
				}

			}
		});
		wv.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				((TimeWheelAdapter)wheel.getViewAdapter()).setIndex(wheel.getCurrentItem());
			}
		});
	}



	@Override
	protected void okClick() {
		super.okClick();

		if(mOnTimeWheelViewClickListener!=null){
			int year = 0,month = 0,day = 0,hour = 0,min = 0,sec = 0;
			try {

				if(years!=null&&years.size()>wv1.getCurrentItem()){
					year=Integer.parseInt(years.get(wv1.getCurrentItem()));
				}
				if(months!=null&&months.size()>wv2.getCurrentItem()){
					month=Integer.parseInt(months.get(wv2.getCurrentItem()));
				}
				if(days!=null&&days.size()>wv3.getCurrentItem()){
					day=Integer.parseInt(days.get(wv3.getCurrentItem()));
				}
				if(hours!=null&&hours.size()>wv4.getCurrentItem()){
					if(is24Hour){
						hour=Integer.parseInt(hours.get(wv4.getCurrentItem()));
					}else{
						String str = hours.get(wv4.getCurrentItem());
						if(str.contains("上午")){
							hour=Integer.parseInt(str.split("上午")[1]);
						}else{
							hour=Integer.parseInt(str.split("下午")[1]);
							if(hour==12){
								hour=0;
							}
						}
					}
				}
				if(mins!=null&&mins.size()>wv5.getCurrentItem()){
					min=Integer.parseInt(mins.get(wv5.getCurrentItem()));
				}
				if(secs!=null&&secs.size()>wv6.getCurrentItem()){
					sec=Integer.parseInt(secs.get(wv6.getCurrentItem()));
				}

			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				mOnTimeWheelViewClickListener.getTime(year, month, day, hour, min, sec);
			}
		}
	}

	protected void initYears(){
		switch (timeType) {
		case TYPE_ALL:
			break;
		case TYPE_ONLY_DATE:
			break;
		case TYPE_ONLY_TIME:
			return;
		case TYPE_ONLY_YEAR_MONTH:
			break;
			case TYPE_DATE_HOUR_MIN:
				break;
		}
		if(years==null){
			years=new ArrayList<String>();
		}else{
			years.clear();
		}
		for (int i = minYear; i <= maxYear; i++) {
			years.add(i+"");
		}

		((TimeWheelAdapter)wv1.getViewAdapter()).setModels(years);
	}

	protected void initMonths(){
		switch (timeType) {
		case TYPE_ALL:
			break;
		case TYPE_ONLY_DATE:
			break;
		case TYPE_ONLY_TIME:
			return;
		case TYPE_ONLY_YEAR_MONTH:
			break;
		case TYPE_DATE_HOUR_MIN:
			break;
		}
		if(months==null){
			months=new ArrayList<String>();
		}else{
			months.clear();
		}
		for (int i = 1; i <= 12; i++) {
			months.add(i+"");
		}
		((TimeWheelAdapter)wv2.getViewAdapter()).setModels(months);
	}


	protected void initDays(String year,String month){
		switch (timeType) {
		case TYPE_ALL:
			break;
		case TYPE_ONLY_DATE:
			break;
		case TYPE_ONLY_TIME:
			return;
		case TYPE_ONLY_YEAR_MONTH:
			return;
			case TYPE_DATE_HOUR_MIN:
				break;
		}
		if(TextUtils.isEmpty(year)||TextUtils.isEmpty(month)){
			return;
		}
		int iYear=0;
		int iMonth = 0;
		try {
			iYear=Integer.parseInt(year);
			iMonth=Integer.parseInt(month);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		if(days==null){
			days=new ArrayList<String>();
		}else{
			days.clear();
		}
		if(tempDays==null){
			tempDays=new ArrayList<String>();
			for (int i = 1; i <=28; i++) {
				tempDays.add(i+"");
			}
		}
		days.addAll(tempDays);

		switch (iMonth) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			days.add(""+29);
			days.add(""+30);
			days.add(""+31);
			break;
		case 4:
		case 6:
		case 9:
		case 11:
			days.add(""+29);
			days.add(""+30);
			break;
		case 2:
			if((iYear%4==0&&iYear%100!=0)&&iYear%400==0){
				days.add(""+29);
			}
			break;
		}

		((TimeWheelAdapter)wv3.getViewAdapter()).setModels(days);
	}


	protected void initTime(){
		switch (timeType) {
		case TYPE_ALL:
			break;
		case TYPE_ONLY_DATE:
			return;
		case TYPE_ONLY_TIME:
			break;
		case TYPE_ONLY_YEAR_MONTH:
			return;
			case TYPE_DATE_HOUR_MIN:
				break;
		}
		hours=new ArrayList<String>();
		mins=new ArrayList<String>();
		secs=new ArrayList<String>();
		for (int i = 0; i < 60; i++) {
			String s="";
			if(i<10){
				s="0"+i;
			}else{
				s=i+"";
			}
			if(i<24&&is24Hour){
				hours.add(s);
			}
			mins.add(s);
			secs.add(s);
		}
		if(!is24Hour){
			hours.add("下午12");
			for (int i = 1; i <= 23; i++) {
				if(i<=12){
					hours.add("上午"+i);
				}else{
					hours.add("下午"+(i%12));
				}
			}
		}
		((TimeWheelAdapter)wv4.getViewAdapter()).setModels(hours);
		((TimeWheelAdapter)wv5.getViewAdapter()).setModels(mins);

		if(timeType!=TYPE_DATE_HOUR_MIN){
			((TimeWheelAdapter)wv6.getViewAdapter()).setModels(secs);
		}
	}






	protected void setOnYearMonthChangeListener(OnYearMonthChangeListener l){
		this.mOnYearMonthChangeListener=l;
	}

	protected  interface OnYearMonthChangeListener{
		void change(WheelView wheel, int oldValue, int newValue);
	}


	/***
	 * 默认的年月变化监听
	 * @author user
	 *
	 */
	protected class MyOnYearMonthChangeListener implements OnYearMonthChangeListener{

		@Override
		public void change(WheelView wheel, int oldValue, int newValue) {
			if(wheel==wv1){
				if(wv2.getCurrentItem()==1){
					//+1是因为下标从0开始
					initDays(minYear+wv1.getCurrentItem()+"", wv2.getCurrentItem()+1+"");
					wv3.setCurrentItem(0);
				}
			}else if(wheel==wv2){
				initDays(minYear+wv1.getCurrentItem()+"", wv2.getCurrentItem()+1+"");
				wv3.setCurrentItem(0);
			}
		}
	}


	public void setOnTimeWheelViewClickListener(OnTimeWheelViewClickListener l){
		mOnTimeWheelViewClickListener=l;
	}

	public  interface OnTimeWheelViewClickListener{
		void getTime(int year, int month, int day, int hour, int min, int sec);
	}
}
